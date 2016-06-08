package main.java.fr.songbird.scoreboard;

/**
 * Created by songbird on 07/06/16.
 */
public class TimerKeyword extends ScoreboardKeyword
{


    private static final String $TIMER = "timer";

    public TimerKeyword(String keyword) throws Exception
    {
        super(keyword);
        assert keyword != null : "keyword is "+keyword;
        if(!keyword.equals("timer"))
        {
            throw new Exception("keyword parameter does equals "+$TIMER+", not '"+keyword+"'.");
        }
    }
}
