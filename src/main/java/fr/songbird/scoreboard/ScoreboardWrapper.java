package main.java.fr.songbird.scoreboard;

import main.java.fr.songbird.config.ConfigYamlFile;
import main.java.fr.songbird.constants.ProgramConstants;
import main.java.fr.songbird.exceptions.DataIntegrityException;
import net.wytrem.logging.BasicLogger;
import net.wytrem.logging.LoggerFactory;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by songbird on 30/05/16.<br>
 * Encapsule les fonctionnalités du scoreboard pour y enregistrer divers objectifs et effectuer des opérations<br>
 * non couvertes par les outils de spigot.
 *
 * @author Songbird
 */
public class ScoreboardWrapper implements ProgramConstants
{
    /**
     * Reférence vers l'instance d'un nouveau scoreboard
     */
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    /**
     * Logger dédié de la classe.
     */
    private static final BasicLogger LOGGER = LoggerFactory.getLogger(ScoreboardWrapper.class);

    private ConfigYamlFile scoreboardConfig;

    {
        try
        {
            scoreboardConfig = ConfigYamlFile.getYamlFile(scoreboardConfigFile);
        }catch(DataIntegrityException die0)
        {
            LOGGER.error(die0.getMessage());
        }
    }

    public ScoreboardWrapper()
    {

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
