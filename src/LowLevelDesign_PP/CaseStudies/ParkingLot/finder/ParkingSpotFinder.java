package ParkingLot.finder;

import java.util.List;

import ParkingLot.Data.ParkingSpot;
import ParkingLot.Manager.ParkingSpotManager;
import ParkingLot.Manager.SpotSelectionManager;
//Stratergy Pattern
public class ParkingSpotFinder {

	private final ParkingSpotManager parkingSpotManager;
	private final SpotSelectionManager spotSelectionManager;
	public ParkingSpotFinder(ParkingSpotManager parkingSpotManager, SpotSelectionManager spotSelectionManager) {
		super();
		this.parkingSpotManager = parkingSpotManager;
		this.spotSelectionManager = spotSelectionManager;
	}
	public ParkingSpot findParkingSpot() {
		
		List<ParkingSpot> parkingSpots = parkingSpotManager.getParkingSpots();
		ParkingSpot parkingSpot = spotSelectionManager.selectParkingSpot(parkingSpots);
		
		return parkingSpot;
	}
	
}
