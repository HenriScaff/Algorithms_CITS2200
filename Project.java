import java.util.*; 

/**
 * Project implements the CITS2200Project interface from UWA.
 * The class contains methods to construct a graph adjacency list and analyse it.
 * @author Henri Scaffidi (Student Number: 23245207)
 */
public class Project implements CITS2200Project {

	/**
	 * Declares 2D ArrayList to hold adjacency list of the graph.
	 * Declares 2D ArrayList to hold adjacency list of graph tranpose.
	 * Declares ArrayList to hold the URL strings.
	 * Declares HashMap to provide mapping between URL strings and their IDs.
	 */ 
	private ArrayList<ArrayList<Integer>> adjacencyList;
	private ArrayList<ArrayList<Integer>> transposeList;
    private ArrayList<String> urlList;
    private HashMap<String, Integer> urlMap;

	/**
	 * Constructs a new Project Object.
	 */     
    public Project() {
    	adjacencyList = new ArrayList<>();
    	transposeList = new ArrayList<>();
        urlList = new ArrayList<>();
        urlMap = new HashMap<>();
    }

    /**
     * Adds vertex (URL) to the field variables.
     * 
     * @param url url to add to the graph representations.
     */
	private void addVert(String url) { 
	    if (!urlMap.containsKey(url)) {
	        int id = urlList.size();
	        urlList.add(url);
	        adjacencyList.add(new ArrayList<Integer>());
	        transposeList.add(new ArrayList<Integer>());
	        urlMap.put(url, id);
	    }
	}

	/**
	 * Adds an edge to the Wikipedia page graph. If the pages do not
	 * already exist in the graph, they will be added to the graph.
	 * This also creates a transpose of said graph.
	 * 
	 * @param urlFrom the URL which has a link to urlTo.
	 * @param urlTo the URL which urlFrom has a link to.
	 */
	public void addEdge(String urlFrom, String urlTo) {
		addVert(urlFrom);
		addVert(urlTo);
		Integer sourceID = urlMap.get(urlFrom);
		Integer destinationID = urlMap.get(urlTo);

		//adds the edge from urlFrom to urlTo in the regular graph
		ArrayList<Integer> sourceDestinations = adjacencyList.get(sourceID);
		if (!sourceDestinations.contains(destinationID)) {
			sourceDestinations.add(destinationID);
		}

		//adds the edge from urlTo to urlFrom in the transpose graph
		ArrayList<Integer> destinationSources = transposeList.get(destinationID);
		if (!destinationSources.contains(sourceID)) {
			destinationSources.add(sourceID);
		}
	}

