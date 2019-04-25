package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import controller.NormalController;
import controller.tic_3dController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.ThreeDView.Tile;


public class NormalView {
	NormalController controller;
	public NormalTile[][] arr;

	public void setController(NormalController control3) {
		controller = control3;
		arr = new NormalTile[3][3];
		setArray();
	}

	public void setArray() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				arr[i][j] = new NormalTile(i, j, controller);
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
				gridPane.add(arr[i][j], i, j);
			}
		}
		gridPane.add( controller.timeLeft , 0, 3);
		Scene scene = new Scene(gridPane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();	
	}



	public class NormalTile extends StackPane {
		Text text = new Text();
		public int x;
		public int y;
		public int z;
		boolean addBefore;
		NormalController controller;

		public NormalTile(int i, int j, NormalController controller) {
			x = i;
			y = j;
			this.controller = controller;
			Rectangle border = new Rectangle(80,80);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			text.setFont(Font.font(32));
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> {
				if(controller.callBack.gameEnd) {
					return;
				}
				
				
				System.out.println(controller);
				if(addBefore || controller.gameEnd ) {
					return;
				}
				addBefore = true;
				text.setText(controller.map.get(controller.curPlayer).mark);				
				controller.setSelection(x, y);
			});
		}

		public void set(String x) {
			addBefore = true;
			text.setText(x);
		}
	}
}
