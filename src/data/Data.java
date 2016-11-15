/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;

import java.util.ArrayList;

import data.ia.Bullet;
import data.ia.MoveEnemy;
import specifications.BulletService;
import specifications.DataService;
import specifications.EnemyService;

public class Data implements DataService{

  private Child child;
  private int stepNumber;
  private Field field;
  private ArrayList<EnemyService> ballon;
  private ArrayList<BulletService> bullet;
  private Level level;
  public Data(){}

  @Override
  public void init(){
    child = new Child(new Position(HardCodedParameters.ChildStartX,HardCodedParameters.ChildStartY));
    stepNumber = 0;
    field = new Field(HardCodedParameters.ChildWidth/2,HardCodedParameters.defaultWidth-(HardCodedParameters.ChildWidth/2.5),HardCodedParameters.ChildHeight/1.3,HardCodedParameters.defaultHeight-250);
    ballon = new ArrayList<EnemyService>();
    bullet = new ArrayList<BulletService>();
    level=new Level(1,25,100,2);
  }

  @Override
  public Position getChildPosition(){ return child.getPosition(); }
  
  @Override
  public Field getField(){ return field; }

  @Override
  public int getStepNumber(){ return stepNumber; }

  @Override
  public void setChildPosition(Position p) { child.setPosition(p); }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }

	@Override
	public int getChildHealth() {
		// TODO Auto-generated method stub
		return child.getHealth();
	}
	
	@Override
	public int getChildScore() {
		// TODO Auto-generated method stub
		return child.getScore();
	}
	
	@Override
	public void setChildHealth(int i) {
		// TODO Auto-generated method stub
		child.setHealth(i);
	}
	
	@Override
	public void setChildScore(int i) {
		// TODO Auto-generated method stub
		child.setScore(i);
	}
	
	@Override
	public void setLevelnbkill(int i) {
		// TODO Auto-generated method stub
		level.nbkill=i;
	}
	
	@Override
	public void setLevelnbpop(int i) {
		// TODO Auto-generated method stub
		level.nbpop=i;
	}

	
	  @Override
	  public ArrayList<EnemyService> getEnemy(){ return ballon; }

	  @Override
	  public ArrayList<BulletService> getBullet(){ return bullet; }
	  
	  @Override
	  public void addEnemy(Position p) { ballon.add(new MoveEnemy(p,level.healthEnemy)); }
	  
	  @Override
	  public void addBullet(Position p) { bullet.add(new Bullet(p)); }
	  
	  @Override
	  public void setEnemy(ArrayList<EnemyService> enemy) { this.ballon=enemy; }
	  
	  @Override
	  public void setBullet(ArrayList<BulletService> bu) { this.bullet=bu; }
	  
	  @Override
	  public Level getLevel(){ return level; }
	  
	  @Override
	  public void updateLevel(){ 
		  level.update();
	  }
}
