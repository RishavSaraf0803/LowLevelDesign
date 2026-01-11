package ParkingLot.Data;

public class ParkingSpot {

	private final String parkingSpotName;
	private final VehicleType vehicleType;
	private final String floorNum;
	private final boolean isFree;
	public ParkingSpot(String parkingSpotName, VehicleType vehicleType, String floorNum, boolean isFree) {
		super();
		this.parkingSpotName = parkingSpotName;
		this.vehicleType = vehicleType;
		this.floorNum = floorNum;
		this.isFree = isFree;
	}
	public String getParkingSpotName() {
		return parkingSpotName;
	}
	public VehicleType getVehicleType() {
		return vehicleType;
	}
	public String getFloorNum() {
		return floorNum;
	}
	public boolean isFree() {
		return isFree;
	}
	
	
}
