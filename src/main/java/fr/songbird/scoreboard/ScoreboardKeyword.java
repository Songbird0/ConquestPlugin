package main.java.fr.songbird.scoreboard;

import org.bukkit.ChatColor;

import java.util.regex.Pattern;

/**
 * @author Songbird
 */
public abstract class ScoreboardKeyword
{
    /**
     * Must begin with '$' token.<br>
     * Example:<br>
     * $myAwesomeVariable
     * pattern.matcher("$myAwesomeVariable").matches(); // => true
     */
    private final Pattern variablePattern;

    public ScoreboardKeyword(String keyword)
    {
        variablePattern = Pattern.compile(new StringBuilder("^").append("\\").append("$").append(keyword).append("$").toString());
    }


    public final Pattern getPattern()
    {
        return variablePattern;
    }
}
