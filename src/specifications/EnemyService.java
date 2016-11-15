package specifications;

import javafx.scene.image.Image;
import tools.Position;
public interface EnemyService{

	  public Position getPosition();
	  public void setPosition(Position p);
	  public Image getEnemyPicture();
	  public int getDy();
	  public int getDx();
	  public void setDx(int dx);
	  public void setDy(int dy);
	int getHealth();
	void setHealth(int health); 
}
