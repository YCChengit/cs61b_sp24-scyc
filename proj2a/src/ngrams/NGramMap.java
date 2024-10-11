package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.*;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    HashMap<String, TimeSeries> words;
    TimeSeries total;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        In wordsIn = new In(wordsFilename);
        words = new HashMap<>();
        while (wordsIn.hasNextLine()) {
            String line = wordsIn.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            int time = Integer.parseInt(parts[1]);
            double count = Double.parseDouble(parts[2]);
            if (!words.containsKey(word)) {
                TimeSeries ts = new TimeSeries();
                ts.put(time, count);
                words.put(word, ts);
            } else {
                words.get(word).put(time, count);
            }
        }

        In countsIn = new In(countsFilename);
        total = new TimeSeries();
        while (countsIn.hasNextLine()) {
            String line = countsIn.readLine();
            String[] parts = line.split(",");
            int time = Integer.parseInt(parts[0]);
            double count = Double.parseDouble(parts[1]);
            total.put(time, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = new TimeSeries(words.get(word), startYear, endYear);
        return ts;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = new TimeSeries(words.get(word), MIN_YEAR, MAX_YEAR);
        return ts;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries ts = new TimeSeries(total, MIN_YEAR, MAX_YEAR);
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = new TimeSeries(words.get(word), startYear, endYear);
        TimeSeries total_s = new TimeSeries(this.total, startYear, endYear);
        TimeSeries weight = ts.dividedBy(total_s);
        return weight;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!words.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = new TimeSeries(words.get(word), MIN_YEAR, MAX_YEAR);
        TimeSeries weight = ts.dividedBy(total);
        return weight;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sum = new TimeSeries();
        TimeSeries sum_w = new TimeSeries();
        for (String word : words) {
            if (!this.words.containsKey(word)) {
                continue;
            }
            TimeSeries ts = new TimeSeries(this.words.get(word), startYear, endYear);
            sum = sum.plus(ts);
        }
        sum_w = sum.dividedBy(new TimeSeries(this.total, startYear, endYear));
        return sum_w;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries sum = new TimeSeries();
        TimeSeries sum_w = new TimeSeries();
        for (String word : words) {
            if (!this.words.containsKey(word)) {
                continue;
            }
            TimeSeries ts = new TimeSeries(this.words.get(word), MIN_YEAR, MAX_YEAR);
            sum = sum.plus(ts);
        }
        sum_w = sum.dividedBy(total);
        return sum_w;
    }

}
