import main.Graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestGraph {
    @Test
    public void createGraph() {
        Graph g = new Graph();
        assertEquals(0, g.size());
    }

    @Test
    public void testSubgraph() {
        Graph g = new Graph();
        g.put(1, new ArrayList<>(Arrays.asList(2, 3)));
        g.put(2, new ArrayList<>(Arrays.asList(4, 5)));
        g.put(3, new ArrayList<>(Arrays.asList(6, 7)));
        g.put(4, new ArrayList<>(Arrays.asList()));
        g.put(5, new ArrayList<>(Arrays.asList()));
        g.put(6, new ArrayList<>(Arrays.asList()));
        g.put(7, new ArrayList<>(Arrays.asList()));

        Graph subgraph0 = g.subgraph(1);
        assertEquals(7, subgraph0.size());
        assertEquals(2, subgraph0.get(1).get(0).intValue());

        Graph subgraph = g.subgraph(2);
        assertEquals(3, subgraph.size());
        assertEquals(4, subgraph.get(2).get(0).intValue());

        Graph subgraph2 = g.subgraph(3);
        assertEquals(3, subgraph2.size());
        assertEquals(6, subgraph2.get(3).get(0).intValue());

        Graph subgraph3 = g.subgraph(4);
        assertEquals(0, subgraph3.size());
    }
}
