package model;

import java.util.Timer;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane{
	Text text = new Text();
	int x;
	int y;
	UltimateTile parentTile;
	boolean addBefore = false;
	
	public Tile(int x, int y, UltimateTile t) {
		Rectangle border = new Rectangle(40,40);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		text.setFont(Font.font(32));
		setAlignment(Pos.CENTER);
		getChildren().addAll(border, text);
		parentTile = t;
		setOnMouseClicked(event -> {
			if(parentTile.controller.nextPosition != null && (parentTile.controller.nextPosition.x != parentTile.x || 
					parentTile.controller.nextPosition.y != parentTile.y)) {
				return;
			}
			if(addBefore) {
				return;
			}
			if(parentTile.controller.determineWinner() != 0) {
				System.out.println("already have winner");
				return;
			}
			System.out.println(parentTile.controller.map);
			System.out.println(parentTile.controller.curPlayer);
			String mark = parentTile.controller.map.get(parentTile.controller.curPlayer).mark;
			boolean res = parentTile.controller.setSelection(parentTile.x, parentTile.y, x, y, parentTile.controller.curPlayer);
			if(res) {
				text.setText(mark);	
			} else {
				return;
			}
			addBefore = true;
		});
	}
	public void set(String s) {
		addBefore = true; 
		text.setText(s);
	}
}