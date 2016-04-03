package fr.songbird.config


import fr.songbird.groovyresources.YamlFileSkeleton
import fr.songbird.exceptions.DataIntegrityException

/**
*
* Classe représentant le fichier de configuration du plugin dans la mémoire.
*
*
*
*/
class ConfigYamlFile 
{
	private HashMap<String, Object> yamlFile;


	private ConfigYamlFile(final HashMap<String, Object> yamlFile)
	{
		this.yamlFile = yamlFile
	}




	public static ConfigYamlFile getYamlFile(File file) throws DataIntegrityException
	{
		Object yamlFile = new YamlFileSkeleton().loadYamlFile(file);
		if(yamlFile in java.util.Map)
		{
			return new ConfigYamlFile((HashMap<String, Object>)yamlFile);
		}
		else
		{
			throw new DataIntegrityException("Le fichier de configuration a subi une modification trop profonde de la part de l'utilisateur, l'intégrité des données attendues est compromise.")
		}
	}
}
