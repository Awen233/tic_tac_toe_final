package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import model.HumanPlayer;
import model.Player;
import model.Records;
import model.ultimateCell;
import view.MainView;

public class Main_controller {
	public Map<String, Records> records;
	public Map<Integer, Player> map;
	public boolean isPvP = true;
	int curPlayer;
	MainView v;
	boolean isPvc;
	public boolean gameEnd;
	
	public Main_controller() {
		getInfo();
		map = new HashMap<>();
	}
	
	public void setMainView(MainView v) {
		this.v = v;
	}
	
	public void startNewGame(int numPlayers, int timeoutInSecs) {

		
	}
	
	public void createPlayer(String username, String marker, int playerNum) {
		if(playerNum != 2 && playerNum != 1) {
			return;
		} 
		if(username == null && marker == null) {
			isPvc = true;
			map.put(2, new HumanPlayer("X", "Computer Player"));
			return;
		}
		map.put(playerNum, new HumanPlayer(marker, username));
	}
	
	private void getInfo() {
		try {
			FileInputStream fis = new FileInputStream("hashmap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			records = (Map<String, Records>) ois.readObject();
			fis.close();
			ois.close();
			System.out.println("Serialized HashMap data is retrieved in hashmap.ser");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void storeInfo() {
		try {
			FileOutputStream fos = new FileOutputStream("hashmap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(records);
			oos.close();
			fos.close();
			System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void addInfo(String username, Records r) {
		records.put(username, r);
		storeInfo();
	}

	public Map<String, Records> getRecords() {
		if(records.size() == 0) {
			System.out.println("getRecords ?");
			getInfo();
		}
		return records;
	}
	
	public void declareWinner(int winner, Timer timer) {
		timer.cancel();
		if(winner != 3) {
			String username = map.get(winner).username;
			String loser = map.get(winner % 2 + 1).username;
			if(records.get(username) != null) {
				records.get(username).win++; 
			} else {
				records.put(username, new Records( 1 , 0, "X")); 
			}
			if(records.get(loser) != null) {
				records.get(loser).loss++; 
			} else {
				records.put(loser, new Records( 0, 1, "X")); 
			}
			storeInfo();
			v.declareWinner(winner);
		}
		if(winner == 3) {
			v.declareWinner(winner);
		}
		
		
	}

	public void declareTimeout(String loser, String winner) {
		if(records.get(winner) != null) {
			records.get(winner).win++; 
		} else {
			records.put(winner, new Records( 1 , 0, "X")); 
		}
		if(records.get(loser) != null) {
			records.get(loser).loss++; 
		} else {
			records.put(loser, new Records( 0, 1, "X")); 
		}
		storeInfo();
		v.declareTimeOut(loser, winner);
	}
}
