import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final static int[][] offsets = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };

    private final int[][] blocks;
    private int hamming;
    private int manhattan;
    private int zeroPosX;
    private int zeroPosY;

    public Board(int[][] blocks) {
        int n = blocks.length;

        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = blocks[i][j];
                this.blocks[i][j] = val;
            }
        }
        recalcDist();
    }

    private void recalcDist() {
        hamming = 0;
        manhattan = 0;

        int n = blocks.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = blocks[i][j];
                if (val == 0) {
                    zeroPosX = i;
                    zeroPosY = j;
                    continue;
                }

                if (i * n + j + 1 != val)
                    hamming++;

                manhattan += Math.abs(i - Math.floorDiv(val - 1, n)) + Math
                        .abs(j - Math.floorMod(val - 1, n));
            }
        }
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        return this.hamming;
    }

    public int manhattan() {
        return this.manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int swp1x = (zeroPosX + 1) % dimension();
        int swp1y = zeroPosY;
        int swp2x = zeroPosX;
        int swp2y = (zeroPosY + 1) % dimension();

        Board bt = new Board(this.blocks);
        bt.blocks[swp1x][swp1y] = this.blocks[swp2x][swp2y];
        bt.blocks[swp2x][swp2y] = this.blocks[swp1x][swp1y];
        bt.recalcDist();
        return bt;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;

        Board b = (Board) y;
        if (dimension() != b.dimension()) return false;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (b.blocks[i][j] != this.blocks[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // there are 4 possible neighbours
        ArrayList<Board> neighbours = new ArrayList<>();

        for (int[] oxy : offsets) {
            int new0x = zeroPosX + oxy[0];
            int new0y = zeroPosY + oxy[1];
            if (new0x < 0 || new0x >= dimension() || new0y < 0 || new0y >= dimension())
                continue;
            // swap new_0 with zero_pos
            Board nbg = new Board(this.blocks);
            nbg.blocks[zeroPosX][zeroPosY] = this.blocks[new0x][new0y];
            nbg.blocks[new0x][new0y] = 0;
            nbg.recalcDist();
            neighbours.add(nbg);
        }
        return neighbours;
    }

    public String toString() {
        // int n_max = (int) Math.ceil(Math.log10(dimension()*dimension()));
        StringBuilder sB = new StringBuilder();
        sB.append(dimension());
        sB.append('\n');
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                sB.append(' ');
                sB.append(this.blocks[i][j]);
            }
            sB.append('\n');
        }
        return sB.toString();
    }

    public static void main(String[] args) {
        int[][] array = { { 8, 1, 3 }, { 4, 0, 5 }, { 7, 6, 2 } };
        Board b = new Board(array);
        StdOut.println(b.toString());

        for (Board b2 : b.neighbors()) {
            StdOut.println(b2.toString());
        }

    }
}
