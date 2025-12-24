package ParkingLot.Manager;

import java.util.List;

import ParkingLot.Data.EntryPoint;
import ParkingLot.Data.ParkingSpot;
import ParkingLot.Data.SpotSelection;

public class ClosestSpotSelctionManager implements SpotSelectionManager {

	
	private final EntryPoint entryPoint;
	
	public ClosestSpotSelctionManager(EntryPoint entryPoint) {
		super();
		this.entryPoint = entryPoint;
	}

	@Override
	public ParkingSpot selectParkingSpot(List<ParkingSpot> parkingSpots) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
