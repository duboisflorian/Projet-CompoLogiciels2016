package data.ia;

import java.util.Random;
import java.util.stream.IntStream;

import javafx.scene.image.Image;
import specifications.EnemyService;
import tools.Position;

public class MoveEnemy implements EnemyService{

	  private Position position;
	  private Image enemy_picture;
	  private Random gen=new Random();
	  private int dx,dy;

	  public MoveEnemy(Position p){ 
		    
			int c=gen.nextInt(4);
	        if (c==0) {
	        	enemy_picture = new Image("file:src/images/ballon_red.png");
	        } 
	        if (c==1) {
	        	enemy_picture = new Image("file:src/images/ballon_green.png");
	        }  
	        if (c==2) {
	        	enemy_picture = new Image("file:src/images/ballon_blue.png");
	        }  
	        if (c==3) {
	        	enemy_picture = new Image("file:src/images/ballon_yellow.png");
	        }  
	      
		  position=p; 
		  
		  int Low = -8;
		  int High = 8;
		  
		  c = gen.nextInt(High-Low) + Low;
		  while(c==0 || c==1 || c==2 || c==-1 || c==-2)
			  c = gen.nextInt(High-Low) + Low;
		  dx=c;
		  
		  c = gen.nextInt(High-Low) + Low;
		  while(c==0 || c==1 || c==2 || c==-1 || c==-2)
			  c = gen.nextInt(High-Low) + Low;
		  dy=c;
		  }

	  @Override
	  public Position getPosition() { return position; }
	  
	  @Override
	  public Image getEnemyPicture(){return enemy_picture;}

	  @Override
	  public void setPosition(Position p) { position=p; }

	@Override
	public int getDx() {
		return dx;
	}
	@Override
	public void setDx(int dx) {
		this.dx = dx;
	}
	@Override
	public int getDy() {
		return dy;
	}
	@Override
	public void setDy(int dy) {
		this.dy = dy;
	}

	
}
