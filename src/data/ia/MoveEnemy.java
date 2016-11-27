package data.ia;

import java.util.Random;
import java.util.stream.IntStream;

import javafx.scene.image.Image;
import specifications.DataService;
import specifications.EnemyService;
import tools.Position;

public class MoveEnemy implements EnemyService{

	  private Position position;
	  private Image enemy_picture;
	  private Random gen=new Random();
	  private int dx,dy,health;
	  public static int r=0,g=0,b=0,y=0;

	  public MoveEnemy(Position p,int h){ 
		    
			int c=gen.nextInt(4);
	        if (c==0) {
	        	enemy_picture = new Image("file:src/images/ballon_red.png");
	        	r++;
	        } 
	        if (c==1) {
	        	enemy_picture = new Image("file:src/images/ballon_green.png");
	        	g++;
	        }  
	        if (c==2) {
	        	enemy_picture = new Image("file:src/images/ballon_blue.png");
	        	b++;
	        }  
	        if (c==3) {
	        	enemy_picture = new Image("file:src/images/ballon_yellow.png");
	        	y++;
	        }  
	      
		  position=p; 
		  
		  int Low = -10;
		  int High = 10;
		  
		  c = gen.nextInt(High-Low) + Low;
		  while(c==0 || c==1 || c==2 || c==-1 || c==-2 || c==-3 || c==3 || c==-4 || c==4 )
			  c = gen.nextInt(High-Low) + Low;
		  dx=c;
		  
		  c = gen.nextInt(High-Low) + Low;
		  while(c==0 || c==1 || c==2 || c==-1 || c==-2 || c==-3 || c==3 || c==-4 || c==4 )
			  c = gen.nextInt(High-Low) + Low;
		  dy=c;
		  
		  setHealth(h);
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
	@Override
	public int getHealth() {
		return health;
	}
	@Override
	public void setHealth(int health) {
		this.health = health;
	}
	
	
}
