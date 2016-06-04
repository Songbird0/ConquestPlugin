package main.java.fr.songbird.cron;

import net.wytrem.logging.BasicLogger;
import net.wytrem.logging.LoggerFactory;

import javax.swing.event.EventListenerList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by songbird on 01/06/16.<br>
 * Nox class extension.<br>
 * Using:<br>
 * <pre>
 * Calendar c = new GregorianCalendar();
 * Noxine noxine = new Noxine(c.get(Calendar.SECOND), c.get(Calendar.MINUTE), c.get(Calendar.HOUR));
 * noxine.setOffset(10);
 * noxine.addEventListener(noxine.new MyTest());
 * </pre>
 * @author Songbird
 */
public class Noxine extends Nox
{
    private EventListenerList listenersList = new EventListenerList();
    private final BasicLogger LOGGER = LoggerFactory.getLogger(Noxine.class);

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
        int scrapTimeResult = 0;
        Calendar c = new GregorianCalendar();
        try
        {
            scrapTimeResult = new ScrapTime(c.get(Calendar.SECOND), c.get(Calendar.MINUTE), c.get(Calendar.HOUR)).getSum();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
       if(scrapTimeResult >= timeEnding.getSum())
       {
           LOGGER.info("Time has been reached.");
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
