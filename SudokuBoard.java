/**********************************************************
 * A class that represents a 9x9 SudokuBoard.
 *
 * A 9x9 2D array of Tiles is used to represent the board.
 * See Tile.java for class representing a Tile.
 *
 * Tarif Haque
 **********************************************************/

import java.util.ArrayList;
import java.util.Collections;

public class SudokuBoard {

    private Tile[][] board;
    private ArrayList<Tile> assignments;

    /**********************************************************
     * Construct a new SudokuBoard by passing in a 2D array
     * of integers representing the board. The first dimension
     * represents rows, the second dimension represents
     * columns.
     **********************************************************/
    public SudokuBoard(int[][] arrayBoard) {
        assignments = new ArrayList<Tile>();
        board = new Tile[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Tile(arrayBoard[i][j], i, j);
            }
        }
    }

    /**********************************************************
     * Returns an array of integers that represents the board's
     * configuration.
     **********************************************************/
    public int[][] getBoardConfiguration() {
        int[][] configuration = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                configuration[i][j] = board[i][j].getValue();
            }
        }
        return configuration;
    }

    /**********************************************************
     * Checks if this board is "equal to" a given board. That
     * is, checks if the given board has the same values in the
     * same places as this board.
     **********************************************************/
    public boolean equals(SudokuBoard b) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.getTile(i, j).getValue() != b.getTile(i, j).getValue())
                    return false;
            }
        }
        return true;
    }

    /**********************************************************
     * Get the tile in the ith row and jth column.
     **********************************************************/
    public Tile getTile(int i, int j) {
        return board[i][j];
    }

    /**********************************************************
     * Determines whether this board has been solved.
     **********************************************************/
    public boolean solved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getValue() == 0) return false;
            }
        }
        return true;
    }

    public boolean isValidSolvedBoard() {
        // check if each row is valid
        for (int i = 0; i < 9; i++) {
            if (invalidSet(grabRow(i))) return false;
        }

        // check if each column is valid
        for (int j = 0; j < 9; j++) {
            if (invalidSet(grabCol(j))) return false;
        }

        // check if each 3x3 grid is valid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (invalidSet(grabGrid(i, j))) return false;
            }
        }

        return true;

    }

    public boolean invalidSet(Tile[] set) {
        int sum = 0;

        for (int i = 0; i < set.length; i++) {
            sum = sum + set[i].getValue();
        }

        // sum of all tile values must equal 45 if valid
        if (sum != 45) return true;
        else return false;
    }

    /**********************************************************
     * Eliminate all values that lie in a tile's row, column.
     * or 3x3 grid.
     **********************************************************/
    private void computePossibilities(Tile tile) {

        // only compute possibilities if the tile has not been
        // assigned a value from 1 - 9 already
        if (tile.getValue() == 0) {
            // eliminate possibilities by examining the tile's row
            for (int i = 0; i < 9; i++) {
                tile.eliminatePossibility(board[tile.getRow()][i].getValue());
            }

            // eliminate possibilities by examining the tile's column
            for (int i = 0; i < 9; i++) {
                tile.eliminatePossibility(board[i][tile.getCol()].getValue());
            }

            // eliminate possibilities by examining the tile's 3x3 grid
            int rowCount = (tile.getRow() / 3) * 3;
            int colCount = (tile.getCol() / 3) * 3;

            for (int i = rowCount; i < rowCount + 3; i++) {
                for (int j = colCount; j < colCount + 3; j++) {
                    tile.eliminatePossibility(board[i][j].getValue());
                }
            }
        }

    }

    /**********************************************************
     * For each tile in the board, compute the tile's possible
     * values. That is, eliminate all values that lie in the
     * tile's row, column. or 3x3 grid.
     **********************************************************/
    public void computeBoardPossibilities() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                computePossibilities(board[i][j]);
            }
        }
    }

    /**********************************************************
     * Grab the given row and column from the sudoku board.
     * Returns a set of 9 Tiles.
     **********************************************************/
    public Tile[] grabGrid(int rowCount, int colCount) {
        rowCount = rowCount * 3;
        colCount = colCount * 3;

        Tile[] grid = new Tile[9];
        int index = 0;
        for (int i = rowCount; i < rowCount + 3; i++) {
            for (int j = colCount; j < colCount + 3; j++) {
                grid[index] = board[i][j];
                index++;
            }
        }

        return grid;
    }

    /**********************************************************
     * Grab the ith row from the sudoku board.
     * Returns a set of 9 Tiles.
     **********************************************************/
    public Tile[] grabRow(int i) {
        Tile[] row = new Tile[9];
        for (int j = 0; j < 9; j++) {
            row[j] = board[i][j];
        }
        return row;
    }

    /**********************************************************
     * Grab the jth column from the sudoku board.
     * Returns a set of 9 Tiles.
     **********************************************************/
    public Tile[] grabCol(int j) {
        Tile[] col = new Tile[9];
        for (int i = 0; i < 9; i++) {
            col[i] = board[i][j];
        }
        return col;
    }

    /**********************************************************
     * Returns a 2D array that contains the number of
     * possibilities for each tile in the sudoku board.
     **********************************************************/
    public int[][] getBoardPossibilities() {
        int[][] possibilities = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                possibilities[i][j] = board[i][j].getNumberPossibilities();
            }
        }

        return possibilities;
    }

    /**********************************************************
     * Checks if there exists a conflict in the board
     * e.g. is any row, column, or 3x3 grid invalid according
     * to the rules of sudoku? If so, return true.
     **********************************************************/
    public boolean boardConflictExists() {

        // check for conflicts in each row
        for (int i = 0; i < 9; i++) {
            if (conflictExists(grabRow(i))) return true;
        }

        // check for conflicts in each column
        for (int j = 0; j < 9; j++) {
            if (conflictExists(grabCol(j))) return true;
        }

        // check for conflicts in each 3x3 grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (conflictExists(grabGrid(i, j))) return true;
            }
        }

        return false;
    }

    /**********************************************************
     * Checks if there exists a conflict in a set of 9 tiles
     * e.g. has a value from 1 - 9 been assigned to more
     * than one tile? If so, returns true.
     **********************************************************/
    private boolean conflictExists(Tile[] set) {

        // initialize "seen" flags for each number 1-9 to false
        boolean[] seenFlags = new boolean[10];

        for (int i = 1; i < set.length; i++) {

            int value = set[i].getValue();

            if (value != 0) {
                // we're seeing not null value again in set, so conflict exists
                if (seenFlags[value]) return true;
                seenFlags[value] = true;
            }

        }
        return false;
    }

    /**********************************************************
     * Given a set of 9 tiles, assign all values (1-9) to tiles
     * if that value is only possible in a single tile. Note
     * that no such value may exist or many such values may
     * exist.
     **********************************************************/
    public void assignValues(Tile[] set) {

        int[] counts = new int[10];
        int[] setIndices = new int[10];

        for (int i = 1; i <= 9; i++) {
            for (int j = 0; j < set.length; j++) {
                if (set[j].isPossible(i)) {
                    counts[i]++;
                    setIndices[i] = j;
                }
            }
        }

        for (int i = 1; i <= 9; i++) {
            // Assign number that is only possible in one tile in the set
            // to that tile, if it has not already been assigned.
            if (counts[i] == 1 && set[setIndices[i]].getValue() == 0) {
                set[setIndices[i]].assignValue(i);
                assignments.add(set[setIndices[i]]);
            }
        }

    }

    public ArrayList<Tile> getAssignments() {
        return assignments;
    }

    public ArrayList<Tile> getTilesSortedByPossibilities() {

        ArrayList<Tile> tiles = new ArrayList<Tile>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j].getNumberPossibilities() >= 2) {
                    tiles.add(board[i][j]);
                }
            }
        }

        Collections.shuffle(tiles);
        Collections.sort(tiles);

        return tiles;
    }

    public ArrayList<Guess> getGuessList() {

        ArrayList<Guess> guesses = new ArrayList<Guess>();
        for (Tile t : getTilesSortedByPossibilities()) {
            int[] possibilities = t.getPossibilities();
            double numPossibilities = t.getNumberPossibilities() * 1.0;
            for (int i = 0; i < possibilities.length; i++) {
                guesses.add(new Guess(t.getRow(), t.getCol(), possibilities[i], 1.0 / numPossibilities));
            }
        }

        return guesses;
    }

}
