package main.java.fr.songbird.scoreboard;

/**
 * Created by songbird on 10/06/16.
 * @author Songbird
 */
public class ScoreWrapper
{
    private final String scoreTitle;
    private String scoreValue;
    private final int index;
    public ScoreWrapper(final String scoreTitle, final String scoreValue, final int index)
    {
        assert scoreTitle != null;
        assert scoreValue != null;
        assert index >= 0;
        this.scoreTitle = scoreTitle;
        this.scoreValue = scoreValue;
        this.index = index;
    }


    /**
     * Erase previous score value and apply the newer.
     * @param value
     */
    public void setScoreValue(final String value)
    {
        assert value != null;
        this.scoreValue = value;
    }
    public String getScoreContent()
    {
        return scoreTitle + scoreValue;
    }
}
