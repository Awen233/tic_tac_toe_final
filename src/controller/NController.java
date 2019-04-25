package controller;

import java.util.*;

import controller.NormalController.ExpireTask;
import javafx.scene.text.Text;
import model.ComputerPlayer;
import model.Player;
import model.Position;
import view.NView;
import view.NormalView;
import view.NormalView.NormalTile;

public class NController {
	public String[][] array;
	public Map<Integer, Player> map;
	public int curPlayer = 1;
	public boolean gameEnd; 
	public int winner = 0;
	public Main_controller callBack;
	int secs;
	int size;
	public Text timeLeft;
	Timer timer;
	boolean timeout;
	int n;
	NView view;
	
	public NController( NView view, int n, Map<Integer, Player> map, Main_controller callBack, int timer) {
		this.map = map;
		this.callBack = callBack;
		this.secs = timer;
		this.view = view;
		array = new String[n][n];
		timeLeft = new Text();
		this.timer = new Timer();
		this.n = n;
	}
	
	public boolean setSelection(int row, int col) {
		if(timeout) {
			callBack.declareTimeout(map.get(curPlayer).username, map.get(curPlayer%2 + 1).username);
			return true;
		}
		if(gameEnd) {
			return true;
		}
		String mark = map.get(curPlayer).mark;
		array[row][col] = mark;
		boolean res = check(row, col, array);
		if(res) {
			winner = curPlayer;
			callBack.declareWinner(winner, timer);
		}
		size++;
		if(size == n*n && winner == 0) {
			winner = 3;
			callBack.declareWinner(winner, timer);
		}
		move();
		return true;
	}
	
	private boolean check(int row, int col, String[][] array) {
		String cur = array[row][col];
		boolean res = true;
		for(int i = 0; i < array.length; i++) {
			if(array[i][col] != cur) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		for(int i = 0; i < array[0].length; i++) {
			if(array[row][i] != cur ) {
				res = false;
			}
		}
		if(res) return res;
		res = true;
		int count = 0;
		int curRow = row;
		int curCol = col;
		while(curRow >= 0 && curCol >= 0 && array[curRow][curCol] == cur) {
			curRow --;
			curCol --;
			count++;
		}
		int n = array.length;
		curRow = row;
		curCol = col;
		int count2 = 0;
		while(curRow < n && curCol < n && array[curRow][curCol] == cur) {
			curRow ++;
			curCol ++;
			count2 ++;
		}
		if(count + count2 - 1 >= n) {
			return true;
		}
		count = 0;
		curRow = row;
		curCol = col;
		while(curRow >= 0 && curCol < n && array[curRow][curCol] == cur) {
			curRow --;
			curCol ++;
			count++;
		}
		count2 = 0;
		curRow = row;
		curCol = col;
		while(curRow < n && curCol >= 0 && array[curRow][curCol] == cur) {
			curRow ++;
			curCol --;
			count2 ++;
		}
		if(count + count2 - 1 >= n) {
			return true;
		}
		return false;
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
		Position p = ComputerPlayer.nextMove(array);
		int temp = curPlayer;
		view.arr[p.x][p.y].set(map.get(curPlayer).mark);
		setSelection(p.x, p.y);
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
		NController callbackClass;
		int x = 10;

		public ExpireTask(NController callbackClass, int time){
			this.callbackClass = callbackClass;
			x = time;
		}

		public void run(){
			x--;
			callbackClass.timeLeft.setText("time left: " + Integer.valueOf(x));
			if(x == 0) {
				callbackClass.timeout();
			}
		}
	}
}
