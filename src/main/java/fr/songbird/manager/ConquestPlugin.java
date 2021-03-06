package main.java.fr.songbird.manager;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import main.java.fr.songbird.config.ConfigYamlFile;
import main.java.fr.songbird.constants.ProgramConstants;
import main.java.fr.songbird.core.ConquestPluginCore;
import main.java.fr.songbird.core.ReachedZoneListener;
import main.java.fr.songbird.exceptions.DataIntegrityException;
import main.java.fr.songbird.nation.Nation;
import main.java.fr.songbird.player.PlayerWrapper;
import net.wytrem.logging.BasicLogger;
import net.wytrem.logging.LoggerFactory;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe principale chargée de faire le pont entre les différents composants du plugin (tels que le scoreboard), et de veiller aux changements d'états des régions représentant les villages sur la carte.
 * @author songbird
 * @version 0.0.4_0-ALPHA
 */
public class ConquestPlugin extends JavaPlugin implements Listener, ProgramConstants, ReachedZoneListener
{


    /**
     * Référence de l'objet encapsulant la connexion.
     */
	private Connection sqlConnection;
    /**
     * Pointeur vers l'instance du serveur Spigot.
     */
	private final Server server;
    /**
     * Pool de fils d'exécution extensible selon les moyens de la machine sur laquelle il est initialisé.
     */
	private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Fichier de configuration relatif au coeur du plugin
     * Structure par défaut de ce fichier de configuration:
     * <pre>
     * world: {hisName: your world name}
     * regions:
     *   theirName: {nomRegion: NomScoreboard}
     * devise: {points de bataille: 1, points d'honneur: 1}
     * </pre>
     */
	private final ConfigYamlFile configFile;
    /**
     * Fichier de configuration encapsulant les données nécessaires à une connexion vers la base de données.
     * Structure par défaut de ce fichier de configuration:
     * <pre>
     *     {
     *       hostname: null,
     *       port: null,
     *       username: null,
     *       password: null
     *     }
     * </pre>
     */
    private ConfigYamlFile sqlConfigFile;

	/**
	* <p>
	* Chemin du fichier de configuration qui sera rechargé à chaque reboot du serveur.<br>
	* Le rechargement du fichier à la volée n'est pas encore supportée dans cette version.
	*
	*
	*
	*
	* </p>
	*
	*/
	private final File filePath;

	/**
	* Référence vers une instance du coeur du plugin.
	*
	*/
	private final ConquestPluginCore core;

	/**
	 * Scoreboard rattaché aux plugins conquete.
	 */

	/**
	* Profils des joueurs.
	*/
	private LinkedList<PlayerWrapper> playerProfiles;
	/**
	 * Parser du fichier de configuration.
	 */
	private static final Yaml loader;
	/**
	 * Liste des Nations présentes, Neutral étant une pseudo-nation permettant de cibler les joueurs qui n'ont pas encore choisi leur nation.(restriction des actions sur le serveur tant qu'ils restent neutres)
	 */
	private static LinkedList<Nation> nations; //neutre, nation 1, nation 2

	public static final BasicLogger LOGGER;

	static
	{
		loader = new Yaml();
		LOGGER =  LoggerFactory.getLogger(ConquestPlugin.class);
		if(!ConfigYamlFile.checkConfigFile())
        {
            LOGGER.warning("Config files doesn't exists.\nCreating...");
            ConfigYamlFile.setBasicConfigFileArchetype();
        }
        else
            LOGGER.success("Config files loaded.");
	}

	{
		CONFIGFILEPATH.mkdirs();
		filePath = new File(new StringBuffer()
			.append(CONFIGFILEPATH.toString())
			.append(File.separator)
			.append("config.yml").toString()
			);
		if(!filePath.exists())
		{
			try
			{
				filePath.createNewFile();
			}catch(IOException ioe0)
			{
				LOGGER.error(ioe0.getMessage());
			}
		}
		core = new ConquestPluginCore(filePath);
		configFile = (ConfigYamlFile)core.getConfigFile();
        try
        {
            sqlConfigFile = ConfigYamlFile.getYamlFile(mysqlConfigFile);
        } catch (DataIntegrityException die0)
        {
            LOGGER.error(die0.getMessage());
        }
	}

	public ConquestPlugin()
	{
		server = Bukkit.getServer();
		playerProfiles = new LinkedList<>();
		Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");

        assert (worldGuard == null || !(worldGuard instanceof WorldGuardPlugin)) : "Le serveur n'a pas réussi à charger le plugin worldguard ou n'existe pas.";
        core.setwGPlugin(worldGuard);

	}


	private static void setNations(final Nation...nations)
	{
		ConquestPlugin.nations = (LinkedList) Arrays.asList(nations);
	}



	private static boolean datasIntegrityChecking(Map<String, String> data)
	{
		HashMap<String, String> dispoTab = new HashMap<>(); //Tableau associatif charge de recolter le status de chaque cles (presentes ou non)
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

    private void killTracer(EntityDeathEvent ede)
    {
        LivingEntity entity = ede.getEntity();
        Entity killer = entity.getKiller();
        if(entity instanceof Monster || entity instanceof Animals)
        {
            if(killer instanceof org.bukkit.entity.Player)
            {
                for(PlayerWrapper player : getPlayerProfilesList())
                {
                    if(player.getPlayerProfile().get("username").equals(killer.getName()))
                    {
                        player.addBattlePoints(1);
                    }
                }
            }
        }
        else if(entity instanceof org.bukkit.entity.Player)
        {
            for(PlayerWrapper player : getPlayerProfilesList())
            {
                if(player.getPlayerProfile().get("username").equals(killer.getName()))
                {
                    player.addBattlePoints(3);
                }
            }
        }
    }




    public static java.sql.Connection getBDDConnection(Map<String, String> data) throws DataIntegrityException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e)
        {
            LOGGER.error("Driver's class have not been loaded.");
        }
        if(datasIntegrityChecking(data))
        {
            Connection reference = null;
            boolean error = false;
            try
            {
                reference = DriverManager.getConnection(data.get("hostname"), data.get("username"), data.get("password"));
                return reference;
            } catch (SQLException sqle0)
            {
                error = true;
                LOGGER.error("An error was occurred while establishing connection: "+sqle0.getMessage());
            }finally
            {
                if(error)
                {
                    if(reference != null)
                    {
                        try
                        {
                            if(!reference.isClosed())
                            {
                                reference.close();
                            }
                        }catch(SQLException sqle1)
                        {
                            LOGGER.error(sqle1.getMessage());
                        }
                    }
                    else
                        LOGGER.warning("reference variable references nothing.");
                }
            }
        }
        else
            throw new DataIntegrityException("L'intégrité des données est compromise. Veillez à compléter correctement le document avant de relancer le plugin.");

        return null;
    }

