import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class BoggleSolver {
    private HashSet<String> words;

    public BoggleSolver(String[] dictionary) {
        words = new HashSet<String>();
        Collections.addAll(words, dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        ArrayList<String> s = new ArrayList<>();
        s.add("AAA");
        return s;
    }

    public int scoreOf(String word) {
        int len = 0;
        for (char c : word.toCharArray()) {
            len += c == 'Q' ? 2 : 1;
        }
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
}