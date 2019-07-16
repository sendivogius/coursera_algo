import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class SAP {
    private final Digraph digraph;
    private final int nV;
    private Iterable<Integer> lastV;
    private Iterable<Integer> lastW;
    private ArrayList<Integer> cachedResult;


    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        digraph = new Digraph(G);
        nV = G.V();
    }

    private boolean isValidNode(Integer node) {
        return node != null && node >= 0 && node < nV;
    }

    private ArrayList<Integer> getShortestAncestors(int v, int w) {
        return getShortestAncestors(Collections.singletonList(v),
                                    Collections.singletonList(w));
    }

    private HashMap<Integer, Integer> getAncestors(Iterable<Integer> nodes) {
        for (Integer i : nodes)
            if (!isValidNode(i)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, nodes);
        HashMap<Integer, Integer> ancestors = new HashMap<Integer, Integer>();
        for (int i = 0; i < nV; i++)
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

        if (v.equals(lastV) && w.equals(lastW) && cachedResult != null)
            return cachedResult;

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

        cachedResult = new ArrayList<>();
        cachedResult.add(lastCommon);
        cachedResult.add(lastCommon == -1 ? -1 : lastCommonDist);

        lastV = v;
        lastW = w;

        return cachedResult;
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

        int length = sap.length(Arrays.asList(13, 23, null, 24), Arrays.asList(6, 16, 17));
        int ancestor = sap.ancestor(Arrays.asList(13, 23, 24), Arrays.asList(6, 16, 17));
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            length = sap.length(v, w);
            ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}