package org.project.Logic.embAsp.minimax;

import java.util.*;

/**
 * Class used to build a not oriented graph using an {@link java.util.LinkedHashMap} as adjacency list. <p>
 * @param  the type of the vertex. Must implement {@link Object#equals(Object)} and {@link Object#hashCode()}.
 */

public class MyGraph<T> {
    private LinkedHashMap<T, List<T>> adjacencyList;
    private T root;
    private T best;


    public MyGraph() {
        this.adjacencyList = new LinkedHashMap<>(100);
        root = null;
        best = null;
    }


    public void addAll(Collection<T> vertices) {
        for (T vertex : vertices) {
            addVertex(vertex);
        }
    }

    /**
     * Add a vertex to the graph. <p>
     * If the vertex is already present, the method does nothing.
     * @param vertex the vertex to add.
     * @throws IllegalArgumentException if the vertex is null.
     */
    public void addVertex(T vertex) {
        if (root == null) {
            root = vertex;
            best = root;
        }
        if (vertex == null) throw new IllegalArgumentException("Vertex cannot be null.");
        adjacencyList.putIfAbsent(vertex, new LinkedList<>());
    }


    /**
     * Add an edge between two vertices. <p>
     * If the edge is already present, the method does nothing. <p>
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @throws IllegalArgumentException <p>if the source vertex does not exist. .
     */
    public void addEdge(T source, T destination) {
        // check if source exist
        if (!adjacencyList.containsKey(source))
            throw new IllegalArgumentException("Vertex " + source + " does not exist.");
        if (!adjacencyList.containsKey(destination))
            throw new IllegalArgumentException("Desination " + destination + " does not exist.");

        List<T> edges = adjacencyList.get(source);

        if (edges.contains(destination)) return;
        edges.add(destination);
//        adjacencyList.get(destination).add(source); // Add the following line if the graph is undirected
    }

    /**
     * Edge between two vertices. <p>
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @return true if the edge exists, false otherwise.
     */
    public boolean edge(T source, T destination) {
        if (!adjacencyList.containsKey(source))
            throw new IllegalArgumentException("source does not exist: " + source );
        if (!adjacencyList.containsKey(destination))
            throw new IllegalArgumentException("destination does not exist: " + destination);

        return adjacencyList.get(source).contains(destination);
    }



    /**
     * Find a path between two vertices using a Breadth First Search algorithm. <p>
     * @param start the start vertex.
     * @param end the end vertex.
     */

    public Collection<T> find(T start, T end) {
        if (start == null) throw new IllegalArgumentException("start cannot be null.");
        if (end == null) throw new IllegalArgumentException("end cannot be null.");

//        ArrayDeque<GridState> path = new ArrayDeque<>(); // LIFO
//        path.add(start);

        LinkedList<T> path = new LinkedList<>();
    //--OPTIMIZATION
        if (start.equals(end)) {
            path.add(start);
        }

        else if (adjacencyList.get(start).contains(end)) {
            path.add(start);
            path.add(end);
        }

    //--SEARCH
        else {
            List<T> visited ;
            for (T adj : adjacencyList.get(start)) {
                visited = DFSBranch(adj, end); // If end is reached, visited contains the path from start to end
                if (visited.getLast().equals(end)) {
                    continue;
                }

            }
        }

    //--RETURN
        if (path.getLast() == end) {
            return path;
        }
        return null;

    }


    public List<T> DFSBranch(T start, T end) {
        List<T> visited = new LinkedList<>();
        ArrayDeque<T> path = new ArrayDeque<>();
        DFSBranch(start, end, visited, path);
        stopDFSBranch = false;

        return visited;
    }
    private boolean stopDFSBranch = false;
    /**
     * This method performs the same search of {@link MyGraph#DFS(T, T, List)} but stops as soon as it finds a terminal vertex.
     * @param start
     * @param end
     * @param visited
     */
    private void DFSBranch(T start, T end, List<T> visited, ArrayDeque<T> path) {

    }

    private boolean stopDFS = false;
    /**
     * Depth First Search algorithm. <p>
     * The method is recursive and uses a stop condition to exit the recursion. <p>
     * If {@code end} is reached, it is added to the end of {@code visited}, otherwise {@code visited} does not contain {@code end}.
     * @param start the start vertex.
     * @param end the end vertex.
     * @param visited the list of visited vertices, must be {@code empty}.
     */
    private void DFS(T start, T end, List<T> visited) {
        if (!stopDFS) {
            if (start == null || end == null)
                throw new IllegalArgumentException("Start and end vertices cannot be null.");

            visited.add(start);

            for (T adjacent : adjacencyList.get(start)) {
                // EXIT CONDITION
                if (adjacent.equals(end)) {
                    visited.add(end);
                    stopDFS = true;
                }

                if (!stopDFS && !visited.contains(adjacent) ) {
                    DFS(adjacent, end, visited);
                }
            }

        }
    }

    /**
     * Depth First Search algorithm. <p>
     * @param start the start vertex.
     * @param end the end vertex.
     * @return the list of visited vertices.
     */
    public List<T> DFS(T start, T end){
        List<T> visited = new LinkedList<>();
        DFS(start, end, visited);
        stopDFS = false;

        return visited;
    }

    public List<T> DFSTerminal(T start) {
        List<T> visited = new LinkedList<>();
        DFSTerminal(start, visited);
        stopDFS = false;

        return visited;
    }

    private void DFSTerminal(T start, List<T> visited) {
        if (!stopDFS) {
            if (start == null )
                throw new IllegalArgumentException("Start cannot be null.");

            visited.add(start);

            for (T adjacent : adjacencyList.get(start)) {
                // EXIT CONDITION: adjacent have no children
                if (adjacencyList.get(adjacent).isEmpty()) {
                    visited.add(adjacent);
                    stopDFS = true;
                }

                if (!stopDFS && !visited.contains(adjacent) ) {
                    DFSTerminal(adjacent, visited);
                }
            }

        }
    }


    public T getRoot() {
        return root;
    }

    public Set<T> getVertices() {
        return adjacencyList.keySet();
    }

    public List<T> getAdjacents(T vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean containsVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }


    /**
     * Set the best vertex of the graph. <p>
     * @param best the best vertex.
     */
    public void setBest(T best) {
        this.best = best;
    }

    /**
     * Get the best vertex of the graph. <p>
     * If the best vertex has not been set, the method returns the root vertex.
     * @return the best vertex.
     */
    public T getBest() {
        return best;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();


        for (T vertex : adjacencyList.keySet()) {
            builder.append(vertex.toString()).append(":{ ");

            List<T> value = adjacencyList.get(vertex);
            for (T adjacent : value) {
                if (value.indexOf(adjacent) == 0)
                    builder.append(adjacent.toString());
                else
                    builder.append(",").append(adjacent.toString());
            }
            builder.append(" }\n");
        }
        return builder.toString();
    }


}