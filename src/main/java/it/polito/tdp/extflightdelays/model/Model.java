package it.polito.tdp.extflightdelays.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> IdMap;
	private ExtFlightDelaysDAO dao;
	
	public Model() {
//		Per inizializzarli
		dao = new ExtFlightDelaysDAO();
		this.IdMap = new HashMap<>();
		this.dao.loadAllAirports(IdMap);
	}
	
	public void creaGrafo(int nAirlines) {
		
//		AGGIUNTA DEI VERTICI
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(nAirlines, IdMap));
		
		List<Rotta> edges = this.dao.getRotte(IdMap);
		for(Rotta r : edges) {
			Airport origin = r.getOrigin();
			Airport destination = r.getDestination();
			int n = r.getN();
			
			if(this.grafo.vertexSet().contains(origin) && this.grafo.vertexSet().contains(destination)) {
				DefaultWeightedEdge edge = this.grafo.getEdge(origin, destination);
				if (edge != null) {  // ci dice se l'arco esiste oppure no
					double weight = this.grafo.getEdgeWeight(edge);
					weight += n;
					this.grafo.setEdgeWeight(origin, destination, weight);
				} else {
					this.grafo.addEdge(origin, destination);
					this.grafo.setEdgeWeight(origin, destination, n);
				}
			}
		}
		
		System.out.println("Grafo creato");
		System.out.println("Ci sono " + this.grafo.vertexSet().size() + " vertici");
		System.out.println("Ci sono " + this.grafo.edgeSet().size() + " edges");
	}
	
//	public List<Airport> getVertici(){
//		
//		List<Airport> aeroporti = new LinkedList<Airport>();
//		for(Airport a : this.grafo.vertexSet()) {
//			aeroporti.add(a);
//		}
//		return aeroporti;
//	}
	
	public List<Airport> getVertici(){
		return new LinkedList<Airport>(grafo.vertexSet());
	}
	
	public boolean esistePercorso(Airport origin, Airport destination) {
		
//		PERMETTE DI OTTENERE VARIE CONNESSIONI DI UN GRAFO
		ConnectivityInspector<Airport, DefaultWeightedEdge> inspect = new ConnectivityInspector<Airport, DefaultWeightedEdge>(this.grafo);

//		RITORNA UN INSIEME DI AEROPORTI CHE SONO CONNESSI ALL'ORIGINI		
		Set<Airport> componenteConnessaOrigine = inspect.connectedSetOf(origin);
		
//		VEDE SE TRA GLI AEROPORTI CONNESSI C'E' QUELLO DI DESTINAZIONE
		return componenteConnessaOrigine.contains(destination);
	}
	
	public List<Airport> trovaPercorso(Airport origin, Airport destination) {
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> visita = new BreadthFirstIterator<>(this.grafo, origin);
		
		List<Airport> raggiungibili = new LinkedList<Airport>();
		List<Airport> percorso = new LinkedList<Airport>();
		
//		MI TROVA LA COMPONENTE CONNESSA
//		while(visita.hasNext()) {
//			Airport a = visita.next();
//			percorso.add(a);
//		}
//		return percorso;
		while(visita.hasNext()) {
			Airport a = visita.next();
			raggiungibili.add(a);
		}
		Airport corrente = destination;
		percorso.add(corrente);
		DefaultWeightedEdge e = visita.getSpanningTreeEdge(corrente);
		while (e != null) {
			Airport precedenteAlCorrente = Graphs.getOppositeVertex(this.grafo, e, corrente);
			percorso.add(0, precedenteAlCorrente);
			corrente = precedenteAlCorrente;
			e = visita.getSpanningTreeEdge(corrente);
		}
		
		return percorso;
	}
	
	public List<Airport> trovaPercorso2(Airport origin, Airport destination){
		List<Airport> percorso = new ArrayList<>();
	 	BreadthFirstIterator<Airport,DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, origin);
	 	Boolean trovato = false;  //per vedere se ha trovato l'aeroporto di destinazione
	 	
	 	//visito il grafo fino alla fine o fino a che non trovo la destinazione
	 	while(it.hasNext() & !trovato) { //serve per visitare il grafo in quanto non posso usare il get se prima non ho visitato tutti i vertici
	 		Airport visitato = it.next();
	 		if(visitato.equals(destination))  //se quello che visito è uguale alla destinazione allora lo metto true
	 			trovato = true;
	 	}
	 
	 
	 	/* se ho trovato la destinazione, costruisco il percorso risalendo l'albero di visita in senso
	 	 * opposto, ovvero partendo dalla destinazione fino ad arrivare all'origine, ed aggiungo gli aeroporti
	 	 * ad ogni step IN TESTA alla lista
	 	 * se non ho trovato la destinazione, restituisco null.
	 	 */
	 	if(trovato) {
	 		percorso.add(destination);  //aggiunge la destinazione
	 		Airport step = it.getParent(destination);  //trova il vertice opposto alla destinazione
	 		while (!step.equals(origin)) {  //finchè il vertice precendente non è uguale all'origine 
	 			percorso.add(0,step);  //aggiunge in testa alla lista percorso il vertice step
	 			step = it.getParent(step);  //prende il precedente di step
	 		}
		 
		 percorso.add(0,origin); //aggiunge l'origine
		 return percorso;
	 	} else {
	 		return null;
	 	}
	}
}



















