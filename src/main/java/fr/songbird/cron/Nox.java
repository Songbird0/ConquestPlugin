package main.java.fr.songbird.cron;

/**
 * One Xelor for govern the Time.
 * <br><br>
 * Nox class is a tiny task scheduler.
 *
 * @author Songbird
 */
public class Nox
{
    /**
     * Schedule of time that represents the beginning.<br>
     * For example:<br>
     * Begin at 13h. (1pm)
     */
    ScrapTime timeBeginning = null;

    /**
     * Time offset between the beginning and the end.<br>
     * For example:<br>
     * Begin at: 13h. (1pm)<br>
     * Finish at: 18h. (6pm)<br>
     * Offset = 18 - 13 = 5 hours.
     */
    ScrapTime offset = null;

    /**
     * Schedule of time that represents the ending.<br>
     * For example:<br>
     * Begin at: 13h. (1pm)<br>
     * Offset = 5 hours.<br>
     * Finish at: 13 + 5 = 18h. (6pm)<br>
     */
    ScrapTime timeEnding = null;

    /**
     * Initializes timeBeginning value.<br>
     * @param seconds seconds of timeBeginning variable
     * @param minutes (optionnal) You could initialize the timeBeginning value with seconds parameter only.
     * @param hours (optionnal) You could initialize the timeBeginning value with seconds or minutes parameter only.
     */
    public Nox(final int seconds, final int minutes, final int hours) throws Exception
    {
        if(!(seconds >= 0)
             && !(minutes >= 0)
             && !(hours >= 0))
        {
           throw new Exception
           (
               new StringBuilder("One (or greater) of parameters is negative:\n")
               .append("second=")
               .append(seconds)
               .append("\n")
               .append("minutes=")
               .append(minutes)
               .append("\n")
               .append("hours=")
               .append(hours).toString()
           );
        }
        this.timeBeginning = new ScrapTime(seconds, minutes, hours);
    }

    /**
     * Initializes the offset and compute the end time.
     * @param seconds Seconds offset time
     * @param minutes Minutes offset time
     * @param hours Hours offset time
     */
    public final void setOffset(final int seconds, final int minutes, final int hours)
    {
        this.offset = new ScrapTime(seconds, minutes, hours);
        this.timeEnding = new ScrapTime(offset.getSum() + timeBeginning.getSum());
    }

    /**
     * Overloaded setOffset method.
     * @param seconds Seconds offset time
     * @param minutes Minutes offset time
     * @see {@link Nox#setOffset(int, int, int)}
     */
    public final void setOffset(final int seconds, final int minutes)
    {
        setOffset(seconds, minutes, 0);
    }

    /**
     * Overloaded setOffset method.
     * @param seconds Seconds offset time.
     * @see {@link Nox#setOffset(int, int)}
     * @see {@link Nox#setOffset(int, int, int)}
     */
    public final void setOffset(final int seconds)
    {
        setOffset(seconds, 0, 0);
    }

    /**
     * You can put a floating minutes ! :-)<br>
     * Converts floating minutes in seconds.
     * @param minutes Floating minutes
     */
    public final int convertDoubleMinutes(final double minutes)
    {
        return (int)(60*minutes);
    }

    /**
     * Converts integer minutes in seconds.
     * @param minutes integer minutes
     */
    public final int convertIntegerMinutes(final int minutes)
    {
        return (60*minutes);
    }

    /**
     * Converts seconds in ms.
     * @param seconds
     */
    public final int toMS(final int seconds)
    {
        return seconds*1000;
    }

    /**
     * You can put a floating hours ! :-)<br>
     * Converts floating hours in seconds.
     * @param hours floating hours
     */
    public final int convertDoubleHours(final double hours)
    {
        return (int)(hours*3600);
    }

    /**
     * Converts integer hours in seconds.
     * @param hours integer hours.
     */
    public final int convertIntegerHours(final int hours)
    {
        return (3600*hours);
    }
}
