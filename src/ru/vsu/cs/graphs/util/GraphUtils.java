package ru.vsu.cs.graphs.util;

import ru.vsu.cs.NullIterator;
import ru.vsu.cs.graphs.*;

import java.util.Scanner;

public class GraphUtils {
    public static WeightedDigraph fromStr(String str) throws InstantiationException, IllegalAccessException {
        WeightedDigraph graph = new SimpleWeightedDigraph(0);

        Scanner scanner = new Scanner(str);
        while (scanner.hasNext()) {
            graph.addEdge(scanner.nextInt(), scanner.nextInt(), scanner.nextDouble());
        }
        return graph;
    }

    public static String toDot(Graph graph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        boolean isDigraph = graph instanceof Digraph;
        sb.append(isDigraph ? "digraph" : "strict graph").append(" {").append(nl);
        for (int v1 = 0; v1 < graph.vertexCount(); v1++) {
            //int count = 0;
            if (graph instanceof WeightedGraph) {
                Iterable<WeightedGraph.WeightedEdgeTo> edges = ((WeightedGraph) graph).adjacenciesWithWeights(v1);
                if (!(edges instanceof NullIterator)) {
                    for (WeightedGraph.WeightedEdgeTo v2: edges) {
                        sb.append(String.format("  %d %s %d[label=\"%4$s\",weight=\"%4$s\"]", v1, (isDigraph ? "->" : "--"), v2.to(), v2.weight())).append(nl);
                        //count++;
                    }
                }
            }
            else {
                for (Integer v2 : graph.adjacencies(v1)) {
                    sb.append(String.format("  %d %s %d", v1, (isDigraph ? "->" : "--"), v2)).append(nl);
                    //count++;
                }
            }
//            if (count == 0) {
//                sb.append(v1).append(nl);
//            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
