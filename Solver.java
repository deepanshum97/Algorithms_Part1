import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

    private List<Board> solution;
    private boolean isSolvable;

    private class EnhancedBoard implements Comparable<EnhancedBoard> {
        int moves;
        Board board;
        EnhancedBoard parent;
        int priority;

        public EnhancedBoard(Board board, int moves, EnhancedBoard parent) {
            this.moves = moves;
            this.board = board;
            this.parent = parent;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(EnhancedBoard other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    /**
     * Find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        Board twinBoard = initial.twin();
        isSolvable = true;

        MinPQ<EnhancedBoard> minPQ = new MinPQ<>();
        MinPQ<EnhancedBoard> twinMinPQ = new MinPQ<>();

        minPQ.insert(new EnhancedBoard(initial, 0, null));
        twinMinPQ.insert(new EnhancedBoard(twinBoard, 0, null));

        EnhancedBoard prev = null;
        while (!minPQ.isEmpty()) {
            EnhancedBoard curr = minPQ.delMin();
            EnhancedBoard currTwin = twinMinPQ.delMin();

            if (curr.board.isGoal()) {
                prev = curr;
                break;
            }

            if (currTwin.board.isGoal()) {
                prev = null;
                isSolvable = false;
                break;
            }

            for (Board neighbor : curr.board.neighbors()) {
                if (curr.parent != null && neighbor.equals(curr.parent.board)) {
                    continue;
                }

                minPQ.insert(new EnhancedBoard(neighbor, curr.moves + 1, curr));
            }

            for (Board neighbor : currTwin.board.neighbors()) {
                if (currTwin.parent != null && neighbor.equals(currTwin.parent.board)) {
                    continue;
                }

                twinMinPQ.insert(new EnhancedBoard(neighbor, currTwin.moves + 1, currTwin));
            }
        }

        generateSolution(prev);
    }

    private void generateSolution(EnhancedBoard prev) {
        solution = new ArrayList<>();
        while (prev != null) {
            solution.add(prev.board);
            prev = prev.parent;
        }

        Collections.reverse(solution);
    }

    /**
     * Is the initial board solvable? (see below)
     *
     * @return boolean
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * Min number of moves to solve initial board
     *
     * @return int
     */
    public int moves() {
        return solution.size() - 1;
    }

    /**
     * Sequence of boards in a shortest solution
     *
     * @return
     */
    public Iterable<Board> solution() {
        if (!isSolvable) return null;

        return new ArrayList<>(solution);
    }

    /**
     * Test client (see below)
     *
     * @param args
     */
    public static void main(String[] args) {
        In in = new In("/Users/deepansm/workspace/Algs4/src/src/princeton/assignment4/puzzle04.txt");
        // In in = new In("/Users/deepansm/workspace/Algs4/src/src/princeton/assignment4/puzzle3x3-unsolvable.txt");
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