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
        int manhattan = 0;
        int x_goal, y_goal;
         for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                x_goal = blocks[i][j] / N - 1;
                y_goal = blocks[i][j] % N;
                manhattan += Math.abs(i - x_goal);
                manhattan += Math.abs(j - y_goal);
            }
         }
         return manhattan;
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
        board[a.getX()][b.getY()] = board[b.getX()][b.getY()];
        board[b.getX()][b.getY()] = old;
    }

    private boolean isInside(Coordinate p) {
        if (p.getX() > 0 && p.getX() < N && p.getY() > 0 && p.getY() < N)
            return true;
        return false;
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        Stack<Coordinate> coordsNeighbors;
        int[][] blocksDuplicated;
        Coordinate zero;
        Board b;

        zero = findZero();
        coordsNeighbors = getCrossNeighbors(zero);
        for (Coordinate neighbor : coordsNeighbors) {
            blocksDuplicated = duplicateBlocks();
            exch(blocksDuplicated, zero, neighbor);
            b = new Board(blocksDuplicated);
        }

        return neighbors;
    }

    private Stack<Coordinate> getCrossNeighbors(Coordinate c) {
            Stack<Coordinate> neighbors;
            Coordinate n;

            neighbors = new Stack<Coordinate>();
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
}