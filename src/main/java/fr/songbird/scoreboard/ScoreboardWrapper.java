package main.java.fr.songbird.scoreboard;

import main.java.fr.songbird.config.ConfigYamlFile;
import main.java.fr.songbird.constants.ProgramConstants;
import main.java.fr.songbird.exceptions.DataIntegrityException;
import net.wytrem.logging.BasicLogger;
import net.wytrem.logging.LoggerFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * Created by songbird on 30/05/16.<br>
 * Encapsule les fonctionnalités du scoreboard pour y enregistrer divers objectifs et effectuer des opérations<br>
 * non couvertes par les outils de spigot.
 *
 * @author Songbird
 */
public class ScoreboardWrapper implements ProgramConstants
{

    private TimerKeyword tk = null;
    private LocationKeyword lk = null;

    /**
     * Reférence vers l'instance d'un nouveau scoreboard
     */
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    /**
     * Logger dédié à la classe.
     */
    private static final BasicLogger LOGGER = LoggerFactory.getLogger(ScoreboardWrapper.class);

    private ConfigYamlFile scoreboardConfig;
    private String colorPrefix;


    public ScoreboardWrapper()
    {

        loadScoreboardConfigFile();
        initializeKeywords();
        objectivesGeneration();
    }

    private void objectivesGeneration()
    {
        Object sas = scoreboardConfig.getYamlMap();
        assert sas instanceof HashMap;
        HashMap<String, Object> yamlMap = (HashMap<String, Object>)sas;
        Object objectivesList = yamlMap.get("Objectives");
        assert (objectivesList != null) : "objectives list doesn't exist.";
        assert (objectivesList instanceof List) : "objectivesList variable are not instance of List";
        for(Object objectives : (List)objectivesList)
        {
            assert(objectives instanceof Map) : "objectives variable are not instance of Map";
            Map map = (Map)objectives;
            Objective objective = scoreboard.registerNewObjective(getAdequateColor((String)map.get("color"))+(String)map.get("title"), (String)map.get("criteria"));
            Object objectiveScoresList = map.get("ObjectiveScores");
            assert objectiveScoresList != null : "objectiveScoresList="+objectiveScoresList.toString();
            assert objectiveScoresList instanceof List : "objectiveScoresList="+objectiveScoresList.getClass();
            List list = (List)objectiveScoresList;
            for(Object scoreProperties : list)
            {
                assert scoreProperties instanceof Map : "scoreProperties="+scoreProperties;
                Map scorePropertiesMap = (Map)scoreProperties;
                String name = (String)scorePropertiesMap.get("name");
                String coloredName = getAdequateColor((String)scorePropertiesMap.get("color")) + name;
                String value = (String)scorePropertiesMap.get("value");
                int index = (int)scorePropertiesMap.get("index");

                Score score = objective.getScore(coloredName + value);

                if(tk.getPattern().matcher(value).matches())
                {
                     tk.addTimerListener(score);
                }
            }
        }
    }

    private ChatColor getAdequateColor(String word)
    {
        String wordUC = word.toUpperCase();
        if(wordUC.contains("AQUA"))
        {
            colorPrefix = "AQUA";
            return ChatColor.AQUA;
        }
        else if(wordUC.contains("BLACK"))
        {
            colorPrefix = "BLACK";
            return ChatColor.BLACK;
        }
        else if(wordUC.contains("BLUE"))
        {
            colorPrefix = "BLUE";
            return ChatColor.BLUE;
        }
        else if(wordUC.contains("BOLD"))
        {
            colorPrefix = "BOLD";
            return ChatColor.BOLD;
        }
        else if(wordUC.contains("DARK_AQUA"))
        {
            colorPrefix = "DARK_AQUA";
            return ChatColor.DARK_AQUA;
        }
        else if(wordUC.contains("DARK_BLUE"))
        {
            colorPrefix = "DARK_BLUE";
            return ChatColor.DARK_BLUE;
        }
        else if(wordUC.contains("DARK_GRAY"))
        {
            colorPrefix = "DARK_GRAY";
            return ChatColor.DARK_GRAY;
        }
        else if(wordUC.contains("DARK_GREEN"))
        {
            colorPrefix = "DARK_GREEN";
            return ChatColor.DARK_GREEN;
        }
        else if(wordUC.contains("DARK_PURPLE"))
        {
            colorPrefix = "DARK_PURPLE";
            return ChatColor.DARK_PURPLE;
        }
        else if(wordUC.contains("DARK_RED"))
        {
            colorPrefix = "DARK_RED";
            return ChatColor.DARK_RED;
        }
        else if(wordUC.contains("GOLD"))
        {
            colorPrefix = "GOLD";
            return ChatColor.GOLD;
        }
        else if(wordUC.contains("GRAY"))
        {
            colorPrefix = "GRAY";
            return ChatColor.GRAY;

        }
        else if(wordUC.contains("GREEN"))
        {
            colorPrefix = "GREEN";
            return ChatColor.GREEN;
        }
        else if(wordUC.contains("ITALIC"))
        {
            colorPrefix = "ITALIC";
            return ChatColor.ITALIC;
        }
        else if(wordUC.contains("LIGHT_PURPLE"))
        {
            colorPrefix = "LIGHT_PURPLE";
            return ChatColor.LIGHT_PURPLE;
        }
        else if(wordUC.contains("MAGIC"))
        {
            colorPrefix = "MAGIC";
            return ChatColor.MAGIC;
        }
        else if(wordUC.contains("RED"))
        {
            colorPrefix = "RED";
            return ChatColor.RED;
        }
        else if(wordUC.contains("YELLOW"))
        {
            colorPrefix = "YELLOW";
            return ChatColor.YELLOW;
        }

        return null;
    }

    /**
     * Initializes keywords.
     * @see {@link ScoreboardWrapper#tk}
     * @see {@link ScoreboardWrapper#lk}
     */
    private void initializeKeywords()
    {
        try
        {
            tk = new TimerKeyword("timer");
            lk = new LocationKeyword("location");
        }catch(Exception e0)
        {
            e0.printStackTrace();
        }
    }


    /**
     * Loads scoreboard configuration file in the memory
     * @see {@link ScoreboardWrapper#scoreboardConfig}
     */
    private void loadScoreboardConfigFile()
    {
        try
        {
            scoreboardConfig = ConfigYamlFile.getYamlFile(scoreboardConfigFile);
        }catch(DataIntegrityException die0)
        {
            LOGGER.error(die0.getMessage());
        }
    }

    /**
     * Example:<br>
     * <code>
     *     new ScoreboardWrapper().formatMe(2.5); // => 2:30:0 (2 hours:30 minutes:0 seconds
     * </code>
     * @param hour Can be a Double|Integer object
     * @return formatted hour
     * @throws Exception
     */
    public String formatMe(Number hour) throws Exception, AssertionError
    {
        assert (hour instanceof Double || hour instanceof Integer) : "Must be Double|Integer instance. hour="+hour;
        if((double)hour > 0.0)
        {
            int base = (int)hour * 3600;
            int base_minute = base/60;
            int seconds = base%60;
            int hours = base_minute/60;
            int minutes = base_minute%60;

            return Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
        }

        throw new Exception("hour =< 0");
    }

    public Scoreboard getCurrentScoreboard()
    {
        return scoreboard;
    }

}
