package main.java.fr.songbird.constants;

import java.io.File;

/**
 * Constantes du projet. (Nation, arguments de commande, chemins de fichiers de configuration)
 * @author songbird
 *
 */
public interface ProgramConstants
{
	/**
	 * Le seul et unique argument que la commande nation est censée recevoir.
	 */
	String COMMANDARG = "join";
    /**
     * Tableau contenant le nom de chaque nation. (Actuellement: Lutha, Gondar)
     */
	String[] NATIONNAME = new String[]{"Lutha", "Gondar"};
    /**
     * Nom de la commande. (En l'occurrence: nation)
     */
	String COMMANDCORE = "nation";
    /**
     * Chemin du répertoire dans lequel tous les fichiers de configuration propres à ce plugin devront se trouver.
     */
	File CONFIGFILEPATH = new File(new StringBuffer().append(System.getProperty("user.home")).append(File.separator).append("ConfigConquestPlugin").toString());
    /**
     * Chemin du fichier de configuration du plugin.
     */
	File genericConfigFile = new File(new StringBuffer().append(CONFIGFILEPATH.toString()).append(File.separator).append("config.yml").toString());
    /**
     * Chemin du fichier de configuration de la base de donnée.
     */
    File mysqlConfigFile = new File(new StringBuffer().append(CONFIGFILEPATH.toString()).append(File.separator).append("mysqlconfig.yml").toString());
    /**
     * Chemin du fichier de configuration du scoreboard.
     */
    File scoreboardConfigFile = new File(new StringBuffer().append(CONFIGFILEPATH.toString()).append(File.separator).append("scoreboardConfig.yml").toString());

}
