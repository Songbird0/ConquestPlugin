package main.java.fr.songbird.config

import main.java.fr.songbird.constants.ProgramConstants
import main.java.fr.songbird.exceptions.DataIntegrityException
import main.java.fr.songbird.groovyresources.YamlFileSkeleton


/**
*
* Classe représentant un fichier (au format yaml) de configuration quelconque dans la mémoire.
*
*/
class ConfigYamlFile implements ProgramConstants
{
    /**
     * Structure du fichier yaml représenté en mémoire.
     */
    private final HashMap<String, Object> yamlFile

    /**
     * Charge le fichier de configuration en mémoire.
     * @param yamlFile Structure du fichier yaml représenté en mémoire.
     */
    private ConfigYamlFile(final HashMap<String, Object> yamlFile)
    {
        this.yamlFile = yamlFile
    }


    //###### DYNAMIC/INSTANCE METHODS ######

    /**
     *
     * @return yamlFile Structure du fichier yaml représenté en mémoire.
     */
    public def getYamlMap()
    {
        return yamlFile;
    }

    /**
     * Renvoie le nom du monde dans lequel se trouve le plugin.
     * @return worldName
     * @throws DataIntegrityException
     */
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


    public def get(racine)
    {
        assert racine in String || racine in Integer: "Le parametre n'est pas une chaine de caracteres."
        def value = null;
        assert  (value = yamlFile.get(racine)).getClass() in [String.class, Integer.class, Map.class]: "La clé est introuvable."
        return value in String ?
                (String)value : value in Integer ?
                (Integer)value : value in Map ?
                (Map)value : null
    }

    public def get(racine, children)
    {
        assert (racine.class in [String.class, Integer.class]) && (children.class in [String.class, Integer.class]) : "les parametres ne sont pas de type String ou Integer"
        def racineValue = get(racine)
        assert racineValue in Map : "Surchage de la methode inutile, ne comporte pas de fils."
        def childrenValue = racineValue.get(children)
        assert childrenValue != null : "childrenValue is $childrenValue"
        return childrenValue

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
        if(!checkConfigFile())
        {
            CONFIGFILEPATH.mkdirs();

            new YamlFileSkeleton
                    (
                            genericConfigFile,
                            "generic.skeleton",
                            [
                                    "world":"your world name",
                                    "regions" :
                                    [
                                            "nomRegion1":"NomScoreboard1",
                                            "nomRegion2":"NomScoreboard2"
                                    ],
                                    "devise" :
                                    [
                                            "points d'honneur":1
                                    ],
                                    "villages":
                                            [
                                                    "monday":"villageName",
                                                    "tuesday":"villageName",
                                                    "wednesday":"villageName",
                                                    "thursday":"villageName",
                                                    "friday":"villageName"
                                            ]
                            ]
                    )

            new YamlFileSkeleton(mysqlConfigFile, "mysql.skeleton")
            new YamlFileSkeleton
                    (
                            scoreboardConfigFile,
                            "generic.skeleton",
                            [
                                    "Objectives":
                                            [
                                                    [
                                                            "title":"MyAwesomeObjective",
                                                            "color":"blue",
                                                            "criteria":"dummy",
                                                            "ObjectiveScores":
                                                                    [
                                                                            [
                                                                                    "name":"myAwesomeScore",
                                                                                    "value":0,
                                                                                    "color":"aqua"
                                                                            ],
                                                                            [
                                                                                    "name":"AnotherScore",
                                                                                    "value":1,
                                                                                    "color":"red"
                                                                            ]

                                                                    ]
                                                    ],
                                                    [
                                                            "title":"anotherObjective",
                                                            "color":"multicolor",
                                                            "criteria":"dummy",
                                                            "ObjectivesScores":
                                                                    [
                                                                            [
                                                                                    "name":"scoreName",
                                                                                    "value":0,
                                                                                    "color":"purple"
                                                                            ]
                                                                    ]
                                                    ]
                                            ],
                                    "rotation":false

                            ]
                    )
        }

    }

    public static boolean checkConfigFile()
    {
        if(genericConfigFile.exists()
                && mysqlConfigFile.exists()
                && scoreboardConfigFile.exists())
        {
            return true;
        }
        return false;
    }

    public static void main(String... strings)
    {
        setBasicConfigFileArchetype();
    }
}
