package view;

import controller.NController;
import controller.NormalController;
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
import view.NormalView.NormalTile;

public class NView {
	NController controller;
	public NTile[][] arr;
	int n;

	public void setController(NController control3, int n) {
		controller = control3;
		this.n = n;
		arr = new NTile[n][n];
		setArray();
	}

	public void setArray() {
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				arr[i][j] = new NTile(i, j, controller);
			}
		}
	}

	public void showStage() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		gridPane.setVgap(10); //vertical gap in pixels
		gridPane.setPadding(new Insets(10, 10, 10, 10)); 

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				gridPane.add(arr[i][j], i, j);
			}
		}
		System.out.println(controller);
		gridPane.add(controller.timeLeft , 0, n);
		Scene scene = new Scene(gridPane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();	
	}

	public class NTile extends StackPane {
		Text text = new Text();
		public int x;
		public int y;
		boolean addBefore;
		NController controller;

		public NTile(int i, int j, NController controller) {
			x = i;
			y = j;
			this.controller = controller;
			Rectangle border = new Rectangle(60,60);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			text.setFont(Font.font(32));
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> {
				if(controller.callBack.gameEnd) {
					return;
				}
				
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
