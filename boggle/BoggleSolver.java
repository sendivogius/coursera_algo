import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    private final TST<Integer> words;

    private BoggleBoard board;
    private HashSet<String> foundWords;
    private boolean[] visited;
    private int[][] neighbours;

    private int rows;
    private int cols;

    public BoggleSolver(String[] dictionary) {
        words = new TST<Integer>();

        for (String w : dictionary) {
            if (w.length() > 2)
                words.put(w, scoreWord(w));
        }
    }

    private int scoreWord(String word) {
        final int len = word.length();
        // if (len < 3)
        //     return 0;
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

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int words = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            words++;
        }
        StdOut.println("Words = " + words);
        StdOut.println("Score = " + score);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        this.rows = board.rows();
        this.cols = board.cols();
        this.visited = new boolean[rows * cols];
        this.foundWords = new HashSet<>();
        precalcNeighbours();

        for (int i = 0; i < rows * cols; i++)
            dfs(i, ""); // add board and foundWords

        return this.foundWords;
    }

    private void precalcNeighbours() {
        this.neighbours = new int[rows * cols][];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int ix = i * cols + j;
                ArrayList<Integer> hs = new ArrayList<>();
                for (int dx = -1; dx < 2; dx++)
                    for (int dy = -1; dy < 2; dy++) {
                        if (dx == 0 && dy == 0)
                            continue;
                        int ni = i + dx;
                        int nj = j + dy;
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols)
                            hs.add(ni * cols + nj);
                    }

                this.neighbours[ix] = new int[hs.size()];
                for (int k = 0; k < hs.size(); k++)
                    this.neighbours[ix][k] = hs.get(k);
            }
        }
    }


    private char getLetterAt(int ix) {
        int i = ix / cols;
        int j = ix % cols;
        return this.board.getLetter(i, j);
    }

    private void dfs(int i, String prefix) {
        // create possible word ending at (i, j)
        char currentLetter = getLetterAt(i);
        String currentWord = prefix + ((currentLetter == 'Q') ? "QU" : currentLetter);

        // check if found word or need to look further
        boolean checkFurther = false;
        if (words.contains(currentWord)) {
            this.foundWords.add(currentWord);
            checkFurther = true;
        }
        else {
            checkFurther = hasKeysWithPrefix(currentWord);
        }

        if (!checkFurther)
            return;

        // run dfs on unvisited neighbours
        visited[i] = true;
        for (int ni : neighbours[i])
            if (!visited[ni])
                dfs(ni, currentWord);

        visited[i] = false;
    }


    private boolean hasKeysWithPrefix(String currentWord) {
        if (currentWord.length() > 2)
            return words.keysWithPrefix(currentWord).iterator().hasNext();
        return true;
    }

    public int scoreOf(String word) {
        if (words.contains(word))
            return words.get(word);
        return 0;
    }
}