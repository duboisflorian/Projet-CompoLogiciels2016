package data;

import tools.Position;

public class Child {
	private Position position;
	private int Health, Score;
	
	public Child(Position p){
		setScore(0);
		setHealth(3);
		setPosition(p);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getHealth() {
		return Health;
	}

	public void setHealth(int health) {
		Health = health;
	}

	public int getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}
}
