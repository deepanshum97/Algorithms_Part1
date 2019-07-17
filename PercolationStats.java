import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double T;

    /**
     * Perform trials independent experiments on an n-by-n grid
     *
     * @param n
     * @param trials
     */
    public PercolationStats(int n, int trials) {
        // Perform n Trials
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N or T is invalid");
        }

        this.T = (double) trials;

        double samples[] = new double[trials];
        for (int trial = 0 ; trial < trials ; trial++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                percolation.open(row, col);
            }

            samples[trial] = percolation.numberOfOpenSites() / (double)(n * n);
        }

        mean = StdStats.mean(samples);
        stddev = StdStats.stddev(samples);
    }

    /**
     * Sample mean of percolation threshold
     *
     * @return
     */
    public double mean() {
        return mean;
    }

    /**
     * Sample standard deviation of percolation threshold
     *
     * @return
     */
    public double stddev() {
        return stddev;
    }

    /**
     * Low endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceLo() {
        return mean - (1.96 * stddev() / Math.sqrt(T));
    }

    /**
     * High endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceHi() {
        return mean + (1.96 * stddev() / Math.sqrt(T));
    }

    /**
     * Test client (described below)
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
