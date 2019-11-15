cd ..
javac *.java
move *.class InputOutputPuzzles/.
cd InputOutputPuzzles

java Sudoku nakedSingle.in > nakedSingle.out
java Sudoku hiddenSingle.in > hiddenSingle.out
java Sudoku nakedDouble.in > nakedDouble.out
java Sudoku easy.in > easy.out
java Sudoku tough.in > tough.out
java Sudoku diabolical.in > diabolical.out
java Sudoku extreme7.in > extreme7.out

del *.class
cd ..
cd Scripts