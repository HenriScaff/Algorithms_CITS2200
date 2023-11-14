import java.io.*;
import java.util.*;

public class CITS2200ProjectTester {
	public static void loadGraph(Project project, String path) {
		// The graph is in the following format:
		// Every pair of consecutive lines represent a directed edge.
		// The edge goes from the URL in the first line to the URL in the second line.
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while (reader.ready()) {
				String from = reader.readLine();
				String to = reader.readLine();
				//System.out.println("Adding edge from " + from + " to " + to);
				project.addEdge(from, to);
			}
		} catch (Exception e) {
			System.out.println("There was a problem:");
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		//Load Graph File
		String pathToGraphFile = "example_graph.txt";
		Project proj = new Project();
		loadGraph(proj, pathToGraphFile);

		//Shortest Path
		System.out.println("\n------------SHORTEST PATH------------"); 
		System.out.println("Between /wiki/Flow_network and /wiki/Out-of-kilter_algorithm"); 
		System.out.println("\t" + proj.getShortestPath("/wiki/Flow_network", "/wiki/Out-of-kilter_algorithm")); 

		//Hamiltonian Path
		System.out.println("\n------------HAMILTONIAN PATH------------");
		String[] hPath = proj.getHamiltonianPath();
		for (String path : hPath) {
			System.out.println(path);
		} 

		//Strongly Connected Components
		System.out.println("\n------------STRONGLY CONNECTED COMPONENTS------------");
		String[][] comps = proj.getStronglyConnectedComponents();	
		for (String[] scc : comps) {
			System.out.println("SCC:");
			for (String url : scc) {
				System.out.println("\t" + url);
			}
		}
		
		//Centers
		System.out.println("\n------------CENTERS------------");
		String[] centers = proj.getCenters();
		for (String cent : centers) {
			System.out.println("" + cent);
		}

		System.out.println("\n");
	}
}