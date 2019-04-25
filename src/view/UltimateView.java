package view;

import controller.Main_controller;
import controller.UltimateTic_controller;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Records;
import model.Tile;
import model.UltimateTile;

public class UltimateView {
	
	public UltimateTile[][] playBoard = new UltimateTile[3][3];
	MainView v;
	UltimateTic_controller controller;
	
	public UltimateView() {
		v = MainView.getInstance(); 
	}
	
	public void setupPlayerBoard() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				System.out.println(i + " " + j);
				playBoard[i][j] = new UltimateTile(i, j, controller);
			}
		}
	}
	
	
	public void showStage() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		gridPane.setVgap(10); //vertical gap in pixels
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				gridPane.add(getlocalScene(playBoard[i][j]), i, j);
			}
		}
		if(controller.secs != 0) {
			gridPane.add(controller.timeLeft, 0, 3);
		}
		Scene scene = new Scene(gridPane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}
	
	public GridPane getlocalScene(UltimateTile board) {
		GridPane gridPane = new GridPane();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Tile cur = board.array[i][j];
				gridPane.add(cur, i, j);
			}
		}
		board.setPane(gridPane);
		return gridPane;
	}

	public void setController(UltimateTic_controller controller) {
		this.controller = controller;
		setupPlayerBoard();
	}


}
