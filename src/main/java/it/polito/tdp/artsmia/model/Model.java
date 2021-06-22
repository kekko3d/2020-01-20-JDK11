package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Integer peso = -1;
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph <Artist, DefaultWeightedEdge> grafo;
	private HashMap<Integer, Artist> idMap;
	
	// per il secondo metodo
	LinkedList<Adiacenza> listaAdiacenze;
	public void model(){

	}
	
	public void CreaGrafo (String ruolo) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao = new ArtsmiaDAO();
		DefaultWeightedEdge b;
		idMap = new HashMap<Integer, Artist> () ;

		for(Artist a : dao.listV(ruolo)) {
			idMap.put(a.getArtistId(), a);
		}
		Graphs.addAllVertices(grafo, idMap.values());
		
		//ci serrve per il metodo dopo 
		listaAdiacenze = new LinkedList <Adiacenza>();
		for(Adiacenza e : dao.getAdiacenze()) {
			if(idMap.containsKey(e.getId1())
					&& idMap.containsKey(e.getId2())){
				
				//questo if e il seguente servono a non aggiungere lo stesso arco più volte
				if(this.grafo.containsVertex(idMap.get(e.getId1())) ||
						this.grafo.containsVertex(idMap.get(e.getId2()))) {


					b = this.grafo.getEdge(idMap.get(e.getId1()), idMap.get(e.getId2()));
					if(b == null) {
						Graphs.addEdgeWithVertices(this.grafo, idMap.get(e.getId1()), idMap.get(e.getId2()), e.getPeso());
						listaAdiacenze.add(e);

					}
				}
			}
		}
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());

	}

	
	public List<Adiacenza> artistiConnessi() {
		
		Collections.sort(listaAdiacenze, new Comparator<Adiacenza>() {
			@Override
			public int compare(Adiacenza a1, Adiacenza a2) {
				return a2.getPeso() - a1.getPeso();
			}
		});
		return listaAdiacenze;
	}
	
	
	
	public List<Artist> init(Integer id) {
		ArrayList<Artist> listArtist = new ArrayList <Artist>();
		ArrayList<Artist> finale = new ArrayList <Artist>();
		ArrayList<Artist> parziale = new ArrayList <Artist>();

		Artist startV = idMap.get(id);
		recursion(parziale, finale, 0, startV );
		
		return finale;
		
	}
	
	
	public void recursion(ArrayList <Artist> parziale, ArrayList <Artist> finale, int pesoParziale, Artist startV) {

//			if(pesoParziale == 5) {
//				return;
//			}
			if(pesoParziale >= this.peso) {
	
				System.out.println(parziale);
	
				System.out.println(peso);
	
				finale.clear();
	
				finale.addAll(parziale);
	
				this.peso = pesoParziale;
	
				}


		for(Artist neigh : Graphs.neighborListOf(grafo, startV)) {
			
			if(!parziale.contains(neigh)) {
				DefaultWeightedEdge e = grafo.getEdge(startV, neigh);

				if(grafo.getEdgeWeight(e) == 1) {
				pesoParziale += 1;
				
				parziale.add(neigh);
					recursion(parziale, finale, pesoParziale, neigh);
				parziale.remove(neigh);

				pesoParziale -= 1;

				}
			}
		}
	}
	

}
