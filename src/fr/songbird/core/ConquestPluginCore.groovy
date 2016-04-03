package fr.songbird.core

import javax.swing.event.EventListenerList

import net.wytrem.logging.BasicLogger
import net.wytrem.logging.LoggerFactory

import org.bukkit.Location

import fr.songbird.groovyresources.YamlFileSkeleton



class ConquestPluginCore 
{

	private static final BasicLogger LOGGER = LoggerFactory.getLogger(ConquestPluginCore.class);

	final def ell = new EventListenerList();
	/*
	* Coordonnées de la zone WorldGuard
	*/
	synchronized def x, y, z;

	/*
	* Informations relatives à la position du joueur
	*/
	synchronized def userLocation; 

	synchronized def RegionsSet;

	/*
	* Constructeur par défaut - destiné à recevoir les coordonnées du joueur cible dans un temps différent de celui de son appel.
	*/
	ConquestPluginCore()
	{

	}

	private def getRZL()
	{
		return ell.getListeners(ReachedZoneListener.class);
	}

	private synchronized def fireWhenZoneHasBeenReached()
	{
		for(ReachedZoneListener rzl : getRZL())
		{
			rzl.whenZoneHasBeenReached();
		}
	}




	public def addReachedZoneListener(ReachedZoneListener rzl)
	{
		ell.add(ReachedZoneListener.class, rzl);
	}


	/*
	* Va vérifier à chaque appel si le joueur se trouve dans les limites d'une zone.
	*
	*/
	public synchronized def run()
	{

		

	}




}
