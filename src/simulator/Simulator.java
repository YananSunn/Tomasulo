package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


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
    
    public static final int ADDTIME = 3;
    public static final int LDTIME = 3;
    public static final int JUMPTIME = 1;
//    public static final int DIVTIME = 40;
//    public static final int MULTIME = 12;
    
    public static final int DIVTIME = 4;
    public static final int MULTIME = 4;
    
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
                
            	instructions.instructionsString[line] = new String(tempString);
            	
                String tempType = "";
                int count = 0;
                while(tempString.charAt(count) != ',') {
                	tempType += tempString.charAt(count);
                	count++;
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
                else {
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
		while((currentInstr < instructions.instrSize || emptyReservationSta() == false) && clock < 20) {
			System.out.println("runSimulator now is " + this.clock);
			System.out.println("currentInstr now is " + this.currentInstr);
			System.out.println("emptyReservationSta() now is" + emptyReservationSta());
			System.out.println(addReserv.busySize + " "+ mulReserv.busySize + " "+loadReserv.busySize);
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
			WriteBack();
			
			// then update the info
			// check RS to run
			ExecComp();
		
			this.clock++;
		}
		printAll();
	}
	
	public int runPartSimulator(int clk) {
		clearAll();
		while((currentInstr < instructions.instrSize || emptyReservationSta() == false) && clock <= clk) {
			System.out.println("runSimulator now is " + this.clock);
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
			WriteBack();
			
			// then update the info
			// check RS to run
			ExecComp();
		
			this.clock++;
		}
		printAll();
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
			case SUB:
				if(this.addReserv.busySize < this.addReserv.totalSize) {
					canIssue = true;
				}
				break;
			case MUL:
			case DIV:
				if(this.mulReserv.busySize < this.mulReserv.totalSize) {
					canIssue = true;
				}
				break;
			case LD:
				if(this.loadReserv.busySize < this.loadReserv.totalSize) {
					canIssue = true;
				}
				break;
			case JUMP:
				if(this.addReserv.busySize < this.addReserv.totalSize) {
					canIssue = true;
				}
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
			this.addReserv.busySize++;
			this.addReserv.reservationStation[selectedRS].type = ADD;
			this.addReserv.reservationStation[selectedRS].time = ADDTIME;
			this.addReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
			}
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 6;
			
			break;
		case SUB:
			while(this.addReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			System.out.println("SUB selectedRS:" + selectedRS);
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.addReserv.reservationStation[selectedRS].isBusy = true;
			this.addReserv.busySize++;
			this.addReserv.reservationStation[selectedRS].type = SUB;
			this.addReserv.reservationStation[selectedRS].time = ADDTIME;
			this.addReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
			}
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 6;
			break;
		case MUL:
			while(this.mulReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			System.out.println("MUL selectedRS:" + selectedRS);
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.mulReserv.reservationStation[selectedRS].isBusy = true;
			this.mulReserv.busySize++;
			this.mulReserv.reservationStation[selectedRS].type = MUL;
			this.mulReserv.reservationStation[selectedRS].time = MULTIME;
			this.mulReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
			}
			
			// deal with reg
			this.reg.fuState[this.instructions.instruction[this.currentInstr].operand1] = selectedRS - 9;
			
			break;
		case DIV:
			while(this.mulReserv.reservationStation[selectedRS].isBusy) {
				selectedRS++;
			}
			System.out.println("DIV selectedRS:" + selectedRS);
			
			// deal with instr
			if(this.instructions.instruction[this.currentInstr].issueClock == -1) {
				this.instructions.instruction[this.currentInstr].issueClock = this.clock;
			}
			// deal with RS
			// busy time run is useful   type V Q
			this.mulReserv.reservationStation[selectedRS].isBusy = true;
			this.mulReserv.busySize++;
			this.mulReserv.reservationStation[selectedRS].type = DIV;
			this.mulReserv.reservationStation[selectedRS].time = DIVTIME;
			this.mulReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3] != -10086) {
				this.mulReserv.reservationStation[selectedRS].Q[1] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand3];
			}
			else {
				this.mulReserv.reservationStation[selectedRS].V[1] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand3];
			}
			
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
			this.addReserv.busySize++;
			this.addReserv.reservationStation[selectedRS].type = JUMP;
			this.addReserv.reservationStation[selectedRS].time = JUMPTIME;
			this.addReserv.reservationStation[selectedRS].instrIndex = currentInstr;
			
			if(this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2] != -10086) {
				this.addReserv.reservationStation[selectedRS].Q[0] = this.reg.fuState[this.instructions.instruction[this.currentInstr].operand2];
			}
			else {
				this.addReserv.reservationStation[selectedRS].V[0] = this.reg.fuValue[this.instructions.instruction[this.currentInstr].operand2];
			}
			System.out.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeere" +selectedRS + " "+ currentInstr);
			this.addReserv.reservationStation[selectedRS].V[1] = currentInstr;
			
			break;
		}
	}

	public void WriteBack() {
		System.out.println("WriteBack()");
		// add
		for(int i = 0; i < 6; i++) {
			if(addReserv.reservationStation[i].isRun == true && addReserv.reservationStation[i].time == 0) {
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
				case SUB:
					reg.tempReg[reg.tempCount] = reg.tempReg[addReserv.reservationStation[i].V[0]] - reg.tempReg[addReserv.reservationStation[i].V[1]];
					reg.tempCount++;
					break;
//				case JUMP:
//					if(instructions.instruction[addReserv.reservationStation[i].instrIndex].operand1 == reg.tempReg[addReserv.reservationStation[i].V[0]]) {
//						// jump
//						currentInstr = reg.tempReg[addReserv.reservationStation[i].V[1]] + instructions.instruction[addReserv.reservationStation[i].instrIndex].operand3;
//					}
//					else {
//						// not jump
//					}
//					break;
				}
				// update FU
//				reg.Fu[instructions.instruction[addReserv.reservationStation[i].instrIndex].operand1] = reg.tempCount - 1;
				
				int ind = addReserv.reservationStation[i].instrIndex;
				// addReserv clear
				addReserv.reservationStation[i].clearReserv();
				addReserv.busySize--;
				addFunc.busySize--;
				
				// update RS
				updateReservationSta(ind, i - 6, reg.tempCount - 1);
			}
		}
		
		// mul
		for(int i = 0; i < 3; i++) {
			if(mulReserv.reservationStation[i].isRun == true && mulReserv.reservationStation[i].time == 0) {
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
				}
				// update FU
//				reg.Fu[instructions.instruction[mulReserv.reservationStation[i].instrIndex].operand1] = reg.tempCount - 1;
				
				int ind = mulReserv.reservationStation[i].instrIndex;
				// addReserv clear
				mulReserv.reservationStation[i].clearReserv();
				mulReserv.busySize--;
				mulFunc.busySize--;
				
				// update RS
				updateReservationSta(ind, i - 9, reg.tempCount - 1);
			}
		}
		
		// load
		for(int i = 0; i < 3; i++) {
			if(loadReserv.reservationStation[i].isRun == true && loadReserv.reservationStation[i].time == 0) {
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
				// addReserv clear
				loadReserv.reservationStation[i].clearReserv();
				loadReserv.busySize--;
				loadFunc.busySize--;
				
				// update RS and FU
				updateReservationSta(ind, i - 12, reg.tempCount - 1);
			}
		}
		
		
		
	}
	
	void updateReservationSta(int instrIndex, int writeBackId, int tempIndex) {
		// add
		for(int i = 0; i < 6; i++) {
			// wait for data
			if(addReserv.reservationStation[i].Q[0] == writeBackId) {
				// update data
				addReserv.reservationStation[i].V[0] = tempIndex;
				addReserv.reservationStation[i].Q[0] = 0;
			}
			if(addReserv.reservationStation[i].Q[1] == writeBackId) {
				// update data
				addReserv.reservationStation[i].V[1] = tempIndex;
				addReserv.reservationStation[i].Q[1] = 0;
			}
//			if(readyToRun(addReserv.reservationStation[i])) {
//				addReserv.reservationStation[i].isRun = true;
//			}
		}
		
		// mul
		for(int i = 0; i < 3; i++) {
			// wait for data
			if(mulReserv.reservationStation[i].Q[0] == writeBackId) {
				// update data
				mulReserv.reservationStation[i].V[0] = tempIndex;
				mulReserv.reservationStation[i].Q[0] = 0;
			}
			if(mulReserv.reservationStation[i].Q[1] == writeBackId) {
				// div 0 
				if(reg.tempReg[tempIndex] == 0 && mulReserv.reservationStation[i].type == DIV) {
					reg.tempReg[reg.tempCount] = 1;
					mulReserv.reservationStation[i].V[1] = reg.tempCount;
					mulReserv.reservationStation[i].Q[1] = 0;
					reg.tempCount++;
				}
				else {
					// update data
					mulReserv.reservationStation[i].V[1] = tempIndex;
					mulReserv.reservationStation[i].Q[1] = 0;
				}
			}
			
//			if(readyToRun(mulReserv.reservationStation[i])) {
//				mulReserv.reservationStation[i].isRun = true;
//			}
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
		case SUB:
		case MUL:
		case DIV:
		case LD:
			reg.fuValue[instructions.instruction[instrIndex].operand1] = tempIndex;
			break;
		case JUMP:
			break;
		}
		
	}

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
	
	void updateFunc() {
		int co = 0;
		
		// add
		for(int i = 0; i < 6; i++) {
			if(addReserv.reservationStation[i].isBusy && addReserv.reservationStation[i].isRun) {
				co++;
			}
		}
		addFunc.busySize = co;
		
		// mul
		co = 0;
		for(int i = 0; i < 3; i++) {
			if(mulReserv.reservationStation[i].isBusy && mulReserv.reservationStation[i].isRun) {
				co++;
			}
		}
		mulFunc.busySize = co;
		
		// load
		co = 0;
		for(int i = 0; i < 3; i++) {
			if(loadReserv.reservationStation[i].isBusy && loadReserv.reservationStation[i].isRun) {
				co++;
			}
		}
		loadFunc.busySize = co;
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
	
	
	void printSta() {
		addReserv.checkReserv();
//		mulReserv.checkReserv();
//		loadReserv.checkReserv();
	}
	
	void printFunc() {
		addFunc.checkFunc();
//		mulFunc.checkFunc();
//		loadFunc.checkFunc();
	}
	
	void printAll() {
		instructions.checkInstr();
		printSta();
		reg.checkFu();
		printFunc();
	}
	
	void printJump() {
		System.out.println("waitJump:" + waitJump);
//		System.out.println("isJump:" + isJump);
//		System.out.println("jumpTime:" + jumpTime);
//		System.out.println("jumpIndex:" + jumpIndex);
//		System.out.println("jumpV:" + jumpV);
//		System.out.println("jumpQ:" + jumpQ);
	}
}
