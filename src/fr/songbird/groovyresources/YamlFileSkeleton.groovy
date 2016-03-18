package fr.songbird.groovyresources

import org.yaml.snakeyaml.Yaml

class YamlFileSkeleton 
{
	
	public YamlFileSkeleton(String skeletonName, File yamlFilePath)
	{
		this.skeletonName = skeletonName;
		this.yamlFilePath = yamlFilePath;
	}
	
	
	public void mySqlYamlFileArchetype()
	{
		def yamlSkeleton = 
		[
			"hostname":null,
			"port": null,
			"username" : null,
			"password" : null
		]
		
		def yaml = new Yaml();
		
	}
	
}
