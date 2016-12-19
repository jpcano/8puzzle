/******************************************************************************
 *  
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.ST;
import java.util.Comparator;

public class Solver {

    private Node problem;
    private int moves = 0;
    boolean solvable = true;
    private Node solution = null;

    public Solver (Board initial) {
        if (initial == null)
            throw new NullPointerException("Solver expects a board to solve");
        problem = new Node(initial, 0, null);
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;

        private Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        public int compareTo(Node that) {
            if (this.moves < that.moves)
                return -1;
            else if (this.moves > that.moves)
                return 1;
            else
                return 0;
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
        Node result;
        result = solve();
        return result != null ? true : false;
    }

    public int moves() {
        Node result;
        result = solve();
        return result != null ? result.moves : -1;
    }

    public Iterable<Board> solution() {
        Node actualNode;
        Stack<Board> result = new Stack<Board>();

        actualNode = solve();
        while (actualNode != null) {
                result.push(actualNode.board);
                actualNode = actualNode.prev;
        }
        return result;
    }

    private Node solve() {
        MinPQ<Node> actualPQ, testPQ;
        Node actualNode,testNode;
        Node neighbor;
        ST<Node, int[]> actualBST, testBST;

        if (solvable && solution != null)
            return solution;
        if (!solvable)
            return null;

        actualNode = problem;
        testNode = new Node(problem.board.twin(), 0, null);
        actualPQ = new MinPQ<Node>(priorityOrder());
        actualPQ.insert(actualNode);
        actualBST = new ST<Node, int[]>();
        testPQ = new MinPQ<Node>(priorityOrder());
        testPQ.insert(testNode);
        testBST = new ST<Node, int[]>();

        actualBST.put(actualNode, null);
        testBST.put(testNode, null);
        while (true) {
            if (actualNode != null) {
                if (actualNode.board.isGoal())
                    break;
                actualNode = searchNode(actualNode, actualPQ, actualBST);
            }
            else break;
            if (testNode != null) {
                if (testNode.board.isGoal())
                    break;
                testNode = searchNode(testNode, testPQ, testBST);
            }      
        }

        if (actualNode != null && actualNode.board.isGoal()) {
            solution = actualNode;
            return actualNode;
        }
        else {
            solvable = false;
            return null;
        }
    }
    
    private Node searchNode (Node root, MinPQ<Node> pq, ST<Node, int[]> bst) {
        Node actualNode;
        Node neighbor;
        Board actualBoard;

        actualNode = root;
        actualBoard = actualNode.board;
        for (Board b: actualBoard.neighbors()) {
            neighbor = new Node(b, actualNode.moves + 1, actualNode);
            if (!bst.contains(neighbor)) {
                pq.insert(neighbor);
                bst.put(neighbor, null);
            }
        }
        if (pq.isEmpty())
            return null;
        return pq.delMin();
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