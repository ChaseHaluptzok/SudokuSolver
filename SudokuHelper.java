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
		int stepOut = 0;
		int c = 0;
		boolean update = true;
		printOut();
		propagate();
		printOutLogic();
		while(update && (stepOut < 729)){
			update = false;
			if(nakedSingle()){
				update = true;
			}if(!update && hiddenSingle()){		
				update = true;
			}if(!update && nakedDouble()){
				update = true;
			}if(update){
				printOutLogic();
			}
			stepOut++;
		}
	}
	public boolean nakedDouble(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if((board[i][j].cardinality() == 2) && (!board[i][j].get(9))){
					if(nakedDoubleHelper(i,j,board[i][j].nextSetBit(0),board[i][j].previousSetBit(8))){
						return true;
					}
				}
			}
		}return false;
	}
	public boolean nakedDoubleHelper(int i, int j, int val1, int val2){
		boolean ret = false;
		for(int u = 0; u < 9; u++){
			if((board[u][j].get(val1) && board[u][j].get(val2) && 
						(u != i)) && (board[u][j].cardinality() == 2)){
				if(nakedDoubleResolver(i,j,val1,val2,u,j,"col")){
					ret = true;
				}
			}
			if((board[i][u].get(val1) && board[i][u].get(val2) && 
						(u != j)) && (board[i][u].cardinality() == 2)){
				if(nakedDoubleResolver(i,j,val1,val2,i,u,"row")){
					ret = true;
				}
			}
			if(board[cbi(i,u)][cbj(j,u)].get(val1) && board[cbi(i,u)][cbj(j,u)].get(val2) && !(cbi(i,u) == i && cbj(j,u) == j) && (board[cbi(i,u)][cbj(j,u)].cardinality() == 2)){
				if(nakedDoubleResolver(i,j,val1,val2,cbi(i,u),cbj(j,u),"box")){
					ret = true;
				}
			} 
		}return ret;
	}
	public boolean nakedDoubleResolver(int i, int j, int val1, int val2, int u, int v, String unit){
		boolean ret = false;
		System.out.printf("naked double %d %d found in cells(r,c)/(r,c): (%d,%d)/(%d,%d); via "+unit+"\nclears:\n",val1+1,val2+1,i+1,j+1,u+1,v+1);
		for(int w = 0; w < 9; w++){
			if(unit.compareTo("col") == 0){
				if(board[w][j].get(val1) && !(w == i || w == u)){
					ret = true;
					board[w][j].clear(val1);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val1+1,w+1,j+1);
				}if(board[w][j].get(val2) && (w != i && w != u)){
					ret = true;
					board[w][j].clear(val2);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val2+1,w+1,j+1);
				}
			}else if(unit.compareTo("row") == 0){
				if(board[i][w].get(val1) && !(w == j || w == v)){
					ret = true;
					board[i][w].clear(val1);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val1+1,i+1,w+1);
				}if(board[i][w].get(val2) && (w != j && w != v)){
					ret = true;
					board[i][w].clear(val2);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val2+1,i+1,w+1);
				}
			}else{
				if(board[cbi(i,w)][cbj(j,w)].get(val1) && !(cbi(i,w) == i && cbj(j,w) == j) && !(cbi(i,w) == u && cbj(j,w) == v)){
					ret = true;
					board[cbi(i,w)][cbj(j,w)].clear(val1);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val1+1,cbi(i,w)+1,cbj(j,w)+1);
				}if(board[cbi(i,w)][cbj(j,w)].get(val2) && !(cbi(i,w) == i && cbj(j,w) == j) && !(cbi(i,w) == u && cbj(j,w) == v)){
					ret = true;
					board[cbi(i,w)][cbj(j,w)].clear(val2);
					System.out.printf("\t%d in cell(r,c): (%d,%d)\n",val2+1,cbi(i,w)+1,cbj(j,w)+1);
				}
			}
		}
		return ret;
	}
	public int cbi(int i, int u){
		return 3*(i/3)+u/3;
	}
	public int cbj(int j, int u){
		return 3*(j/3)+u%3;
	}
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
					System.out.printf("hidden single %d found in cell(r,c): (%d,%d); via column\n", curr+1, i+1, j+1);
					return hiddenSingleResolver(i,j,curr);
				}
			}for(int u = 0; u < 9; u++){
				if((board[i][u].get(curr)) && (u != j))
					break;
				if(u == 8){
					System.out.printf("hidden single %d found in cell(r,c): (%d,%d); via row\n", curr+1, i+1, j+1);
					return hiddenSingleResolver(i,j,curr);
				}
			}
			for(int u = 0; u < 9; u++){
				if((board[cbi(i,u)][cbj(j,u)].get(curr)) && !((cbi(i,u) == i) && (cbj(j,u)) == j))
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
		return true;
	}
	public boolean nakedSingle(){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(board[i][j].cardinality() == 1){
					board[i][j].set(9);
					System.out.printf("naked single %d found in cell(r,c): (%d,%d)\n", board[i][j].nextSetBit(0)+1, i+1, j+1);
					constrain(i,j,board[i][j].nextSetBit(0));
					return true;
				}
			}
		}
		return false;
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
			}System.out.println("|");
			for(int j=0;j<9;j++){
				if(j%3 == 0){System.out.print("| ");}
				for(int k=3;k<6;k++){
					printOutLogicHelper(i,j,k);
				}System.out.print(" ");
			}System.out.println("|");
			for(int j=0;j<9;j++){
				if(j%3 == 0){System.out.print("| ");}
				for(int k=6;k<9;k++){
					printOutLogicHelper(i,j,k);
				}System.out.print(" ");
			}System.out.println("|");			
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
