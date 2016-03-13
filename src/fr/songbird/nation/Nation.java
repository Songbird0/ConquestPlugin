package fr.songbird.nation;

import org.bukkit.scoreboard.Team;

public class Nation 
{
	
	private final String nationName;
	private final int nationId;
	private final Team team;
	
	
	public Nation(final String nationName, final int nationId, final Team team)
	{
		this.nationName = nationName;
		this.nationId = nationId;
		this.team = team;
	}
	
	
	
	public final String getNationName()
	{
		return nationName;
	}
	
	public final int getNationId()
	{
		return nationId;
	}
	
	public final Team getTeam()
	{
		return team;
	}

}
