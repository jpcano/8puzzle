/******************************************************************************
 *  
 *
 ******************************************************************************/

import java.util.Comparator;
import java.lang.Math;
import edu.princeton.cs.algs4.Stack;

public class Board {
    private int[][] blocks;
    private int N;
    private int manhattan = -1;

    public Board (int[][] blocks) {
        N = blocks.length; 
        this.blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.blocks[i][j] = blocks[i][j];
    }

    private class Coordinate {
        private int x, y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i*N + j+1)
                    hamming++;
        return hamming;
    }

    public int manhattan() {
        int m = manhattan;
        if (m != -1) return m;
        m = 0;
        int x_goal, y_goal;
         for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0) {
                    x_goal = (blocks[i][j] - 1) / N;
                    y_goal = (blocks[i][j] - 1) % N;
                    m += Math.abs(i - x_goal);
                    m += Math.abs(j - y_goal);
                }
            }
         }
         manhattan = m;
         return m;
    }

    public boolean isGoal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != 0 && blocks[i][j] != i*N + j+1)
                    return false;
        return true;
    }

    public Board twin() {
        int[][] twin;
        int old;
        twin = duplicateBlocks();
        if (twin[0][0] == 0 || twin[0][1] == 0) 
            exch(twin, new Coordinate(1, 0), new Coordinate(1, 1));
        else 
            exch(twin, new Coordinate(0,0), new Coordinate(0, 1));
        return new Board(twin);
    }

    private int[][] duplicateBlocks() {
        int[][] duplicate;
        duplicate = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                duplicate[i][j] = blocks[i][j];
        return duplicate;
    }

    private void exch(int[][] board, Coordinate a, Coordinate b) {
        int old;
        old = board[a.getX()][a.getY()];
        board[a.getX()][a.getY()] = board[b.getX()][b.getY()];
        board[b.getX()][b.getY()] = old;
    }

    private boolean isInside(Coordinate p) {
        //System.out.println("checking " + p.getX() + " " + p.getY());
        if (p.getX() >= 0 && p.getX() < N && p.getY() >= 0 && p.getY() < N) {
            //System.out.println("ok  " + p.getX() + " " + p.getY());
            return true;
        }
        return false;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        // Checking dimensions of the boards
        if (this.dimension() != that.dimension()) return false;
        // Checking contents of the boards
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        int[][] blocksDuplicated;
        Coordinate zero;
        Board b;

        zero = findZero();
        for (Coordinate neighbor : getCrossNeighbors(zero)) {
            blocksDuplicated = duplicateBlocks();
            exch(blocksDuplicated, zero, neighbor);
            b = new Board(blocksDuplicated);
            neighbors.push(b);
        }

        return neighbors;
    }

    private Stack<Coordinate> getCrossNeighbors(Coordinate c) {
            Stack<Coordinate> neighbors;
            Coordinate n;

            neighbors = new Stack<Coordinate>();
            //System.out.println(c.getX() + " " + c.getY());
            n = new Coordinate(c.getX(), c.getY() - 1);
            if (isInside(n))
                neighbors.push(n);
            n = new Coordinate(c.getX() + 1, c.getY());
            if (isInside(n))
                neighbors.push(n);
            n = new Coordinate(c.getX(), c.getY() + 1);
            if (isInside(n))
                neighbors.push(n);
            n = new Coordinate(c.getX() - 1, c.getY());
            if (isInside(n))
                neighbors.push(n);
            return neighbors;
    }

    private Coordinate findZero () {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == 0) {
                    return new Coordinate(i, j);
                }
            }
        }
        return new Coordinate(0,0);
    }

    public String toString() {
        String out = "";
        String separator;

        out += N + "\n";
        for (int i = 0; i < N; i++) {
            separator = " ";
            for (int j = 0; j < N; j++) {
                out += separator + blocks[i][j];
                separator = "  ";
            }
            out += "\n";
        }
        return out;
    }

    public static void main (String[] args) { 
        int[][] blocks = {{5, 8, 7},{1, 4, 6},{3, 0, 2}};
        Board b = new Board(blocks);
        System.out.println("Start board:");
        System.out.println(b);
        System.out.println("Dimension: " + b.dimension());
        System.out.println("Hamming: " + b.hamming());
        System.out.println("Manhattan: " + b.manhattan());
        System.out.println("Is Goal?: " + b.isGoal());

        Board twin = b.twin();
        System.out.println("Twin:");
        System.out.println(twin);

        System.out.println("Board equals Twin?: " + b.equals(twin));

        System.out.println("Neighbors:");
        for (Board n: b.neighbors()) {
            System.out.println(n);
        }


    }
}