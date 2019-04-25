package controller;

import java.util.*;

import javafx.scene.text.Text;
import model.ComputerPlayer;
import model.Player;
import model.Position;
import model.Tile;
import model.ultimateCell;
import view.ThreeDView;

public class tic_3dController {
	
	String[][][] board = new String[3][3][3];
	public Map<Integer, Player> map;
	public int curPlayer = 1;
	public boolean gameEnd; 
	public int winner = 0;
	public Main_controller callBack;
	ThreeDView view;
	public Text timeLeft;
	Timer timer;
	boolean timeout;
	int secs;
	
	public tic_3dController(ThreeDView view, Map<Integer, Player> map, Main_controller callBack, int timer) {
		secs = timer;
		this.map = map;
		this.view = view;
		gameEnd = false;
		this.callBack = callBack;
		timeLeft = new Text();
		this.timer = new Timer();
		setUp();
	}
	
	private void setUp() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int z = 0; z < 3; z++) {
					board[i][j][z] = "";
				}
			}
		}
	}
	
	
	public void setSelection(int x, int y, int z) {
		if(timeout) {
			callBack.declareTimeout(map.get(curPlayer).username, map.get(curPlayer%2 + 1).username);
			return;
		}
		
		String mark = map.get(curPlayer).mark;
		System.out.println("mark is: " + mark);
		board[x][y][z] = mark;
		boolean res = check(x, y, z);
		if(res) {
			System.out.println("we got res");
			winner = curPlayer;
			declareWinner();
		}
		move();
	}
	
	public void declareWinner() {
		callBack.declareWinner(winner, timer);
	}
	
	public void move() {
		if(secs != 0) {
			restartTimer();
		}
		
		
		curPlayer = curPlayer %2 + 1;
		if(callBack.isPvc && curPlayer == 2) {
			computerPlay();
		}
	}
	
	
	public void computerPlay() {
		view.ThreeDView.Tile t = view.getRandomTile();
		int temp = curPlayer;
		setSelection(t.x, t.y, t.z);
		t.set(map.get(temp).mark);
	}
	
	
	
	public boolean check(int x, int y, int z) {
		String[][] array = board[x];
		boolean res = check(y, z, array);
		if(res) return res;
		array = makeY(board, y);
		res = check(x, z, array);
		if(res) return res;
		array = makeZ(board, z);
		res = check(x, y, array);
		if(res) return res;
		
		if(x != 1 && y != 1 && z != 1) {
			res = true;
			String cur = board[x][y][z];
			for(int i = 0; i < 3; i++) {
				System.out.println(board[i][i][i]);
				if(board[i][i][i].compareTo(cur) != 0) {
					res = false;
					break;
				}
			}
			if(res) return res;
			res = true;
			for(int i = 0; i < 3; i++) {
				if(board[2 - i][0 + i][0 + i].compareTo(cur) != 0) {
					res = false;
					break;
				}
			}
			if(res) return res;
			
		}
		String cur = board[x][y][z];
	
		if(y == 2 && x == 0 && z == 0 || y == 1 && x == 1 && z == 1 || y == 0 && x == 2 && z == 2 ) {
			res = true;
			for(int i = 0; i < 3; i++) {
				if(board[0 + i][2 - i][0 + i]  != cur ) {
					res = false;
				}
			}			
		}

		return res; 
	}
	
	private String[][] makeZ(String[][][] board, int z) {
		String[][] array = new String[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				array[i][j] = board[i][j][z];
			}
		}
		return array;
	}

	private String[][] makeY(String[][][] board, int y){
		String[][] array = new String[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				array[i][j] = board[i][y][j];
			}
		}
		return array;
	}
	
	private boolean check(int curRow, int curCol, String[][] array) {
		String cur = array[curRow][curCol];
		boolean res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][curCol] != cur) {
				res = false;
			}
		}
//		System.out.println("1");
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
	
	public void print() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int z = 0; z < 3; z++) {
					System.out.println();
				}
			}
		}
	}
	
	public void restartTimer() {
		timer.cancel();
		timer = new Timer();
		timer.schedule(new ExpireTask(this, secs), 1000, 1000);
	}
	
	public void timeout() {
		timeout = true;
		timer.cancel();
	}
	
	class ExpireTask extends TimerTask{
		tic_3dController callbackClass;
		int x = 10;

		public ExpireTask(tic_3dController callbackClass, int time){
			this.callbackClass = callbackClass;
			x = time;
		}


		public void run(){
			x--;
			callbackClass.timeLeft.setText("" + Integer.valueOf(x));;
			
			if(x == 0) {
				callbackClass.timeout();
			}
		}
	}
}
