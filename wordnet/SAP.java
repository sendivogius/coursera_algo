import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class SAP {
    private final Digraph digraph;

    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    private boolean isValidNode(int node) {
        return node >= 0 && node < digraph.V();
    }

    private ArrayList<Integer> getShortestAncestors(int v, int w) {
        HashMap<Integer, Integer> ancestorsV = getAncestors(v);
        HashMap<Integer, Integer> ancestorsW = getAncestors(w);
        int lastCommon = -1;
        int lastCommonDist = Integer.MAX_VALUE;
        for (int i : ancestorsV.keySet()) {
            if (!ancestorsW.containsKey(i))
                continue;
            int distBy = ancestorsV.get(i) + ancestorsW.get(i);
            if (distBy < lastCommonDist) {
                lastCommonDist = distBy;
                lastCommon = i;
            }
        }

        ArrayList<Integer> ret = new ArrayList<>();
        ret.add(lastCommon);
        ret.add(lastCommon == -1 ? -1 : lastCommonDist);
        return ret;
    }

    private HashMap<Integer, Integer> getAncestors(int node) {
        if (!isValidNode(node))
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, node);
        HashMap<Integer, Integer> ancestors = new HashMap<Integer, Integer>();
        for (int i = 0; i < digraph.V(); i++)
            if (bfs.hasPathTo(i))
                ancestors.put(i, bfs.distTo(i));
        return ancestors;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getShortestAncestors(v, w).get(1);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return getShortestAncestors(v, w).get(0);
    }

    private ArrayList<Integer> getShortestAncestors(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        ArrayList<Integer> minLength = new ArrayList<>();
        minLength.add(-1);
        minLength.add(Integer.MAX_VALUE);
        for (Integer v1 : v) {
            if (v1 == null || !isValidNode(v1))
                throw new IllegalArgumentException();
            for (Integer w1 : w) {
                if (w1 == null || !isValidNode(w1))
                    throw new IllegalArgumentException();
                ArrayList<Integer> v1w1 = getShortestAncestors(v1, w1);
                if (v1w1.get(0) != -1 && v1w1.get(1) < minLength.get(1)) {
                    minLength = v1w1;
                }
            }
        }
        if (minLength.get(0) == -1) {
            minLength.remove(1);
            minLength.add(-1);
        }
        return minLength;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return getShortestAncestors(v, w).get(1);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return getShortestAncestors(v, w).get(0);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        // int length = sap.length(Arrays.asList(13, 23, 24), Arrays.asList(6, 16, 17));
        // int ancestor = sap.ancestor(Arrays.asList(13, 23, 24), Arrays.asList(6, 16, 17));
        // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}