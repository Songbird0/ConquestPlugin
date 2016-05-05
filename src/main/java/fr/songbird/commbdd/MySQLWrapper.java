package main.java.fr.songbird.commbdd;


import com.huskehhh.mysql.mysql.MySQL;

public class MySQLWrapper extends MySQL
{
	

	/**
	 * Le constructeur MySQLWrapper peut être utilisé seul, mais peut également être géré entièrement par des procédures de vérification intégrées au plugin pour veiller à l'intégrité du document avant son utilisation.
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 */
	public MySQLWrapper(String hostname, String port, String username, String password) 
	{
		super(hostname, port, username, password);
	}

}
