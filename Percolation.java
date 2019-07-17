import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF weightedQuickUnionUF;
    private WeightedQuickUnionUF auxWeightedQuickUnionUF;
    private boolean isSiteOpen[];
    private int N;
    private int dimension;
    private int openSiteCount;

    /**
     * Create n-by-n grid, with all sites blocked
     *
     * @param n
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater than 0");
        }

        this.N = n * n + 2;
        this.dimension = n;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(N);
        this.auxWeightedQuickUnionUF = new WeightedQuickUnionUF(N);
        this.isSiteOpen = new boolean[N + 1];
    }

    /**
     * Open site (row, col) if it is not open already
     *
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) return;

        int siteMapping = rowColSiteMapping(row, col);
        openSite(siteMapping);
        connectIfRequired(row, col, row - 1, col);
        connectIfRequired(row, col, row + 1, col);
        connectIfRequired(row, col, row, col + 1);
        connectIfRequired(row, col, row, col - 1);
    }

    private void validate(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("row = " + row + ", col = " + col + " are not in the permitted range.");
    }

    private void openSite(int siteMapping) {
        isSiteOpen[siteMapping] = true;
        openSiteCount++;

        if (topLayerSite(siteMapping)) {
            weightedQuickUnionUF.union(0, siteMapping);
            auxWeightedQuickUnionUF.union(0, siteMapping);
        }

        if (bottomLayerSite(siteMapping)) {
            weightedQuickUnionUF.union(siteMapping, N - 1);
        }
    }

    private boolean topLayerSite(int siteMapping) {
        return siteMapping >= 1 && siteMapping <= dimension;
    }

    private boolean bottomLayerSite(int siteMapping) {
        return siteMapping >= N - dimension - 1 && siteMapping <= N - 2;
    }

    private void connectIfRequired(int row, int col, int x, int y) {
        if (!isValid(x, y)) return;
        if (isSiteOpen[rowColSiteMapping(x, y)]) {
            weightedQuickUnionUF.union(rowColSiteMapping(row, col), rowColSiteMapping(x, y));
            auxWeightedQuickUnionUF.union(rowColSiteMapping(row, col), rowColSiteMapping(x, y));
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 1 && x <= dimension && y >= 1 && y <= dimension;
    }

    /**
     * Is site (row, col) open ?
     *
     * @param row
     * @param col
     *
     * @return
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return isSiteOpen[rowColSiteMapping(row, col)];
    }

    /**
     * Is site (row, col) full ?
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        return auxWeightedQuickUnionUF.connected(0, rowColSiteMapping(row, col));
    }

    /**
     * Number of open sites
     *
     * @return
     */
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    /**
     * Does the system percolate ?
     *
     * @return
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, N - 1);
    }

    private int rowColSiteMapping(int row, int col) {
        return (row - 1) * dimension + col;
    }

    public static void main(String[] args) {

    }
}
