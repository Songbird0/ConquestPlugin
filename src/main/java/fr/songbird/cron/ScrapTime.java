package main.java.fr.songbird.cron;



/**
 * Represents a time schedule. (a scrap time)
 * @author Songbird
 */
public class ScrapTime
{
    /**
     * Seconds part.
     */
    private int seconds = 0;
    /**
     * Minutes part.
     */
    private int minutes = 0;
    /**
     * Hours part.
     */
    private int hours = 0;
    /**
     * Earlier variables sum.
     */
    private int sum = 0;

    /**
     * If all parameters are null (equals 0), then time will be equals to midnight.
     * For example:
     * new ScrapTime(0, 0, 0); // => midnight
     * new ScrapTime(0); //midnight
     * @param seconds May be used for write the time in seconds only.
     * @param minutes (optionnal) May be used for write the time in seconds and minutes only. (or minutes only)
     * @param hours (optionnal) May be used for write the time with the three variables.
     */
    public ScrapTime(final int seconds, final int minutes, final int hours) throws Exception
    {
        if(seconds >= 0
                && minutes >= 0
                && hours >= 0
                )
        {
            this.seconds = seconds;
            this.minutes = minutes;
            this.hours = hours;
            this.sum = seconds + (minutes*60 + hours*3600);
        }
        else
        {
            throw new Exception("Error, one of parameters are negative");
        }
    }

    public ScrapTime(final int seconds, final int minutes) throws Exception
    {
        new ScrapTime(seconds, minutes, 0);
    }


    public ScrapTime(final int seconds) throws Exception
    {
        new ScrapTime(seconds, 0, 0);
    }


    /**
     * Gets schedule's seconds
     */
    public final int getSeconds()
    {
        return seconds;
    }

    /**
     * Gets schedule's minutes
     */
    public final int getMinutes()
    {
        return minutes;
    }

    /**
     * Gets schedule's hours
     */
    public final int getHours()
    {
        return hours;
    }

    /**
     * Gets time in ms
     */
    public final int toMs()
    {
        return (seconds + (minutes*60) + (hours*3600))*1000;
    }

    /**
     * Converts ms in seconds
     */
    public final int toSeconds(final int ms)
    {
        return ms/1000;
    }

    /**
     * Gets time sum
     */
    public final int getSum()
    {
        return this.sum;
    }
}
