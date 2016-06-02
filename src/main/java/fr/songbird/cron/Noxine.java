package main.java.fr.songbird.cron;

import javax.swing.event.EventListenerList;
import java.util.Calendar;

/**
 * Created by songbird on 01/06/16.
 * Nox class extension.
 * @author Songbird
 */
public class Noxine extends Nox
{
    private EventListenerList listenersList = new EventListenerList();
    private boolean timeHasBeenReached = false;

    public Noxine(final int seconds, final int minutes, final int hours) throws Exception
    {
        super(seconds, minutes, hours);
    }

    public Noxine(final int seconds, final int minutes) throws Exception
    {
        super(seconds, minutes, 0);
    }

    public Noxine(final int seconds) throws Exception
    {
        super(seconds, 0, 0);
    }



    public final void computeTimeLeft()
    {
       final int scrapTimeResult = new ScrapTime(Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR).getSum();
       if(scrapTimeResult >= timeEnding.getSum())
       {
           timeHasBeenReached = true;
           fireWhenOffsetHasBeenReached();
       }
    }

    public final void addEventListener(ReachedTimeListener rtl)
    {
        listenersList.add(ReachedTimeListener.class, rtl);
    }

    protected final void fireWhenOffsetHasBeenReached()
    {
        for(ReachedTimeListener rtl : listenersList.getListeners(ReachedTimeListener.class))
        {
            rtl.whenOffsetHasBeenReached();
        }
    }




}
