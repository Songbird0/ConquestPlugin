package fr.songbird.player;

import static fr.songbird.manager.ConquestPlugin.LOGGER;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.songbird.manager.ConquestPlugin;
import fr.songbird.nation.Nation;

public class PlayerWrapper 
{
	private JSONObject profile;
	private static File bddPath;
	
	{
		profile = new JSONObject();
	}
	
	static
	{

		StringBuilder builder = new StringBuilder();
		builder.append(ConquestPlugin.formatClassName(ConquestPlugin.class));
		builder.append("_lib");
		builder.append(File.separator);
		builder.append("bdd");
		bddPath = new File (builder.toString());
	}
	
	
	@SuppressWarnings("unchecked")
	public PlayerWrapper(final Player player, final Nation nation)
	{
		profile.put("username", player.getName());
		profile.put("uuid", player.getUniqueId());
		profile.put("nation", nation.getNationName());
		profile.put("position", player.getLocation());
		writeProfile(profile);
	}
	
	
	private void writeProfile(final JSONObject profile)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Construction du chemin");
		builder.append(bddPath.getAbsolutePath());
		builder.append(": ");
		builder.append(bddPath.mkdir());
		LOGGER.info(builder.toString());
		
		builder.delete(0, builder.length());
		builder.append(bddPath.getAbsolutePath());
		builder.append(File.separator);
		builder.append(profile.get("username"));
		builder.append(".json");
		
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
				writer.flush();
				writer.close();
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
	
	public static void setPlayerProfile(Player player, ArrayList<JSONObject> playerProfiles)
	{
		JSONParser parser = new JSONParser();
		
		try
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(bddPath);
			buffer.append(File.separator);
			buffer.append(player.getName());
			buffer.append(".json");
			
			JSONObject jO = (JSONObject)parser.parse(new FileReader(buffer.toString()));
			
			playerProfiles.add(jO);
		}catch(FileNotFoundException fnf0)
		{
			LOGGER.error(fnf0.getMessage());
		}catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
		}
		catch(ParseException pe0)
		{
			LOGGER.error(pe0.getMessage());
		}
	}
	
	public static void main(String...strings)
	{
		
	}

}
