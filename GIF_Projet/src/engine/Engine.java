/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;
import specifications.EngineService;
import specifications.DataService;
import specifications.EnemyService;
import specifications.RequireDataService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import javafx.application.Platform;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              ChildStep=HardCodedParameters.ChildStep;
  private Timer engineClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  private double ChildVX,ChildVY;
  public Engine(){}

  @Override
  public void bindDataService(DataService service){
    data=service;
  }
  
  @Override
  public void init(){
    engineClock = new Timer();
    command = User.COMMAND.NONE;
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;
    ChildVX = 0;
    ChildVY = 0;
    gen = new Random();
  }

  @Override
  public void start(){
    engineClock.schedule(new TimerTask(){
      public void run() {
    	  
    	 if(data.getChildHealth()<=HardCodedParameters.MinHealth)  {
  			JOptionPane.showMessageDialog(null,"You are dead");
  			stop();
  			Platform.exit();
  		
  		}
    	  
  		int ii=gen.nextInt(100);
        if (ii<=10) {
        	spawnEnemy();
        }  
    	  
        ArrayList<EnemyService> ballon = new ArrayList<EnemyService>();
        
        updateSpeedChild();
        updateCommandChild();
        updatePositionChild();
        
        for (EnemyService b:data.getEnemy()){
        	move(b);
            if (collisionChildEnemy(b)){
                data.setChildHealth(data.getChildHealth()-1);
              } else {
            	  ballon.add(b);
              }
            }

        data.setEnemy(ballon);

        data.setStepNumber(data.getStepNumber()+1);
      }

    },0,HardCodedParameters.enginePaceMillis);
  }

  @Override
  public void stop(){
    engineClock.cancel();
  }

  @Override
  public void setChildCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=true;
    if (c==User.COMMAND.RIGHT) moveRight=true;
    if (c==User.COMMAND.UP) moveUp=true;
    if (c==User.COMMAND.DOWN) moveDown=true;
  }
  
  @Override
  public void releaseChildCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=false;
    if (c==User.COMMAND.RIGHT) moveRight=false;
    if (c==User.COMMAND.UP) moveUp=false;
    if (c==User.COMMAND.DOWN) moveDown=false;
  }

  private void updateSpeedChild(){
    ChildVX*=friction;
    ChildVY*=friction;
  }

  private void updateCommandChild(){
    if (moveLeft && data.getChildPosition().x>data.getField().xmin) ChildVX-=ChildStep;
    if (moveRight && data.getChildPosition().x<data.getField().xmax) ChildVX+=ChildStep;
    if (moveUp && data.getChildPosition().y>data.getField().ymin) ChildVY-=ChildStep;
    if (moveDown && data.getChildPosition().y<data.getField().ymax) ChildVY+=ChildStep;
  }
  
  private void updatePositionChild(){
	    data.setChildPosition(new Position(data.getChildPosition().x+ChildVX,data.getChildPosition().y+ChildVY));
  }

  private void spawnEnemy(){
	    int x=0;
	    int y=0;
	    boolean cont=true;
	    while (cont) {
	      x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.85))+HardCodedParameters.defaultWidth*.1);
	      cont=false;
	      for (EnemyService p:data.getEnemy()){
	        if (p.getPosition().equals(new Position(x,y))) cont=true;
	      }
	    }
	    data.addEnemy(new Position(x,y));
	  }

  private void move(EnemyService p){
	  
	  p.setPosition(new Position(p.getPosition().x+p.getDx(),p.getPosition().y+p.getDy()));
		if (p.getPosition().x <= 0) {
			  p.setPosition(new Position(0,p.getPosition().y));
			p.setDx(-p.getDx());
		}
		if (p.getPosition().x > data.getField().xmax) {
			p.setDx(-p.getDx());
		}
		if (p.getPosition().y <= 0) {
			  p.setPosition(new Position(p.getPosition().x,0));
				p.setDy(-p.getDy());
		}
		if (p.getPosition().y> data.getField().ymax) {
			p.setDy(-p.getDy());
		}
		
	  }
  
  private boolean collisionChildEnemy(EnemyService e){
return (
     ( (data.getChildPosition().x-e.getPosition().x)*(data.getChildPosition().x-e.getPosition().x)-150)+
      ((data.getChildPosition().y-e.getPosition().y)*(data.getChildPosition().y-e.getPosition().y) +90)<
      0.25*(HardCodedParameters.ChildWidth+HardCodedParameters.enemyWidth)*(HardCodedParameters.ChildWidth+HardCodedParameters.enemyWidth)
    );
  }
  
  private boolean collisionChildEnemys(){
    for (EnemyService e:data.getEnemy()) if (collisionChildEnemy(e)) return true; return false;
  }
  
}