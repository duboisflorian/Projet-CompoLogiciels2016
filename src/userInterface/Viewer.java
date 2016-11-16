/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import tools.HardCodedParameters;
import tools.Position;
import specifications.ViewerService;
import specifications.BulletService;
import specifications.EnemyService;
import specifications.ReadService;
import specifications.RequireReadService;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Viewer implements ViewerService, RequireReadService{
  private static final int spriteSlowDownRate=HardCodedParameters.spriteSlowDownRate;
  private static final double defaultMainWidth=HardCodedParameters.defaultWidth,
                              defaultMainHeight=HardCodedParameters.defaultHeight;
  private ReadService data;
  private ImageView ChildAvatar;
  private Image ChildSpriteSheet,Field,ballon_color;
  private ArrayList<Rectangle2D> ChildAvatarViewports;
  private ArrayList<Integer> ChildAvatarXModifiers;
  private ArrayList<Integer> ChildAvatarYModifiers;
  private String highscore;
  private int ChildAvatarViewportIndex;
  private double xShrink,yShrink,shrink,xModifier,yModifier,ChildScale;

  public Viewer(){}
  
  @Override
  public void bindReadService(ReadService service){
    data=service;
  }

  @Override
  public void init(){
    xShrink= 1;
    yShrink=1;
    xModifier=0;
    yModifier=0;
    
    //Yucky hard-conding
    ChildSpriteSheet = new Image("file:src/images/perso.png");
    ChildAvatar = new ImageView(ChildSpriteSheet);    
    ChildAvatar = new ImageView(ChildSpriteSheet);
    ChildAvatarViewports = new ArrayList<Rectangle2D>();
    ChildAvatarXModifiers = new ArrayList<Integer>();
    ChildAvatarYModifiers = new ArrayList<Integer>();

    ChildAvatarViewports.add(new Rectangle2D(0,0,90,250));
    ChildAvatarXModifiers.add(6);ChildAvatarYModifiers.add(-6);

    Field = new Image("file:src/images/fond.jpg");
    ballon_color = new Image("file:src/images/ballon_red.png");     
  }

  @Override
  public Parent getPanel(){
    shrink=Math.min(xShrink,yShrink);
    xModifier=.01*shrink*defaultMainHeight;
    yModifier=.01*shrink*defaultMainHeight;

    //Highscore 
//    try{
//    	InputStream flux=new FileInputStream("src/backoffice/highscore.txt"); 
//    	InputStreamReader lecture=new InputStreamReader(flux);
//    	BufferedReader buff=new BufferedReader(lecture);
//    	String ligne;
//    	while ((ligne=buff.readLine())!=null){
//    		highscore=ligne;
//    	}
//    	buff.close(); 
//    	}		
//    	catch (Exception e){
//    	System.out.println(e.toString());
//    	}
//    if(data.getChildScore()>Integer.parseInt(highscore)){
//		File f = new File ("src/backoffice/highscore.txt");
//		 
//		try
//		{
//		    FileWriter fw = new FileWriter (f);
//		 
//		        fw.write(Integer.toString(data.getChildScore()));
//		 
//		    fw.close();
//		}
//		catch (IOException exception)
//		{
//		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
//		}
//		}
    // Partie2
    Rectangle partie2 = new Rectangle(defaultMainWidth*shrink+150,
            400*shrink);
    partie2.setFill(Color.BLACK);
    partie2.setTranslateX(shrink*(defaultMainWidth+160));
	
	Text highscoretxt = new Text(shrink*(defaultMainWidth+160),
	     shrink*50,
	     "Highscore : " + data.getHighscore());
	highscoretxt.setFont(new Font(.04*shrink*defaultMainHeight));
	highscoretxt.setFill(Color.WHITE);
	
	Text nbpartie = new Text(shrink*(defaultMainWidth+160),
		     shrink*100,
		     "Nb de partie jouee : ");
	nbpartie.setFont(new Font(.04*shrink*defaultMainHeight));
	nbpartie.setFill(Color.WHITE);
    //Yucky hard-conding
    Rectangle background = new Rectangle(defaultMainWidth*shrink+150,
                                  defaultMainHeight*shrink);
    background.setFill(new ImagePattern(Field));
    
    Text level = new Text(-0.1*shrink*defaultMainHeight+.1*shrink*defaultMainWidth,
                           -0.08*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Niveau: " + data.getLevel().level + "  ("+ data.getLevel().nbkill +"/"+data.getLevel().nbEnemy+")");
    level.setFont(new Font(.05*shrink*defaultMainHeight));
    level.setFill(Color.WHITE);
    
    Text Score = new Text(-0.1*shrink*defaultMainHeight+.1*shrink*defaultMainWidth,
                           -0.03*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Score: " + data.getChildScore());
    Score.setFont(new Font(.05*shrink*defaultMainHeight));
    Score.setFill(Color.WHITE);
    
    int index=ChildAvatarViewportIndex/spriteSlowDownRate;
    ChildScale=HardCodedParameters.ChildHeight*shrink/ChildAvatarViewports.get(index).getHeight();
    ChildAvatar.setViewport(ChildAvatarViewports.get(index));
    ChildAvatar.setFitHeight(HardCodedParameters.ChildHeight*shrink);
    ChildAvatar.setPreserveRatio(true);
    ChildAvatar.setTranslateX(shrink*data.getChildPosition().x+
                               shrink*xModifier+
                               -ChildScale*0.5*ChildAvatarViewports.get(index).getWidth()+
                               shrink*ChildScale*ChildAvatarXModifiers.get(index)
                              );
    ChildAvatar.setTranslateY(shrink*data.getChildPosition().y+
                               shrink*yModifier+
                               -ChildScale*0.5*ChildAvatarViewports.get(index).getHeight()+
                               shrink*ChildScale*ChildAvatarYModifiers.get(index)
                              );
    ChildAvatarViewportIndex=(ChildAvatarViewportIndex+1)%(ChildAvatarViewports.size()*spriteSlowDownRate);  
    
    Group panel = new Group();
    panel.getChildren().addAll(background,level,Score,ChildAvatar,partie2,highscoretxt,nbpartie);

    ArrayList<EnemyService> ballon = data.getEnemy();
    EnemyService e;

    
    for (int i=0; i<ballon.size();i++){
      e=ballon.get(i);
    
      Rectangle ballonAvatar = new Rectangle(HardCodedParameters.enemyWidth*shrink,
    		  HardCodedParameters.enemyWidth*shrink);
      ballonAvatar.setFill(new ImagePattern(e.getEnemyPicture()));
      
      ballonAvatar.setTranslateX(shrink*e.getPosition().x);
      ballonAvatar.setTranslateY(shrink*e.getPosition().y);
      panel.getChildren().add(ballonAvatar);
      
      if(e.getHealth() != data.getLevel().healthEnemy)
      {
    	   Rectangle Health = new Rectangle();
    	    Health.setX(shrink*e.getPosition().x+5);
    	    Health.setFill(Color.GREENYELLOW);
    	    Health.setY(shrink*e.getPosition().y - 10);
    	    Health.setWidth((e.getHealth()*75)/data.getLevel().healthEnemy);
    	    Health.setHeight(10);
    	  panel.getChildren().add(Health);
      }
    }
    
    ArrayList<BulletService> bullet = data.getBullet();
    BulletService b;
    
    for (int i=0; i<bullet.size();i++){
      b=bullet.get(i);
    
      Rectangle bulletAvatar = new Rectangle(HardCodedParameters.bulletWidth*shrink,
    		  HardCodedParameters.bulletHeight*shrink);
      bulletAvatar.setFill(new ImagePattern(new Image("file:src/images/flechette.png")));
      
      bulletAvatar.setTranslateX(shrink*b.getBulletPosition().x);
      bulletAvatar.setTranslateY(shrink*b.getBulletPosition().y);
      panel.getChildren().add(bulletAvatar);
    }
    
    for (double i=0.0; i<(double)data.getChildHealth()/20;i=i + 0.05){
    	
        Rectangle health = new Rectangle(HardCodedParameters.healthWidth*shrink,
      		  HardCodedParameters.healthWidth*shrink);
        health.setFill(new ImagePattern(new Image("file:src/images/heart.png")));
        
        health.setTranslateX(-0.1*shrink*defaultMainHeight+(.999 - i)*shrink*defaultMainWidth+150);
        health.setTranslateY(-0.09*shrink*defaultMainWidth+shrink*defaultMainHeight);
        panel.getChildren().add(health);
      }
    
    ArrayList<Position> lollipop = data.getLollipop();
    Position p;
    
    for (int i=0; i<lollipop.size();i++){
    	p=lollipop.get(i);
        Rectangle lolli = new Rectangle(HardCodedParameters.lollipopWidth*shrink,
        		HardCodedParameters.lollipopHeight*shrink);
        lolli.setFill(new ImagePattern(new Image("file:src/images/sucette.png")));
        
        lolli.setTranslateX(shrink*p.x);
        lolli.setTranslateY(shrink*p.y);
        panel.getChildren().add(lolli);
      }
    
    if(data.getSnail().exist==true){
        Rectangle es = new Rectangle(100*shrink,
        		100*shrink);
        if(data.getSnail().pick==false)
        	es.setFill(new ImagePattern(new Image("file:src/images/eSCARGOT.png")));
        else
        	es.setFill(new ImagePattern(new Image("file:src/images/EscargotBonbon.png")));
        
        es.setTranslateX(shrink*data.getSnail().p.x);
        es.setTranslateY(shrink*data.getSnail().p.y);
        if(data.getSnail().snaildirection==2){
        	es.setRotationAxis(Rotate.Y_AXIS);
        	es.setRotate(-180);
        }
        panel.getChildren().add(es);
    }
    
    return panel;
  }

  @Override
  public void setMainWindowWidth(double width){
    xShrink=width/defaultMainWidth;
  }
  
  @Override
  public void setMainWindowHeight(double height){
    yShrink=height/defaultMainHeight;
  }
}
