/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
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
  private Sound.SOUND sound;
  private ArrayList<Position> lollipop;
  private int highscore;
  private Snail snail;
  public Data(){}

  @Override
  public void init(){
    child = new Child(new Position(HardCodedParameters.ChildStartX,HardCodedParameters.ChildStartY));
    stepNumber = 0;
    field = new Field(HardCodedParameters.ChildWidth/2.6,HardCodedParameters.defaultWidth+120-(HardCodedParameters.ChildWidth/2.5),HardCodedParameters.ChildHeight/1.5,HardCodedParameters.defaultHeight-250);
    ballon = new ArrayList<EnemyService>();
    bullet = new ArrayList<BulletService>();
    level=new Level(1,10,100,2);
    sound = Sound.SOUND.None;
    lollipop = new ArrayList<Position>();
    snail=new Snail();
    highscore=getXMLHightscore();
 
  }

  private int getXMLHightscore() {
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setIgnoringElementContentWhitespace(true);
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        File fileXML = new File("src/backoffice/jeu.xml");
	        Document xml;
	        
	        xml = builder.parse(fileXML);
	        Element root = xml.getDocumentElement();
	        
	        NodeList nodes = root.getChildNodes();
	        Node n = nodes.item(1);
	           
	        NodeList classement = n.getChildNodes()  ;
	        Node class1 = classement.item(1);
	        
	        NodeList class1balises = class1.getChildNodes();
	           Node n1 = class1balises.item(3);
	           return Integer.parseInt(n1.getTextContent()); 
	     } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	     } catch (SAXException e) {
	        e.printStackTrace();
	     } catch (IOException e) {
	        e.printStackTrace();
	     }
	return 0;
}

@Override
  public Position getChildPosition(){ return child.getPosition(); }
  
  @Override
  public Field getField(){ return field; }
  
  @Override
  public Snail getSnail(){ return snail; }
  
  @Override
  public void setSnail(boolean b,Position p){  snail.p=p; snail.exist=b; }
  
  @Override
  public void SnailPick(boolean b){
	  snail.pick=b;
  }

  @Override
  public int getStepNumber(){ return stepNumber; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }

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
	  public ArrayList<Position> getLollipop(){ return lollipop; }
	  
	  @Override
	  public void addEnemy(Position p) { ballon.add(new MoveEnemy(p,level.healthEnemy)); }
	  
	  @Override
	  public void addBullet(Position p) { bullet.add(new Bullet(p)); }
	  
	  @Override
	  public void addLollipop(Position p) { lollipop.add(p); }
	  
	  @Override
	  public void setEnemy(ArrayList<EnemyService> enemy) { this.ballon=enemy; }
	  
	  @Override
	  public void setLollipop(ArrayList<Position> l) { this.lollipop=l; }
	  
	  @Override
	  public void setBullet(ArrayList<BulletService> bu) { this.bullet=bu; }
	  
	  @Override
	  public Level getLevel(){ return level; }
	  
	  @Override
	  public void updateLevel(){ 
		  level.update();
	  }
	  
	  @Override
	  public void setSnailDirection(int i){ 
		  snail.snaildirection=i;
	  }

	@Override
	public int getHighscore() {
		return highscore;
	}
	@Override
	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}

}
