package ParkingLot.Factory;

import ParkingLot.Data.EntryPoint;
import ParkingLot.Data.SpotSelection;
import ParkingLot.Manager.ClosestSpotSelctionManager;
import ParkingLot.Manager.ParkingSpotManager;
import ParkingLot.Manager.RandomSpotSelectionManager;
import ParkingLot.Manager.SpotSelectionManager;

public class SpotSelectionManagerFactory {

	
	private SpotSelectionManagerFactory() {
		
	}
	
	public static SpotSelectionManager getSpotSelectionManager(SpotSelection spotSelection, EntryPoint entryPoint) {
		
		if(spotSelection.equals(SpotSelection.RANDOM)) {
			return new RandomSpotSelectionManager();
		}
		else if(spotSelection.equals(SpotSelection.CLOSEST)) {
			return getClosestParkingSpotSelector(entryPoint);
		}
		else {
			throw new RuntimeException("SpotSelection is not valid");
		}
		
	}
	
	private static SpotSelectionManager getClosestParkingSpotSelector(EntryPoint entryPoint) {
		return new ClosestSpotSelctionManager(entryPoint);
	}
}
