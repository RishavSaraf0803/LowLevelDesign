package ParkingLot.Ticket;

import ParkingLot.Data.ParkingSpot;
import ParkingLot.Data.Ticket;
import ParkingLot.Data.Vehicle;

public class TicketGenerator {

	private TicketGenerator(){
		
	}
	
	public static Ticket generateTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
		String ticketNumber = getUniqueTickeNumber();
		//logic to check if isFree then park & persist in DB
		if(!parkingSpot.isFree()) {
			throw new RuntimeException("Parking spot is not free");
		}
		return new Ticket(ticketNumber, vehicle, parkingSpot);
	}
	
	private static String getUniqueTickeNumber() {
		return "";
	}
}
