package simulator;

public class Reserv {
	boolean isBusy;
	boolean isRun;
	boolean canRB;
	int time;
	int type;
	int addr;
	
	int[] V = {-1, -1};
	int[] Q = {0, 0};
	
	int instrIndex;
	
	
	public static final int ADD5 = -1;
	public static final int ADD4 = -2;
	public static final int ADD3 = -3;
	public static final int ADD2 = -4;
	public static final int ADD1 = -5;
	public static final int ADD0 = -6;
	public static final int MUL2 = -7;
	public static final int MUL1 = -8;
	public static final int MUL0 = -9;
	public static final int LOAD2 = -10;
	public static final int LOAD1 = -11;
	public static final int LOAD0 = -12;
	
	public static final int LD = 0;
    public static final int JUMP = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    
    public static final int ADDI = 7;
    public static final int SUBI = 8;
    public static final int NOP = 9;
    
    Reserv(){
    	isBusy = false;
    	isRun = false;
    	canRB = false;
    	time = -1;
    	type = -1;  
    	addr = -1;
    	instrIndex = -1;
    }
    
    void clearReserv() {
    	isBusy = false;
    	isRun = false;
    	time = -1;
    	type = -1;  
    	addr = -1;
    	instrIndex = -1;
    	
    	for(int i = 0; i < 2; i++) {
    		V[i] = -1;
    		Q[i] = 0;
    	}
    }
    
}
