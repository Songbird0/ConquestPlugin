package main.java.fr.songbird.core

import com.sk89q.worldguard.bukkit.WGBukkit
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import main.java.fr.songbird.config.ConfigYamlFile
import main.java.fr.songbird.exceptions.DataIntegrityException
import net.wytrem.logging.BasicLogger
import net.wytrem.logging.LoggerFactory
import org.bukkit.Location
import org.bukkit.plugin.Plugin

import javax.swing.event.EventListenerList
import java.util.Map.Entry

class ConquestPluginCore
{

	private static final BasicLogger LOGGER = LoggerFactory.getLogger(ConquestPluginCore.class);

	/**
	* Liste des listeners pour les déplacements du joueur.
	*
	*/
	final def ell = new EventListenerList();


    final def configFile;


	/**
	* Reference vers la version de Worldguard en memoire
	*
	*/
	def wGPlugin

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
		assert yamlFile in File

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
		def x = userLocation.getX();
		def y = userLocation.getY();
		def z = userLocation.getZ();

		def regionsSet = WGBukkit.getRegionManager(configFile.getWorldName()).getApplicableRegions(userLocation)
		def nameRegionsSet = configFile.getNameRegionsSet()

		nameRegionsSet.entrySet().each
		{
            Entry<String, String> entry ->
			def value = entry.getValue()
			assert value != null: "Chaine de caracteres inexistante"

		}



		for(ProtectedRegion region : regionsSet)
		{

		}

	}


    def getUserLocation()
    {
        return userLocation
    }

    void setUserLocation(userLocation)
    {
        assert userLocation in Location : "userLocation isn't Location object"
        this.userLocation = userLocation
    }

	def getwGPlugin()
	{
		return wGPlugin
	}

	void setwGPlugin(Plugin wGPlugin)
	{
		this.wGPlugin = wGPlugin
	}

    def getConfigFile()
    {
        return configFile
    }




}
