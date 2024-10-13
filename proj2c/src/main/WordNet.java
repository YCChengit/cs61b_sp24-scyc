package main;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {

    private Graph graphs;
    private HashMap<String, List<Integer>> wordToId;

    public WordNet(String filehypernyms, String filewords) {
        In hypernymsIn = new In(filehypernyms);
        In wordsIn = new In(filewords);

        graphs = new Graph();
        wordToId = new HashMap<>();

        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] parts = line.split(",");
            int node = Integer.parseInt(parts[0]);
            if (!graphs.containsKey(node)) {
                graphs.put(node, new ArrayList<>());
            }
            for (int i = 1; i < parts.length; i++) {
                int child = Integer.parseInt(parts[i]);
                if (!graphs.containsKey(child)) {
                    graphs.put(child, new ArrayList<>());
                }
                graphs.get(node).add(child);
            }
        }

        while (wordsIn.hasNextLine()) {
            String line = wordsIn.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String words = parts[1];
            String[] wordList = words.split(" ");
            for (String word : wordList) {
                if (!wordToId.containsKey(word)) {
                    wordToId.put(word, new ArrayList<>());
                    wordToId.get(word).add(id);
                } else {
                    wordToId.get(word).add(id);
                }
            }
        }
    }

    public ArrayList<Integer> hyponyms(String word) {
        if (!wordToId.containsKey(word)) {
            return new ArrayList<>();
        }
        List<Integer> id = wordToId.get(word);
        ArrayList<Integer> hyponyms = new ArrayList<>();
        for (int i : id) {
            Graph subgraph = graphs.subgraph(i);
            hyponyms.addAll(subgraph.keySet());
        }
        return hyponyms;
    }

    public ArrayList<Integer> hyponyms(List<String> words) {
        ArrayList<Integer> common = new ArrayList<>();
        for (String word : words) {
            ArrayList<Integer> hyponyms = hyponyms(word);
            if (common.isEmpty()) {
                common.addAll(hyponyms);
            } else {
                common.retainAll(hyponyms);
            }
        }
        return common;
    }

    public ArrayList<Integer> ancestors(String word) {
        if (!wordToId.containsKey(word)) {
            return new ArrayList<>();
        }
        List<Integer> id = wordToId.get(word);
        ArrayList<Integer> ancestors = new ArrayList<>();
        for (int i : id) {
            ancestors.add(i);
            ancestors.addAll(graphs.ancestors(i));
        }
        return ancestors;
    }

    public ArrayList<Integer> ancestors(List<String> words) {
        ArrayList<Integer> common = new ArrayList<>();
        for (String word : words) {
            ArrayList<Integer> ancestors = ancestors(word);
            if (common.isEmpty()) {
                common.addAll(ancestors);
            } else {
                common.retainAll(ancestors);
            }
        }
        return common;
    }

    public ArrayList<String> nouns(ArrayList<Integer> ids) {
        ArrayList<String> words = new ArrayList<>();
        for (int id : ids) {
            for (String word : wordToId.keySet()) {
                if (wordToId.get(word).contains(id) && !words.contains(word)) {
                    words.add(word);
                }
            }
        }
        return words;
    }
}
