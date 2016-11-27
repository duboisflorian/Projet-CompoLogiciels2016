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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import data.ia.MoveEnemy;

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
	
	Text highscoretxt = new Text(shrink*(defaultMainWidth+160),
	     shrink*100,
	     "Highscore : " + data.getHighscore());
	highscoretxt.setFont(new Font(.04*shrink*defaultMainHeight));
	highscoretxt.setFill(Color.WHITE);
	
	Text nbpartie = new Text(shrink*(defaultMainWidth+160),
		     shrink*150,
		     "Nb de partie jouee : " + data.getnbparties());
	nbpartie.setFont(new Font(.04*shrink*defaultMainHeight));
	nbpartie.setFill(Color.WHITE);
	
	Text TempsTotal = new Text(shrink*(defaultMainWidth+160),
		     shrink*200,
		     "Temps total joué : " + data.getTempsTotal() + " minutes");
	TempsTotal.setFont(new Font(.04*shrink*defaultMainHeight));
	TempsTotal.setFill(Color.WHITE);
	
	Text TempsMoyen = new Text(shrink*(defaultMainWidth+160),
		     shrink*250,
		     "Temps moyen : " + data.getTempsMoyen() + " minutes");
	TempsMoyen.setFont(new Font(.04*shrink*defaultMainHeight));
	TempsMoyen.setFill(Color.WHITE);
	
    //Yucky hard-conding
    Rectangle background = new Rectangle(defaultMainWidth*shrink+150,
                                  defaultMainHeight*shrink);
    background.setFill(new ImagePattern(Field));
    
    // Partie2
    Rectangle partie2 = new Rectangle(defaultMainWidth*shrink+150,
            defaultMainHeight*shrink);
    partie2.setFill(Color.BLACK);
    partie2.setTranslateX((defaultMainWidth+155)*shrink);
    
    // separation
    Rectangle sep = new Rectangle(defaultMainWidth*shrink+150,
            10*shrink);
    sep.setFill(Color.WHITE);
    sep.setTranslateX((defaultMainWidth+155)*shrink);
    sep.setTranslateY((defaultMainHeight/2.4)*shrink);
    
    // Partie3
    Rectangle partie3 = new Rectangle(defaultMainWidth*shrink+150,
            defaultMainHeight*shrink);
    partie3.setFill(Color.BLACK);
    partie3.setTranslateX((defaultMainWidth+155)*shrink);
    partie3.setTranslateY((defaultMainHeight/2)*shrink);
    

    //ballon par seconde
	Text invocballon = new Text(shrink*(defaultMainWidth+160),
		     shrink*450,
		     "Ballons : tous les " + data.getLevel().invoc +"s");
	invocballon.setFont(new Font(.04*shrink*defaultMainHeight));
	invocballon.setFill(Color.WHITE);
	

    //Flechette par seconde
	Text flechettepars = new Text(shrink*(defaultMainWidth+160),
		     shrink*500,
		     "Flechettes : tous les " + HardCodedParameters.bulletPaceMillis +"ms");
	flechettepars.setFont(new Font(.04*shrink*defaultMainHeight));
	flechettepars.setFill(Color.WHITE);
	
	
    //couleur bleu
	Text couleurb = new Text(shrink*(defaultMainWidth+160),
		     shrink*600,
		     "Ballon bleu ( " + MoveEnemy.b +" ) ");
	couleurb.setFont(new Font(.04*shrink*defaultMainHeight));
	couleurb.setFill(Color.WHITE);
	
    //couleur rouge
	Text couleurr = new Text(shrink*(defaultMainWidth+160),
		     shrink*650,
		     "Ballon rouge ( " + MoveEnemy.r +" ) ");
	couleurr.setFont(new Font(.04*shrink*defaultMainHeight));
	couleurr.setFill(Color.WHITE);
	
    //couleur vert
	Text couleurv = new Text(shrink*(defaultMainWidth+160),
		     shrink*700,
		     "Ballon vert ( " + MoveEnemy.g +" ) ");
	couleurv.setFont(new Font(.04*shrink*defaultMainHeight));
	couleurv.setFill(Color.WHITE);
	
    //couleur jaune
	Text couleurj = new Text(shrink*(defaultMainWidth+160),
		     shrink*750,
		     "Ballon jaune ( " + MoveEnemy.y +" ) ");
	couleurj.setFont(new Font(.04*shrink*defaultMainHeight));
	couleurj.setFill(Color.WHITE);
    
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
    panel.getChildren().addAll(background,partie2,partie3,sep,level,Score,ChildAvatar,highscoretxt,nbpartie,TempsTotal,TempsMoyen,invocballon,flechettepars,couleurb,couleurr,couleurj,couleurv);

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


