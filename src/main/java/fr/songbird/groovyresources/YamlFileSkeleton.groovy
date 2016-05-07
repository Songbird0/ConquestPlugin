package main.java.fr.songbird.groovyresources

import main.java.fr.songbird.exceptions.DataIntegrityException
import org.yaml.snakeyaml.Yaml

import static main.java.fr.songbird.manager.ConquestPlugin.LOGGER

class YamlFileSkeleton implements YamlFileSkeletonKeywords
{
	final def yamlFilePath

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
	 * @see {@link YamlFileSkeleton#YamlFileSkeleton(File, String, Map)}
	 * @throws DataIntegrityException
	 */
	YamlFileSkeleton(File yamlFilePath, String skeletonName) throws DataIntegrityException
	{
		this.yamlFilePath = yamlFilePath;


		switch(skeletonName)
		{
			case sqlKeyword : mySqlYamlFileArchetype(); break;
			default:
				StringBuilder builder = new StringBuilder();
				builder.append("Le mot-clé que vous avez renseigné (").append(skeletonName).append(") est inconnu, ou n'a pas été utilisé avec le constructeur adéquat.\nVeuillez vous référer à la javadoc.")
				throw new DataIntegrityException(builder.toString());
		}
	}

	/**
	*
	* Constructeur dédié à l'écriture d'un fichier yaml non pris en charge par la classe. (archétype inconnu)
	* @param yamlFilePath Chemin du fichier à écrire
	* @param skeletonName Nom de la structure de base du fichier à écrire (Si la structure n'est pas reconnu, le nom doit forcément être "generic.skeleton")
	* @param yamlFileContent Contenu du fichier si le mot-clé est "generic.skeleton" (peut contenir actuellement tous les objets fils de la classe Map et List)
	* @see {@link YamlFileSkeleton#YamlFileSkeleton(File)}
	* @see {@link YamlFileSkeleton#YamlFileSkeleton(File, String)}
	* @see {@link YamlFileSkeleton#genericYamlFileArchetype(Map)}
	*/
	YamlFileSkeleton(File yamlFilePath, String skeletonName, def yamlFileContent)
	{
		this.yamlFilePath = yamlFilePath
		genericYamlFileArchetype(yamlFileContent)
	}




	private void mySqlYamlFileArchetype()
	{
		def yamlSkeleton =
		[
			"hostname":null,
			"port": null,
			"username" : null,
			"password" : null
		]

		genericYamlFileArchetype(yamlSkeleton)
	}

	/**
	* Generation d'un fichier yaml basé sur la map passée en paramètre par l'utilisateur: genericYamlFileArchetype(def maMap = [:])
	*/
	private void genericYamlFileArchetype(genericYamlSkeleton) throws DataIntegrityException
	{
		if(genericYamlSkeleton in Map || genericYamlSkeleton in List)
		{
			FileWriter writer = null;
			def yaml = new Yaml();
			def yString = yaml.dump(genericYamlSkeleton);
			try
			{
				writer = new FileWriter((File)yamlFilePath);
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
		else
		{
			throw new DataIntegrityException("Le fichier yaml ne contient ni une liste, ni de tableau associatif.")
		}
	}


	public Object loadYamlFile()
	{
		InputStream inputStream = new FileInputStream((File)yamlFilePath);

		def yaml = new Yaml();
		def object = yaml.load(inputStream);

		return object;
	}

}
