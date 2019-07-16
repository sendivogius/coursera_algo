import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Bag<Integer>> nounToId = new ST<>();
    private final ArrayList<String> synsets = new ArrayList<String>();
    private final SAP sapFinder;

    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile == null || hypernymsFile == null)
            throw new IllegalArgumentException();

        In synsetsIn = new In(synsetsFile);
        while (!synsetsIn.isEmpty()) {
            String[] line = synsetsIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            synsets.add(line[1]);
            for (String noun : line[1].split(" ")) {
                if (nounToId.contains(noun)) {
                    nounToId.get(noun).add(id);
                }
                else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    nounToId.put(noun, bag);
                }
            }
        }

        Digraph wordnet = new Digraph(synsets.size());
        In hypernymsIn = new In(hypernymsFile);
        while (!hypernymsIn.isEmpty()) {
            String[] line = hypernymsIn.readLine().split(",");
            int from = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int to = Integer.parseInt(line[i]);
                wordnet.addEdge(from, to);
            }
        }

        if (!isValidDAG(wordnet))
            throw new IllegalArgumentException("Not a DAG");

        sapFinder = new SAP(wordnet);
    }

    private boolean isValidDAG(Digraph graph) {
        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle()) return false;

        int nRoots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0)
                nRoots++;
            if (nRoots > 1)
                return false;
        }
        return true;
    }

    public Iterable<String> nouns() {
        return nounToId.keys();
    }

    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return nounToId.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        Bag<Integer> nouns1 = nounToId.get(nounA);
        Bag<Integer> nouns2 = nounToId.get(nounB);
        return sapFinder.length(nouns1, nouns2);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        Bag<Integer> nouns1 = nounToId.get(nounA);
        Bag<Integer> nouns2 = nounToId.get(nounB);
        int ancestorId = sapFinder.ancestor(nouns1, nouns2);
        return synsets.get(ancestorId);
    }

}
