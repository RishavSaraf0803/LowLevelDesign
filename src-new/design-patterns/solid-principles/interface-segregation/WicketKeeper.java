package InterfaceSegregationPrinciple;

public class WicketKeeper implements Player, Keeper{
	
	public void keep() {
		System.out.println("WicketKeeper is Keeping");
	}
	
	public void bat() {
		System.out.println("WicketKeeper is Batting");
	}

}
