import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF uf;
    private final boolean[][] openSites;
    private final boolean[] botConnected;
    private int numOpenSites;
    private final int size;
    private int fullRoot;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Attempt to create 0-cell grid.");
        }

        uf = new WeightedQuickUnionUF(n*n + 1);
        openSites = new boolean[n][n];
        botConnected = new boolean[n*n + 1];
        size = n;
        fullRoot = size * size;

        for (int i = 0; i < n; i++) {
            uf.union(n*n, i);
            botConnected[n * n - 1 - i] = true;
        }
    }

    public void open(int row, int col) {
        validate(row, col);

        if (!isOpen(row, col)) {
            openSites[row-1][col-1] = true;
            numOpenSites++;

            int n = rowColToN(row, col);
            boolean bottomConnected = false;

            if (col > 1 && isOpen(row, col - 1)) {
                if (botConnected[uf.find(n-1)]) {
                    bottomConnected = true;
                }
                uf.union(n, n - 1);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (col < size && isOpen(row, col + 1)) {
                if (botConnected[uf.find(n+1)]) {
                    bottomConnected = true;
                }
                uf.union(n, n + 1);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (row < size && isOpen(row + 1, col)) {
                if (botConnected[uf.find(n+size)]) {
                    bottomConnected = true;
                }
                uf.union(n, n + size);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }
            if (row > 1 && isOpen(row - 1, col)) {
                if (botConnected[uf.find(n-size)]) {
                    bottomConnected = true;
                }
                uf.union(n, n - size);
                if (uf.find(n) == uf.find(size * size)) {
                    fullRoot = uf.find(n);
                }
            }

            if(bottomConnected || botConnected[n]) {
                botConnected[uf.find(n)] = true;
            }
        }
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);

        return openSites[row-1][col-1];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);

        return isOpen(row, col) && uf.find(rowColToN(row, col)) == fullRoot;
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        return botConnected[fullRoot];
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

        System.out.println(grid.uf.find(10));
    }
}