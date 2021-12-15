import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public final class PercolationStats {
    private static final double CONFIDENCE_COEFFICIENT = 1.96;

    private final int numberOfTrials;

    private final double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        /* Validation */

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and the number of trials must be greater than zero");
        }

        /* Private fields initialization */

        thresholds = new double[trials];

        numberOfTrials = trials;

        /* Set thresholds */

        for (int currentTrial = 0; currentTrial < trials; currentTrial++) {
            thresholds[currentTrial] = getThreshold(n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_COEFFICIENT * stddev()) / Math.sqrt(numberOfTrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_COEFFICIENT * stddev()) / Math.sqrt(numberOfTrials));
    }

    private double getThreshold(int n) {
        var percolation = new Percolation(n);

        while (!percolation.percolates()) {
            percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
        }

        return (double) percolation.numberOfOpenSites() / (n * n);
    }

    // test client (see below)
    public static void main(String[] args) {
        var percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" +
                percolationStats.confidenceLo() + ", " +
                percolationStats.confidenceHi() + "]");
    }
}
