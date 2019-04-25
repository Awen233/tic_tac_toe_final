package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

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

public class ThreeDView {

	public Tile[][][] board;

	tic_3dController controller;
	public void setController(tic_3dController control3) {
		controller = control3;
		board = new Tile[3][3][3];
		getBoard();
	}

	public void showStage() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		gridPane.setVgap(10); //vertical gap in pixels
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 
		
		for(int i = 0; i < 3; i++) {
			board[i] = createLevelBoard(i);
			GridPane pane = createPane(board[i]);
			gridPane.add(pane, 0, i);
		}
		gridPane.add(new Text("time left: "), 0, 3);
		gridPane.add(controller.timeLeft, 0, 4);
		Scene scene = new Scene(gridPane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();	
	}

	private GridPane createPane(Tile[][] tiles) {
		GridPane pane = new GridPane(); 
		pane.setMinWidth(300);
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				pane.add(tiles[i][j], j, i);
			}
		}
		return pane;
	}

	public Tile[][] createLevelBoard(int z) {
		Tile[][] arr = new Tile[3][3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				arr[i][j] = new Tile(i, j, z);
			}
		}
		return arr;
	}
	
	public void getBoard() {
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int z = 0; z < 3; z++) {
					board[i][j][z] = new Tile(i,j,z); 
				}
			}
		}
	}
	
	public Tile getRandomTile() {
		List<Tile> lis = new ArrayList<>();
		for(Tile[][] matr : board) {
			for(Tile[] arr : matr) {
				for(Tile t : arr) {
					if(t.addBefore == false) {
						lis.add(t);
					}
				}
			}
		}
		Random r = new Random();
		int choice = r.nextInt(lis.size());
		return lis.get(choice);	
	}

	public class Tile extends StackPane{
		Text text = new Text();
		public int x;
		public int y;
		public int z;
		boolean addBefore;

		public Tile(int i, int j, int z) {
			x = i;
			y = j;
			this.z = z;
			Rectangle border = new Rectangle(50,50);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			text.setFont(Font.font(32));
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> {
				if(controller.callBack.gameEnd) {
					return;
				}
				
				if(addBefore) {
					return;
				}
				addBefore = true;
				System.out.println("x: " + x + "\t" + "y: " + y + "\t z:" + this.z);
				text.setText(controller.map.get(controller.curPlayer).mark);				
				controller.setSelection(x, y, z);
			});
		}
		
		public void set(String x) {
			addBefore = true;
			text.setText(x);
		}
	}
}



