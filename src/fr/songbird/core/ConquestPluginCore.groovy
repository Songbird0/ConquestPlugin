package fr.songbird.core

import javax.swing.event.EventListenerList

import net.wytrem.logging.BasicLogger
import net.wytrem.logging.LoggerFactory

import org.bukkit.Location

import fr.songbird.groovyresources.YamlFileSkeleton
import fr.songbird.config.ConfigYamlFile

import fr.songbird.exceptions.DataIntegrityException



class ConquestPluginCore 
{

	private static final BasicLogger LOGGER = LoggerFactory.getLogger(ConquestPluginCore.class);

	/**
	* Liste des listeners pour les déplacements du joueur.
	*
	*/
	final def ell = new EventListenerList();

	final def configFile;


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
	* @yamlFile Chemin du fichier de configuration. (Le constructeur attend ici un objet File)
	*/
	ConquestPluginCore(yamlFile)
	{
		assert yamlFile in java.io.File

		try 
		{
			configFile = ConfigYamlFile.getYamlFile(yamlFile)
		}
		catch(DataIntegrityException die0) 
		{
			LOGGER.error(die0.getMessage())	
		}
		
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
