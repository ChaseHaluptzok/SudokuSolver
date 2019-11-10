
import java.util.Scanner;
import java.util.BitSet;
import java.io.File;
public class Sudoku{
	public static void main(String[] args){
		int cell;
		for(int a = 0; a < args.length; a++){
			BitSet[][] board = new BitSet[9][9];
			try{
				Scanner sc = new Scanner(new File(args[a]));
				for(int i = 0; i < 9; i++){
					for(int j = 0; j < 9; j++){
						cell = sc.nextInt();
						board[i][j] = new BitSet(10);
						if(cell > 0){
							board[i][j].set(cell-1);
							board[i][j].set(9);
						}else{
							board[i][j].set(0,9,true);
						}
					}
				}
				sc.close();
			}catch(Exception e){
				System.out.println("Problem with input file");
				e.printStackTrace();
			}
			SudokuHelper test = new SudokuHelper(board);
			//if input wants debug
			//	test.solve(debug True)
			//else
			// 	test.solve(debug False)
		}
	}
}
