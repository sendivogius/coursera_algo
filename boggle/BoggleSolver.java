import edu.princeton.cs.algs4.TrieST;

import java.util.ArrayList;

public class BoggleSolver {
    private TrieST<Integer> words;

    public BoggleSolver(String[] dictionary) {
        words = new TrieST<Integer>();
        for (String w : dictionary) {
            words.put(w, scoreWord(w));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        ArrayList<String> s = new ArrayList<>();
        s.add("AAA");
        return s;
    }

    private int scoreWord(String word) {
        final int len = word.length();
        if (len < 3)
            return 0;
        if (len < 5)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        return 11;
    }

    public int scoreOf(String word) {
        if (words.contains(word))
            return words.get(word);
        return 0;
    }
}