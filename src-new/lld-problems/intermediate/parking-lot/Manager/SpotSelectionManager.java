package ParkingLot.Manager;

import java.util.List;

import ParkingLot.Data.ParkingSpot;

public interface SpotSelectionManager {

	public ParkingSpot selectParkingSpot(List<ParkingSpot> parkingSpots);
}
