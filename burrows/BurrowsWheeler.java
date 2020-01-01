import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        final int n = csa.length();
        char[] lastCol = new char[n];
        int first = -1;
        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0)
                first = i;
            lastCol[i] = s.charAt((n + csa.index(i) - 1) % n);
        }

        BinaryStdOut.write(first);
        BinaryStdOut.write(new String(lastCol));
        BinaryStdOut.flush();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        final String t = BinaryStdIn.readString();

        // final String t = "ARD!RCAAAABB";
        // int first = 3;

        final int n = t.length();
        char[] out = new char[n];

        int[] counts = new int[256];
        for (int i = 0; i < counts.length; i++)
            counts[i] = 0;

        for (int i = 0; i < n; i++)
            counts[t.charAt(i)]++;

        for (int i = 1; i < counts.length; i++)
            counts[i] += counts[i - 1];

        int[] next = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            char cc = t.charAt(i);
            int newPos = --counts[cc];
            out[newPos] = cc;
            next[newPos] = i;
        }

        // recover original
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(out[first]);
            first = next[first];
        }
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else
            inverseTransform();
    }

}