This program solves and generates standard 9x9 Sudoku Boards.

First, straightforward logic is used to deterministically assign tiles.
Each tile's possibilities are computed according to the rules of
sudoku. That is, numbers appearing in the tile's row, column or box
are eliminated from consideration. Each row, column, and box are then
"reduced" by identifying "naked singles," or numbers that are only
possible once in a row, column, or box. We continue this reduction
process until no more tiles can be definitively assigned using
the approach.

This approach can solve a good number of boards all by itself.
However, many boards do not relent to this method. So, after all tiles
have been deterministically assigned, we introduce a random component to
the algorithm. We make sure to save the configuration of the board at this
point. This part of the algorithm is what makes this both a puzzle solver,
and generator.

1. Build a list of all possible guesses for the board.
A single guess is comprised of a Tile, and possible value that can be
assigned to it. Shuffle the list of guesses, and sort the list, with
the most "likely" guesses first. The likelihood of a guess is the
1.0 / number of possibilities for the guessed Tile.

2. Make a guess on the board by selecting one of the most likely guesses
from the list of guesses. Now reduce the board using our initial
deterministic approach. If we've solved the board after guessing, then
great! The program has accomplished it's task.

But otherwise, our board is still unsolved. At this point, a conflict on
the board may exist. That is, the board has been "reduced" to a state in
which a row, column, or box is invalid according to the rules of sudoku.
If this is the case, we revert the board back to its "determined" state
and repeat from Step 1.

In the final case, we've made a guess, and attempted to reduce the board,
but still our board is unsolved, yet a board conflict does not exist. So
we may simply guess again by starting at Step 1.