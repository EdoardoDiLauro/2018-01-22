package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	Graph<Result,DefaultWeightedEdge> grafo = new DirectedWeightedMultigraph<Result,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	
	private SerieADAO dao = new SerieADAO();
	
	public List<Team> getSquadre(){
		List<Team> teams = dao.listTeams();
		return teams;
	}
	
	public Graph<Result, DefaultWeightedEdge> getGrafo(String team) {
		List<Season> stagioni = dao.listSeasons(team);
		List<Result> annate = new ArrayList<Result>();
		for (Season s : stagioni) {
			Result r = new Result (s.getSeason(), team);
			r.setDescription(dao.descSeason(s.getSeason()));
			r.setMatches(dao.listMatches(team, s.getSeason()));
			annate.add(r);
		}
		Graphs.addAllVertices(this.grafo, annate);
		for (Result r1 : annate) {
			for (Result r2 : annate) {
				if (!r1.equals(r2)) {
					if (r1.getPoints()>=r2.getPoints()) Graphs.addEdgeWithVertices(this.grafo, r2, r1, r1.getPoints()-r2.getPoints());
					else if (r1.getPoints()<r2.getPoints()) Graphs.addEdgeWithVertices(this.grafo, r1, r2, r2.getPoints()-r1.getPoints());
				}
			}
		}
		return grafo;
	}

	public void setGrafo(Graph<Result, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public List<Season> getStagioni(String team){
		List<Season> result = dao.listSeasons(team);		
		return result;
	}
	
	public String getPunti (String team){
		List<Season> stagioni = dao.listSeasons(team);
		List<Result> annate = new ArrayList<Result>();
		for (Season s : stagioni) {
			Result r = new Result (s.getSeason(), team);
			r.setMatches(dao.listMatches(team, s.getSeason()));
			annate.add(r);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Risultati per anno: \n");
		for (Result r : annate) {
			sb.append(r.getSeason() +" "+ r.getPoints() +"\n");
		}
		return sb.toString();
		
	}
	
	public String getDescrizione (int stagione) {
		return dao.descSeason(stagione);
	}
	
	public String getAnnatadoro(String team) {
		Graph<Result,DefaultWeightedEdge> annategr = this.getGrafo(team);
		Result b = null;
		int max = 0;
		for (Result v : annategr.vertexSet()) {
			int value = annategr.inDegreeOf(v)-annategr.outDegreeOf(v);
			if (value>max) b=v;
		}
		int in = 0;
		int out = 0;
		for (DefaultWeightedEdge e : annategr.incomingEdgesOf(b)) in = (int) (in + annategr.getEdgeWeight(e));
		for (DefaultWeightedEdge e : annategr.outgoingEdgesOf(b)) out = (int) (out + annategr.getEdgeWeight(e));
		StringBuilder sb = new StringBuilder();
		sb.append("Annata d'oro: \n");
		sb.append(b.getDescription() + " " + (in-out));
		return sb.toString();
	}

}
