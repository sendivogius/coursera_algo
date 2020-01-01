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

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
    }

    ;

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else
            inverseTransform();
    }

}