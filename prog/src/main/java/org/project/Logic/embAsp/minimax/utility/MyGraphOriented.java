package org.project.Logic.embAsp.minimax.utility;

import java.util.*;

/**
 * Class used to build an oriented graph using an {@link java.util.LinkedHashMap} as adjacency list. <p>
 * @param <T> the type of the vertex. Must implement {@link Object#equals(Object)} and {@link Object#hashCode()}.
 */

public class MyGraphOriented<T> {
    private LinkedHashMap<T, List<T>> adjacencyList;
    private T root;
    private T best;


    public MyGraphOriented() {
        this.adjacencyList = new LinkedHashMap<>();
        root = null;
        best = null;
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
     * Oriented edge between two vertices. <p>
     * @param source the source vertex.
     * @param destination the destination vertex.
     * @return true if the edge exists, false otherwise.
     */
    public boolean edge(T source, T destination) {
        if (!adjacencyList.containsKey(source))
            throw new IllegalArgumentException("Vertex " + source + " does not exist.");
        if (!adjacencyList.containsKey(destination))
            throw new IllegalArgumentException("Desination " + destination + " does not exist.");

        return adjacencyList.get(source).contains(destination);
    }

    /**
     * Given a vertex V, return the list of vertices that have an edge to V. <p>
     * @param destination the destination vertex.
     * @return the list of vertices that have an edge to V.
     */
    public List<T> entryVertex(T destination) {
        List<T> entry = new LinkedList<>();
        for (T vertex : adjacencyList.keySet()) {
            if (adjacencyList.get(vertex).contains(destination)) entry.add(vertex);
        }
        return entry;
    }

    public List<T> reverseDFS(T start, T end, List<T> visited) {
        visited.add(start);

        if (start.equals(end)) return visited;

        for (T entry : entryVertex(start)) {
            if (!visited.contains(entry)) {
                List<T> path = reverseDFS(entry, end, visited);
                if (path != null) return path;
            }
        }
        return null;
    }

    /**
     * Find a path between two vertices using a Breadth First Search algorithm. <p>
     * @param start the start vertex.
     * @param end the end vertex.
     * @param visited the list of visited vertices.
     */
    public void findDFS(T start, T end, List<T> visited) {
        visited.add(start);

        if (start.equals(end)) return;

        for (T adjacent : adjacencyList.get(start)) {
            if (!visited.contains(adjacent)) {
                findDFS(adjacent, end, visited);
            }
        }
    }

    public T getRoot() {
        return root;
    }

    public Set<T> getVertices() {
        return adjacencyList.keySet();
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