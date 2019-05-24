package simulator;

public class RegisterResult {
	int[] Fu;
	int[] tempReg;
	int tempCount;
	
	RegisterResult(){
		Fu = new int[32];
		for(int i = 0; i < 32; i++) {
			Fu[i] = -10086;
		}
		
		tempReg = new int[5005];
		tempCount = 0;
	}
	
	void checkFu() {
		System.out.println("This is Register Result Status:");
		for(int i = 0; i < 32; i++) {
			System.out.print(i + ":" + Fu[i] + " ");
		}
		System.out.println();
		for(int i = 0; i < tempCount; i++) {
			System.out.print(i + ":" + tempReg[i] + " ");
		}
		System.out.println();
	}
}
