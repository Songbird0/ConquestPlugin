package main.java.fr.songbird.config

import main.java.fr.songbird.constants.ProgramConstants
import main.java.fr.songbird.exceptions.DataIntegrityException
import main.java.fr.songbird.groovyresources.YamlFileSkeleton


/**
*
* Classe représentant le fichier de configuration du plugin dans la mémoire.
*
*
*
*/
class ConfigYamlFile implements ProgramConstants
{
    private final HashMap<String, Object> yamlFile


    private ConfigYamlFile(final HashMap<String, Object> yamlFile)
    {
        this.yamlFile = yamlFile
    }


    //###### DYNAMIC/INSTANCE METHODS ######


    public String getWorldName() throws DataIntegrityException
    {
    	if(!null.equals(yamlFile.get("world")))
    	{
    		return yamlFile.get("world")
    	}
    	else
    	{
    		throw new DataIntegrityException("Nom du monde non reconnu.")
    	}
    }


    /**
    * getter pour le nom des regions visible sur le scoreboard
    */
    public def getNameRegionsSet()
    {

    	assert yamlFile.get("regions") in Map : "Regions n'est pas une table de hashage"
    	assert yamlFile.get("regions").get("theirName") in Map : "theirName n'existe pas"

    	return yamlFile.get("regions").get("theirName")
    }


    //###### STATIC METHODS ######


    public static ConfigYamlFile getYamlFile(File file) throws DataIntegrityException
    {
        def yamlFile = new YamlFileSkeleton(file).loadYamlFile()
        if(yamlFile in Map)
        {
            return new ConfigYamlFile((HashMap<String, Object>)yamlFile);
        }
        else
            throw new DataIntegrityException("Le fichier de configuration a subi une modification trop profonde de la part de l'utilisateur, l'intégrité des données attendues est compromise.")

    }

    /**
    *
    *    Ecriture du fichier de base et construction du fichier de configuration pour connecter le plugin a la base de donnees.
    *
    *
    */
    public static void setBasicConfigFileArchetype()
    {
        CONFIGFILEPATH.mkdirs();

        new YamlFileSkeleton
        (
            genericConfigFile,
            "generic.skeleton",
            [
                "world": [
                            "hisName": "your world name"
                         ],
                "regions" : [
                                "theirName" : ["nomRegion":"NomScoreboard"],
                                "theirLocation":["NomRegion":"CoordonneesXYZ"]
                            ],
                "devise" : [

                            "points de bataille":1,
                            "points d'honneur":1

                           ]
            ]
        )

        new YamlFileSkeleton(mysqlConfigFile, "mysql.skeleton")
    }
}
