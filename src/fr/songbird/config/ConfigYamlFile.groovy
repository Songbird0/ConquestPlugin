package fr.songbird.config


import fr.songbird.groovyresources.YamlFileSkeleton

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




	public static ConfigYamlFile getYamlFile(File file)
	{
		Object yamlFile = new YamlFileSkeleton().loadYamlFile(file);
		if(yamlFile in java.util.Map)
		{
			return new ConfigYamlFile((HashMap<String, Object>)yamlFile);
		}
		else
		{
			return null;
		}
	}
}
