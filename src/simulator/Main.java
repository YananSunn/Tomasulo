package simulator;



public class Main {
	public static void main(String[] args)
	{
		Simulator simu = new Simulator();
		simu.readFileByLines(simu.fileName);
		
		simu.instructions.checkInstr();
//		simu.addReserv.checkReserv();
//		simu.addFunc.checkFunc();
//		simu.reg.checkFu();
		
		
		simu.runSimulator();
		
//		simu.instructions.checkInstr();
//		simu.loadReserv.checkReserv();
//		simu.loadFunc.checkFunc();
//		simu.reg.checkFu();
		
	}
}