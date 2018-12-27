
/********************************************************************
 * AdjListsGraph.java @version 2018.11.08
 * Implementation of the Graph.java interface using Lists of
 * Adjacent nodes
 * KNOWN FEATURES/BUGS: 
 * It handles unweighted graphs only, but it can be extended.
 * It does not handle operations involving non-existing vertices
 * 
 * ***updated by Debbie to include getArc, getVertex and getIndexOfVertex methods
 ********************************************************************/

import java.util.*;
import java.io.*;
import javafoundations.*;
public class AdjListsGraph<T> implements Graph<T>{
    private final int NOT_FOUND = -1;

    private Vector<LinkedList<T>> arcs;   // adjacency matrices of arcs
    private Vector<T> vertices;   // values of vertices

    /******************************************************************
     * Constructor. Creates an empty graph.
     ******************************************************************/
    public AdjListsGraph() {
        this.arcs = new Vector<LinkedList<T>>();
        this.vertices = new Vector<T>();
    }

    /*****************************************************************
     * Creates and returns a new graph using the data found in the input file.
     * If the file does not exist, a message is printed. 
     *****************************************************************/
    public static AdjListsGraph<String> AdjListsGraphFromFile(String tgf_file_name) {
        AdjListsGraph<String> g = new AdjListsGraph<String>();
        try{ // to read from the tgf file
            Scanner scanner = new Scanner(new File(tgf_file_name));
            //read vertices
            while (!scanner.next().equals("#")){
                String token = "";
                token = scanner.nextLine();
                g.addVertex(token); 
            }
            //read arcs
            while (scanner.hasNext()){
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                g.addArc(from-1, to-1);
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println(" ***(T)ERROR*** The file was not found: " + ex);
        }
        return g;
    }

    /******************************************************************
     * Returns true if the graph is empty and false otherwise. 
     ******************************************************************/
    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /******************************************************************
     * Returns the number of vertices in the graph.
     ******************************************************************/
    public int getNumVertices() { 
        return vertices.size(); 
    }

    /******************************************************************
     * Returns the number of arcs in the graph by counting them.
     ******************************************************************/
    public int getNumArcs() {
        int totalArcs = 0;
        for (int i = 0; i < vertices.size(); i++){ //for each vertex
            //add the number of its connections
            totalArcs = totalArcs + arcs.get(i).size(); 
        }
        return totalArcs; 
    }

    /******************************************************************
     * Returns true iff a connection exists from v1 to v2.
     ******************************************************************/
    public boolean isArc (T vertex1, T vertex2){ 
        try {
            //find index of origin vertex in the vector of vertices
            int index = vertices.indexOf(vertex1);
            //find the list of verteces connected to the origin vertex
            LinkedList<T> l = arcs.get(index);
            //check whether the destination vertex is amng them
            return (l.indexOf(vertex2) != -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(vertex1 + " vertex does not belong in the graph");
            return false;
        }
    }

    /******************************************************************
     *Returns true iff an arc exist from vertex1 to vertex2, AND an arc 
     *exists from vertex2 to vertex1.
     *@param vertex1    The origin vertex for the edge
     *@param vertex2    The destination vertex for the edge
     *@return           true if an edge between vertex1 and vertex2 exists, 
     *                  false otherwise.
     ******************************************************************/
    public boolean isEdge (T vertex1, T vertex2) {
        return isArc(vertex1, vertex2) && isArc(vertex2, vertex1); 
    }

    /******************************************************************
     * Adds the input vertex to the graph, if it does not belong to it already.
     * @param The vertex to be added to the graph
     ******************************************************************/
    public void addVertex (T vertex) {
        if (vertices.indexOf(vertex) == -1) { //the vertex is not already there
            // add it to the vertices vector
            vertices.add(vertex); 

            //also add an empty, for now, list to hold its cnnections
            arcs.add(new LinkedList<T>()); 
        }
    }

    /******************************************************************
     * Removes the input vertex from the graph. Notice that if the vertex 
     * does not exist in the graph, the graph is not altered by this method.
     * Uses equals() on T for testing equality.
     * @param vertex    The vertex to be removed from the graph
     ******************************************************************/
    public void removeVertex (T vertex) {
        //find the index of the vertex in the vector of vertices
        int index = vertices.indexOf(vertex);
        //remove the vertex. 
        this.removeVertex(index);
    }

    /******************************************************************
     * Helper method. Removes the vertex at the given index from the graph.   
     * Note that this may affect the index values of other vertices.
     ******************************************************************/
    private void removeVertex (int index) {
        T vertexToBeRemoved = vertices.get(index);
        //remove vertex from vertices vector
        vertices.remove(index); 

        //remove its list of adjacent vertices from the vector
        arcs.remove(index); 

        //remove the vertex from all the other adjacent lists, wherever it was found
        for (int i = 0; i < arcs.size(); i++) { // for each linked list in the arcs vector
            for (T otherVertex : arcs.get(i)) { // for each vertex in each list
                //if the vertex to be removed is found
                if (otherVertex.equals(vertexToBeRemoved))
                //just remove it
                    arcs.get(i).remove(vertexToBeRemoved);
            }
        }
    }

    /******************************************************************
     * Adds an edge between two vertices in the graph.
     * If one or both vertices do not exist, the graph is not altered.
     * @param vertex1   one of the end points of the edge
     * @param vertex2   the other end point of the edge
     ******************************************************************/
    public void addEdge (T vertex1, T vertex2) {
        //if one of the two vertices does not exist, the isArc() will not alter the graph
        this.addArc (vertex1, vertex2);
        addArc (vertex2, vertex1);
    }

    /******************************************************************
     * Adds an arc with origin v1 and destination v2.
     * If one of the vertices 9or both) does not exist, the graph is not altered.
     * @param source        The point of origin for the arc to be added
     * @param destination   The end point for the arc to be added
     ******************************************************************/
    public void addArc (T source, T destination){
        int sourceIndex = vertices.indexOf(source);
        int destinationIndex = vertices.indexOf(destination);

        //if source and destination exist, add the arc, else do nothing 
        if ((sourceIndex != -1) && (destinationIndex != -1)){
            LinkedList<T> l = arcs.get(sourceIndex);
            l.add(destination);
        }
    }

    /******************************************************************
     * Helper. Adds an arc between the two input vertices.
     * PRECONDITION: the arc does not exist already
     ******************************************************************/
    public void addArc (int index1, int index2) {
        //if (indexIsValid(index1) && indexIsValid(index2))
        //vertices.get(index1).add(v2);

        //get the list of vertices of the origin
        LinkedList<T> l = arcs.get(index1);
        //find the destination vertex, based on its index in the vertices vector
        T destination = vertices.elementAt(index2);
        //add the destination to the adjacent list of the origin
        l.add(destination);
    }

    /******************************************************************
     * Removes the edge between the two input vertices.
     * If one or both vertices do not exist, the graph is not altered.
     * @param vertex1       one end point of the edge to be removed
     * @param vertex2       the other end point of the edge to be removed
     ******************************************************************/
    public void removeEdge (T vertex1, T vertex2) {
        removeArc (vertex1, vertex2);
        removeArc (vertex2, vertex1);
    }

    /******************************************************************
     * Removes the arc from vertex v1 to vertex v2,
     * if the vertices exist, else does not change the graph. 
     * @param vertex1       the origin of the edge to be removed
     * @param vertex2       the destination of the edge to be removed
     ******************************************************************/
    public void removeArc (T vertex1, T vertex2) {
        int index1 = vertices.indexOf(vertex1);
        int index2 = vertices.indexOf(vertex2);
        removeArc (index1, index2);
    }

    /******************************************************************
     * Helper. Removes an arc from index v1 to index v2.
     ******************************************************************/
    private void removeArc (int index1, int index2) {
        //if (indexIsValid(index1) && indexIsValid(index2))
        T destination = vertices.get(index2);
        LinkedList<T> connections = arcs.get(index1);
        connections.remove(destination);
    }

    /******************************************************************
    Returns a string representation of the graph. 
    @return     A string represention of the graph, which includes 
    its vertices and adjacent vertices from each one of them.
     ******************************************************************/
    public String toString() {
        if (vertices.size() == 0) return "Graph is empty";

        String result = "Vertices: \n";
        result = result + vertices;

        result = result + "\n\nEdges: \n";
        for (int i=0; i< vertices.size(); i++)
            result = result + "from " + vertices.get(i) + ": "  + arcs.get(i) + "\n";

        return result;
    }

    /******************************************************************
     * Saves the current graph into a .tgf file.
     * If the file does not exist, it is created. If it exists, it is overwitten. 
     * If it cannot save the file, a message is printed. 
     * @param fName     The name of the file to write to 
     *****************************************************************/
    public void saveTGF(String fName) {
        try {
            PrintWriter writer = new PrintWriter(new File(fName));

            //write vertices by iterating through vector "vertices"
            for (int i = 0; i < vertices.size(); i++) {
                writer.print((i+1) + " " + vertices.get(i).toString());
                writer.println("");
            }
            writer.print("#"); 
            writer.println("");

            //write arcs by iterating through arcs vector
            for (int i = 0; i < arcs.size(); i++){ //for each linked list in arcs
                for (T vertex :arcs.get(i)) {
                    int index2 = vertices.indexOf(vertex);
                    writer.print((i+1) + " " + (index2+1));
                    writer.println("");
                }
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("***ERROR***" +  fName + " could not be written: " + ex);
        }
    }

    /********************************************************************
     * ADDITIONAL CODE ADDED FOR FINAL PROJECT
     ********************************************************************/   
    /**
     * Gets the indicated arc
     * 
     * @param   index   index of requested arc
     * @return          requested arc
     */
    public LinkedList<T> getArc(int index) {
        return arcs.get(index);
    }

    /**
     * Gets the indicated vertex
     * 
     * @param   index   index of requested vertex
     * @return          requested vertex
     */
    public T getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * Gets the index of the given vertex. Return -1 if it cannot be found
     * 
     * @param   verted   vertex whose index is to be found
     * @return           index of vertex, -1 if cannot be found
     */
    public int getIndexOfVertex(T vertex) {
        for (int i = 0; i < vertices.size(); i++) {
            if (vertex.equals(vertices.get(i))) {
                return i;
            }
        }

        return -1;
    }
}