package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import controller.Main_controller;
import controller.NController;
import controller.NormalController;
import controller.UltimateTic_controller;
import controller.tic_3dController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ComputerPlayer;
import model.Player;
import model.Records;
import model.Tile;
import model.UltimateTile;


public class MainView {
	private BorderPane root = new BorderPane();
	private Scene scene;
	private Text statusNode;
	private final int windowWidth = 800;
	private final int windowHeight = 600;
	private static MainView instance;
	Main_controller controller;
	UltimateTic_controller control;
	Timer timer;
	boolean timeLimit;
	Text timeLeft;
	GridPane ulPane;

	private MainView() {
		controller = new Main_controller();
		this.scene = new Scene(this.root, 800.0D, 600.0D);
		this.statusNode = new Text("no status");
		this.statusNode.setFill(Color.YELLOW);
		this.root.setTop(this.buildSetupPane());
		this.root.setRight(getPlayerPane());
		controller.setMainView(this);
	}

	public static MainView getInstance() {
		if(instance == null) {
			instance = new MainView();
		}
		return instance;
	}

	public Scene getMainScene() {
		return this.scene;
	}

	public GridPane buildSetupPane() {
		Text username = new Text("username:");  
		Text marker = new Text("marker: ");       
		TextField usernameField = new TextField();
		TextField markerField = new TextField();
		Button button1 = new Button("Submit"); 
		Line line = new Line();
		line.setStartX(0.0f); 
		line.setStartY(0.0f);         
		line.setEndX((float) windowWidth); 
		line.setEndY(0.0f);

		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				String username = usernameField.getText();
				String marker = markerField.getText();
				controller.addInfo(username,  new Records(0, 0, marker));
				updatePlayerPane();
			}
		}; 
		button1.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);  
		GridPane gridPane = new GridPane();    
		gridPane.setMinSize(windowWidth, (int) windowHeight/4); 
		gridPane.setPadding(new Insets(10, 10, 10, 10));  
		gridPane.setVgap(5); 
		gridPane.setHgap(5);   
		gridPane.setAlignment(Pos.CENTER); 
		gridPane.add(username, 0, 0); 
		//TODO #3: Remove comment so that the label will show
		gridPane.add(marker, 1, 0); 

		gridPane.add(usernameField, 0, 1); 

		//TODO #4: Add the text field for the default value
		gridPane.add(markerField, 1, 1); 
		gridPane.add(button1, 2, 1); 
		gridPane.add(line, 0, 2, 3, 1); 
		gridPane.add(new Text("select mode"), 3, 1);
		ChoiceBox<String> mode = new ChoiceBox<>();
		mode.getItems().addAll("PvP", "PvC");
		mode.setValue("PvP");
		gridPane.add(mode, 4, 1);
		Button submit = new Button("confirm selection");

		Button exit = new Button("exit game");
		EventHandler<MouseEvent> exitHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				System.exit(3);
			}
		}; 
		exit.addEventFilter(MouseEvent.MOUSE_CLICKED, exitHandler);
		EventHandler<MouseEvent> submitHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				String choice = mode.getValue();
				if(choice.compareTo("PvP") == 0) {
					controller.isPvP = true;
				} else {
					controller.isPvP = false;
				}
				root.setLeft(selectPlayerPane());
			}
		};  
		submit.addEventHandler(MouseEvent.MOUSE_CLICKED, submitHandler);
		gridPane.add(submit, 4, 0);
		gridPane.add(exit, 5, 0);
		return gridPane;
	}

	private GridPane selectPlayerPane() {
		GridPane gridPane = new GridPane();

		gridPane.setMinSize((int)windowWidth/4,  windowHeight); 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       
		gridPane.setAlignment(Pos.TOP_CENTER);

		ChoiceBox<String> player = new ChoiceBox<>();

		player.getItems().addAll(controller.getRecords().keySet());
		gridPane.add(new Text("choose first player"), 0, 0);
		gridPane.add(player, 0, 1);
		ChoiceBox<String> player2 = new ChoiceBox<>(); 
		if(controller.isPvP) {
			player2.getItems().addAll(controller.getRecords().keySet());
			gridPane.add(new Text("choose second player"), 0, 2);
			gridPane.add(player2, 0, 3);
		}


		ChoiceBox<Integer> timerChoice = new ChoiceBox<>(); 
		timerChoice.getItems().addAll(0, 5, 10, 20, 30, 40, 50, 60);
		gridPane.add(new Text("choose timer, 0 for unlimit amount of time"), 0, 4);
		gridPane.add(timerChoice, 0, 5);

		Button submit = new Button("confirm");
		submit.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				if(player.getValue() == null || timerChoice.getValue() == null ) {
					return; 
				}
				if(player2.getValue() == null && controller.isPvP) return;

				if(controller.isPvP && player.getValue().compareTo(player2.getValue()) == 0) {
					System.out.println("return here");
					return;
				}

				if(controller.isPvP) {
					controller.startNewGame(2, 0);
				} else {
					controller.startNewGame(1, 0); 
				}
				controller.createPlayer(player.getValue(), controller.getRecords().get(player.getValue()).mark, 1);
				if(controller.isPvP) {
					controller.createPlayer(player2.getValue(), controller.getRecords().get(player2.getValue()).mark, 2);
				}else {
					controller.createPlayer(null, null, 2);
				}

				Integer time = timerChoice.getValue();

				System.out.println("start addMainPane");
				root.setCenter(addMainPane(time));
			}
		});
		gridPane.add(submit, 0, 6);
		return gridPane;
	}

	public GridPane addMainPane(int timer) {
		Text text = new Text("Select game mode");
		Button selectUltimate = new Button("ultimate_tic_tac_toe");
		Button select3D = new Button("3D_tic_tac_toe");
		Button simpleVersion = new Button("tradition version");
		Button ntimesn = new Button("play two D in nn board");

		TextField nchoice = new TextField();
		Text t = new Text("enter n before play n n board");
		
		EventHandler<MouseEvent> nTimesN = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
			
				if(nchoice.getText() == null ) {
					return;
				}
				int n;
				try {
					n =  Integer.parseInt(nchoice.getText());
				} catch (NumberFormatException ee) {
					return;
				}
				NView view = new NView();
				NController contro = new NController(view, n, controller.map, controller, timer);
				view.setController(contro, n);
				controller.gameEnd = false;
				view.showStage();
			}
		};
		
		
		ntimesn.addEventFilter(MouseEvent.MOUSE_CLICKED, nTimesN);
		
		EventHandler<MouseEvent> simple = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				NormalView view = new NormalView();
				NormalController contro = new NormalController(view, controller.map, controller, timer);
				view.setController(contro);
				controller.gameEnd = false;
				view.showStage();
			}
		};


		EventHandler<MouseEvent> ultimateHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				UltimateView view = new UltimateView();
				control = new UltimateTic_controller(view, controller, controller.map, timer);
				view.setController(control);
				controller.gameEnd = false;
				view.showStage();
			}
		};
		EventHandler<MouseEvent> threeDHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) { 
				ThreeDView view = new ThreeDView();
				tic_3dController control3 = new tic_3dController(view, controller.map, controller, timer );
				view.setController(control3);
				controller.gameEnd = false;
				view.showStage();

			}
		};
		GridPane pane = new GridPane();
		selectUltimate.addEventFilter(MouseEvent.MOUSE_CLICKED, ultimateHandler);
		select3D.addEventFilter(MouseEvent.MOUSE_CLICKED, threeDHandler);
		simpleVersion.addEventFilter(MouseEvent.MOUSE_CLICKED, simple);
		
		pane.add(text,0, 0);
		pane.add(simpleVersion, 0, 3);
		pane.add(selectUltimate, 0, 1);
		pane.add(select3D, 0, 2);
		pane.add(t, 0, 4);
		pane.add(nchoice, 0, 5);
		pane.add(ntimesn, 0, 6);
		return pane;   
	}

	private void updatePlayerPane() {
		this.root.setRight(getPlayerPane());
	} 

	public GridPane getPlayerPane() {
		GridPane gridPane = new GridPane();   
		gridPane.setMinSize((int)windowWidth/4,  windowHeight); 
		//Setting the padding  
		//		gridPane.setPadding(new Insets(10, 10, 10, 10));      
		//Setting the vertical and horizontal gaps between the columns 
		gridPane.setVgap(5); 
		gridPane.setHgap(5);       
		gridPane.setAlignment(Pos.TOP_CENTER);
		gridPane.add(new Text("username"), 0, 0);
		gridPane.add(new Text("mark"), 1, 0);
		gridPane.add(new Text("win"), 2, 0);
		gridPane.add(new Text("loss"), 3, 0);
		//Setting the Grid alignment 
		int i = 1;
		int j = 1;
		Map<String, Records> records = controller.getRecords();
		for(String key : records.keySet()) {
			gridPane.add(new Text(key), 0, i);
			Records s = records.get(key);
			String mark = s.mark;
			gridPane.add(new Text(mark), 1, j);
			gridPane.add(new Text(Integer.toString(records.get(key).win)), 2, j);
			gridPane.add(new Text(Integer.toString(records.get(key).loss)), 3, j);
			i++;
			j++;
		}
		return gridPane;
	}


	public void declareWinner(int winner) {
		controller.gameEnd = true;
		Stage stage = new Stage();
		GridPane gridPane = new GridPane();
		Button exit = new Button("exit");
		EventHandler<MouseEvent> endHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) {
				stage.close();
				Platform.exit();
				System.exit(0);
			}
		};  
		//Registering the event filter 
		exit.addEventHandler(MouseEvent.MOUSE_CLICKED, endHandler);  
		GridPane.setConstraints(exit, 2, 0);
		String username;
		String loser;
		if(winner != 3) {
			gridPane.getChildren().addAll(new Text("Game Over, the winner is: " + controller.map.get(winner).username ), exit);
		} else {
			gridPane.getChildren().addAll(new Text("draw no one win"), exit);
		}
		String musicFile = "win.mp3"; 
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		gridPane.setMinSize(200, 200);
		stage.setScene(new Scene(gridPane));
		stage.sizeToScene();
		stage.show();
	}

	public void declareTimeOut(String loser, String winner) {
		controller.gameEnd = true;
		Stage stage = new Stage();
		GridPane gridPane = new GridPane();
		Button exit = new Button("exit");
		EventHandler<MouseEvent> endHandler = new EventHandler<MouseEvent>() { 
			@Override 
			public void handle(MouseEvent e) {
				stage.close();
				Platform.exit();
				System.exit(0);
			}
		};  
		//Registering the event filter 
		exit.addEventHandler(MouseEvent.MOUSE_CLICKED, endHandler);  
		GridPane.setConstraints(exit, 2, 0);
		gridPane.getChildren().addAll(new Text(loser + " timeout, the winner is: " + winner), exit);
		String musicFile = "win.mp3"; 
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		gridPane.setMinSize(200, 200);
		stage.setScene(new Scene(gridPane));
		stage.sizeToScene();
		stage.show();
	}

}
