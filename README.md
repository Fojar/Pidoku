# Pidoku

A solver for the special Pi day Sudoku puzzle.

This puzzle is different from standard Sudoku in that it's larger,
the blocks are not square, and some values can appear multiple times.

The program uses only deduction. It was sufficient to solve this puzzle
but might be insufficient for more difficult puzzles of this type. For
such cases more sophisticated deduction would need to be added, or else
brute-force search through permutations.

* To compile the project invoke `gradlew compileJava`.
* To run the project invoke `gradlew run`.
