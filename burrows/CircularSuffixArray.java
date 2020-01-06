import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;


public class CircularSuffixArray {
    private class StringSuffixComparator implements Comparator<Integer> {

        public StringSuffixComparator() {
        }

        private char charAt(int index, int offset) {
            return str.charAt((index + offset) % len);
        }

        @Override
        public int compare(Integer index1, Integer index2) {
            for (int i = 0; i < len; i++) {
                char left = charAt(index1, i);
                char right = charAt(index2, i);
                if (left < right)
                    return -1;
                if (left > right)
                    return 1;
            }
            return 0;
        }
    }

    private final String str;
    private final int len;
    private final Integer[] suffixOrder;

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        str = s;
        len = str.length();
        suffixOrder = new Integer[len];
        for (int i = 0; i < length(); i++) {
            suffixOrder[i] = i;
        }
        Arrays.sort(suffixOrder, new StringSuffixComparator());
    }

    public int index(int i) {
        if (i < 0 || i > length() - 1)
            throw new IllegalArgumentException();
        return suffixOrder[i];
    }

    public int length() {
        return len;
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i : csa.suffixOrder
        ) {
            StdOut.println(i);
        }
    }

}

