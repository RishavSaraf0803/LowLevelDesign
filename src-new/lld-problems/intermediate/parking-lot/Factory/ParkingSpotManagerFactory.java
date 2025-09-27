package ParkingLot.Factory;

import ParkingLot.Data.VehicleType;
import ParkingLot.Manager.FourWheelerParkingSpotManager;
import ParkingLot.Manager.ParkingSpotManager;
import ParkingLot.Manager.ThreeWheelerParkingSpotManager;
import ParkingLot.Manager.TwoWheelerParkingSpotManager;

public class ParkingSpotManagerFactory {

	private ParkingSpotManagerFactory() {
		
	}
	
	public static ParkingSpotManager  getParkingSpotManager(VehicleType vehicleType) {
		
		if(vehicleType.equals(VehicleType.TWOWHEELER)) {
			return new TwoWheelerParkingSpotManager();
		}
		else if(vehicleType.equals(VehicleType.THREEWHEELER)) {
			return new ThreeWheelerParkingSpotManager();
		}
		else if(vehicleType.equals(VehicleType.FOURWHEELER)) {
			return new FourWheelerParkingSpotManager();
		}
		else
		throw new RuntimeException("Vehicle Type is not valid");
	}
}
