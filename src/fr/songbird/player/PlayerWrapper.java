package fr.songbird.player;

import static fr.songbird.manager.ConquestPlugin.LOGGER;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import fr.songbird.manager.ConquestPlugin;
import fr.songbird.nation.Nation;

public class PlayerWrapper 
{
	private JSONObject profile;
	private static File bddPath;
	
	{
		profile = new JSONObject();
	}
	
	static
	{

		StringBuilder builder = new StringBuilder();
		builder.append(ConquestPlugin.formatClassName(ConquestPlugin.class));
		builder.append("_lib");
		builder.append(File.separator);
		builder.append("bdd");
		bddPath = new File (builder.toString());
	}
	
	
	@SuppressWarnings("unchecked")
	public PlayerWrapper(final Player player, final Nation nation)
	{
		profile.put("username", player.getName());
		profile.put("uuid", player.getUniqueId());
		profile.put("nation", nation.getNationName());
		profile.put("position", player.getLocation());
		writeProfile(profile);
	}
	
	
	private void writeProfile(final JSONObject profile)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Construction du chemin");
		builder.append(bddPath.getAbsolutePath());
		builder.append(": ");
		builder.append(bddPath.mkdir());
		LOGGER.info(builder.toString());
		
		builder.delete(0, builder.length());
		builder.append(bddPath.getAbsolutePath());
		builder.append(File.separator);
		builder.append(profile.get("username"));
		builder.append(".json");
		
		final File bddFilePath = new File(builder.toString());
		FileWriter writer = null;
		
		try
		{
			writer = new FileWriter(bddFilePath);
			
			writer.write(profile.toJSONString());
			
		}catch(IOException ioe0)
		{
			LOGGER.error(ioe0.getMessage());
		}finally
		{
			try
			{
				writer.flush();
				writer.close();
			}catch(IOException ioe1)
			{
				LOGGER.error(ioe1.getMessage());
			}
		}
	}
	
	public static boolean heExists(String username)
	{
		FilenameFilter fnf = new FilenameFilter()
				{
					public boolean accept(File dir, String name) 
					{
						if(name.lastIndexOf('.') > 0)
						{
							int lastIndex = name.lastIndexOf('.');
							
							String extension = name.substring(lastIndex);
							
							if(extension.equals(".json"))
							{
								return true;
							}
						}
						return false;
					}
					
				};
				
			
	    File[] files = bddPath.listFiles(fnf);
		if(files.length != 0)
		{
			for(File file : files)
			{
				if(file.getName().contains(username))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static void main(String...strings)
	{
		System.out.println("Spyglass_: "+PlayerWrapper.heExists("Spyglass_"));
		System.out.println("José: "+PlayerWrapper.heExists("José"));
	}

}
