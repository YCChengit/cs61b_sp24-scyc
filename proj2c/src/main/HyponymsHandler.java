package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;
    private NGramMap ngm;

    public HyponymsHandler(WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> word = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        NgordnetQueryType ngordnetQueryType = q.ngordnetQueryType();

        if (k == 0 && ngordnetQueryType == NgordnetQueryType.HYPONYMS) {
            ArrayList<Integer> hyponyms = wn.hyponyms(word);
            ArrayList<String> hyponymsWords = wn.nouns(hyponyms);
            hyponymsWords.sort(String::compareTo);
            return hyponymsWords.toString();
        }else if (k == 0 && ngordnetQueryType == NgordnetQueryType.ANCESTORS) {
            ArrayList<Integer> ancestors = wn.ancestors(word);
            ArrayList<String> ancestorsWords = wn.nouns(ancestors);
            ancestorsWords.sort(String::compareTo);
            return ancestorsWords.toString();
        }else if (k != 0 && ngordnetQueryType == NgordnetQueryType.HYPONYMS) {
            ArrayList<Integer> hyponyms = wn.hyponyms(word);
            ArrayList<String> hyponymsWords = wn.nouns(hyponyms);
            ArrayList<Double> count_history = ngm.sumHistory(hyponymsWords, startYear, endYear);
            TreeMap<Double, String> countToWord = new TreeMap<>();
            for (int i = 0; i < hyponymsWords.size(); i++) {
                countToWord.put(count_history.get(i), hyponymsWords.get(i));
            }
            ArrayList<String> topK = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                topK.add(countToWord.pollLastEntry().getValue());
            }
            topK.sort(String::compareTo);
            return topK.toString();
        }else if (k != 0 && ngordnetQueryType == NgordnetQueryType.ANCESTORS) {
            ArrayList<Integer> ancestors = wn.ancestors(word);
            ArrayList<String> ancestorsWords = wn.nouns(ancestors);
            ArrayList<Double> count_history = ngm.sumHistory(ancestorsWords, startYear, endYear);
            TreeMap<Double, String> countToWord = new TreeMap<>();
            for (int i = 0; i < ancestorsWords.size(); i++) {
                countToWord.put(count_history.get(i), ancestorsWords.get(i));
            }
            ArrayList<String> topK = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                topK.add(countToWord.pollLastEntry().getValue());
            }
            topK.sort(String::compareTo);
            return topK.toString();
        }
        return null;
    }
}
