import java.util.Scanner;
import java.io.File;
public class Sudoku {
	public static void main(String[] args){
		final int iterations = 1000;
		final int currentInternationallyAcceptedNumberOfNanoSecondsInASecond = 1000000000;
		for(int a = 0; a < args.length; a++) {
			long sTime = System.nanoTime();
			for(int b = 0; b < iterations; b++){
				int[][] x = new int[9][9];
				try{
					Scanner sc = new Scanner(new File(args[a]));
					for(int i = 0; i < 9; i++){
						for(int j = 0; j < 9; j++) {
							x[i][j] = sc.nextInt();
						}
					}
					sc.close();
				}catch(Exception e){
					System.out.println("Problem with input file");
					e.printStackTrace();
				}
				GameBoard test = new GameBoard(x,0);
				//test.bruteForce(x);
				//System.out.println();
			}
			long eTime = System.nanoTime();
			System.out.printf("\n%d iterations of "+args[a]+" took: %f seconds\nAverage runtime: %f seconds",iterations,(double)(eTime-sTime)/currentInternationallyAcceptedNumberOfNanoSecondsInASecond,(double)((eTime-sTime)/iterations)/currentInternationallyAcceptedNumberOfNanoSecondsInASecond);
		}
	}
}
