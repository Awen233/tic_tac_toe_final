package model;

import java.io.File;

import controller.UltimateTic_controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.MainView;

public class UltimateTile {
	public Tile[][] array;
	int x;
	int y;
	UltimateTic_controller controller;
	GridPane pane;
	int localWinner = 0;


	public void setPane(GridPane pane) {
		this.pane = pane;
	}

	public UltimateTile(int x, int y, UltimateTic_controller controller) {
		this.x = x; 
		this.y = y;
		array = new Tile[3][3];
		this.controller = controller;
		createTile();
	}

	public void createTile() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				array[i][j] = new Tile(i, j, this);
			}
		}
	}
	

	public void setBackgournd(String x) {
		if(localWinner == 3) {
			pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
			return;
		}
		WritableImage image =  textToImage(x);
		BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, false) );
		pane.setBackground(new Background(bImage));
	}
	
	public void setBorder() {
		pane.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));;
	}
	
	public void unsetBorder() {
		pane.setBorder(null);
	}

	private WritableImage textToImage(String text) {
		Text t = new Text(text);
		t.setFont(new Font(160));
		t.setFill(Color.AQUAMARINE); 
		return t.snapshot(null, null);
	}

}
