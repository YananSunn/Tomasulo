package simulator;

public class Instruction {
	public Instr[] instruction;
	int instrSize;
	
	Instruction(){
		instruction = new Instr[1000];
	}
	
	void checkInstr() {
		System.out.println("This is Instrction status:");
		System.out.println("Size:" + instrSize);
		for(int i = 0; i < instrSize; i++) {
			System.out.println(i + " round:" );
			System.out.println(instruction[i].instrType + ":" + instruction[i].operand1 + " "+ instruction[i].operand2 + " "+ instruction[i].operand3);
			System.out.println(" issueClock:" + instruction[i].issueClock +
					" execCompClock:" + instruction[i].execCompClock +
					" writeResultClock:" + instruction[i].writeResultClock +
					" useReserv:" + instruction[i].useReserv);
		}
	}
}
