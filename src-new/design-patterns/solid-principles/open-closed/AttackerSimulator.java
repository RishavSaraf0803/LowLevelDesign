package Open_Close_Principle;

import java.util.ArrayList;
import java.util.List;

public class AttackerSimulator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Attacker attack = new Attacker();
		ArrayList<SuperHero> superHeroes = new ArrayList<>();
		
		superHeroes.add(new BATMAN());
		superHeroes.add(new SPIDERMAN());
		superHeroes.add(new SUPERMAN());
		//superHeroes.add(new ());
		attack.attackWithSuperHeroes(superHeroes);
		
		

	}

}
