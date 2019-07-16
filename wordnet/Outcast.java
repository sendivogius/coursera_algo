import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet net) {
        wordnet = net;
    }

    public String outcast(String[] nouns) {
        int maxDist = 0;
        String maxNoun = null;
        for (String noun : nouns) {
            int dist = distTo(noun, nouns);
            if (dist > maxDist) {
                maxDist = dist;
                maxNoun = noun;
            }
        }

        return maxNoun;
    }

    private int distTo(String noun, String[] nouns) {
        int dist = 0;
        for (String s : nouns) {
            if (s.equals(noun)) continue;
            dist += wordnet.distance(noun, s);
        }
        return dist;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}