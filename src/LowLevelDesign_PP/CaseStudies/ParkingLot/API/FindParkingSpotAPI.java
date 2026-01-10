package ParkingLot.API;

import ParkingLot.Data.EntryPoint;
import ParkingLot.Data.ParkingSpot;
import ParkingLot.Data.SpotSelection;
import ParkingLot.Data.VehicleType;
import ParkingLot.Factory.ParkingSpotManagerFactory;
import ParkingLot.Factory.SpotSelectionManagerFactory;
import ParkingLot.Manager.ParkingSpotManager;
import ParkingLot.Manager.SpotSelectionManager;
import ParkingLot.finder.ParkingSpotFinder;

public class FindParkingSpotAPI {

	public ParkingSpot findParkingSpot(EntryPoint entryPoint, VehicleType vehicleType, SpotSelection spotSelection) {
		
		ParkingSpotManager parkingSpotManager = ParkingSpotManagerFactory.getParkingSpotManager(vehicleType);
		
		SpotSelectionManager spotSelectionManager = SpotSelectionManagerFactory.getSpotSelectionManager(spotSelection, entryPoint);
		return new ParkingSpotFinder(parkingSpotManager, spotSelectionManager).findParkingSpot();
	}
}
