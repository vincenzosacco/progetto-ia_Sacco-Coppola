package org.project.Logic.embAsp.minimax.utility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class used to build a graph using an {@link java.util.HashMap} as adjacency list.
 * This version uses generics to allow for different types of vertex identifiers.
 */
public class Graph<T> {
    private Map<T, List<T>> adjacencyList;
    private T root;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        root = null;
    }

    public void addVertex(T vertex) {
        if (root == null) root = vertex;
        adjacencyList.putIfAbsent(vertex, new LinkedList<>());
    }

    public void addEdge(T source, T destination) {
        if (!adjacencyList.containsKey(source))
            throw new IllegalArgumentException("Vertex " + source + " does not exist.");

        List<T> edges = adjacencyList.get(source);
        if (edges.contains(destination)) return;
        edges.add(destination);
        // Add the following line if the graph is undirected
        adjacencyList.get(destination).add(source);
    }

    public T getRoot() {
        return root;
    }

    public List<T> getAdjacent(T vertex) {
        return adjacencyList.get(vertex);
    }

    public boolean containsVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (T vertex : adjacencyList.keySet()) {
            builder.append(vertex.toString()).append(": ");
            for (T adjacent : adjacencyList.get(vertex)) {
                builder.append(adjacent.toString()).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}