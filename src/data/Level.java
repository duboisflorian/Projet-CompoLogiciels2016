package data;

public class Level {
	
	public Level(int level, int nbEnemy, int healthEnemy, double invoc) {
		this.level = level;
		this.nbEnemy = nbEnemy;
		this.healthEnemy = healthEnemy;
		this.invoc = invoc;
		nbkill=0;
		nbpop=0;
	}

	public int level, nbEnemy, healthEnemy, nbkill,nbpop;
	public double invoc;

	public void update() {
		// TODO Auto-generated method stub
		level++;
		healthEnemy=healthEnemy+25;
		nbEnemy=nbEnemy+5;
		invoc=invoc-0.5;
		nbkill=0;
		nbpop=0;
	}

	
}
