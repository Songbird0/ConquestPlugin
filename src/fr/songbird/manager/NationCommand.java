package fr.songbird.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.songbird.constants.ProgramConstants;

/**
 * Classe chargée de récupérer les commandes exécutées liées au plugin conquete.
 * @author songbird
 * @version 0.0.3_0-ALPHA
 */
public class NationCommand implements CommandExecutor, ProgramConstants
{
	
	
	
	public NationCommand()
	{
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender cmdSender, Command command, String label, String[] args)  
	{
		
		CommandEngine ce = this.new CommandEngine();
		
		if(cmdSender instanceof Player)
		{
			if(ce.checkCommandIntegrity(command, args))
			{
				((Player) cmdSender).getPlayer().sendMessage("Commande intègre et lancée avec succès !");
			}
			else
			{
				((Player) cmdSender).getPlayer().sendMessage("La commande est invalide.");
			}
		}
		
		return true;
	}
	
	/**
	 * Classe interne qui vérifie l'intégrité de la commande avant d'enchaîner les instructions suivantes.
	 * @author songbird
	 *
	 */
	class CommandEngine
	{
		
		
		public CommandEngine()
		{
			
		}
		
		/**
		 * 
		 * @param cmd Le mot-clé de la commande
		 * @param args Les arguments passés à la commande
		 * @return true si la commande ne comporte aucune erreur, sinon false.
		 */
		protected boolean checkCommandIntegrity(Command cmd, String...args)
		{
			final int argsLength = args.length;
			//Ternaire pour tester l'intégrité de la commande passée par l'utilisateur.
			return cmd.getName().equals(COMMANDCORE) ? 
					(argsLength == 2 ? (args[0].equals(COMMANDARG) ? 
							(args[1].equals(NATIONNAME[0]) ? 
									true : (args[1].equals(NATIONNAME[1]) ? 
											true : false)) : false) : false) : false;
		}
		
		
	}

	
	

}
