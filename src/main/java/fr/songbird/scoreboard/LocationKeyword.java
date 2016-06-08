package main.java.fr.songbird.scoreboard;

/**
 * Created by songbird on 07/06/16.
 */
public class LocationKeyword extends ScoreboardKeyword
{

    public static final String $LOCATION = "location";

    public LocationKeyword(String keyword) throws Exception
    {
        super(keyword);
        assert keyword != null : "keyword is "+keyword;
        if(!keyword.equals($LOCATION))
        {
            throw new Exception("keyword paramter does equals "+$LOCATION+", not '"+keyword+"'.");
        }
    }
}
