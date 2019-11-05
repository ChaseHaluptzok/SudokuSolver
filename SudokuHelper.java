import java.util.BitSet;
public class SudokuHelper {
	public final String HORIZONTALSEP = "+-------+-------+-------+";
	public final String HORIZONTALSEPLOG = "+-------------+-------------+-------------+";
	public BitSet[][] board;
	public int guesses, steps;
	public SudokuHelper(BitSet[][] x){
		this.board = new BitSet[9][9];
		this.steps = 0;
		this.guesses = 0;
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				this.board[i][j] = new BitSet(10);
				this.board[i][j].or(x[i][j]);
			}
		}
		solve();
		printOut();
	}
	public void solve(){
		int c = 0;
		boolean update = true;
		printOut();
		propagate();
		printOutLogic();
		while(update){
			update = false;
			if(nakedSingle()){
				update = true;
				printOutLogic();
			}
			if(hiddenSingle()){		
				update = true;
				printOutLogic();
			}
		}
	}
	/*public boolean nakedDouble(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if((board[i][j].cardinality() == 2) && (!board[i][j].get(9))){
					for(int u = 0; u < 9; u++){
						if((u != i) && (board[u][j].equals(board[i][j]))){
							nakedDoubleFoundHelper(i, j, u)
						}
					}
				}
			}
		}
	}*/
	public boolean hiddenSingle(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if((!board[i][j].get(9))){
					if(hiddenSingleHelper(i,j)){
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean hiddenSingleHelper(int i, int j){
		int curr = board[i][j].nextSetBit(0);
		while((curr < 9) && (curr > -1)){
			for(int u = 0; u < 9; u++){
				if((board[u][j].get(curr)) && (u != i))
					break;
				if(u == 8){
					System.out.printf("hidden single %d found in cell(r,c): (%d,%d); via row\n", curr+1, i+1, j+1);
					return hiddenSingleResolver(i,j,curr);
				}
			}for(int u = 0; u < 9; u++){
				if((board[i][u].get(curr)) && (u != j))
					break;
				if(u == 8){
					System.out.printf("hidden single %d found in cell(r,c): (%d,%d); via column\n", curr+1, i+1, j+1);
					return hiddenSingleResolver(i,j,curr);
				}
			}for(int u = 0; u < 9; u++){
				if((board[3*(i/3)+u/3][3*(j/3)+u%3].get(curr)) && (u != i) && (u != j))
					break;
				if(u == 8){			
					System.out.printf("hidden single %d found in cell(r,c): (%d,%d); via box \n", curr+1, i+1, j+1);
					return hiddenSingleResolver(i,j,curr);
				}
			}
			curr = board[i][j].nextSetBit(curr+1);
		}
		return false;
	}
	public boolean hiddenSingleResolver(int i, int j, int s){
		board[i][j].clear();
		board[i][j].set(9);
		board[i][j].set(s);
		constrain(i,j,s);
		//printOutLogic();
		return true;
	}
	public boolean nakedSingle(){
		boolean ret = false; 
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j].cardinality() == 1){
					ret = true;
					board[i][j].set(9);
					System.out.printf("naked single %d found in cell(r,c): (%d,%d)\n", board[i][j].nextSetBit(0)+1, i+1, j+1);
					constrain(i,j,board[i][j].nextSetBit(0));
					//printOutLogic();
				}
			}
		}
		return ret;
	}
	public void propagate(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j].get(9)){
					constrain(i,j,board[i][j].nextSetBit(0));
				}
			}
		}
	}
	public void constrain(int i, int j, int k){
		for(int u = 0; u < 9; u++){
			board[u][j].clear(k);
			board[i][u].clear(k);
			board[3*(i/3)+u/3][3*(j/3)+u%3].clear(k);
		}board[i][j].set(k);
		//printOutLogic();
	}
	public void printOut(){
		for(int i=0;i<9;i++) {
			if(i%3 == 0){
				System.out.print(HORIZONTALSEP);
				System.out.println();
			}
			for(int j=0;j<9;j++) {
				if(j%3 == 0){
					System.out.print("| ");
				}
				if(board[i][j].get(9)){
					System.out.printf("%d ", board[i][j].nextSetBit(0)+1);
				}else{
					System.out.print("  ");
				}
			}
			System.out.print("|");
			System.out.println();
		}
		System.out.println(HORIZONTALSEP);
		//System.out.printf("Guesses: %d\n", guesses);
	}
	public void printOutLogic(){
		System.out.printf("STEP: %d\n", steps++);
		int x;
		for(int i=0;i<9;i++) {
			if(i%3 == 0){
				System.out.print(HORIZONTALSEPLOG);
				System.out.println();
			}
			for(int j=0;j<9;j++){
				if(j%3 == 0){System.out.print("| ");}
				for(int k=0;k<3;k++){
					printOutLogicHelper(i,j,k);
				}System.out.print(" ");
			}System.out.println();
			for(int j=0;j<9;j++){
				if(j%3 == 0){System.out.print("| ");}
				for(int k=3;k<6;k++){
					printOutLogicHelper(i,j,k);
				}System.out.print(" ");
			}System.out.println();
			for(int j=0;j<9;j++){
				if(j%3 == 0){System.out.print("| ");}
				for(int k=6;k<9;k++){
					printOutLogicHelper(i,j,k);
				}System.out.print(" ");
			}System.out.println();			
		}
		System.out.print(HORIZONTALSEPLOG);
		System.out.println("\n****************************************************************************\n");
	}
	public void printOutLogicHelper(int i, int j, int k){
		if(board[i][j].get(9)){
			if(k == 4){
				System.out.printf("%d",board[i][j].nextSetBit(0)+1);
			}else{
				System.out.print("*");
			}
		}else{
			if(board[i][j].get(k)){
				System.out.printf("%d",k+1);
			}else{
				System.out.print(" ");
			}
		}
	}
}
