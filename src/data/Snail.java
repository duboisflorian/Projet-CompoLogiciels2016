package data;

import tools.Position;

public class Snail {

	public Position p;
	public boolean exist;
	public boolean pick;
	public int snaildirection;
	
	public Snail(){
		exist=false;
		p=new Position(0,0);
		pick = false;
		snaildirection=0;
	}
	
	
}
