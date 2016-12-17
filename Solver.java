/******************************************************************************
 *  
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

public class Solver {

    private Node problem;
    private int moves = 0;

    public Solver (Board initial) {
        if (initial == null)
            throw new NullPointerException("Solver expects a board to solve");
        problem = new Node(initial, 0, null);
    }

    private class Node {
        private Board board;
        private int moves;
        private Node prev;

        private Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
    }

    private Comparator<Node> priorityOrder() {
        return new ByPriority();
    }
    
    private class ByPriority implements Comparator<Node> {
        public int compare(Node a, Node b) {
            int pa = a.moves + a.board.manhattan();
            int pb = b.moves + b.board.manhattan();
            if (pa < pb)       return -1;
            else if (pa > pb)  return 1;      
            else               return 0;  
        }
    }
    
    public boolean isSolvable() {
        return solve() ==  null ? false : true;
    }

    public int moves() {
        Node solution;
        solution = solve();
        return solution.moves;
    }

    public Iterable<Board> solution() {
        Node actualNode;
        Stack<Board> solution = new Stack<Board>();

        actualNode = solve();
        while (actualNode != null) {
            solution.push(actualNode.board);
            actualNode = actualNode.prev;
        }
        return solution;
    }

    private Node solve() {
        MinPQ<Node> actualPQ, testPQ;
        Node actualNode,testNode;
        Board actualBoard, testBoard;
        Node neighbor;

        testNode = new Node(problem.board.twin(), 0, null);
        actualPQ = new MinPQ<Node>(priorityOrder());
        testPQ = new MinPQ<Node>(priorityOrder());
        actualPQ.insert(problem);
        testPQ.insert(testNode);
        actualNode = actualPQ.delMin();
        actualBoard = actualNode.board;
        testNode = testPQ.delMin();
        testBoard = testNode.board;
        while (!actualBoard.isGoal() && !testBoard.isGoal()) {
            for (Board b: actualBoard.neighbors()) {
                neighbor = new Node(b, actualNode.moves + 1, actualNode);
                actualPQ.insert(neighbor);
            }
            for (Board b: testBoard.neighbors()) {
                neighbor = new Node(b, testNode.moves + 1, testNode);
                testPQ.insert(neighbor);
            }
            actualNode = actualPQ.delMin();
            actualBoard = actualNode.board;
            testNode = testPQ.delMin();
            testBoard = testNode.board;
        }
        return actualBoard.isGoal() ? actualNode : null;
    }
    
    public static void main (String[] args) { 
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}