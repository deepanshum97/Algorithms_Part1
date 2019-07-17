import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int tiles[][];
    private int N;
    private int firstRandom;
    private int secondRandom;
    private int manhattanDistance;
    private int hammingDistance;

    private class Coordinate {
        int row;
        int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Create a board from an n-by-n array of tiles,
     * where tiles[row][col] = tile at (row, col)
     *
     * @param tiles
     *
     */
    public Board(int[][] tiles) {
        this.N = tiles[0].length;
        this.tiles = deepCopy(tiles, N);
        this.firstRandom = StdRandom.uniform(N * N - 1) + 1;
        this.secondRandom = StdRandom.uniform(N * N - 1) + 1;
        while (secondRandom == firstRandom) {
            secondRandom = StdRandom.uniform(N * N - 1) + 1;
        }

        this.hammingDistance = preComputeHamming();
        this.manhattanDistance = preComputeManhattan();
    }

    private int[][] deepCopy(int[][] tiles, int n) {
        int arr[][] = new int[n][n];
        for (int i = 0 ; i < n ; i++) {
            for (int j = 0 ; j < n ; j++) {
                arr[i][j] = tiles[i][j];
            }
        }

        return arr;
    }

    private Coordinate[] buildCoordinates(int[][] tiles) {
        Coordinate [] coordinates = new Coordinate[N * N];
        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {
                coordinates[tiles[i][j]] = new Coordinate(i, j);
            }
        }

        return coordinates;
    }

    /**
     * String representation of this board
     *
     * @return
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(dimension());
        builder.append(System.lineSeparator());
        for (int i = 0 ; i < N ; i++) {
            builder.append(" ");
            for (int j = 0 ; j < N ; j++) {
                builder.append(tiles[i][j]);
                if (j != N - 1) {
                    builder.append("  ");
                }
            }
            if (i != N - 1) {
                builder.append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    /**
     * Board dimension n
     *
     * @return
     */
    public int dimension() {
        return N;
    }

    /**
     * Number of tiles out of place
     *
     * @return int
     */
    public int hamming() {
        return hammingDistance;
    }

    private int preComputeHamming() {
        int hammingDist = 0;
        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {
                int tileMapping = expectedTileNumber(i, j);
                if (tileMapping == 0) {
                    continue;
                }

                if (tileMapping != tiles[i][j]) {
                    hammingDist += 1;
                }
            }
        }

        return hammingDist;
    }

    private int expectedTileNumber(int row, int col) {
        int mapping = N * row + col + 1;
        return mapping == N * N ? 0 : mapping;
    }

    /**
     * Sum of Manhattan distances between tiles and goal
     *
     * @return
     */
    public int manhattan() {
        return manhattanDistance;
    }

    private int preComputeManhattan() {
        Coordinate[] coordinates = buildCoordinates(tiles);

        int manhattanDist = 0;
        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {
                int tileMapping = expectedTileNumber(i, j);
                if (tileMapping == 0) {
                    continue;
                }

                manhattanDist += Math.abs(i - coordinates[tileMapping].row);
                manhattanDist += Math.abs(j - coordinates[tileMapping].col);
            }
        }

        return manhattanDist;
    }

    /**
     * Is this board the goal board
     *
     * @return
     */
    public boolean isGoal() {
        return manhattanDistance == 0;
    }

    /**
     * Does this board equal to other board
     *
     * @param other
     * @return boolean
     */
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!(other instanceof Board)) return false;

        Board otherBoard = (Board) other;
        if (dimension() != otherBoard.dimension()) return false;

        for (int i = 0 ; i < N ; i++) {
            for (int j = 0 ; j < N ; j++) {
                if (tiles[i][j] != otherBoard.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * All neighboring boards
     *
     * @return
     */
    public Iterable<Board> neighbors() {
        Coordinate[] coordinates = buildCoordinates(tiles);
        List<Board> neighbors = new ArrayList<>();
        generate(neighbors, coordinates[0].row - 1, coordinates[0].col, coordinates[0]);
        generate(neighbors, coordinates[0].row, coordinates[0].col - 1, coordinates[0]);
        generate(neighbors, coordinates[0].row + 1, coordinates[0].col, coordinates[0]);
        generate(neighbors, coordinates[0].row, coordinates[0].col + 1, coordinates[0]);

        return neighbors;
    }

    private void generate(List<Board> neighbors, int row, int col, Coordinate reference) {
        if (isValid(row, col)) {
            int newTiles[][] = deepCopy(tiles, N);
            swap(newTiles, row, col, reference);

            neighbors.add(new Board(newTiles));
        }
    }

    private void swap(int[][] newTiles, int row, int col, Coordinate coordinate) {
        int temp = newTiles[coordinate.row][coordinate.col];
        newTiles[coordinate.row][coordinate.col] = newTiles[row][col];
        newTiles[row][col] = temp;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }

    /**
     * A board that is obtained by exchanging any pair of blocks
     *
     * @return Board
     */
    public Board twin() {
        int newTiles[][] = deepCopy(tiles, N);
        Coordinate[] coordinates = buildCoordinates(tiles);
        swap(newTiles, coordinates[firstRandom].row, coordinates[firstRandom].col, coordinates[secondRandom]);

        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int [][]tiles = new int[3][3];
        tiles[0][0] = 1;
        tiles[0][1] = 0;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 2;
        tiles[1][2] = 5;
        tiles[2][0] = 7;
        tiles[2][1] = 8;
        tiles[2][2] = 6;

        Board board = new Board(tiles);
        StdOut.println(board);
        StdOut.println();

        StdOut.println("Printing Neighbors");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }

        StdOut.println();
        StdOut.println("Hamming Distance : " + board.hamming());
        StdOut.println("Manhattan Distance : " + board.manhattan());

        StdOut.println("Printing Twin");
        StdOut.println(board.twin());
        StdOut.println();

        StdOut.println("Printing Twin Again");
        StdOut.println(board.twin());
        StdOut.println();
    }
}
