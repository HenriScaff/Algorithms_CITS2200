public interface CITS2200Project {
	/**
	 * Adds an edge to the Wikipedia page graph. If the pages do not
	 * already exist in the graph, they will be added to the graph.
	 * 
	 * @param urlFrom the URL which has a link to urlTo.
	 * @param urlTo the URL which urlFrom has a link to.
	 */
	public void addEdge(String urlFrom, String urlTo);

	/**
	 * Finds the shortest path in number of links between two pages.
	 * If there is no path, returns -1.
	 * 
	 * @param urlFrom the URL where the path should start.
	 * @param urlTo the URL where the path should end.
	 * @return the length of the shorest path in number of links followed.
	 */
	public int getShortestPath(String urlFrom, String urlTo);

	/**
	 * Finds all the centers of the page graph. The order of pages
	 * in the output is not considered.
	 * 
	 * @return an array containing all the URLs that correspond to pages that are centers.
	 */
	public String[] getCenters();

	/**
	 * Finds all the strongly connected components of the page graph.
	 * Every strongly connected component can be represented as an array 
	 * containing the page URLs in the component. The return value is thus an array
	 * of strongly connected components. Order in output not considered.
	 * 
	 * @return an array containing every strongly connected component.
	 */
	public String[][] getStronglyConnectedComponents();

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
	public String[] getHamiltonianPath();
}
