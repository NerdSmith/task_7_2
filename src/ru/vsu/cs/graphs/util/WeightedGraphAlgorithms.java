package ru.vsu.cs.graphs.util;

import ru.vsu.cs.Pair;
import ru.vsu.cs.graphs.WeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class WeightedGraphAlgorithms {
    public static ArrayList<Pair<Integer, List<Integer>>> getAllPathsWithLength(WeightedGraph graph, int s, int d) {
        ArrayList<Pair<Integer, List<Integer>>> pathsWithLength = new ArrayList<>();
        boolean[] isVisited = new boolean[graph.vertexCount()];
        ArrayList<Integer> pathList = new ArrayList<>();
        int currLen = 0;

        pathList.add(s);

        getAllPathsWithLengthUtil(graph, s, d, isVisited, pathList, currLen, pathsWithLength);
        return pathsWithLength;
    }

    private static void getAllPathsWithLengthUtil(WeightedGraph graph,
                                           Integer curr,
                                           Integer dest,
                                           boolean[] isVisited,
                                           List<Integer> localPathList,
                                           int currLen,
                                           ArrayList<Pair<Integer, List<Integer>>> arrayList) {
        if (curr.equals(dest)) {
            arrayList.add(new Pair<>(currLen, new ArrayList<>(localPathList)));
            return;
        }

        isVisited[curr] = true;
        for (WeightedGraph.WeightedEdgeTo edge : graph.adjacenciesWithWeights(curr)) {
            if (!isVisited[edge.to()]) {
                localPathList.add(edge.to());
                currLen += edge.weight();
                getAllPathsWithLengthUtil(graph, edge.to(), dest, isVisited, localPathList, currLen, arrayList);

                currLen -= edge.weight();
                localPathList.remove(Integer.valueOf(edge.to()));
            }
        }
        isVisited[curr] = false;
    }
}
