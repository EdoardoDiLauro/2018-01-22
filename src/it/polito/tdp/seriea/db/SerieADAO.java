package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Team infoTeam(String team) {
		String sql = "SELECT team FROM teams WHERE team = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team);
			ResultSet res = st.executeQuery();
			Team t = null;

			while (res.next()) {
				t = new Team (res.getString("team"));
				
			}
			
			conn.close();

			return t;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Season> listSeasons(String team) {
		String sql = "SELECT DISTINCT season, description FROM seasons WHERE season IN (SELECT Season FROM matches WHERE HomeTeam = ? OR AwayTeam = ?)";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, team);
			st.setString(2, team);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Season infoSeason(int anno) {
		String sql = "SELECT season, description FROM seasons WHERE season = ?";


		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery();
			Season result = null;
			
			while (res.next()) {
				result = new Season (res.getInt("season"), res.getString("description"));

				}

			conn.close();
			return result;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String descSeason(int anno) {
		String sql = "SELECT season, description FROM seasons WHERE season = ?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery();
			
			String result = null;
			
			
			while (res.next()) {
				result = res.getString("description");
				}

			conn.close();
			return result;
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Match> listMatches(String team, int stagione) {
		String sql = "SELECT match_id, Season, 'div', date, HomeTeam, AwayTeam, fthg, ftag, ftr FROM matches WHERE (HomeTeam = ? OR AwayTeam = ?) AND Season = ?  ";
		List<Match> result = new ArrayList<Match>();
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, team);
			st.setString(2, team);
			st.setInt(3, stagione);
			
			
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String away = res.getString("AwayTeam");
				String home = res.getString("HomeTeam");
				result.add(new Match(res.getInt("match_id"), this.infoSeason(res.getInt("Season")), res.getString("Div"), res.getDate("date").toLocalDate(), this.infoTeam(home), this.infoTeam(away), res.getInt("fthg"), res.getInt("ftag"), res.getString("ftr")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
