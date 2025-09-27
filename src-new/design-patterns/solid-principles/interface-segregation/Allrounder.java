package InterfaceSegregationPrinciple;

public class Allrounder implements Player, Fielder, Baller{

	public void bat() {
		System.out.println("Allrounder is Batting");
	}
	
	public void ball() {
		System.out.println("Allrounder is Balling");
	}
	
	public void field() {
		System.out.println("Allrounder is Fielding");
	}
}
