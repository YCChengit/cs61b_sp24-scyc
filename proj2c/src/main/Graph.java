package main;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Graph extends TreeMap<Integer, ArrayList<Integer>> {

    public Graph() {
        super();
    }

    public Graph subgraph(int root) {
        if (!this.containsKey(root)) {
            return null;
        } else if (this.get(root).isEmpty()) {
            return new Graph();
        }
        Graph subgraph = new Graph();
        subgraph.put(root, this.get(root));
        for (int i : this.get(root)) {
            if (this.containsKey(i)) {
                subgraph.put(i, this.get(i));
                Graph subsubgraph = this.subgraph(i);
                for (int j : subsubgraph.keySet()) {
                    subgraph.put(j, subsubgraph.get(j));
                }
            }
        }
        return subgraph;
    }

    public ArrayList<Integer> ancestors(int child) {
        ArrayList<Integer> ancestors = new ArrayList<>();
        if (!this.containsKey(child)) {
            return ancestors;
        }
        for (int i : this.keySet()) {
            if (this.get(i).contains(child)) {
                ancestors.add(i);
                ancestors.addAll(ancestors(i));
            }
        }
        return ancestors;
    }

}
