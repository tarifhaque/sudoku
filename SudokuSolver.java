import java.io.*;
import java.util.*;

public class SudokuSolver {

    private SudokuBoard board;
    private PrintWriter writer;
    private static long startTime = System.currentTimeMillis();

    /**********************************************************
     * Construct a new SudokuSolver by passing in the input
     * file name from which the board is read and the output
     * file name to which the solve steps and solved board
     * will be written.
     **********************************************************/
    public SudokuSolver(String inputFile, String outputFile) {
        board = readInputFile(inputFile);
        try {
            writer = new PrintWriter(outputFile, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********************************************************
     * Output a string to both the console and the output
     * file.
     **********************************************************/
    public void output(final String msg) {
        System.out.print(msg);
        writer.print(msg);
    }

    /**********************************************************
     * Populate a SudokuBoard by reading from an input file.
     **********************************************************/
    private SudokuBoard readInputFile(String filename) {
        Scanner input = null;

        try {
            input = new Scanner (new FileReader(filename)); 
        } catch (FileNotFoundException e) {
            output("Cannot find the input file.\n");
            System.exit(0);
        }

        int rowCount = 0;
        int colCount = 0;

        int[][] arrayBoard = new int[9][9];

        while (input.hasNext()) {
            String token = input.next();
            int value;
            if (token.equals("x")) value = 0;
            else value = Integer.parseInt(token);
            
            arrayBoard[rowCount][colCount] = value;

            if (colCount != 8) colCount++;
            else {
                colCount = 0;
                rowCount++;
            }
        }

        input.close();

        return new SudokuBoard(arrayBoard);
    }

    /**********************************************************
     * Write board configuration to both console and output
     * file.
     **********************************************************/
    public void writeBoard() {

        int[][] configuration = board.getBoardConfiguration();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                output(String.valueOf(configuration[i][j]) + " ");
            }
            output("\n");
        }

        output("\n");
    }

    /**********************************************************
     * Write a 2D grid that contains the number of possibilities
     * for each tile in the board.
     **********************************************************/
    public void writeBoardPossibilities() {
        int[][] possibilities = board.getBoardPossibilities();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                output(String.valueOf(possibilities[i][j]) + " ");
            }
            output("\n");
        }
    }

    /**********************************************************
     * Checks if two board configurations are equal.
     **********************************************************/
    public boolean equalConfiguration(int[][] a, int[][] b) {

        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (a[i][j] != b[i][j]) return false;
            }
        }

        return true;
    }

    private boolean solveBoardDeterministcally() {

        int[][] configuration = board.getBoardConfiguration();
        int cycleCount = 0;

        while (!board.solved()) {
            board.computeBoardPossibilities();

            // reduce possibilities for each row
            for (int i = 0; i < 9; i++) {
                board.assignValues(board.grabRow(i));
            }

            // reduce possibilities for each column 
            for (int j = 0; j < 9; j++) {
                board.assignValues(board.grabCol(j));
            }

            // reduce possibilities for each 3x3 grid
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board.assignValues(board.grabGrid(i, j));
                }
            }

            int[][] newConfiguration = board.getBoardConfiguration();

            // if the reductions have made no progress after two cycles, break
            if (equalConfiguration(configuration, newConfiguration)) {
                cycleCount++;
                if (cycleCount == 2) break;
            }

            configuration = newConfiguration;
        }

        if (board.isValidSolvedBoard()) return true;
        else return false;
    }

    public void solveBoard() {

        if (solveBoardDeterministcally()) {
            output("Board has been solved without any guessing.\n");
            writeDefiniteAssignments();
            writeFinalSolvedBoard();
        }

        else {
            output("Board cannot be solved without guessing.\n");

            output("Determined board\n");
            writeBoard();

            output("Definite assignments: \n");
            writeDefiniteAssignments();

            output("Unassigned tiles: \n");
            ArrayList<Tile> tiles = board.getTilesSortedByPossibilities();
            for (Tile t : tiles) {
                output(t.toString() + "\n");
            }
            output("\n");

            // save the configuration of the determined board
            int[][] savedConfiguration = board.getBoardConfiguration();

            while (true) {

                ArrayList<Guess> guesses = board.getGuessList();
                Collections.shuffle(guesses);
                Collections.sort(guesses);

                if (guesses.isEmpty()) {
                    board = new SudokuBoard(savedConfiguration);
                    board.computeBoardPossibilities();
                    continue;
                }

                // Grab one of the most likely guesses & make the guess
                Guess g = guesses.get(0);
                board.getTile(g.row, g.col).assignValue(g.guess);
                output(g.toString() + "\n");

                if (solveBoardDeterministcally()) {
                    output("Bored has been solved non-deterministically.\n");
                    writeFinalSolvedBoard();
                    break;
                } else {
                    output("Board could not be solved after guess. \n");
                    if (board.boardConflictExists()) {
                        output("A board conflict exists after guess. Return to determined state. \n");
                        board = new SudokuBoard(savedConfiguration);
                        board.computeBoardPossibilities();
                    } else {
                        output("..but a board conflict does not exist. So guess again. \n");
                    }
                }

            }

        }
    }

    private void writeFinalSolvedBoard() {
        output("Final solved board: \n");
        writeBoard();
        long endTime = System.currentTimeMillis();
        output("Runtime: " + String.valueOf(endTime - startTime) + " milliseconds \n");
    }

    public void writeDefiniteAssignments() {
        ArrayList<Tile> assignments = board.getAssignments();

        for (Tile t : assignments) {
            output(t.assignmentStatement() + " \n");
        }

        output("\n");
    }

    public void cleanUp() {
        writer.close();
    }

	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver("input.txt", "output.txt");

        solver.output("Initial Sudoku Board: \n");

        solver.writeBoard();

        solver.solveBoard();

        solver.cleanUp();
	}

}
