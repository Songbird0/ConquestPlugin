package fr.songbird.groovyresources;

import org.yaml.snakeyaml.Yaml;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.InputStream;


import static fr.songbird.manager.ConquestPlugin.LOGGER;

class YamlFileSkeleton 
{
	def skeletonName
	def yamlFilePath
	
	/**
	 * Constructeur dédié au chargement d'un fichier yaml, et non à l'écriture.
	 * @param yamlFilePath  Chemin du fichier à charger en mémoire
	 * @see {@link YamlFileSkeleton#YamlFileSkeleton(File, String)}
	 */
	YamlFileSkeleton(File yamlFilePath)
	{
		this.yamlFilePath = yamlFilePath;
	}
	
	/**
	 * Constructeur dédié à l'écriture d'un fichier yaml.
	 * @param yamlFilePath Chemin du fichier à écrire
	 * @param skeletonName Nom de la structure de base du fichier à écrire ("sql" pour générer un fichier de configuration dédié à la connexion à une bdd; "generic" si vous voulez personnaliser votre fichier de configuration facilement)
	 * @see {@link YamlFileSkeleton#mySqlYamlFileArchetype()}
	 * @see {@link YamlFileSkeleton#genericYamlFileArchetype(Map)}
	 */
	YamlFileSkeleton(File yamlFilePath, String skeletonName)
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
		if(genericYamlSkeleton in [java.util.Map, java.util.List])
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
	
	
	public Object loadYamlFile(File yamlFile)
	{
		InputStream inputStream = new FileInputStream(yamlFile);
		
		def yaml = new Yaml();
		def object = yaml.load(inputStream);
		
		return object;
	}
	
}
