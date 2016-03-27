package fr.songbird.core

import javax.swing.event.EventListenerList

import net.wytrem.logging.BasicLogger
import net.wytrem.logging.LoggerFactory

import org.bukkit.Location



class ConquestPluginCore 
{

	private static final BasicLogger LOGGER = LoggerFactory.getLogger(ConquestPluginCore.class);

	private def ell = new EventListenerList();
	private int x, y, z; //Coordonnées de la zone WorldGuard

	ConquestPluginCore()
	{

	}

	
	def getRZL()
	{
		return ell.getListeners(ReachedZoneListener.class);
	}

	def addReachedZoneListener(ReachedZoneListener rzl)
	{
		ell.add(ReachedZoneListener.class, rzl);
	}

	private def fireWhenZoneHasBeenReached()
	{
		for(ReachedZoneListener rzl : getRZL())
		{
			rzl.whenZoneHasBeenReached();
		}
	}




}
