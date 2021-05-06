package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
    private Graph<Airport, DefaultWeightedEdge> grafo;
    private Map<Integer, Airport> idMap;
    private ExtFlightDelaysDAO dao;
    Graph<Airport, DefaultWeightedEdge> grafo2;
    
    public Model() {
    	idMap= new HashMap<Integer, Airport>();
    	dao= new ExtFlightDelaysDAO();
    }
    
    public void creaGrafo(double distanza) {
    	
    	grafo= new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    	
    	//Aggiungo i vertici (airport)
    	dao.loadAllAirports(idMap);
    	Graphs.addAllVertices(this.grafo, idMap.values());
    	
    	
    	//Aggiungo gli archi
    	
    	for (Adiacenza a : dao.getAdiacenze(idMap)) {
    		if (this.grafo.containsVertex(a.getA1()) && this.grafo.containsVertex(a.getA2())) {
    			DefaultWeightedEdge e= this.grafo.getEdge(a.getA1(), a.getA2());
    			if (e==null) {
    				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
    			}
    			else {
    				double pesoVecchio= this.grafo.getEdgeWeight(e);
    				double pesoNuovo= a.getPeso();
    				this.grafo.setEdgeWeight(e, (pesoNuovo+pesoVecchio)/2);
    			}
    		}
    		else {
    			if (!this.grafo.containsVertex(a.getA1())) {
    				this.grafo.addVertex(a.getA1());
    			}
    			if (!this.grafo.containsVertex(a.getA2())) {
    				this.grafo.addVertex(a.getA2());
    			}
    			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
    		}
    	}
    	
    	grafo2= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    	for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
    		if (this.grafo.getEdgeWeight(e) >= distanza) {
    			if (!grafo2.containsVertex(this.grafo.getEdgeSource(e))) {
    				grafo2.addVertex(this.grafo.getEdgeSource(e));
    			}
    			if (!grafo2.containsVertex(this.grafo.getEdgeTarget(e))) {
    				grafo2.addVertex(this.grafo.getEdgeTarget(e));
    			}
    			if (!grafo2.containsEdge(e)) {
    				Graphs.addEdge(grafo2, this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e));
    				
    			}
    		}
    	}
    	
    }
    
    public String getInfoGrafo() {
    	String s= "Grafo creato con "+this.grafo2.vertexSet().size()+" vertici e "+this.grafo2.edgeSet().size()+" archi";
    	return s;
    }
    
    public String getArchi() {
    	String s="";
    	for (DefaultWeightedEdge e : this.grafo2.edgeSet()) {
    		s+=this.grafo2.getEdgeSource(e).getAirportName()+" - "+this.grafo2.getEdgeTarget(e).getAirportName()+", "+this.grafo2.getEdgeWeight(e)+" miglia\n";
    		
    	}
    	return s;
    }
}
