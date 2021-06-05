package ru.vsu.cs.graphs;

import ru.vsu.cs.NullIterator;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SimpleWeightedDigraph implements WeightedDigraph {
    protected static class Edge implements WeightedEdgeTo {
        int destination;
        double weight;

        public Edge(int destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int to() {
            return destination;
        }

        @Override
        public double weight() {
            return weight;
        }
    }

    private int vertices;
    private int edges;
    private List<List<WeightedEdgeTo>> adjacencylist;

    private static Iterable<WeightedEdgeTo> nullIterableEdges = new NullIterator<>();

    private static Iterable<Integer> nullIterableInt = new NullIterator<>();

    public SimpleWeightedDigraph(int vertices) {
        this.vertices = vertices;
        adjacencylist = new ArrayList<>(vertices);
        for (int i = 0; i <vertices ; i++) {
            adjacencylist.add(new ArrayList<>());
        }
    }

    @Override
    public int vertexCount() {
        return vertices;
    }

    @Override
    public int edgeCount() {
        return edges;
    }

    private int countingRemove(List<WeightedEdgeTo> list, int v) {
        int count = 0;
        if (list != null) {
            for (Iterator<WeightedEdgeTo> it = list.iterator(); it.hasNext(); ) {
                if (it.next().to() == v) {
                    it.remove();
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void removeEdge(int v1, int v2) {
        edges -= countingRemove(adjacencylist.get(v1), v2);
    }

    @Override
    public Iterable<Integer> adjacencies(int v) {
        List<WeightedEdgeTo> adj = adjacencylist.get(v);
        if (adj == null) {
            return nullIterableInt;
        }
        else {
            return adj.stream().map(edge -> edge.to()).collect(Collectors.toList());
        }
    }

    @Override
    public void addEdge(int v1, int v2, double weight) {
        int maxV = Math.max(v1, v2);
        for (; vertices <= maxV; vertices++) {
            adjacencylist.add(null);
        }
        if (!isAdj(v1, v2)) {
            if (adjacencylist.get(v1) == null) {
                adjacencylist.set(v1, new ArrayList<>());
            }
            adjacencylist.get(v1).add(new Edge(v2, weight));
            edges++;
        }
    }

    @Override
    public Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v) {
        return adjacencylist.get(v) == null ? nullIterableEdges : adjacencylist.get(v);
    }
}