	/**
	 * Conducts Breadth-First-Search for getShortestPath()
	 * and getCenters(). Distances of vertices from startVertex
	 * are tracked.
	 * 
	 * @param startVertex the page ID to begin BFS from.
	 * @param distance array to store distances of vertices from startVertex.
	 */
	private void bfsHelper(Integer startVertex, int[] distance) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            Integer currentVertex = queue.poll();
            for (Integer adjacentVertex : adjacencyList.get(currentVertex)) {
            	if (distance[adjacentVertex] > distance[currentVertex] + 1) {
            		distance[adjacentVertex] = distance[currentVertex] + 1;
            		queue.add(adjacentVertex);
            	}
            }
        }
	}

	/**
	 * Finds the shortest path in number of links between two pages.
	 * If there is no path, returns -1.
	 * 
	 * @param urlFrom the URL where the path should start.
	 * @param urlTo the URL where the path should end.
	 * @return the length of the shortest path in number of links followed.
	 */
	public int getShortestPath(String urlFrom, String urlTo) {
		if (!urlMap.containsKey(urlFrom) || !urlMap.containsKey(urlTo)) {
			return -1;
		}
		if (urlFrom.equals(urlTo)) {
			return 0;
		}

		int numVertices = urlList.size();
        int[] distance = new int[numVertices];
        Integer sourceID = urlMap.get(urlFrom);
		Integer destinationID = urlMap.get(urlTo);

        for (int i = 0; i < numVertices; i++) {
        	distance[i] = Integer.MAX_VALUE;
        }
        distance[sourceID] = 0;

        bfsHelper(sourceID, distance);

        if (distance[destinationID] == Integer.MAX_VALUE) {
        	return -1;
        }

        return distance[destinationID];
	}

	/**
	 * Finds all the centers of the page graph. The order of pages
	 * in the output does not matter. Any order is correct as long as
	 * all the centers are in the array, and no pages that aren't centers
	 * are in the array.
	 * 
	 * @return an array containing all the URLs that correspond to pages that are centers.
	 */
	public String[] getCenters() {

		int numVertices = urlList.size();
		int[] distance = new int[numVertices];
		int minEccentricity = Integer.MAX_VALUE;
		ArrayList<String> centers = new ArrayList<>();

		for (String url : urlList) {

			Integer vertexID = urlMap.get(url);
        	for (int i = 0; i < numVertices; i++) { 
        		distance[i] = Integer.MAX_VALUE;
        	}
        	distance[vertexID] = 0;

        	bfsHelper(vertexID, distance);

        	int eccentricity = -2;
        	for (int dist : distance) {
        		if (dist > eccentricity && dist != Integer.MAX_VALUE) {
        			eccentricity = dist;
        		}
        	}

        	if (eccentricity < minEccentricity) {
        		minEccentricity = eccentricity;
        		centers.clear();
        		centers.add(url);
        	}
        	else if (eccentricity == minEccentricity) {
        		centers.add(url);
        	}
		}

		if (centers.size() == 0) {
			String[] empty = new String[0];
			return empty;
		}

		String[] toreturn = new String[centers.size()];
		for (int i = 0; i < centers.size(); i++) {
			toreturn[i] = centers.get(i);
		}

		return toreturn;

	}

	/**
	 * Conducts Depth-First-Search for getStronglyConnectedComponents().
	 * This is DFS on the original graph, tracking postOrder for later use.
	 * 
	 * @param currentVertex the current page ID to search from.
	 * @param colour array of ints, representing explored/partially-explored/unvisited nodes.
	 * @param postOrder stack to track post order.
	 */
	private void dfsOG(int currentVertex,  int[] colour, Stack<Integer> postOrder) {
        colour[currentVertex] = 1;

        for (Integer adjacentVertex : adjacencyList.get(currentVertex)) {
            if (colour[adjacentVertex] == 0) {
            	dfsOG(adjacentVertex, colour, postOrder);
            }
        }

        colour[currentVertex] = 2;
        postOrder.push(currentVertex);
    }

	/**
	 * Conducts Depth-First-Search for getStronglyConnectedComponents().
	 * This is DFS on the transposed graph, computing an SCC.
	 * 
	 * @param currentVertex the current page ID to search from.
	 * @param colour array of ints, representing explored/partially-explored/unvisited nodes.
	 * @param theSCC array to store the SCC
	 */
    private void dfsBack(int currentVertex, int[] colour, ArrayList<Integer> theSCC) {
        colour[currentVertex] = 1;

        for (Integer adjacentVertex : transposeList.get(currentVertex)) {
            if (colour[adjacentVertex] == 0) {
            	theSCC.add(adjacentVertex);
            	dfsBack(adjacentVertex, colour, theSCC);
            }
        }

        colour[currentVertex] = 2;    	
    }

	/**
	 * Finds all the strongly connected components of the page graph.
	 * Every strongly connected component can be represented as an array 
	 * containing the page URLs in the component. The return value is thus an array
	 * of strongly connected components. The order of elements in these arrays
	 * does not matter. Any output that contains all the strongly connected
	 * components is considered correct.
	 * 
	 * @return an array containing every strongly connected component.
	 */
	public String[][] getStronglyConnectedComponents() {

        int numVertices = urlList.size();
        int[] colour = new int[numVertices]; //2 = black, 1 = grey, 0 = white
        Stack<Integer> postOrder = new Stack<Integer>();

        for (int i = 0; i < numVertices; i++) {
            colour[i] = 0;
        }

        //DFS 1: On original graph, record the post-order
        for (int i = 0; i < numVertices; i++) {
        	if (colour[i] == 0) {
        		dfsOG(i, colour, postOrder);
        	}
        }

        //Reset the colours
        for (int i = 0; i < numVertices; i++) {
            colour[i] = 0;
        }
        
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();

        //DFS 2: On the transpose, using post-order, build SCCs
        while (!postOrder.isEmpty()) {
        	Integer currentVertex = postOrder.pop();
        	if (colour[currentVertex] == 0) {
        		ArrayList<Integer> newSCC = new ArrayList<>();
        		newSCC.add(currentVertex);
        		dfsBack(currentVertex, colour, newSCC);
        		components.add(newSCC);
        	}
        }

        if (components.size() == 0) { 
        	String[][] empty = new String[0][0];
			return empty;
        }

        String[][] toReturn = new String[components.size()][];
        for (int i = 0; i < components.size(); i++) {
        	ArrayList<Integer> component = components.get(i);
        	toReturn[i] = new String[component.size()];
        	for (int j = 0; j < component.size(); j++) {
        		toReturn[i][j] = urlList.get(component.get(j));
        	}
        }

        return toReturn;
	}

	/**
	 * Conducts Depth-First-Search for getHamiltonianPath().
	 * 
	 * @param currentVertex the current page ID to search from.
	 * @param colour array of ints, representing explored/partially-explored/unvisited nodes.
	 * @param hPath stack to store the Hamiltonian Path
	 * @return true if Hamiltonian Path found, false otherwise
	 */
	private boolean dfsHamiltonian(int currentVertex,  int[] colour, Stack<Integer> hPath) {

        colour[currentVertex] = 1;
        hPath.push(currentVertex);

        for (Integer adjacentVertex : adjacencyList.get(currentVertex)) {
            if (colour[adjacentVertex] == 0) {
            	if (dfsHamiltonian(adjacentVertex, colour, hPath)) {
				    return true;
				}
            }
        }

        if (hPath.size() == urlList.size()) { //Found a hamiltonian path
        	return true;
        }

        //At a dead end and have not found a hPath - pop from stack and mark node as unvisited
        colour[currentVertex] = 0; 
        hPath.pop();
        return false;

    }

	/**
	 * Finds a Hamiltonian path in the page graph. There may be many
	 * possible Hamiltonian paths. Any of these paths is a correct output.
	 * This method should never be called on a graph with more than 20
	 * vertices. If there is no Hamiltonian path, this method will
	 * return an empty array. The output array should contain the URLs of pages
	 * in a Hamiltonian path. The order matters, as the elements of the
	 * array represent this path in sequence. So the element [0] is the start
	 * of the path, and [1] is the next page, and so on.
	 * 
	 * @return a Hamiltonian path of the page graph.
	 */
	public String[] getHamiltonianPath() { 

		int numVertices = urlList.size();

		if (numVertices > 20) {
			String[] empty = new String[0];
			return empty;
		}

		for (String url : urlList) { //starting DFS from every vertex

			Stack<Integer> pathTracker = new Stack<Integer>(); 
			int startVertex = urlMap.get(url);
			int[] colour = new int[numVertices]; //2 = black, 1 = grey, 0 = white

	        for (int i = 0; i < numVertices; i++) { //set all vertices to unvisited
	            colour[i] = 0;
	        }

	        boolean pathFound = dfsHamiltonian(startVertex, colour, pathTracker);
	        if (pathFound) {
	        	String[] hPath = new String[numVertices];
	        	for (int i = numVertices - 1; i >= 0; i--) {
	        		hPath[i] = urlList.get(pathTracker.pop());
	        	}
	        	return hPath;
	        }

		}

		//No path
		String[] empty = new String[0];
		return empty;

	}

}