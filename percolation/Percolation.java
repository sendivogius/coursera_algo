import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[] grid;
    private final int n;
    private final WeightedQuickUnionUF unifiedSet;
    private final WeightedQuickUnionUF unifiedSetFill;
    private final int topIdx;
    private final int bottomIdx;
    private int nOpened;

    public Percolation(int n) {            // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        grid = new boolean[n*n];
        topIdx = n*n;
        bottomIdx = topIdx + 1;
        nOpened = 0;

        for (int i = 0; i < grid.length; i++)
            grid[i] = false;

        unifiedSet = new WeightedQuickUnionUF(n*n+2);
        unifiedSetFill = new WeightedQuickUnionUF(n*n+1);

        if (n > 1) {
            for (int i = 0; i < n; i++) {
                unifiedSet.union(topIdx, i);
                unifiedSetFill.union(topIdx, i);
                unifiedSet.union(bottomIdx, this.n * this.n - i - 1);
            }
        }
    }

    private int getIdx(int row, int col) {
        return (row-1)* n +col-1;
    }

    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col))
            return;

        nOpened++;
        int idx = getIdx(row, col);
        grid[idx] = true;

        if (n == 1) {
            unifiedSet.union(idx, bottomIdx);
            unifiedSet.union(idx, topIdx);
            unifiedSetFill.union(idx, topIdx);
            return;
        }

        if (col > 1 && isOpen(row, col-1)) {
            unifiedSet.union(idx, getIdx(row, col - 1));
            unifiedSetFill.union(idx, getIdx(row, col - 1));
        }
        if (col < n && isOpen(row, col+1)) {
            unifiedSet.union(idx, getIdx(row, col + 1));
            unifiedSetFill.union(idx, getIdx(row, col + 1));
        }
        if (row < n && isOpen(row+1, col)) {
            unifiedSet.union(idx, getIdx(row + 1, col));
            unifiedSetFill.union(idx, getIdx(row + 1, col));
        }
        if (row > 1 && isOpen(row-1, col)) {
            unifiedSet.union(idx, getIdx(row - 1, col));
            unifiedSetFill.union(idx, getIdx(row - 1, col));
        }
    }

    private void validate(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n)
            throw new IllegalArgumentException();
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[getIdx(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && unifiedSetFill.connected(getIdx(row, col), topIdx);
    }

    public int numberOfOpenSites() {
        return nOpened;
    }

    public boolean percolates() {
        return unifiedSet.connected(bottomIdx, topIdx);
    }
}
