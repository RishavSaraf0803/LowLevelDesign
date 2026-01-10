package ParkingLot.Ticket;

import java.time.LocalDateTime;

import ParkingLot.Data.Ticket;
import ParkingLot.Data.Vehicle;
import ParkingLot.Factory.ParkingSpotManagerFactory;

public class FeeCalculator {

	
	public double calculateFee(Ticket ticket) {
		Vehicle vehicle = ticket.getVehicle();
		LocalDateTime entryTime = vehicle.getEntryTime();
		LocalDateTime exitTime = LocalDateTime .now();
		ParkingSpotManagerFactory.getParkingSpotManager(vehicle.getVehicleType());
		//double duration = (double) new LocalDateTime();
		return 0;
	}
}
