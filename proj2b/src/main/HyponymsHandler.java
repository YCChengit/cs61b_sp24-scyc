package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;

    public HyponymsHandler(WordNet wn) {
        this.wn = wn;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> word = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        ArrayList<Integer> hyponyms = wn.hyponyms(word);
        ArrayList<String> hyponymsWords = wn.nouns(hyponyms);
        hyponymsWords.sort(String::compareTo);
        return hyponymsWords.toString();
    }
}
