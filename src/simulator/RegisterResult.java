package simulator;

public class RegisterResult {
	int[] fuState;
	int[] fuValue;
	int[] tempReg;
	int tempCount;
	
	RegisterResult(){
		fuState = new int[32];
		fuValue = new int[32];
		for(int i = 0; i < 32; i++) {
			fuState[i] = -10086;
			fuValue[i] = -10086;
		}
		
		tempReg = new int[5005];
		tempCount = 0;
	}
	
	void checkFu() {
		System.out.println("This is Register Result Status:");
		for(int i = 0; i < 32; i++) {
			System.out.print(i + ":" + fuState[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < tempCount; i++) {
			System.out.print(i + ":" + tempReg[i] + " ");
		}
		System.out.println();
	}
	
	void clearFu() {
		for(int i = 0; i < 32; i++) {
			fuState[i] = -10086;
			fuValue[i] = -10086;
		}
		for(int i = 0; i < tempCount; i++) {
			tempReg[i] = -10086;
		}
		tempCount = 0;
	}
}
