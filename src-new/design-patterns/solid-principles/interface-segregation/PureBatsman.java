package InterfaceSegregationPrinciple;

public class PureBatsman implements Player, Fielder{
	
	public void bat() {
		System.out.println("Pure Batsman is Batting");
	}
	public void field() {
		System.out.println("Pure Batsman is Fielding");
	}
	

}
