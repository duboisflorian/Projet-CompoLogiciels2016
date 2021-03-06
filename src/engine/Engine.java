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
import specifications.BulletService;
import specifications.DataService;
import specifications.EnemyService;
import specifications.RequireDataService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.Rectangle;
import javafx.application.Platform;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              ChildStep=HardCodedParameters.ChildStep;
  private Timer engineClock,bulletClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  private double ChildVX,ChildVY;
  
  private Date bulletStartClock,balloonStartClock;
  
  public Engine(){}

  @Override
  public void bindDataService(DataService service){
    data=service;
  }
  
  @Override
  public void init(){
	bulletStartClock=new Date();
	balloonStartClock=new Date();
    engineClock = new Timer();
    bulletClock = new Timer();
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
    	  
    	  // Custom date format
  		  SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS"); 

  		  String d1 = format.format(bulletStartClock);
  		  String d2 = format.format(new Date());
  		  
  		// Get msec from each, and subtract.
  		long diff = 0;
		try {
			diff = format.parse(d2).getTime() - format.parse(d1).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

  		if((double) diff>=HardCodedParameters.bulletPaceMillis){
  			spawnBullet();
  			bulletStartClock=new Date();
  		}
    	  
    	  if(data.getChildScore()>data.getHighscore()){
    			    data.setHighscore(data.getChildScore());
    			}
    	  
    	 if(data.getChildHealth()<=HardCodedParameters.MinHealth)  {
    		 JOptionPane.showMessageDialog(null,"Vous avez perdu !");
      		try {
      			data.setFin(new Date());
      		    SimpleDateFormat formater = new SimpleDateFormat("hh:mm:ss");
      		    System.out.println(formater.format(data.getFin()));
      		    
				WriteXML();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      		
  			stop();
  			Platform.exit();  		
  		}
    	 
    	 
 		  d1 = format.format(balloonStartClock);
 		  d2 = format.format(new Date());
 		  
 		// Get msec from each, and subtract.
 		diff = 0;
		try {
			diff = format.parse(d2).getTime() - format.parse(d1).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	 
        if (((double) diff>=(data.getLevel().invoc *1000)&& data.getLevel().nbpop<data.getLevel().nbEnemy)||data.getLevel().nbpop==0) {
        	balloonStartClock=new Date();
        	data.setLevelnbpop( data.getLevel().nbpop+1);
        	spawnEnemy();
        }
        
        if(data.getLevel().nbkill==data.getLevel().nbEnemy){
        	data.updateLevel();
        }
    	  
        ArrayList<EnemyService> ballon = new ArrayList<EnemyService>();
        ArrayList<BulletService> bullet = new ArrayList<BulletService>();
        ArrayList<Position> lollipop = new ArrayList<Position>();
        
        data.setSoundEffect(Sound.SOUND.None);
        
        updateSpeedChild();
        updateCommandChild();
        updatePositionChild();
        
        for (EnemyService b:data.getEnemy()){
        	move(b);
            if (collisionChildEnemy(b)){
                data.setChildHealth(data.getChildHealth()-1);
                data.setLevelnbkill( data.getLevel().nbkill+1);
                data.setChildScore(data.getChildScore()+1);
                data.setSoundEffect(Sound.SOUND.ChildGotHit);
              	if(data.getChildScore()%5==0) 
            	{
            		spawnlollipop(b);
            	}
              } else {
            	  if(b.getHealth()>0)ballon.add(b);
            	  else{
                      data.setLevelnbkill( data.getLevel().nbkill+1);
                      data.setSoundEffect(Sound.SOUND.EnemyDestroyed);
            	  data.setChildScore(data.getChildScore()+1);
              	if(data.getChildScore()%5==0) 
            	{
            		spawnlollipop(b);
            	}
              }
              }
            }
        
        for (BulletService b:data.getBullet()){
        	Fire(b);
            if (collisionBulletEnemy(b)){
              } else {
            	  if (b.getBulletPosition().y>0) bullet.add(b);
              }
            }
        
        for (Position p:data.getLollipop()){
        	int a=0,b=0;
        	moveLollipop(p);
        	if(data.getSnail().exist==true){
        		moveSnail();
        	}
        		
        	if(p.y>=data.getField().ymax && data.getSnail().exist==false){
        		spawnSnail(p);
        	}
        	
            if (collisionChildLollipop(p)){
            	if(data.getChildHealth()<HardCodedParameters.MaxHealth){
            		data.setChildHealth(data.getChildHealth()+1);
            		data.SnailPick(false);
            		a++;
            	}
              } 
            
            if(data.getSnail().p.y >= data.getField().ymax && collisionSnailLollipop(p)){
            data.SnailPick(true);
            b++;
            }
            
            if(a==0 && b==0){
            	  lollipop.add(p);
              }
            }

        if(data.getSnail().exist==true)
        	moveSnail();
        
        data.setEnemy(ballon);
        data.setBullet(bullet);
        data.setLollipop(lollipop);
        data.setStepNumber(data.getStepNumber()+1);
      }


    },0,HardCodedParameters.enginePaceMillis);
  }

  @Override
  public void stop(){
    engineClock.cancel();
    System.exit(0);
  }

  @Override
  public void setChildCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=true;
    if (c==User.COMMAND.RIGHT) moveRight=true;
    if (c==User.COMMAND.UP) moveUp=true;
    if (c==User.COMMAND.DOWN) moveDown=true;
  }
  
	public boolean collisionBulletEnemy(BulletService b) {
		
		Rectangle rect2 = new Rectangle((int)b.getBulletPosition().x - 20,(int) b.getBulletPosition().y - 20, 20, 20);
		
		for(EnemyService e:data.getEnemy()){

			Rectangle rect1 = new Rectangle((int)e.getPosition().x - 20,(int) e.getPosition().y - 20, 48, 60);
			
		  if (rect1.getX() < rect2.getX() + rect2.getWidth() &&
		     rect1.getX() + rect1.getWidth() > rect2.getX() &&
		     rect1.getY() < rect2.getY() + rect2.getHeight() &&
		     rect1.getHeight() + rect1.getY() > rect2.getY()) {
			  e.setHealth(e.getHealth()-b.getDeg());
		      return true;
		  }
		}
		return false;
	}

	public void Fire(BulletService b) {
		b.setBulletPosition(new Position(b.getBulletPosition().x,b.getBulletPosition().y- HardCodedParameters.bulletStep));
	}
	
	public void spawnBullet() {
	    int x=(int) data.getChildPosition().x-10;
	    int y=(int)data.getChildPosition().y-(HardCodedParameters.ChildHeight/2)-25;
	    data.addBullet(new Position(x,y));
	}
	
	private void spawnlollipop(EnemyService b) {
	    int x=(int) b.getPosition().x;
	    int y=(int)b.getPosition().y;
	    data.addLollipop(new Position(x,y));
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

  public void spawnEnemy(){
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
  
  private void moveLollipop(Position p){
	  if(p.y<data.getField().ymax)
		p.y=p.y+HardCodedParameters.lollipopStep;
	  }
  
  private void moveSnail(){
	 if((data.getSnail().p.x<data.getField().xmax && data.getSnail().snaildirection==1)||(data.getSnail().p.x>data.getField().xmin && data.getSnail().snaildirection==2)){
		  if(data.getSnail().snaildirection==1)
		data.setSnail(true, new Position(data.getSnail().p.x+10,data.getSnail().p.y));
		  if(data.getSnail().snaildirection==2)
		data.setSnail(true, new Position(data.getSnail().p.x-10,data.getSnail().p.y));  
	  } else{
		  data.setSnail(false, data.getSnail().p);
		  data.SnailPick(false);	
	  	}
	  
	  }

  private void spawnSnail(Position p){
	  if(p.x>HardCodedParameters.defaultWidth/2){
		data.setSnail(true, new Position(0,data.getField().ymax));
		data.setSnailDirection(1);
	  } else{
		  data.setSnail(true, new Position(data.getField().xmax-10,data.getField().ymax)); 
		  data.setSnailDirection(2);
	  }
	  }  
  
  private boolean collisionChildEnemy(EnemyService e){
	  Rectangle rect1 = new Rectangle((int)data.getChildPosition().x - 75,(int) data.getChildPosition().y - 100, 75, 100);
	  Rectangle rect2 = new Rectangle((int)e.getPosition().x - 20,(int) e.getPosition().y - 20, 20, 20);


	  if (rect1.getX() < rect2.getX() + rect2.getWidth() &&
	     rect1.getX() + rect1.getWidth() > rect2.getX() &&
	     rect1.getY() < rect2.getY() + rect2.getHeight() &&
	     rect1.getHeight() + rect1.getY() > rect2.getY()) {
	      return true;
	  }else{
		  return false;
	  }
  }
  
  private boolean collisionChildLollipop(Position p){
	  Rectangle rect1 = new Rectangle((int)data.getChildPosition().x - 75,(int) data.getChildPosition().y - 100, 75, 100);
	  Rectangle rect2 = new Rectangle((int)p.x - 20,(int) p.y - 20, 20, 20);


	  if (rect1.getX() < rect2.getX() + rect2.getWidth() &&
	     rect1.getX() + rect1.getWidth() > rect2.getX() &&
	     rect1.getY() < rect2.getY() + rect2.getHeight() &&
	     rect1.getHeight() + rect1.getY() > rect2.getY() && data.getChildHealth()<HardCodedParameters.MaxHealth) {
	      return true;
	  }else{
		  return false;
	  }
  }
  
	private boolean collisionSnailLollipop(Position po) {
		  Rectangle rect1 = new Rectangle((int)data.getSnail().p.x - 50,(int) data.getSnail().p.y-100, 100, 100);
		  Rectangle rect2 = new Rectangle((int)po.x - 20,(int) po.y - 20, 20, 20);


		  if (rect1.getX() < rect2.getX() + rect2.getWidth() &&
		     rect1.getX() + rect1.getWidth() > rect2.getX() &&
		     rect1.getY() < rect2.getY() + rect2.getHeight() &&
		     rect1.getHeight() + rect1.getY() > rect2.getY()) {
		      return true;
		  }else{
			  return false;
		  }
	}
  private void WriteXML() throws TransformerException{
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setIgnoringElementContentWhitespace(true);
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        File fileXML = new File("src/backoffice/jeu.xml");
	        Document xml;
	        
	        xml = builder.parse(fileXML);
	        Element root = xml.getDocumentElement();
	        
	        NodeList nodes = root.getChildNodes();
	        
	        //NB PARTIES
	        Node parties = nodes.item(3);
	        
	        NodeList listesparties = parties.getChildNodes();
	        Node nbparties = listesparties.item(1);
	        int nb = Integer.parseInt(nbparties.getTextContent());
	        nb++;
	        nbparties.setTextContent(Integer.toString(nb));
	        
	        
	        
	        //Temps total
	        
      		// Custom date format
    		  SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss"); 

    		  String d1 = format.format(data.getDebut());
    		  String d2 = format.format(data.getFin());
    		  
    		// Get msec from each, and subtract.
    		long diff = format.parse(d2).getTime() - format.parse(d1).getTime();
    		long diffSeconds = diff / 1000;
    		
	        parties = nodes.item(3);
	        
	        listesparties = parties.getChildNodes();
	        Node tts = listesparties.item(3);

	        int tempst = Integer.parseInt(tts.getTextContent()) + (int) diffSeconds;
	        tts.setTextContent(Integer.toString(tempst));
	        
	        
	        //Temps moyen
	        parties = nodes.item(3);
	        
	        listesparties = parties.getChildNodes();
	        Node tm = listesparties.item(5);

	        int tempsm = Integer.parseInt(tts.getTextContent()) / Integer.parseInt(nbparties.getTextContent());
	        tm.setTextContent(Integer.toString(tempsm));
	        
	        //TOP SCORE
	        Node classement = nodes.item(1);
	        
	        NodeList numeros = classement.getChildNodes();
	        Node numero1 = numeros.item(1);	  
	        Node numero2 = numeros.item(3);
	        Node numero3 = numeros.item(5);
	        
	        if(data.getChildScore()>Integer.parseInt(numero1.getChildNodes().item(3).getTextContent())){
	        	JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
	        	String nom = jop.showInputDialog(null, "Veuillez entrer votre pseudo", "Classement n 1 !", JOptionPane.QUESTION_MESSAGE);
	        	
		        numero3.getChildNodes().item(3).setTextContent(numero2.getChildNodes().item(3).getTextContent());
		        numero2.getChildNodes().item(3).setTextContent(numero1.getChildNodes().item(3).getTextContent());
		        numero1.getChildNodes().item(3).setTextContent(Integer.toString(data.getChildScore()));
		        
		        numero3.getChildNodes().item(1).setTextContent(numero2.getChildNodes().item(1).getTextContent());
		        numero2.getChildNodes().item(1).setTextContent(numero1.getChildNodes().item(1).getTextContent());
		        numero1.getChildNodes().item(1).setTextContent(nom);
	        }
	        else if(data.getChildScore()>Integer.parseInt(numero2.getChildNodes().item(3).getTextContent())){
	        	JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
	        	String nom = jop.showInputDialog(null, "Veuillez entrer votre pseudo", "Classement n 2 !", JOptionPane.QUESTION_MESSAGE);
	        	
		        numero3.getChildNodes().item(3).setTextContent(numero2.getChildNodes().item(3).getTextContent());
		        numero2.getChildNodes().item(3).setTextContent(Integer.toString(data.getChildScore()));
		        
		        numero3.getChildNodes().item(1).setTextContent(numero2.getChildNodes().item(1).getTextContent());
		        numero2.getChildNodes().item(1).setTextContent(nom);
	        }
	        
	        else if(data.getChildScore()>Integer.parseInt(numero3.getChildNodes().item(3).getTextContent())){
	        	JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
	        	String nom = jop.showInputDialog(null, "Veuillez entrer votre pseudo", "Classement n 3 !", JOptionPane.QUESTION_MESSAGE);
	        	
		        numero3.getChildNodes().item(3).setTextContent(Integer.toString(data.getChildScore()));
		        
		        numero3.getChildNodes().item(1).setTextContent(nom);
	        }
	       
	        //for output
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(xml);
            
            //write to file
            StreamResult file = new StreamResult(new File("src/backoffice/jeu.xml"));
 
            transformer.transform(source, file);
            System.out.println("DONE");
	        
	     } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	     } catch (SAXException e) {
	        e.printStackTrace();
	     } catch (IOException e) {
	        e.printStackTrace();
	     } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} 
  
}