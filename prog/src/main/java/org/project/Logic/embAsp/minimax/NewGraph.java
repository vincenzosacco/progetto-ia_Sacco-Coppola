package org.project.Logic.embAsp.minimax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewGraph {
    static class Node {
        final int id, value;
        List<Node> children;
        final boolean isMax;
        Node parent;

        Node(GridState state, boolean isMax) {
            this.id = state.id;
            this.value = isMax ? state.value : -state.value; // If node is MIN then value is negative
            this.isMax = isMax;
            this.children = new ArrayList<>();
        }
        // Constructor for root node
        protected Node(GridState state) {
            this.id = state.id;
            this.value = state.value ;
            this.isMax = true;
            this.children = new ArrayList<>();
            parent = null;
        }


        int utility(){
            // search max value in children
            int maxChildVal = Integer.MIN_VALUE;
            for (Node child : children) {
                maxChildVal = Math.max(maxChildVal, child.value);
            }

            return value + maxChildVal;
        }

        @Override
        public String toString() {
            String root = (isMax ? "Max_" : "Min_") + id + "[" + value + "]";
            String par;
            if (parent instanceof RootNode r ) par = r.toString();
            else if (parent == null)
                throw new NullPointerException("Parent is null");
            else par = (parent.isMax ? "Max_" : "Min_")+parent.id;

            return root +"<- "+ par;
        }
    }
    static class RootNode extends Node {

        RootNode(GridState state) {
            super(state);
        }

        @Override
        public String toString() {
            return "ROOT" + "[" + value + "]";
        }
    }


//--BUILD GRAPH---------------------------------------------------------------------------------------------------------

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    HashMap<Node, GridState> depth1Nodes = new HashMap<>();


    RootNode buildGraph(GridState rootState, int maxUnitCode, int depth) throws Exception {
        RootNode root = new RootNode(rootState);
        first = depth;
        addChildren(root, rootState, maxUnitCode, depth, true);

        return root;
    }

    private int first ;
    private void addChildren(Node root, GridState rootState, int maxUnitCode, int depth, boolean isMax) throws Exception {
    //--END RECURSION
        if (depth == 0) {
            // Calcola il valore del nodo e termina la ricorsione
            return ;
        }

    //  Generate children, first children are MAX
        List<GridState> childrenStates;
        if (isMax)
            childrenStates = GraphBuilder.childrenOf(rootState, maxUnitCode, true); // MAX add only best moves
        else
            childrenStates = GraphBuilder.childrenOf(rootState, maxUnitCode, false); // MIN add all possible moves (no best moves)

        for (GridState childState : childrenStates) {
            Node child = new Node(childState, isMax);

            root.children.add(child);
            child.parent = root;

            //
            if (depth == first) {
                depth1Nodes.put(child, childState);
            }

            addChildren(child, childState, maxUnitCode, depth - 1, !isMax);
        }


//
//        List<Callable<Node>> tasks = new ArrayList<>();
//        for (int i = 0; i < NUM_MOVES; i++) { // NUM_MOVES è il numero di possibili mosse da questo nodo
//            final int move = i;
//            tasks.add(() -> {
//                Node child = new Node(0); // Sostituisci 0 con il calcolo effettivo del valore
//                // Aggiungi logica per calcolare il valore del nodo figlio basato sulla mossa
//                return buildGraph(child, depth - 1);
//            });
//        }
//
//        try {
//            List<Future<Node>> results = executor.invokeAll(tasks);
//            for (Future<Node> result : results) {
//                root.getChildren().add(result.get());
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        return root;
    }

    void shutdown() {
        executor.shutdown();
    }

    static String graphToString(Node root) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        if (root instanceof RootNode rootNode) {
            sb.append(rootNode).append("\n");
        }
        else {
            sb.append(root.toString()).append("\n");
        }

        for (Node child : root.children) {
            sb.append(graphToString(child));
        }
        return sb.toString();
    }
}