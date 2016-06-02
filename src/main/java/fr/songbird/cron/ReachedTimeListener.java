package main.java.fr.songbird.cron;

import java.util.EventListener;

/**
 * Created by songbird on 01/06/16.
 */
public interface ReachedTimeListener extends EventListener
{
    void whenOffsetHasBeenReached();
}
