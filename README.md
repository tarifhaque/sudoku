Solve standard 9x9 Sudoku Boards.

After all tiles have been deterministically assigned, we introduce
a random component to the algorithm.

Organize tiles in ascending order of their number of possibilities,
excluding tiles with one possibility, which have already been assigned
a value.

1. Grab the tile with the lowest number of possibilities.

2. Assign that tile a random value among its possibilities.
   Mark the tile as "guessed."

3. Attempt to solve the board non-deterministically by assuming the tile assigned
   in Step 2 was a correct assignment. That is, reduce rows, columns, and 3x3 grids
   until we get "stuck" again or we've solved the board.

   > If we get "stuck" : check if there is a board conflict e.g. is any row, column,
   or 3x3 grid invalid according to the rules of sudoku? If there is a board
   conflict, return to Step 1.

