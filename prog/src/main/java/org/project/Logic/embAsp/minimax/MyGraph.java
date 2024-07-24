package org.project.Logic.embAsp.minimax;

import java.util.*;

/**
 * Class used to build a not oriented graph using an {@link java.util.LinkedHashMap} as adjacency list. <p>
 * @param  the type of the vertex. Must implement {@link Object#equals(Object)} and {@link Object#hashCode()}.
 */

public class MyGraph {
    private LinkedHashMap<GridState, List<GridState>> adjacencyList;
    private GridState root;


    public MyGraph() {
        this.adjacencyList = new LinkedHashMap<>(100);
        root = null;
    }


    public void addAll(Collection<GridState> vertices) {
        for (GridState vertex : vertices) {
            addVertex(vertex);
        }
    }

    /**
     * Add a vertex to the graph. <p>
     * If the vertex is already present, the method does nothing.
     * @param vertex the vertex to add.
     * @return true if the vertex is added, false otherwise.
     * @throws IllegalArgumentException if the vertex is null.
     */
    public boolean addVertex(GridState vertex) {
        if (vertex == null) throw new IllegalArgumentException("Vertex cannot be null.");

        if (root == null) {
            root = vertex;
        }

        return adjacencyList.putIfAbsent(vertex, new LinkedList<>()) == null;

    }


    /**
     * Add an edge between two vertices. <p>
     * If the edge is already present, the method does nothing. <p>
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @throws IllegalArgumentException <p>if the source vertex does not exist. .
     */
    public void addEdge(GridState source, GridState destination) {
        // check if source exist
        if (!adjacencyList.containsKey(source))
            throw new IllegalArgumentException("Vertex " + source + " does not exist.");
        if (!adjacencyList.containsKey(destination))
            throw new IllegalArgumentException("Desination " + destination + " does not exist.");

        List<GridState> edges = adjacencyList.get(source);

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
    public boolean edge(GridState source, GridState destination) {
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

    public Collection<GridState> find(GridState start, GridState end) {
        if (start == null) throw new IllegalArgumentException("start cannot be null.");
        if (end == null) throw new IllegalArgumentException("end cannot be null.");

//        ArrayDeque<GridState> path = new ArrayDeque<>(); // LIFO
//        path.add(start);

        LinkedList<GridState> path = new LinkedList<>();
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
            List<GridState> visited ;
            for (GridState adj : adjacencyList.get(start)) {
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


    public List<GridState> DFSBranch(GridState start, GridState end) {
        List<GridState> visited = new LinkedList<>();
        ArrayDeque<GridState> path = new ArrayDeque<>();
        DFSBranch(start, end, visited, path);
        stopDFSBranch = false;

        return visited;
    }
    private boolean stopDFSBranch = false;
    /**
     * This method performs the same search of {@link MyGraph#DFS(GridState, GridState, List)} but stops as soon as it finds a terminal vertex.
     * @param start
     * @param end
     * @param visited
     */
    private void DFSBranch(GridState start, GridState end, List<GridState> visited, ArrayDeque<GridState> path) {

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
    private void DFS(GridState start, GridState end, List<GridState> visited) {
        if (!stopDFS) {
            if (start == null || end == null)
                throw new IllegalArgumentException("Start and end vertices cannot be null.");

            visited.add(start);

            for (GridState adjacent : adjacencyList.get(start)) {
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
    public List<GridState> DFS(GridState start, GridState end){
        List<GridState> visited = new LinkedList<>();
        DFS(start, end, visited);
        stopDFS = false;

        return visited;
    }

    public List<GridState> DFSWin(GridState start) {
        List<GridState> path = new LinkedList<>();
        DFSWin(start, path);
        stopDFSWin = false;

        if (path.getLast().isWinning())
            return path;

        return null;
    }

    private boolean stopDFSWin = false;
    private void DFSWin(GridState start, List<GridState> path) {
        if (!stopDFSWin) {
            if (start == null )
                throw new IllegalArgumentException("Start cannot be null.");

            path.add(start);

            for (GridState adjacent : adjacencyList.get(start)) {
                if (!stopDFSWin){
                    // EXIT CONDITION: adjacent have no children
                    if (adjacent.isWinning()) {
                        path.add(adjacent);
                        stopDFSWin = true;
                    }

                    else {
                        // ROLLBACK CONDITION: adjacent is not a child of the last vertex in the path
                        if (!edge(path.getLast(), adjacent)) {
                            // Remove sub-path created until now
                            while (!path.getLast().equals(start))
                                path.removeLast();
                        }

                        if (!path.contains(adjacent)) {
                            DFSWin(adjacent, path);
                        }
                    }
                }
            }
        }
    }


    public GridState getRoot() {
        return root;
    }
    public GridState getBestLeaf() {
        // Get the leaf with the max value
        return getLeaves().stream().max(Comparator.comparingInt(l -> l.value)).get();
    }

    public Set<GridState> getVertices() {
        return adjacencyList.keySet();
    }

    public List<GridState> getFathers(GridState vertex) {
        List<GridState> fathers = new LinkedList<>();
        for (GridState father : adjacencyList.keySet()) {
            if (adjacencyList.get(father).contains(vertex))
                fathers.add(father);
        }
        return fathers;
    }
    /**
     * Get the leaves of the graph. <p>
     * A leaf is a vertex with no children.
     * @return the list of leaves.
     */
    public List<GridState> getLeaves() {
        List<GridState> leaves = new LinkedList<>();
        for (GridState vertex : adjacencyList.keySet()) {
            if (adjacencyList.get(vertex).isEmpty())
                leaves.add(vertex);
        }
        return leaves;
    }

    public boolean containsVertex(GridState vertex) {
        return adjacencyList.containsKey(vertex);
    }

//
//    /**
//     * Set the best vertex of the graph. <p>
//     * @param best the best vertex.
//     */
//    public void setBest(GridState best) {
//        this.best = best;
//    }
//
//    /**
//     * Get the best vertex of the graph. <p>
//     * If the best vertex has not been set, the method returns the root vertex.
//     * @return the best vertex.
//     */
//    public GridState getBest() {
//        return best;
//    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();


        for (GridState vertex : adjacencyList.keySet()) {
            builder.append(vertex.toString()).append(":{ ");

            List<GridState> value = adjacencyList.get(vertex);
            for (GridState adjacent : value) {
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