	public static String formatClassName(Class<?> clazz)
	{
		return clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
	}

	public static LinkedList<Nation> getNations()
	{
		return ConquestPlugin.nations;
	}

	//----------------------------------------------//

	@Override
	public void onEnable()
	{
		long start = new Date().getTime();
		server.getPluginManager().registerEvents(this, this);
		core.addReachedZoneListener(this);
		final Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		this.getCommand(COMMANDCORE).setExecutor(new NationCommand());

        try
        {
            File tracer = new File(CONFIGFILEPATH.toString() + File.separator + "tracer.txt");
            tracer.createNewFile();
            System.setOut(new PrintStream(tracer));
        }catch(IOException ioe0)
        {
            LOGGER.error(ioe0.getMessage());
        }

		setNations
		(
			new Nation("Neutral", 0, score.registerNewTeam("neutral")),
			new Nation("Lutha", 1, score.registerNewTeam("lutha")),
			new Nation("Gondar", 2, score.registerNewTeam("gondar"))
	    );


        LOGGER.info(new StringBuilder().append("Start in ").append(Long.toString((new Date().getTime() - start))).append("ms").toString());
	}

	@Override
	public void onDisable()
	{
		pool.shutdown();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void whenPlayerMove(PlayerMoveEvent pme)
	{

			pool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					final org.bukkit.Location location = pme.getPlayer().getLocation();
                    LOGGER.info(new StringBuilder().append("Position du joueur:\n").append("x: ").append(location.getX()).append("\ny: ").append(location.getY()).append("\nz: ").append(location.getZ()).toString());
					core.setUserLocation(location);
				}
			});
	}

	@EventHandler
    public void whenEntityIsDead(org.bukkit.event.entity.EntityDeathEvent ede)
    {
        killTracer(ede);
    }

	@EventHandler
	public void whenPlayerJoin(PlayerJoinEvent pje)
	{
		//pje.getPlayer().setScoreboard(stn.getCurrentScoreboard());

	}

	@EventHandler
    public void whenPlayerQuit(PlayerQuitEvent pqe)
    {
        List<PlayerWrapper> playerProfilesOccurrence = new LinkedList<>();
		//On recupere toutes les occurrences du joueur si il en existe plusieurs. Il ne devrait y en avoir qu'une seule normalement, mais par mesure de sécurité, on procède à un nettoyage.
        for(PlayerWrapper playerWrapper: playerProfiles)
        {
            String username = (String)playerWrapper.getPlayerProfile().get(pqe.getPlayer().getName());
            if(username != null)
            {
                if(username.equals(pqe.getPlayer().getName()))
                {
                    playerProfilesOccurrence.add(playerWrapper);
                }
            }
        }
        try
        {
            sqlConnection = ConquestPlugin.getBDDConnection((Map)sqlConfigFile.getYamlMap());
            Statement userStatement = sqlConnection.createStatement();

        }catch(DataIntegrityException die1)
        {
            LOGGER.error(die1.getMessage());
        }catch(SQLException sqle2)
        {
            LOGGER.error(sqle2.getMessage());
        }
        playerProfiles.removeAll(playerProfilesOccurrence); //On libere la memoire
    }

	@Override
	public void whenZoneHasBeenReached(String regionName)
	{
		//TODO Afficher le nom de la region sur le scoreboard du joueur
	}

	public final LinkedList<PlayerWrapper> getPlayerProfilesList()
	{
		return this.playerProfiles;
	}

	public static void main(String... args)
	{

	}

}
