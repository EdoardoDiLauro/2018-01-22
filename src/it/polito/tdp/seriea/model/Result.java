package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;


public class Result {
	
	
	private int season;
	private String description;
	private String team;
	private int punti;
	List<Match> matches = new ArrayList<Match>();
	
	
	

	public Result(int season, String team) {
		super();
		this.season = season;
		this.team = team;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description ;
	}

	

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public void getPunti() {
		int points = 0;
		List<Match> matches = this.matches;
		for (Match p : matches) {
			if ((p.getAwayTeam().getTeam().equals(this.team) && p.getFtr().equals("A") )|| (p.getHomeTeam().getTeam().equals(this.team) && p.getFtr().equals("H"))) points = points + 3;
			else if (p.getFtr().equals("D")) points ++;
		}
		this.punti= points;	
	}
	
	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public int getPoints() {
		this.getPunti();
		return punti;
	}

}
