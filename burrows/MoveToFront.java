import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else
            decode();
    }
    
    public static void encode() {
        LinkedList<Character> codes = new LinkedList<>();
        for (char c = 0; c <= 255; c++)
            codes.add(c);

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int idx = codes.indexOf(c);
            BinaryStdOut.write((char) idx);
            codes.remove(idx);
            codes.addFirst(c);
        }
        BinaryStdOut.flush();
    }

    public static void decode() {
        LinkedList<Character> codes = new LinkedList<>();
        for (char c = 0; c <= 255; c++)
            codes.add(c);

        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();
            char c = codes.get(idx);
            BinaryStdOut.write(c);
            codes.remove(idx);
            codes.addFirst(c);
        }
        BinaryStdOut.flush();
    }

}