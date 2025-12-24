package InterfaceSegregationPrinciple;

import java.util.Arrays;
import java.util.List;

public class Cricket {

	private final List<PureBatsman> purebatsman;
	private final List<Allrounder> allrounder;
	private final WicketKeeper wicketKeeper;
	private final CricketPlayers cricket;
	
	public Cricket(List<PureBatsman> pb, List<Allrounder> ar, WicketKeeper wk, CricketPlayers cricket) {
		this.purebatsman = pb;
		this.allrounder = ar;
		this.wicketKeeper =  wk;
		this.cricket = cricket;
	}
	
	private void startBatting() {
		for(PureBatsman pb: purebatsman) {
			cricket.batter(pb);
		}
		
		for(Allrounder al : allrounder) {
			cricket.batter(al);
		}
		cricket.batter(wicketKeeper);
	}
	private void startBalling() {
		for(Allrounder ar: allrounder) {
			cricket.ball(ar);
		}
	}
	private void startFielding() {
		for(PureBatsman pb: purebatsman) {
			cricket.field(pb);
		}
		for(Allrounder ar: allrounder) {
			cricket.field(ar);
		}
		
	}
	private void startKeeping() {
		cricket.keeper(wicketKeeper);
	}
	
	public void simulator() {
		System.out.println("Start Cricket");
		startBatting();
		startBalling();

		startFielding();

		startKeeping();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<PureBatsman> pb = Arrays.asList(new PureBatsman(), new PureBatsman(),new PureBatsman(),new PureBatsman(),new PureBatsman(),new PureBatsman());
		List<Allrounder> ar = Arrays.asList(new Allrounder(),new Allrounder(),new Allrounder(),new Allrounder());
		WicketKeeper wk = new WicketKeeper();
		Cricket cricket = new Cricket(pb,ar,wk, new CricketPlayers());
		cricket.simulator();

	}

}
