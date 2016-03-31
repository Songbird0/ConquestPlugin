package fr.songbird.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import fr.songbird.commbdd.MySQLWrapper;
import fr.songbird.constants.ProgramConstants;
import fr.songbird.core.ConquestPluginCore;
import fr.songbird.core.ReachedZoneListener;
import fr.songbird.exceptions.DataIntegrityException;
import fr.songbird.nation.Nation;
import fr.songbird.scoreboard.StatNation;
import net.wytrem.logging.BasicLogger;
import net.wytrem.logging.LoggerFactory;

import java.util.concurrent.*;


/**
 * Classe principale chargée de faire le pont entre les différents composants du plugin (tels que le scoreboard), et de veiller aux changements d'états des régions représentant les villages sur la carte.
 * @author songbird
 * @version 0.0.4_0-ALPHA
 */
public class ConquestPlugin extends JavaPlugin implements Listener, ProgramConstants, ReachedZoneListener
{
	
	
	private MySQLWrapper msw;
	private final Server server;
	private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	
	/**
	* Référence vers une instance du coeur du plugin.
	*
	*/
	private final ConquestPluginCore core;

	/**
	 * Scoreboard rattaché aux plugins conquete.
	 */
	private StatNation stn;
	
	/**
	* Profils des joueurs.
	*/
	private ArrayList<JSONObject> playerProfiles; //TODO Remplacer l'ArrayList par une LinkedList ou équivalent - trop coûteux en ressources
	
	/**
	 * Parser du fichier de configuration.
	 */
	private static final Yaml loader;
	/**
	 * Liste des Nations présentes, Neutral étant une pseudo-nation permettant de cibler les joueurs qui n'ont pas encore choisi leur nation.(restriction des actions sur le serveur tant qu'ils restent neutres)
	 */
	private static ArrayList<Nation> nations; //neutre, nation 1, nation 2
	
	public static final BasicLogger LOGGER;
	
	static
	{
		loader = new Yaml();
		LOGGER =  LoggerFactory.getLogger(ConquestPlugin.class);
	}

	{
		core = new ConquestPluginCore();
	}
	
	public ConquestPlugin(){}
	
	{
		server = Bukkit.getServer();
		stn = new StatNation();
		playerProfiles = new ArrayList<JSONObject>();
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		
		if(plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			LOGGER.error("Le serveur n'a pas réussi à charger le plugin worldguard ou n'existe pas.");
		}
		
	}

	
	private static final void setNations(final Nation...nations)
	{
		ConquestPlugin.nations = new ArrayList<Nation>(nations.length);
		
		for(Nation nation : nations)
		{
			ConquestPlugin.nations.add(nation);
		}
	}
	
	private static final MySQLWrapper getBDDConnection(Map<String, String> data) throws DataIntegrityException
	{
		if(datasIntegrityChecking(data))
		{
			return new MySQLWrapper(data.get("hostname"), data.get("port"), data.get("username"), data.get("password"));
		}
		else
		{
			throw new DataIntegrityException(new String("L'intégrité des données est compromise. Veillez à compléter correctement le document avant de relancer le plugin."));
		}
	}
	
	private static final boolean datasIntegrityChecking(Map<String, String> data)
	{
		HashMap<String, String> dispoTab = new HashMap<String, String>(); //Tableau associatif charge de recolter le status de chaque cles (presentes ou non)
		for(Entry<String, String> entries: data.entrySet())
		{
			if(entries.getValue() != null)
			{
				dispoTab.put(entries.getKey(), "OK");
				LOGGER.success(entries.getKey()+": "+dispoTab.get(entries.getKey()));
			}
			else
			{
				LOGGER.error(entries.getKey()+"'s value is null, please complete for run properly.");
				return false;
			}
		}
		return true;
	}
	
	
	/**
	* Méthode encore utilisée, mais peu recommandée.
	* Le générateur de fichiers de configuration écrit en groovy est bien plus propre.
	*
	*/
	@Deprecated
	public static final MySQLWrapper parsingYamlFile()
	{
		File yamlFile = null;
		try 
		{
			StringBuilder builder = new StringBuilder();
			builder.append("ConquestPlugin_lib");
			builder.append(File.separator);
			builder.append("config.yml");
			yamlFile = new File(builder.toString());
			yamlFile.createNewFile();
			InputStream in = new FileInputStream(yamlFile.getAbsolutePath());


			@SuppressWarnings("unchecked")
			Map<String, String> object = (Map<String, String>)loader.load(in);
			
			
			LOGGER.info("object: "+object);
			
			
			
			return getBDDConnection(object);
			
		} catch (FileNotFoundException fnfe0) 
		{
			LOGGER.error(fnfe0.getMessage());
		} catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
		} catch(Exception e0)
		{
			LOGGER.error(e0.getMessage());
		}
		
		return null;
	}
	
	public final static String formatClassName(Class<?> clazz)
	{
		return clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
	}
	
	public static final ArrayList<Nation> getNations()
	{
		return ConquestPlugin.nations;
	}

	//----------------------------------------------//
	
	@Override
	public void onEnable()
	{
		server.getPluginManager().registerEvents(this, this);
		core.addReachedZoneListener(this);
		final Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		this.getCommand(COMMANDCORE).setExecutor(new NationCommand());
		
		setNations
		(
			new Nation("Neutral", 0, score.registerNewTeam("neutral")),
			new Nation("Lutha", 1, score.registerNewTeam("lutha")),
			new Nation("Gondar", 2, score.registerNewTeam("gondar"))
	    );
		
		
		stn.setCurrentScoreboard(score);

		stn.initializeScoreboard();
		
	}

	@Override
	public void onDisable()
	{
		pool.shutdown();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void whenPlayerMove(PlayerMoveEvent pme)
	{

			new Thread(new Runnable()
			{

				@Override
				public void run() 
				{
					
					final Location location = pme.getPlayer().getLocation();
					core.setX(location.getX());
					core.setY(location.getY());
					core.setZ(location.getZ());
					core.setUserLocation(location);
					
				}
			}).start();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void whenPlayerJoin(PlayerJoinEvent pje)
	{
		pje.getPlayer().setScoreboard(stn.getCurrentScoreboard());
	}

	@Override
	public void whenZoneHasBeenReached()
	{

	}
	
	public final ArrayList<JSONObject> getPlayerProfilesList()
	{
		return this.playerProfiles;
	}
	
	public static void main(String[] args) 
	{

	}

}
