package simulator;

public class Instr {
	public static final int LD = 0;
    public static final int JUMP = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    
    public static final int ADDI = 7;
    public static final int SUBI = 8;
    public static final int NOP = 9;
    public static final int SHL = 10; // ¬ﬂº≠◊Û“∆
    public static final int SAL = 11; // À„ ı◊Û“∆(=SHL)
    public static final int SHR = 12; // ¬ﬂº≠”““∆.( √øŒª”““∆, µÕŒªΩ¯ CF, ∏ﬂŒª≤π 0)
    public static final int SAR = 13; // À„ ˝”““∆
    
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
    
    Instr(int type){
    	instrType = type;
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
