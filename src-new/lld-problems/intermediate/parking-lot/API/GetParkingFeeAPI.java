package ParkingLot.API;

public class GetParkingFeeAPI {

	public double getParkingFee(Ticket ticket) {
		
		return FeeCalculator.claculateFee(ticket);
	}
}
