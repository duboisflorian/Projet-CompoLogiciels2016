package data;

import tools.Position;

public class Lollipop {
	  private Position p;
	  private boolean exist ;
	  
public Lollipop(){
	setExist(false);
}

public boolean isExist() {
	return exist;
}

public void setExist(boolean exist) {
	this.exist = exist;
}

public Position getP() {
	return p;
}

public void setP(Position p) {
	this.p = p;
}
	  
}
