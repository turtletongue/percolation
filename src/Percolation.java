import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public final class Percolation {
    private static final byte VIRTUAL_ROOTS_NUMBER = 2;
    private static final byte FIRST_ROW = 1;

    /* It has top and bottom virtual roots
           X
        1  2  3
        4  5  6
        7  8  9
           X
    */

    private final WeightedQuickUnionUF percolationUnionFind;

    /* It has only top virtual root
           X
        1  2  3
        4  5  6
        7  8  9
    */

    private final WeightedQuickUnionUF fullUnionFind;

    private final boolean[][] isOpened;

    private int numberOfOpenSites = 0;

    private final int topRootIndex;
    private final int bottomRootIndex;

    private final int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        /* Validation */

        if (n <= 0) {
            throw new IllegalArgumentException("N should be greater than zero");
        }

        /* Private fields initialization */

        this.n = n;

        topRootIndex = (n * n);
        bottomRootIndex = topRootIndex + 1;

        /* Union Find initialization */

        percolationUnionFind = new WeightedQuickUnionUF((n * n) + VIRTUAL_ROOTS_NUMBER);
        fullUnionFind = new WeightedQuickUnionUF((n * n) + VIRTUAL_ROOTS_NUMBER - 1);

        /* Sites state creation */

        isOpened = new boolean[n][n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateRowAndCol(row, col);

        if (isOpen(row, col)) {
            return;
        }

        int siteStateRow = row - 1;
        int siteStateCol = col - 1;

        isOpened[siteStateRow][siteStateCol] = true;

        int ufSiteIndex = getUFIndex(row, col);

        /*
            1  *  3
            4 [5] 6
            7  8  9
        */

        if (row != 1 && isOpen(row - 1, col)) {
            var neighborUfSiteIndex = getUFIndex(row - 1, col);

            percolationUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
            fullUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
        }

        /*
            1  2  3
            4 [5] 6
            7  *  9
        */

        if (row != n && isOpen(row + 1, col)) {
            var neighborUfSiteIndex = getUFIndex(row + 1, col);

            percolationUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
            fullUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
        }

        /*
            1  2  3
            * [5] 6
            7  8  9
        */

        if (col != 1 && isOpen(row, col - 1)) {
            var neighborUfSiteIndex = getUFIndex(row, col - 1);

            percolationUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
            fullUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
        }

        /*
            1  2  3
            4 [5] *
            7  8  9
        */

        if (col != n && isOpen(row, col + 1)) {
            var neighborUfSiteIndex = getUFIndex(row, col + 1);

            percolationUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
            fullUnionFind.union(ufSiteIndex, neighborUfSiteIndex);
        }

        numberOfOpenSites++;

        /* Connect to virtual roots */

        if (row == FIRST_ROW) {
            percolationUnionFind.union(ufSiteIndex, topRootIndex);
            fullUnionFind.union(ufSiteIndex, topRootIndex);
        }

        if (row == n) {
            percolationUnionFind.union(ufSiteIndex, bottomRootIndex);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowAndCol(row, col);

        return isOpened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowAndCol(row, col);

        return isOpen(row, col) &&
                fullUnionFind.find(topRootIndex) == fullUnionFind.find(getUFIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolationUnionFind.find(topRootIndex) == percolationUnionFind.find(bottomRootIndex);
    }

    private int getUFIndex(int row, int col) {
        validateRowAndCol(row, col);

        return (((row - 1) * n) + col) - 1;
    }

    private void validateRowAndCol(int row, int col) {
        if (row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Col and row must be greater than zero");
        }

        if (row > n || col > n) {
            throw new IllegalArgumentException("Col and row must be less than or equal to zero");
        }
    }
}
