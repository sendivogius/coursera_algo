/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int n = 0;

        RandomizedQueue<String> randQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String value = StdIn.readString();
            n++;
            if (n <= k) {
                randQueue.enqueue(value);
            } else {
                double p = StdRandom.uniform();
                double pThr = (n-k)/(double) n;
                if (p > pThr) {
                    randQueue.dequeue();
                    randQueue.enqueue(value);
                }
            }
        }

        for (String s: randQueue) {
            StdOut.println(s);
        }
    }
}
