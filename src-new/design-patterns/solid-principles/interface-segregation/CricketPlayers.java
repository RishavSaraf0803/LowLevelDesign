package InterfaceSegregationPrinciple;

import java.util.ArrayList;
import java.util.List;

public class CricketPlayers {

	
	public void batter(Player player) {player.bat();}

	public void ball(Baller baller) {baller.ball();}

	public void field(Fielder fielder) {fielder.field();}

	public void keeper(WicketKeeper wicketKeeper) {wicketKeeper.keep();}
	
	}
	

