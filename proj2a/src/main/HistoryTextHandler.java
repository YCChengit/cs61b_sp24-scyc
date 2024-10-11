package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

import static utils.Utils.SHORT_WORDS_FILE;
import static utils.Utils.TOTAL_COUNTS_FILE;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap ngm;

    HistoryTextHandler(NGramMap ngm) {
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        for (String word : words) {
            response += word + ": ";
            TimeSeries ts = ngm.weightHistory(word, startYear, endYear);
            response += ts.toString() + "\n";
        }

        return response;
    }
}
