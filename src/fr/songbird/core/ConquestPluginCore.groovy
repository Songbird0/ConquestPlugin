package fr.songbird.core

import javax.swing.event.EventListenerList

import net.wytrem.logging.BasicLogger
import net.wytrem.logging.LoggerFactory

import org.bukkit.Location



class ConquestPluginCore 
{

	private static final BasicLogger LOGGER = LoggerFactory.getLogger(ConquestPluginCore.class);

	def ell = new EventListenerList();
	def x, y, z; //Coordonnées de la zone WorldGuard

	ConquestPluginCore(int x, int y, int z)
	{

	}



	public def addReachedZoneListener(ReachedZoneListener rzl)
	{
		ell.add(ReachedZoneListener.class, rzl);
	}
	
	private def getRZL()
	{
		return ell.getListeners(ReachedZoneListener.class);
	}

	private def fireWhenZoneHasBeenReached()
	{
		for(ReachedZoneListener rzl : getRZL())
		{
			rzl.whenZoneHasBeenReached();
		}
	}




}
