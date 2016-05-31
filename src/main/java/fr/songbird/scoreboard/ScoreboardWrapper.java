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
     * Reférence vers l'instance du scoreboard
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

}
