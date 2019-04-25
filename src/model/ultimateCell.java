package model;

import java.util.HashMap;
import java.util.Map;

import controller.UltimateTic_controller;

public class ultimateCell {
	public String[][] array = new String[3][3];
	int x;
	int y;
	int winner = 0;
	int size = 0;
	
	Map<Integer, Player> map = new HashMap<>();
	public ultimateCell(int x, int y, Map<Integer, Player> map) {
		this.x = x;
		this.y = y;
		this.map = map; 
	}
	
	public int setSelection(int row, int col, int currentPlayer) {
		if(winner != 0) {
			return winner;
		}		
		array[row][col] = map.get(currentPlayer).mark;
		size++;
		if(winner != 0) return winner;
		boolean res = check(row, col);
		if(res) winner = currentPlayer;
		if(size == 9 && winner == 0) winner = 3;
		return winner;
	}
	
	private boolean check(int curRow, int curCol) {
		String cur = array[curRow][curCol];
		boolean res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][curCol] != cur) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		for(int i = 0; i < array[0].length; i++) {
			if(array[curRow][i] != cur ) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][i] != cur) res = false;
		}
		if(res) return res;
		res = true;
		for(int i = array.length - 1; i >= 0; i--) {
			if(array[array.length - 1 - i][i] != cur) res = false;
		}
		return res; 
	}
	

	public int determineWinner() {
		return winner;
	}
}