package main.java.fr.songbird.player;

import main.java.fr.songbird.manager.ConquestPlugin;
import main.java.fr.songbird.nation.Nation;
import main.java.fr.songbird.points.Devise;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;

import static main.java.fr.songbird.manager.ConquestPlugin.LOGGER;

/**
 * <p>
 *     Cette classe encapsule une instance de la classe Player censée représenter un joueur présent sur le serveur. <br>
 *     Elle encapsule également une instance de la classe Nation censée appartenir à l'instance du joueur.
 * </p>
 * @author Songbird
 */
public class PlayerWrapper
{
	private JSONObject profile;
	private int battlePoints;
    private int honorPoints;
    private final Devise devise = new Devise();
	private static File bddPath;

	{
		profile = new JSONObject();
	}

	static
	{

		StringBuilder builder = new StringBuilder();
		builder.append(ConquestPlugin.formatClassName(ConquestPlugin.class))
		.append("_lib")
		.append(File.separator)
		.append("bdd");
		bddPath = new File (builder.toString());
	}


	/**
     *
	 * @param player L'instance du joueur
	 * @param nation L'instance de la nation du joueur
     */
	public PlayerWrapper(final Player player, final Nation nation, int battlePoints, int honorPoints)
	{
		profile.put("username", player.getName());
		profile.put("uuid", player.getUniqueId());
		profile.put("nation", nation.getNationName());
		profile.put("position", player.getLocation());
		profile.put("point de bataille", battlePoints);
        profile.put("point d'honneur", honorPoints);
		writeProfile(profile);
	}

    /**
     * Ajoute des points de bataille au joueur.
     * @param battlePoints Nombre de points de bataille à ajouter.
     */
	public void addBattlePoints(final int battlePoints)
    {
        this.battlePoints += battlePoints;
		profile.put("point de bataille", this.battlePoints);
        addHonorPoints(battlePoints * devise.getCoeff());
    }

    /**
     * Ajoute des points d'honneur au joueur.
     * @param honorPoints Nombre de points d'honneur à ajouter.
     */
    private void addHonorPoints(final int honorPoints)
    {
        this.honorPoints += honorPoints;
    }

    /**
     * Soustrait des points de bataille au joueur.
     * @param battlePoints Nombre de points de bataille à soustraire.
     */
    public void removeBattlePoints(final int battlePoints)
    {
        this.battlePoints -= battlePoints;
    }

    /**
     * Soustrait des points d'honneur au joueur.
     * @param honorPoints Nombre de points d'honneur à soustraire.
     */
    public void removeHonorPoints(final int honorPoints)
    {
        this.honorPoints -= honorPoints;
    }


    public JSONObject getPlayerProfile()
    {
        return this.profile;
    }

    /**
     *
     * @param profile
     */
	private void writeProfile(final JSONObject profile)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Construction du chemin")
		.append(bddPath.getAbsolutePath())
		.append(": ")
		.append(bddPath.mkdir());
		LOGGER.info(builder.toString());

		builder.delete(0, builder.length());
		builder.append(bddPath.getAbsolutePath())
		.append(File.separator)
		.append(profile.get("username"))
		.append(".json");

		final File bddFilePath = new File(builder.toString());
		FileWriter writer = null;

		try
		{

                writer = new FileWriter(bddFilePath);

                writer.write(profile.toJSONString());

		}catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
			LOGGER.error("Une erreur est survenue lors de la tentative de sauvegarde lancée par le plugin.");
		}finally
		{
			try
			{
				if(writer != null)
				{
                    writer.flush();
                    writer.close();
				}
			}catch(IOException ioe1)
			{
				LOGGER.error(ioe1.getMessage());
			}
		}
	}

	public static boolean heExists(String username)
	{
		FilenameFilter fnf = new FilenameFilter()
				{
					public boolean accept(File dir, String name)
					{
						if(name.lastIndexOf('.') > 0)
						{
							int lastIndex = name.lastIndexOf('.');

							String extension = name.substring(lastIndex);

							if(extension.equals(".json"))
							{
								return true;
							}
						}
						return false;
					}

				};


	    File[] files = bddPath.listFiles(fnf);
		if(files.length != 0)
		{
			for(File file : files)
			{
				if(file.getName().contains(username))
				{
					return true;
				}
			}
		}
		return false;
	}

	public static void loadPlayerProfile(Player player, LinkedList<PlayerWrapper> playerProfiles)
	{
		JSONParser parser = new JSONParser();

		try
		{
			StringBuilder buffer = new StringBuilder();
			buffer.append(bddPath);
			buffer.append(File.separator);
			buffer.append(player.getName());
			buffer.append(".json");
            assert(heExists(player.getName())) : "Le fichier n'existe pas.";
			JSONObject jO = (JSONObject)parser.parse(new FileReader(buffer.toString()));


		}catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
		}
		catch(ParseException pe0)
		{
			LOGGER.error(pe0.getMessage());
		}
	}

}
