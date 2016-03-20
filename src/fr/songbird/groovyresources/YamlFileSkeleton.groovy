package fr.songbird.groovyresources;

import org.yaml.snakeyaml.Yaml;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;


import static fr.songbird.manager.ConquestPlugin.LOGGER;

class YamlFileSkeleton 
{
	def skeletonName
	def yamlFilePath
	
	YamlFileSkeleton(String skeletonName, File yamlFilePath)
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

		def yString = yaml.dump(yamlSkeleton);

		FileWriter writer = null;
		try
		{
			writer = new FileWriter(yamlFilePath);
			writer.write(yString);
		}
		catch(FileNotFoundException fnf0)
		{
			LOGGER.error(fnf0.getMessage());
		}
		catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
		}
		finally
		{
			writer.flush();
			writer.close();
		}

		
	}

	/**
	* Generation d'un fichier yaml basé sur la map passée en paramètre par l'utilisateur: genericYamlFileArchetype(def maMap = [:])
	*/
	public void genericYamlFileArchetype(genericYamlSkeleton)
	{
		if(genericYamlSkeleton in java.util.Map)
		{
			FileWriter writer = null;
			def yaml = new Yaml();
			def yString = yaml.dump(genericYamlSkeleton);
			try 
			{
				yamlFilePath.createNewFile();
				writer = new FileWriter(yamlFilePath);
				writer.write(yString);
			}
			catch(FileNotFoundException fnf1) 
			{
				LOGGER.error(fnf1.getMessage());
				Runtime.getRuntime().exit(0x1);
			}
			catch(IOException ioe1)
			{
				LOGGER.error(ioe1.getMessage());
				Runtime.getRuntime.exit(0x1);
			}
			finally
			{
				if(!null.equals(writer))
				{
					writer.flush();
					writer.close();
				}
			}
			
		}
	}
	
}
