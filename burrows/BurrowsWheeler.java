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

    private static char[] countsort(char[] array) {
        int[] counts = new int[256];
        char[] out = new char[array.length];
        for (int i = 0; i < array.length; i++)
            counts[array[i]]++;
        for (int i = 1; i < counts.length; i++)
            counts[i] += counts[i - 1];
        for (int i = 0; i < array.length; i++) {
            out[--counts[array[i]]] = array[i];
        }
        return out;
    }


    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        final String t = BinaryStdIn.readString();

        // final String t = "ARD!RCAAAABB";
        // int first = 3;
        char[] chars = countsort(t.toCharArray());
        final int n = chars.length;

        // construct next array
        int startInd = 0;
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            char cc = chars[i];
            if (i == 0 || chars[i - 1] != cc)
                startInd = 0;
            while (t.charAt(startInd) != cc) {
                startInd++;
            }
            next[i] = startInd++;
        }

        // recover original
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(chars[first]);
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