package model;

import java.io.Serializable;

public class Records implements Serializable{
	public int win = 0;
	public int loss = 0;
	public String mark = "X";
	
	public Records(int x, int y, String z) {
		win = x;
		loss = y;
		mark = z;
	}
}