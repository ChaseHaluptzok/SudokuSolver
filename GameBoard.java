public class GameBoard {
	public int[][]  board;// = new int[9][9];
	public int guesses;
	public GameBoard(int[][] x, int y){
		this.board = new int[9][9];
		this.guesses = y;
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				this.board[i][j] = x[i][j];
			}
		}
		bruteForce(board);
		//printOut();
	}
	public void bruteForce(int[][] board) {
		if(guess(board)){
			//System.out.printf("Guess: %d", guesses);
		}else{
			System.out.println("false");
		}
	}
	public boolean guess(int[][] board){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j] != 0) {continue;}
				for(int k = 1; k <= 9; k++){
					if(vaild(board, i, j, k)){
						board[i][j] = k;
						guesses++;
						if(guess(board)){
							return true;
						}
						board[i][j] = 0;
					}
				}
				return false;
			}
		}
		return true;
	}
	public boolean vaild(int[][] board, int r, int c, int k){
		for (int i = 0; i < 9; i++){
			if (board[i][c] == k){
				return false;
			}if (board[r][i] == k){
				return false;
			}if (board[3*(r/3)+i/3][3*(c/3)+i%3] == k){
				return false;
			}
		}return true;
	}
	public void printOut() {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				System.out.printf("%d ", board[i][j]);
			}System.out.println();
		}System.out.printf("Guesses: %d\n", guesses);
	}
}
