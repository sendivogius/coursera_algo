import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double meanThr;
    private final double stddevThr;
    private final double stderrThr;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            thresholds[i] = runOneSimulation(n);
        }

        meanThr = StdStats.mean(thresholds);
        stddevThr = StdStats.stddev(thresholds);
        stderrThr = 1.96*stddevThr/Math.sqrt(trials);
    }

    private double runOneSimulation(int n) {
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int row = StdRandom.uniform(n)+1;
            int col = StdRandom.uniform(n)+1;
            p.open(row, col);
        }
        return p.numberOfOpenSites()/Math.pow(n, 2);
    }

    public double mean() {
        return meanThr;
    }
    public double stddev() {
        return stddevThr;
    }
    public double confidenceLo() {
        return mean() - stderrThr;
    }
    public double confidenceHi() {
        return mean() + stderrThr;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");


    }
}