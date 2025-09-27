package ParkingLot.API;

import ParkingLot.Data.ParkingSpot;
import ParkingLot.Data.Ticket;
import ParkingLot.Data.Vehicle;
import ParkingLot.Ticket.TicketGenerator;

public class GetTicketAPI {
	
	
	public Ticket getTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
		
		return  TicketGenerator.generateTicket(vehicle, parkingSpot);
		
	}

}
