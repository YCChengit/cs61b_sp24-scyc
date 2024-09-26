import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.lang.reflect.Array;


public class Percolation {
    // TODO: Add any necessary instance variables.
    private WeightedQuickUnionUF uf;
    private int N;
    private boolean[][] grid;

    public Percolation(int N) {
        // TODO: Fill in this constructor.
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        uf = new WeightedQuickUnionUF(N * N + 2);
        grid = new boolean[N][N];
        this.N = N;
    }

    public void open(int row, int col) {
        // TODO: Fill in this method.
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IllegalArgumentException("row and col must be between 0 and N-1");
        }
        grid[row][col] = true;
        int index = row * N + col;
        if (row == 0) {
            uf.union(index, N * N);
        }
        if (row == N - 1) {
            uf.union(index, N * N + 1);
        }
        if (row > 0 && grid[row - 1][col]) {
            uf.union(index, index - N);
        }
        if (row < N - 1 && grid[row + 1][col]) {
            uf.union(index, index + N);
        }
        if (col > 0 && grid[row][col - 1]) {
            uf.union(index, index - 1);
        }
        if (col < N - 1 && grid[row][col + 1]) {
            uf.union(index, index + 1);
        }
    }

    public boolean isOpen(int row, int col) {
        // TODO: Fill in this method.
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        // TODO: Fill in this method.
        return uf.connected(row * N + col, N * N);
    }

    public int numberOfOpenSites() {
        // TODO: Fill in this method.
        return uf.count() - 2;
    }

    public boolean percolates() {
        // TODO: Fill in this method.
        return uf.connected(N * N, N * N + 1);
    }

    // TODO: Add any useful helper methods (we highly recommend this!).
    // TODO: Remove all TODO comments before submitting.

}
