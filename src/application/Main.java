package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView mView = MainView.getInstance();
			Scene scene = mView.getMainScene();
			primaryStage.setTitle("utimate tic tac toe - Zhaowen Ding");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}