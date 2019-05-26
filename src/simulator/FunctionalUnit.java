package simulator;

public class FunctionalUnit {
//	Func[] functionalUnit;
	int totalSize;
	int busySize;
	
	FunctionalUnit(int size){
//		functionalUnit = new Func[size];
//		for(int i = 0; i < size; i++) {
//			functionalUnit[i] = new Func();
//		}
		
		totalSize = size;
		busySize = 0;
	}
	
	void checkFunc() {
		System.out.println("This is Functional Unit:");
		System.out.println("Size:" + totalSize + " " + busySize);
//		for(int i = 0; i < totalSize; i++) {
//			System.out.println(i + " round:" );
//			System.out.println(functionalUnit[i].isOccupied + "," + functionalUnit[i].occupiedTime);
//		}
	}
	
	void clearFunc() {
		busySize = 0;
	}
}
