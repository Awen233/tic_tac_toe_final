package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.text.Text;
import model.ComputerPlayer;
import model.Player;
import model.Position;
import view.NormalView;
import view.NormalView.NormalTile;
import view.ThreeDView;


public class NormalController {
	public String[][] array;
	public Map<Integer, Player> map;
	public int curPlayer = 1;
	public boolean gameEnd; 
	public int winner = 0;
	public Main_controller callBack;
	NormalView view;
	int size = 0;
	public Text timeLeft;
	Timer timer;
	boolean timeout;
	int secs;
	
	public NormalController(NormalView view, Map<Integer, Player> map, Main_controller callBack, int timer){
		this.map = map;
		this.view = view;
		gameEnd = false;
		this.callBack = callBack;
		array = new String[3][3];
		secs = timer;
		this.timer = new Timer();
		timeLeft = new Text();
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
		if(size == 9 && winner == 0) {
			winner = 3;
			callBack.declareWinner(winner, timer);
		}
		move();
		return true;
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
		NormalTile t = view.arr[p.x][p.y];
		t.set(map.get(curPlayer).mark);
		setSelection(p.x, p.y);
	}
	
	public int determineWinner() {
		return winner;
	}
	
	private boolean check(int curRow, int curCol, String[][] array) {
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
		NormalController callbackClass;
		int x = 10;

		public ExpireTask(NormalController callbackClass, int time){
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

