package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class Simulator {
	Instruction instructions;
	String fileName;
	
	int currentInstr = 0;
	int clock = 1;
	
	FunctionalUnit addFunc;
	FunctionalUnit mulFunc;
	FunctionalUnit loadFunc;
	
	ReservationStation addReserv;
	ReservationStation mulReserv;
	ReservationStation loadReserv;
	
	RegisterResult reg;
	
	boolean waitJump;
//	boolean isJump;
//	int jumpTime;
//	int jumpIndex;
//	int jumpV;
//	int jumpQ;
	
	public static final int LD = 0;
    public static final int JUMP = 1;
    public static final int ADD = 2;
    public static final int SUB = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    
    // ADDI/SUBI Fdst Fsrc 0x?
    public static final int ADDI = 7;
    public static final int SUBI = 8;
    public static final int NOP = 9;
    
    // SXX Fdst Fsrc 0x? 将Fx中的数据进行？移位
    public static final int SHL = 10; // 逻辑左移
    public static final int SAL = 11; // 算术左移(=SHL)
    public static final int SHR = 12; // 逻辑右移.( 每位右移, 低位进 CF, 高位补 0)
    public static final int SAR = 13; // 算数右移
    
    public static final int ADDTIME = 3;
    public static final int LDTIME = 3;
    public static final int JUMPTIME = 1;
//    public static final int DIVTIME = 40;
//    public static final int MULTIME = 12;
    
    public static final int DIVTIME = 4;
    public static final int MULTIME = 4;
    
    public static final int STIME = 3;
    
    
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
	
	
    
	Simulator(){
		fileName = "test1.nel";
		instructions = new Instruction();
		addFunc = new FunctionalUnit(3);
		mulFunc = new FunctionalUnit(2);
		loadFunc = new FunctionalUnit(2);
		addReserv = new ReservationStation(6);
		mulReserv = new ReservationStation(3);
		loadReserv = new ReservationStation(3);
		reg = new RegisterResult();
		
		waitJump = false;
//		isJump = false;
//		jumpTime = -1;
//		jumpIndex = -1;
//		jumpV = -1;
//		jumpQ = 0;
	}
	
	Simulator(String fname){
		fileName = fname;
		instructions = new Instruction();
		addFunc = new FunctionalUnit(3);
		mulFunc = new FunctionalUnit(2);
		loadFunc = new FunctionalUnit(2);
		addReserv = new ReservationStation(6);
		mulReserv = new ReservationStation(3);
		loadReserv = new ReservationStation(3);
		reg = new RegisterResult();
		
		waitJump = false;
//		isJump = false;
//		jumpTime = -1;
//		jumpIndex = -1;
//		jumpV = -1;
//		jumpQ = 0;
	}
	
	
	// 指令读入
	public void readFileByLines(String fileName) {  
        File file = new File(fileName);  
        BufferedReader reader = null;  
        try {  
//            System.out.println("以行为单位读取文件内容，一次读一整行：");  
            reader = new BufferedReader(new FileReader(file));  
            String tempString = null;  
            int line = 0;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
//                System.out.println("line " + line + ": " + tempString);  
            	if(tempString.length() == 0) {
            		break;
            	}
                
            	instructions.instructionsString[line] = new String(tempString);
            	
                String tempType = "";
                int count = 0;
                System.out.println(tempString);
                if(tempString.equals("NOP")) {
                	tempType = "NOP";
                }
                else {
                	while(tempString.charAt(count) != ',') {
                    	tempType += tempString.charAt(count);
                    	count++;
                    }
                }
//                System.out.println("tempType " + tempType ); 
                count++;
                if(tempType.equals("LD")) {
//                	System.out.println("LD"); 
                	int op1, op2;
                	String tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op1 = dealRegister(tempOp);
                	tempOp = "";
                	while(count < tempString.length()) {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	op2 = dealComplement(tempOp);              	
                	instructions.instruction[line] = new Instr(LD, op1, op2);
                }
                else if(tempType.equals("JUMP")) {
//                	System.out.println("JUMP"); 
                	int op1, op2, op3;
                	String tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op1 = dealComplement(tempOp);
                	tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op2 = dealRegister(tempOp);
                	tempOp = "";
                	while(count < tempString.length()) {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	op3 = dealComplement(tempOp);
                	instructions.instruction[line] = new Instr(JUMP, op1, op2, op3);
                }
                else if(tempType.equals("NOP")) {
                	instructions.instruction[line] = new Instr(NOP);
                }
                else if(tempType.equals("ADDI") || tempType.equals("SUBI")|| tempType.equals("SHL")  || tempType.equals("SAL")  || tempType.equals("SHR")  || tempType.equals("SAR")) {
                	int op1, op2, op3;
                	String tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op1 = dealRegister(tempOp);
                	tempOp = "";
                	System.out.println(tempString);
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op2 = dealRegister(tempOp);
                	tempOp = "";
                	while(count < tempString.length()) {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	op3 = dealComplement(tempOp);
                	
                	switch (tempType) {
                	case "ADDI":
                		instructions.instruction[line] = new Instr(ADDI, op1, op2, op3);
                		break;
                	case "SUBI":
                		instructions.instruction[line] = new Instr(SUBI, op1, op2, op3);
                		break;
                	case "SHL":
                		instructions.instruction[line] = new Instr(SHL, op1, op2, op3);
                		break;
                	case "SAL":
                		instructions.instruction[line] = new Instr(SAL, op1, op2, op3);
                		break;
                	case "SHR":
                		instructions.instruction[line] = new Instr(SHR, op1, op2, op3);
                		break;
                	case "SAR":
                		instructions.instruction[line] = new Instr(SAR, op1, op2, op3);
                		break;
                	}
                }
                else{
//                	System.out.println("other"); 
                	int op1, op2, op3;
                	String tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op1 = dealRegister(tempOp);
                	tempOp = "";
                	while(tempString.charAt(count) != ',') {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	count++;
                	op2 = dealRegister(tempOp);
                	tempOp = "";
                	while(count < tempString.length()) {
                    	tempOp += tempString.charAt(count);
                    	count++;
                    }
                	op3 = dealRegister(tempOp);
                	switch (tempType) {
                	case "ADD":
                		instructions.instruction[line] = new Instr(ADD, op1, op2, op3);
                		break;
                	case "SUB":
                		instructions.instruction[line] = new Instr(SUB, op1, op2, op3);
                		break;
                	case "MUL":
                		instructions.instruction[line] = new Instr(MUL, op1, op2, op3);
                		break;
                	case "DIV":
                		instructions.instruction[line] = new Instr(DIV, op1, op2, op3);
                		break;
                	}
                }
                line++;
            }  
            instructions.instrSize = line;
            reader.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    }  
	
	
	public static int dealComplement(String op) {
		int comp = 0;
		op = op.substring(2, op.length());
		int digit = 0;
		int[] bi = new int[4];
		for(int i = op.length()-1; i > 0; i--) {
			bi = hexTobi(op.charAt(i));
			for(int j = 3; j >= 0; j--) {
				comp += bi[j]*(Math.pow(2,digit));
				digit++;
			}
		}
		if(op.length() == 8) {
			bi = hexTobi(op.charAt(0));
			for(int j = 3; j > 0; j--) {
				comp += bi[j]*(Math.pow(2,digit));
				digit++;
			}
			comp -= bi[0]*(Math.pow(2,digit));
		}
		else {
			bi = hexTobi(op.charAt(0));
			for(int j = 3; j >= 0; j--) {
				comp += bi[j]*(Math.pow(2,digit));
				digit++;
			}
		}
//		System.out.println("comp:" + comp);
		return comp;
	}
	public static int dealRegister(String op) {
		int reg = 0;
		op = op.substring(1, op.length());
		reg = Integer.parseInt(op);
//		System.out.println("reg:" + reg);
		return reg;
	}
	public static int[] hexTobi(char a) {
		int[] bi = new int[4];
		switch (a) {
		case '0':
			bi[0] = 0;
			bi[1] = 0;
			bi[2] = 0;
			bi[3] = 0;
			break;
		case '1':
			bi[0] = 0;
			bi[1] = 0;
			bi[2] = 0;
			bi[3] = 1;
			break;
		case '2':
			bi[0] = 0;
			bi[1] = 0;
			bi[2] = 1;
			bi[3] = 0;
			break;
		case '3':
			bi[0] = 0;
			bi[1] = 0;
			bi[2] = 1;
			bi[3] = 1;
			break;
		case '4':
			bi[0] = 0;
			bi[1] = 1;
			bi[2] = 0;
			bi[3] = 0;
			break;
		case '5':
			bi[0] = 0;
			bi[1] = 1;
			bi[2] = 0;
			bi[3] = 1;
			break;
		case '6':
			bi[0] = 0;
			bi[1] = 1;
			bi[2] = 1;
			bi[3] = 0;
			break;
		case '7':
			bi[0] = 0;
			bi[1] = 1;
			bi[2] = 1;
			bi[3] = 1;
			break;
		case '8':
			bi[0] = 1;
			bi[1] = 0;
			bi[2] = 0;
			bi[3] = 0;
			break;
		case '9':
			bi[0] = 1;
			bi[1] = 0;
			bi[2] = 0;
			bi[3] = 1;
			break;
		case 'A':
			bi[0] = 1;
			bi[1] = 0;
			bi[2] = 1;
			bi[3] = 0;
			break;
		case 'B':
			bi[0] = 1;
			bi[1] = 0;
			bi[2] = 1;
			bi[3] = 1;
			break;
		case 'C':
			bi[0] = 1;
			bi[1] = 1;
			bi[2] = 0;
			bi[3] = 0;
			break;
		case 'D':
			bi[0] = 1;
			bi[1] = 1;
			bi[2] = 0;
			bi[3] = 1;
			break;
		case 'E':
			bi[0] = 1;
			bi[1] = 1;
			bi[2] = 1;
			bi[3] = 0;
			break;
		case 'F':
			bi[0] = 1;
			bi[1] = 1;
			bi[2] = 1;
			bi[3] = 1;
			break;
		}
		return bi;
	}
	
	// 注意除法除0
	public void runSimulator() {
		clearAll();
		while((currentInstr < instructions.instrSize || emptyReservationSta() == false)) {
			System.out.println("runSimulator now is " + this.clock);
			System.out.println("currentInstr now is " + this.currentInstr);
			System.out.println("emptyReservationSta() now is" + emptyReservationSta());
			System.out.println(addReserv.busySize + " "+ mulReserv.busySize + " "+loadReserv.busySize);
			
			WriteBack();
			if(currentInstr < instructions.instrSize) {
				int currentInstrType = instructions.instruction[currentInstr].instrType;
				boolean canIssue = tryIssue(currentInstrType);
				
				// deal with the new instr
				if(canIssue) {
					System.out.println("issue " + currentInstr);
					Issue(currentInstrType);
					currentInstr++;
				}
				else {
					System.out.println("can not issue " + currentInstr);
				}
			}
		
			// deal with the old instr  DO NOT FORGET JUMP
			
			// first check write back
//			WriteBack();
			
			// then update the info
			// check RS to run
			ExecComp();
			updateReSize();
//			WriteBack();
		
			this.clock++;
		}
//		printAll();
	}
	
	public int runPartSimulator(int clk) {
		clearAll();
		while((currentInstr < instructions.instrSize || emptyReservationSta() == false) && clock <= clk) {
			System.out.println("runSimulator now is " + this.clock);
			
			WriteBack();
			
			
			if(currentInstr < instructions.instrSize) {
				int currentInstrType = instructions.instruction[currentInstr].instrType;
				boolean canIssue = tryIssue(currentInstrType);
				
				// deal with the new instr
				if(canIssue) {
					System.out.println("issue " + currentInstr);
					Issue(currentInstrType);
					currentInstr++;
				}
				else {
					System.out.println("can not issue " + currentInstr);
				}
			}
		
			// deal with the old instr  DO NOT FORGET JUMP
			
			// first check write back
//			WriteBack();
			
			// then update the info
			// check RS to run
			ExecComp();
			updateReSize();
//			WriteBack();
		
			this.clock++;
		}
//		printAll();
		
		addReserv.checkReserv();
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(addReserv.reservationStation[0].waitReserv.size());
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[0]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[1]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[2]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[3]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[4]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(addReserv.reservationStation[5]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(mulReserv.reservationStation[0]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(mulReserv.reservationStation[1]));
		System.out.println(addReserv.reservationStation[0].waitReserv.contains(mulReserv.reservationStation[2]));
		
//		reg.checkFu();
//		mulReserv.checkReserv();
//		loadReserv.checkReserv();
		return clock-1;
	}
	
	boolean emptyReservationSta() {
		// last instr is jump!!!
		if(addReserv.busySize > 0) {
			return false;
		}
		if(mulReserv.busySize > 0) {
			return false;
		}
		if(loadReserv.busySize > 0) {
			return false;
		}
		if(waitJump) {
			return false;
		}
		return true;
	}
	
	public boolean tryIssue(int type) {
		if(waitJump == true) {
			return false;
		}
		else {
			boolean canIssue = false;
			switch (type) {
			case ADD:
			case ADDI:
			case SUB:
			case SUBI:
			case JUMP:
				if(this.addReserv.busySize < this.addReserv.totalSize) {
					canIssue = true;
				}
				break;
			case MUL:
			case DIV:
			case SHL:
			case SAL:
			case SHR:
			case SAR:
				if(this.mulReserv.busySize < this.mulReserv.totalSize) {
					canIssue = true;
				}
				break;
			case LD:
				if(this.loadReserv.busySize < this.loadReserv.totalSize) {
					canIssue = true;
				}
				break;
			case NOP:
				canIssue = true;
				break;
			}
			return canIssue;
		}
	}

	
	/*
	 * 选择空闲RS
	 * 检查操作数并根据相应内容填表
	 * sub F1 F1 F2  F1=F1-F2
	 * 注意除0错
	 */
	public void Issue(int type) {
		int selectedRS = 0;
		switch (type) {
		case ADD:
		case ADDI:
		case SUB:
		case SUBI:
			while(this.addReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.addReserv.reservationStation[selectedRS].isBusy = true;
			this.addReserv.busySize++;
			this.addReserv.reservationStation[selectedRS].time = ADDTIME;
			this.addReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			switch(type) {
			case ADD:
				this.addReserv.reservationStation[selectedRS].type = ADD;
				break;
			case SUB:
				this.addReserv.reservationStation[selectedRS].type = SUB;
				break;
			case ADDI:
				this.addReserv.reservationStation[selectedRS].type = ADDI;
				break;
			case SUBI:
				this.addReserv.reservationStation[selectedRS].type = SUBI;
				break;
			}
			
			switch(type) {
			case ADD:
			case SUB:
				if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
					this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
					addHashSet(0, selectedRS, this.addReserv.reservationStation[selectedRS].Q[0]);
				}
				else {
					this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
				}
				if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
					this.addReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
					addHashSet(0, selectedRS, this.addReserv.reservationStation[selectedRS].Q[1]);
				}
				else {
					this.addReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
				}
				break;
			case ADDI:
			case SUBI:
				if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
					this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
					addHashSet(0, selectedRS, this.addReserv.reservationStation[selectedRS].Q[0]);
				}
				else {
					this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
				}
				this.addReserv.reservationStation[selectedRS].V[1] = this.instructions.instruction[this.currentInstr].operand3;
				break;
			}
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 6;
			break;
		case MUL:
		case DIV:
			while(this.mulReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.mulReserv.reservationStation[selectedRS].isBusy = true;
			this.mulReserv.busySize++;
			
			switch(type) {
			case MUL:
				this.mulReserv.reservationStation[selectedRS].type = MUL;
				this.mulReserv.reservationStation[selectedRS].time = MULTIME;
				break;
			case DIV:
				this.mulReserv.reservationStation[selectedRS].type = DIV;
				this.mulReserv.reservationStation[selectedRS].time = DIVTIME;
				break;
			}
			
			this.mulReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
				addHashSet(1, selectedRS, this.mulReserv.reservationStation[selectedRS].Q[0]);
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
				addHashSet(1, selectedRS, this.mulReserv.reservationStation[selectedRS].Q[1]);
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
			}
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 9;	
			break;
		case SHL:
		case SAL:
		case SHR:
		case SAR:
			while(this.mulReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.mulReserv.reservationStation[selectedRS].isBusy = true;
			this.mulReserv.busySize++;
			this.mulReserv.reservationStation[selectedRS].time = STIME;
			switch(type) {
			case SHL:
				this.mulReserv.reservationStation[selectedRS].type = SHL;
				break;
			case SAL:
				this.mulReserv.reservationStation[selectedRS].type = SAL;
				break;
			case SHR:
				this.mulReserv.reservationStation[selectedRS].type = SHR;
				break;
			case SAR:
				this.mulReserv.reservationStation[selectedRS].type = SAR;
				break;
			}
			
			this.mulReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
				addHashSet(1, selectedRS, this.mulReserv.reservationStation[selectedRS].Q[0]);
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			this.mulReserv.reservationStation[selectedRS].V[1] = this.instructions.instruction[this.currentInstr].operand3;
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 9;	
			break;
		case LD:
			while(this.loadReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			System.out.println("LD selectedRS:" + selectedRS);
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			
			// deal with RS
			// type is useless
			// only busy address time run is useful
			this.loadReserv.reservationStation[selectedRS].isBusy = true;
			this.loadReserv.reservationStation[selectedRS].addr = this.instructions.instruction[this.currentInstr].operand2;
			this.loadReserv.busySize++;
			this.loadReserv.reservationStation[selectedRS].time = LDTIME;
			this.loadReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 12;
			break;
		case JUMP:
			// V[0]-F[i]  V[1]-current
			waitJump = true;
			while(this.addReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			System.out.println("ADD selectedRS:" + selectedRS);
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			
			// deal with RS
			// busy time run is useful   type V Q
			this.addReserv.reservationStation[selectedRS].isBusy = true;
			this.addReserv.reservationStation[selectedRS].isRun = false;
			this.addReserv.busySize++;
			this.addReserv.reservationStation[selectedRS].type = JUMP;
			this.addReserv.reservationStation[selectedRS].time = JUMPTIME;
			this.addReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
				addHashSet(0, selectedRS, this.addReserv.reservationStation[selectedRS].Q[0]);
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeere" +selectedRS + " "+ currentInstr);
			this.addReserv.reservationStation[selectedRS].V[1] = currentInstr;
			
			break;
		case NOP:
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
				this.instructions.instruction[this.currentInstr].execCompClock = this.clock;
				this.instructions.instruction[this.currentInstr].writeResultClock = this.clock;
			}
			break;
		}
	}

	public void WriteBack() {
		System.out.println("WriteBack()");
		// add
		for(int i = 0; i < 6; i++) {
			if(addReserv.reservationStation[i].isRun == true && addReserv.reservationStation[i].time == 0) {
				
				if(addFunc.busySize == addFunc.totalSize) {
					int readyRun = -1;
					int readyRunIndex = 10086;
					for(int j = 0; j < 6; j++) {
						if(readyToRun(addReserv.reservationStation[j])) {
							if(addReserv.reservationStation[j].instrIndex < readyRunIndex) {
								readyRun = j;
								readyRunIndex = addReserv.reservationStation[j].instrIndex;
							}
						}
					}
					
					// find one
					if(readyRun != -1) {
						addReserv.reservationStation[readyRun].isRun = true;
						addFunc.busySize++;
					}
				}
				
				// need to write back
				
				// inst
				if(instructions.instruction[addReserv.reservationStation[i].instrIndex].writeResultClock == -1) {
					instructions.instruction[addReserv.reservationStation[i].instrIndex].writeResultClock = clock;
				}
				
				// update temp
				switch (addReserv.reservationStation[i].type) {
				case ADD:
					reg.tempReg[reg.tempCount] = reg.tempReg[addReserv.reservationStation[i].V[0]] + reg.tempReg[addReserv.reservationStation[i].V[1]];
					reg.tempCount++;
					break;
				case ADDI:
					reg.tempReg[reg.tempCount] = reg.tempReg[addReserv.reservationStation[i].V[0]] + addReserv.reservationStation[i].V[1];
					reg.tempCount++;
					break;
				case SUB:
					reg.tempReg[reg.tempCount] = reg.tempReg[addReserv.reservationStation[i].V[0]] - reg.tempReg[addReserv.reservationStation[i].V[1]];
					reg.tempCount++;
					break;
				case SUBI:
					reg.tempReg[reg.tempCount] = reg.tempReg[addReserv.reservationStation[i].V[0]] - addReserv.reservationStation[i].V[1];
					reg.tempCount++;
					break;
				}
				// update FU
//				reg.Fu[instructions.instruction[addReserv.reservationStation[i].instrIndex].operand1] = reg.tempCount - 1;
				
				int ind = addReserv.reservationStation[i].instrIndex;
				
				updateCertainRS(addReserv.reservationStation[i], ind, i-6, reg.tempCount - 1);
				
				// addReserv clear
				addReserv.reservationStation[i].clearReserv();
//				addReserv.busySize--;
				addFunc.busySize--;
				
				// update RS
//				updateReservationSta(ind, i - 6, reg.tempCount - 1);
				
				

			}

		}
		
		// mul
		for(int i = 0; i < 3; i++) {
			if(mulReserv.reservationStation[i].isRun == true && mulReserv.reservationStation[i].time == 0) {
				
				if(mulFunc.busySize == mulFunc.totalSize) {
					int readyRun = -1;
					int readyRunIndex = 10086;
					for(int j = 0; j < 3; j++) {
						if(readyToRun(mulReserv.reservationStation[j])) {
							if(mulReserv.reservationStation[j].instrIndex < readyRunIndex) {
								readyRun = j;
								readyRunIndex = mulReserv.reservationStation[j].instrIndex;
							}
						}
					}
					
					// find one
					if(readyRun != -1) {
						mulReserv.reservationStation[readyRun].isRun = true;
						mulFunc.busySize++;
					}
				}
				
				
				
				// need to write back

				// inst
				if(instructions.instruction[mulReserv.reservationStation[i].instrIndex].writeResultClock == -1) {
					instructions.instruction[mulReserv.reservationStation[i].instrIndex].writeResultClock = clock;
				}
				
				// update temp
				switch (mulReserv.reservationStation[i].type) {
				case MUL:
					reg.tempReg[reg.tempCount] = reg.tempReg[mulReserv.reservationStation[i].V[0]] * reg.tempReg[mulReserv.reservationStation[i].V[1]];
					reg.tempCount++;
					break;
				case DIV:
					reg.tempReg[reg.tempCount] = reg.tempReg[mulReserv.reservationStation[i].V[0]] / reg.tempReg[mulReserv.reservationStation[i].V[1]];
					reg.tempCount++;
					break;
				case SHL:
				case SAL:
					reg.tempReg[reg.tempCount] = reg.tempReg[mulReserv.reservationStation[i].V[0]] << mulReserv.reservationStation[i].V[1];
					reg.tempCount++;
					break;
				case SHR:
					reg.tempReg[reg.tempCount] = reg.tempReg[mulReserv.reservationStation[i].V[0]] >>> mulReserv.reservationStation[i].V[1];
					reg.tempCount++;
					break;
				case SAR:
					reg.tempReg[reg.tempCount] = reg.tempReg[mulReserv.reservationStation[i].V[0]] >> mulReserv.reservationStation[i].V[1];
					reg.tempCount++;
					break;
				}
				// update FU
//				reg.Fu[instructions.instruction[mulReserv.reservationStation[i].instrIndex].operand1] = reg.tempCount - 1;
				
				int ind = mulReserv.reservationStation[i].instrIndex;
				
				updateCertainRS(mulReserv.reservationStation[i], ind, i-9, reg.tempCount - 1);
				
				
				// addReserv clear
				mulReserv.reservationStation[i].clearReserv();
//				mulReserv.busySize--;
				mulFunc.busySize--;
				
				// update RS
//				updateReservationSta(ind, i - 9, reg.tempCount - 1);
				
			}
		}
		
		// load
		loadReserv.checkReserv();
		
		for(int i = 0; i < 3; i++) {
			if(loadReserv.reservationStation[i].isRun == true && loadReserv.reservationStation[i].time == 0) {
				if(loadFunc.busySize == loadFunc.totalSize) {
					int readyRun = -1;
					int readyRunIndex = 10086;
					for(int j = 0; j < 3; j++) {
						if(loadReserv.reservationStation[j].isBusy == true && loadReserv.reservationStation[j].isRun == false) {
							if(loadReserv.reservationStation[j].instrIndex < readyRunIndex) {
								System.out.println("find ready Run:" + j);
								readyRun = j;
								readyRunIndex = loadReserv.reservationStation[j].instrIndex;
							}
						}
					}
					
					// find one
					if(readyRun != -1) {
						loadReserv.reservationStation[readyRun].isRun = true;
						loadFunc.busySize++;
					}
				}
				
				// need to write back

				// inst
				if(instructions.instruction[loadReserv.reservationStation[i].instrIndex].writeResultClock == -1) {
					instructions.instruction[loadReserv.reservationStation[i].instrIndex].writeResultClock = clock;
				}
				
				// update temp
				
				reg.tempReg[reg.tempCount] = loadReserv.reservationStation[i].addr;
				reg.tempCount++;
				
				// update FU  check before cover
				//reg.Fu[instructions.instruction[loadReserv.reservationStation[i].instrIndex].operand1] = reg.tempCount - 1;
				
				int ind = loadReserv.reservationStation[i].instrIndex;
				
				updateCertainRS(loadReserv.reservationStation[i], ind, i-12, reg.tempCount - 1);
				
				// addReserv clear
				loadReserv.reservationStation[i].clearReserv();
//				loadReserv.busySize--;
				loadFunc.busySize--;
				
				// update RS and FU
//				updateReservationSta(ind, i - 12, reg.tempCount - 1);
			}
		}
		
	}
	
	void updateReSize() {
		int co = 0;
		
		// add
		for(int i = 0; i < 6; i++) {
			if(addReserv.reservationStation[i].isBusy) {
				co++;
			}
		}
		addReserv.busySize = co;
		
		// mul
		co = 0;
		for(int i = 0; i < 3; i++) {
			if(mulReserv.reservationStation[i].isBusy) {
				co++;
			}
		}
		mulReserv.busySize = co;
		
		// load
		co = 0;
		for(int i = 0; i < 3; i++) {
			if(loadReserv.reservationStation[i].isBusy) {
				co++;
			}
		}
		loadReserv.busySize = co;
	}
	
	void updateCertainRS(Reserv r, int instrIndex, int writeBackId, int tempIndex) {
		Iterator<Reserv> setIterator = r.waitReserv.iterator();
		while (setIterator.hasNext()) {
			Reserv re = setIterator.next();
			if(re.Q[0] == writeBackId) {
				// update data
				re.V[0] = tempIndex;
				re.Q[0] = 0;
			}
			if(re.Q[1] == writeBackId) {
				
				if(reg.tempReg[tempIndex] == 0 && re.type == DIV) {
					reg.tempReg[reg.tempCount] = 1;
					re.V[1] = reg.tempCount;
					re.Q[1] = 0;
					re.time = 1;
					reg.tempCount++;
				}
				else {
					// update data
					re.V[1] = tempIndex;
					re.Q[1] = 0;
				}
			}
		}
		// Fu
		for(int i = 0; i < 32; i++) {
			if(reg.fuState[i] == writeBackId) {
				reg.fuState[i] = -10086;
			}
		}
		System.out.println(instrIndex);
		switch(instructions.instruction[instrIndex].instrType) {
		case ADD:
		case ADDI:
		case SUB:
		case SUBI:
		case MUL:
		case DIV:
		case LD:
		case SHL:
		case SAL:
		case SHR:
		case SAR:
			if(reg.fuState[instructions.instruction[instrIndex].operand1] == -10086) {
				reg.fuValue[instructions.instruction[instrIndex].operand1] = tempIndex;
			}
			break;
		case JUMP:
			break;
		}
	}
	
	void addHashSet(int type, int index, int waitReservId) {
		switch(waitReservId) {
		case ADD5:
			myAdd(addReserv.reservationStation[5], type, index);
			break;	
		case ADD4:
			myAdd(addReserv.reservationStation[4], type, index);
			break;
		case ADD3:
			myAdd(addReserv.reservationStation[3], type, index);
			break;
		case ADD2:
			myAdd(addReserv.reservationStation[2], type, index);
			break;
		case ADD1:
			myAdd(addReserv.reservationStation[1], type, index);
			break;
		case ADD0:
			myAdd(addReserv.reservationStation[0], type, index);
			break;
		case MUL2:
			myAdd(mulReserv.reservationStation[2], type, index);
			break;
		case MUL1:
			myAdd(mulReserv.reservationStation[1], type, index);
			break;
		case MUL0:
			myAdd(mulReserv.reservationStation[0], type, index);
			break;
		case LOAD2:
			myAdd(loadReserv.reservationStation[2], type, index);
			break;
		case LOAD1:
			myAdd(loadReserv.reservationStation[1], type, index);
			break;
		case LOAD0:
			myAdd(loadReserv.reservationStation[0], type, index);
			break;
		}
		
		
	}
	
	void myAdd(Reserv r, int type, int index) {
		switch(type) {
		case 0:
			r.waitReserv.add(addReserv.reservationStation[index]);
			break;
		case 1:
			r.waitReserv.add(mulReserv.reservationStation[index]);
			break;
		}
	}
	
	
//	void updateReservationSta(int instrIndex, int writeBackId, int tempIndex) {
//		// add
//		for(int i = 0; i < 6; i++) {
//			// wait for data
//			if(addReserv.reservationStation[i].Q[0] == writeBackId) {
//				// update data
//				addReserv.reservationStation[i].V[0] = tempIndex;
//				addReserv.reservationStation[i].Q[0] = 0;
//			}
//			if(addReserv.reservationStation[i].Q[1] == writeBackId) {
//				// update data
//				addReserv.reservationStation[i].V[1] = tempIndex;
//				addReserv.reservationStation[i].Q[1] = 0;
//			}
////			if(readyToRun(addReserv.reservationStation[i])) {
////				addReserv.reservationStation[i].isRun = true;
////			}
//		}
//		
//		// mul
//		for(int i = 0; i < 3; i++) {
//			// wait for data
//			if(mulReserv.reservationStation[i].Q[0] == writeBackId) {
//				// update data
//				mulReserv.reservationStation[i].V[0] = tempIndex;
//				mulReserv.reservationStation[i].Q[0] = 0;
//			}
//			if(mulReserv.reservationStation[i].Q[1] == writeBackId) {
//				// div 0 
//				if(reg.tempReg[tempIndex] == 0 && mulReserv.reservationStation[i].type == DIV) {
//					reg.tempReg[reg.tempCount] = 1;
//					mulReserv.reservationStation[i].V[1] = reg.tempCount;
//					mulReserv.reservationStation[i].Q[1] = 0;
//					mulReserv.reservationStation[i].time = 1;
//					reg.tempCount++;
//				}
//				else {
//					// update data
//					mulReserv.reservationStation[i].V[1] = tempIndex;
//					mulReserv.reservationStation[i].Q[1] = 0;
//				}
//			}
//			
////			if(readyToRun(mulReserv.reservationStation[i])) {
////				mulReserv.reservationStation[i].isRun = true;
////			}
//		}
//		
//		
//		// Fu
//		for(int i = 0; i < 32; i++) {
//			if(reg.fuState[i] == writeBackId) {
//				reg.fuState[i] = -10086;
//			}
//		}
//		System.out.println(instrIndex);
//		switch(instructions.instruction[instrIndex].instrType) {
//		case ADD:
//		case ADDI:
//		case SUB:
//		case SUBI:
//		case MUL:
//		case DIV:
//		case LD:
//		case SHL:
//		case SAL:
//		case SHR:
//		case SAR:
//			if(reg.fuState[instructions.instruction[instrIndex].operand1] == -10086) {
//				reg.fuValue[instructions.instruction[instrIndex].operand1] = tempIndex;
//			}
//			break;
//		case JUMP:
//			break;
//		}
//	}

	void ExecComp() {
		System.out.println("ExecComp()");
		// deal with instr is running
		// add
		for(int i = 0; i < 6; i++) {
			if(addReserv.reservationStation[i].isBusy && addReserv.reservationStation[i].isRun) {
				addReserv.reservationStation[i].time --;
				if(addReserv.reservationStation[i].time == 0) {
					if(instructions.instruction[addReserv.reservationStation[i].instrIndex].execCompClock == -1) {
						instructions.instruction[addReserv.reservationStation[i].instrIndex].execCompClock = clock;
					}
//					addFunc.busySize--;
					
					if(addReserv.reservationStation[i].type == JUMP) {
						if(instructions.instruction[addReserv.reservationStation[i].instrIndex].operand1 == reg.tempReg[addReserv.reservationStation[i].V[0]]) {
							// jump
							currentInstr = addReserv.reservationStation[i].V[1] + instructions.instruction[addReserv.reservationStation[i].instrIndex].operand3;
						}
						else {
							// not jump
						}
						
						waitJump = false;
					}
				}
			}
		}
		
		// mul
		for(int i = 0; i < 3; i++) {
			if(mulReserv.reservationStation[i].isBusy && mulReserv.reservationStation[i].isRun) {
				mulReserv.reservationStation[i].time --;
				if(mulReserv.reservationStation[i].time == 0) {
					if(instructions.instruction[mulReserv.reservationStation[i].instrIndex].execCompClock == -1) {
						instructions.instruction[mulReserv.reservationStation[i].instrIndex].execCompClock = clock;
					}
//					mulFunc.busySize--;
				}
			}
		}
		
		// load
		for(int i = 0; i < 3; i++) {
			if(loadReserv.reservationStation[i].isBusy && loadReserv.reservationStation[i].isRun) {
				loadReserv.reservationStation[i].time --;
				if(loadReserv.reservationStation[i].time == 0) {
					if(instructions.instruction[loadReserv.reservationStation[i].instrIndex].execCompClock == -1) {
						instructions.instruction[loadReserv.reservationStation[i].instrIndex].execCompClock = clock;
					}
//					loadFunc.busySize--;
				}
			}
		}
		
		
		// if the funU is free  pick one to run
		
		// add
		int pickNumMax = addFunc.totalSize - addFunc.busySize;
		for(int pickNum = 0; pickNum < pickNumMax; pickNum++) {
			int readyRun = -1;
			int readyRunIndex = 10086;
			for(int i = 0; i < 6; i++) {
				if(readyToRun(addReserv.reservationStation[i])) {
					if(addReserv.reservationStation[i].instrIndex < readyRunIndex) {
						readyRun = i;
						readyRunIndex = addReserv.reservationStation[i].instrIndex;
					}
				}
			}
			
			// find one
			if(readyRun != -1) {
				addReserv.reservationStation[readyRun].isRun = true;
				addFunc.busySize++;
			}
			else {
				break;
			}
		}
		
		// mul
		pickNumMax = mulFunc.totalSize - mulFunc.busySize;
		for(int pickNum = 0; pickNum < pickNumMax; pickNum++) {
			int readyRun = -1;
			int readyRunIndex = 10086;
			for(int i = 0; i < 3; i++) {
				if(readyToRun(mulReserv.reservationStation[i])) {
					if(mulReserv.reservationStation[i].instrIndex < readyRunIndex) {
						readyRun = i;
						readyRunIndex = mulReserv.reservationStation[i].instrIndex;
					}
				}
			}
			
			// find one
			if(readyRun != -1) {
				mulReserv.reservationStation[readyRun].isRun = true;
				mulFunc.busySize++;
			}
			else {
				break;
			}
		}
		
		// load
		pickNumMax = loadFunc.totalSize - loadFunc.busySize;
		for(int pickNum = 0; pickNum < pickNumMax; pickNum++) {
			int readyRun = -1;
			int readyRunIndex = 10086;
			for(int i = 0; i < 3; i++) {
				if(loadReserv.reservationStation[i].isBusy == true && loadReserv.reservationStation[i].isRun == false) {
					if(loadReserv.reservationStation[i].instrIndex < readyRunIndex) {
						readyRun = i;
						readyRunIndex = loadReserv.reservationStation[i].instrIndex;
					}
				}
			}
			
			// find one
			if(readyRun != -1) {
				loadReserv.reservationStation[readyRun].isRun = true;
				loadFunc.busySize++;
			}
			else {
				break;
			}
		}
	}
	
	boolean readyToRun(Reserv re) {
		if(re.isBusy == true && re.isRun == false) {
			if(re.V[0] >= 0 && re.V[1] >= 0) {
				return true;
			}
		}
		return false;
	}
	
	void clearAll() {
		currentInstr = 0;
		clock = 1;
		waitJump = false;
		addReserv.clearReserv();
		mulReserv.clearReserv();
		loadReserv.clearReserv();
		
		addFunc.clearFunc();
		mulFunc.clearFunc();
		loadFunc.clearFunc();
		
		reg.clearFu();
	}
	
	
//	void printSta() {
//		addReserv.checkReserv();
////		mulReserv.checkReserv();
////		loadReserv.checkReserv();
//	}
//	
//	void printFunc() {
//		addFunc.checkFunc();
////		mulFunc.checkFunc();
////		loadFunc.checkFunc();
//	}
//	
//	void printAll() {
//		instructions.checkInstr();
//		printSta();
//		reg.checkFu();
//		printFunc();
//	}
//	
//	void printJump() {
//		System.out.println("waitJump:" + waitJump);
////		System.out.println("isJump:" + isJump);
////		System.out.println("jumpTime:" + jumpTime);
////		System.out.println("jumpIndex:" + jumpIndex);
////		System.out.println("jumpV:" + jumpV);
////		System.out.println("jumpQ:" + jumpQ);
//	}
}
