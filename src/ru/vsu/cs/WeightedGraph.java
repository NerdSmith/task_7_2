package ru.vsu.cs;


public interface WeightedGraph extends Graph {

    interface WeightedEdgeTo {
        int to();
        double weight();
    }

    void addEdge(int v1, int v2, double weight);

    @Override
    default void addEdge(int v1, int v2) {
        throw new UnsupportedOperationException("Use 'addEdge' with 'weight' param instead");
    }

    Iterable<WeightedEdgeTo> adjacenciesWithWeights(int v);

    default Double getWeight(int v1, int v2) {
        for (WeightedEdgeTo adj : adjacenciesWithWeights(v1)) {
            if (adj.to() == v2) {
                return adj.weight();
            }
        }
        return null;
    }

    @Override
    default boolean isAdj(int v1, int v2) {
        return getWeight(v1, v2) != null;
    }
}
