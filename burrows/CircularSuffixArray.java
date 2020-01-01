import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private class StringSuffixComparator implements Comparator<Integer> {

        public StringSuffixComparator() {
        }

        public String getSuffix(int offset) {
            if (offset == 0)
                return str;
            return str.substring(offset) + str.substring(0, offset - 1);
        }

        @Override
        public int compare(Integer index1, Integer index2) {
            return getSuffix(index1).compareTo(getSuffix(index2));
        }
    }

    private final String str;
    private final Integer[] suffixOrder;

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        str = s;
        suffixOrder = new Integer[length()];
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
        return str.length();
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

