package ParkingLot.Data;

public class Ticket {

	
	private final String ticketNumber;
	private final Vehicle vehicle;
	private final ParkingSpot parkingSpot;
	public Ticket(String ticketNumber, Vehicle vehicle, ParkingSpot parkingSpot) {
		super();
		this.ticketNumber = ticketNumber;
		this.vehicle = vehicle;
		this.parkingSpot = parkingSpot;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}
	
	
}
