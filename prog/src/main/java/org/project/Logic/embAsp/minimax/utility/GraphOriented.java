package org.project.Logic.embAsp.minimax.utility;

import java.util.*;

/**
 * Class used to build an oriented graph using an {@link java.util.LinkedHashMap} as adjacency list. <p>
 * @param <T> the type of the vertex. Must implement {@link Object#equals(Object)} and {@link Object#hashCode()}.
 */

public class GraphOriented<T> {
    private LinkedHashMap<T, List<T>> adjacencyList;
    private T root;

    public GraphOriented() {
        this.adjacencyList = new LinkedHashMap<>();
        root = null;
    }


    /**
     * Add a vertex to the graph. <p>
     * If the vertex is already present, the method does nothing.
     * @param vertex the vertex to add.
     * @throws IllegalArgumentException if the vertex is null.
     */
    public void addVertex(T vertex) {
        if (root == null) root = vertex;
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


    public T getRoot() {
        return root;
    }

    /**
     * Get the adjacent vertices of a vertex. <p>
     * A vertex A is adjacent to a vertex B if there is an edge between A and B. <p>
     * @param vertex the vertex to get the adjacent vertices.
     * @return a list of adjacent vertices.
     */
    public List<T> getAdjacent(T vertex) {
        return adjacencyList.get(vertex);
    }

    public Set<T> getVertices() {
        return adjacencyList.keySet();
    }

    public boolean containsVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    T getVertex(T child) {
        for (T vertex : adjacencyList.keySet()) {
            if (vertex.equals(child)) return vertex;
        }
        return null;
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