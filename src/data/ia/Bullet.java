package data.ia;

import specifications.BulletService;
import tools.Position;

public class Bullet implements BulletService{
	  private Position position;
	  private int deg;

	  public Bullet(Position p){ position=p; setDeg(50);}

	  @Override
	  public Position getBulletPosition() { return position; }

	  @Override
	  public void setBulletPosition(Position p) { position=p; }

	  @Override
	public int getDeg() {
		return deg;
	}
	  @Override
	public void setDeg(int deg) {
		this.deg = deg;
	}
}
