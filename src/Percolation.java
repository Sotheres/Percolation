import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private final boolean[][] openSites;
    private int numOpenSites;
    private final int size;
    private int fullRoot;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Attempt to create 0-cell grid.");
        }

        uf = new WeightedQuickUnionUF(n*n+2);
        for (int i = 0; i < n; i++) {
            uf.union(n*n, i);
            uf.union(n*n+1, n*n-1 - i);
        }

        openSites = new boolean[n][n];
        size = n;
        fullRoot = size * size;
    }

    public void open(int row, int col) {
        validate(row, col);

        if (!isOpen(row, col)) {
            openSites[row-1][col-1] = true;
            numOpenSites++;

            int n = rowColToN(row, col);
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(n, n - 1);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (col < size && isOpen(row, col + 1)) {
                uf.union(n, n + 1);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (row < size && isOpen(row + 1, col)) {
                uf.union(n, n + size);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(n, n - size);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);

        return openSites[row-1][col-1];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);

        int n = rowColToN(row, col);
        return isOpen(row, col) && uf.find(n) == fullRoot;
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return numOpenSites > 0 && uf.find(size*size) == uf.find(size*size + 1);
    }

    private int rowColToN(int row, int col) {
        return size*(row - 1) + col - 1;
    }

    private void validate(int p, int q) {
        if (p < 1 || p > size || q < 1 || q > size) {
            throw new IllegalArgumentException("Index out of openSite bounds.");
        }
    }

    public static void main(String[] args) {
        int n = 10;
        Percolation grid = new Percolation(n);
        int row, col;

        System.out.println(grid.uf.find(1));

//        while (!grid.percolates()) {
//            row = StdRandom.uniform(n) + 1;
//            col = StdRandom.uniform(n) + 1;
//            grid.open(row, col);
//        }
//
//        System.out.println((double) grid.numberOfOpenSites()/(n*n));
    }
}