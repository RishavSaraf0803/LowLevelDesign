package ParkingLot.Manager;

import java.util.List;

import ParkingLot.Data.ParkingSpot;

public interface ParkingSpotManager {

	List<ParkingSpot> getParkingSpots();
	
	double getParkingFees(double duration);
}
