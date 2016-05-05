package main.java.fr.songbird.constants;

import java.io.File;

/**
 * Constantes du projet. (Nation, et arguments de commande)
 * @author songbird
 *
 */
public interface ProgramConstants
{

	public final String COMMANDARG = new String ("join");
	public final String[] NATIONNAME = new String[]{"Lutha", "Gondar"};
	public final String COMMANDCORE = new String("nation");
	public final File CONFIGFILEPATH = new File(new StringBuffer().append(System.getProperty("user.home")).append(File.separator).append("ConfigConquestPlugin").toString());

}
