package model;

public abstract class Player {
	public String mark;
	public String username;
	public int curRow;
	public int curCol;
	
	public Player(String mark, String name) {
		this.mark  =mark;
		this.username = name;
	}
}