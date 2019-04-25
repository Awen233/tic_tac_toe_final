package controller;

import java.util.*;


import javafx.scene.text.Text;
import model.*;
import view.MainView;
import view.UltimateView;

public class UltimateTic_controller {

	public ultimateCell[][] array;
	String[][] board = new String[3][3];
	int winner;
	public int secs = 0;
	public int curPlayer = 1;
	public Map<Integer, Player> map;
	int size;
	public Position nextPosition;
	UltimateView view;
	boolean gameEnd;
	public Main_controller callBack;
	int curRow = -1;
	int curCol = -1;
	public Text timeLeft;
	Timer timer;
	boolean timeout;

	public UltimateTic_controller(UltimateView v, Main_controller callBack, Map<Integer, Player> map, int timer) {
		gameEnd = false;
		this.map = map;
		this.view = v;
		this.callBack = callBack;
		startNewGame(2, timer);
	}

	public void startNewGame(int numPlayers, int timeoutInSecs) {
		array = new ultimateCell[3][3];
		setUpCell();
		timer = new Timer();
		secs = timeoutInSecs;
		winner = 0;
		size = 0;
		timeLeft = new Text();
	}

	public void setUpCell() {
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < 3; j++) {
				array[i][j] = new ultimateCell(i, j, map);	
			}
		}
	}


	public boolean setSelection(int tieRow, int tieCol, int row, int col, int currentPlayer) {
		if(timeout) {
			callBack.declareTimeout(map.get(curPlayer).username, map.get(curPlayer%2 + 1).username);
			return true;
		}
		if(curRow != -1 && (curRow != tieRow || curCol != tieCol)) {
			System.out.println("invalid input");
			return false;
		}
		System.out.println("curRow is: " + curRow  + "   " + curCol);
		System.out.println("selected: " + tieRow + "   " + tieCol);
		
		int localwinner = array[tieRow][tieCol].setSelection(row, col, currentPlayer);

		if(localwinner != 0 && board[tieRow][tieCol] == null) {
			board[tieRow][tieCol] = map.get(curPlayer).mark;
			boolean res = check(tieRow, tieCol, board);
			if(res) {
				winner = currentPlayer;
				declareWinner();
				System.out.println("we finally got winner: " + map.get(curPlayer).username);
			} else {
				view.playBoard[tieRow][tieCol].setBackgournd(callBack.map.get(curPlayer).mark);
			}
		}
		view.playBoard[tieRow][tieCol].unsetBorder();
		view.playBoard[row][col].setBorder();
		curRow = row;
		curCol = col;
		move();
		return true;
	}

	public void declareWinner() {
		callBack.declareWinner(winner, timer);
	}
	
	public void move() {
		if(secs != 0) {
			restartTimer();
		}
		
		curPlayer  = curPlayer%2 + 1;
		if(callBack.isPvc && curPlayer == 2) {
			computerPlay();
		}
	}
	
	public void computerPlay() {
		ultimateCell list = array[curRow][curCol];
		Position computerMove = ComputerPlayer.nextMove(list.array);
		int tempr = curRow;
		int tempc = curCol;
 		
		setSelection(curRow, curCol, computerMove.x, computerMove.y, 2);
		Tile t = view.playBoard[tempr][tempc].array[computerMove.x][computerMove.y];
		t.set(map.get(2).mark);
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

	public int determineWinner() {
		return winner;
	}

	public void setView(UltimateView v) {
		view = v;
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
}


class ExpireTask extends TimerTask{
	UltimateTic_controller callbackClass;
	int x = 10;

	public ExpireTask(UltimateTic_controller callbackClass, int time){
		this.callbackClass = callbackClass;
		x = time;
	}


	public void run(){
		x--;
		callbackClass.timeLeft.setText("time left: " + Integer.valueOf(x));;
		
		if(x == 0) {
			callbackClass.timeout();
		}
	}
}
