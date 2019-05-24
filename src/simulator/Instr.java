package simulator;

public class Instr {
	public static final int LD = 0;
    public static final int JUMP = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    
    int instrType;
    int operand1;
    int operand2;
    int operand3;
    
    int issueClock;
    int execCompClock;
    int writeResultClock;
    
    int useReserv;
    
    Instr(){
    	instrType = -1;
        operand1 = -1;
        operand2 = -1;
        operand3 = -1;
        issueClock = -1;
        execCompClock = -1;
        writeResultClock = -1;
        useReserv = 0;
    }
    
    Instr(int type, int op1, int op2){
    	instrType = type;
        operand1 = op1;
        operand2 = op2;
        operand3 = -1;
        issueClock = -1;
        execCompClock = -1;
        writeResultClock = -1;
        useReserv = 0;
    }
    
    Instr(int type, int op1, int op2, int op3){
    	instrType = type;
        operand1 = op1;
        operand2 = op2;
        operand3 = op3;
        issueClock = -1;
        execCompClock = -1;
        writeResultClock = -1;
        useReserv = 0;
    }
}
