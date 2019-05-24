package simulator;

public class ReservationStation {
	Reserv[] reservationStation;
	int totalSize;
	int busySize;
	
	ReservationStation(int size){
		reservationStation = new Reserv[size];
		for(int i = 0; i < size; i++) {
			reservationStation[i] = new Reserv();
		}
		totalSize = size;
		busySize = 0;
	}
	
	void checkReserv() {
		System.out.println("This is Reservation Stations:");
		System.out.println("Size:" + totalSize + " " + busySize);
		for(int i = 0; i < totalSize; i++) {
			System.out.println(i + " round:" );
			System.out.println("busy: " + reservationStation[i].isBusy + " run: " + reservationStation[i].isRun);
			System.out.println("time:" + reservationStation[i].time + " type:" + reservationStation[i].type + " addr:" + reservationStation[i].addr);
			

			System.out.println(reservationStation[i].V[0]+","+ reservationStation[i].V[1]);
			
			System.out.println(reservationStation[i].Q[0]+","+reservationStation[i].Q[1]);
		}
	}
}
