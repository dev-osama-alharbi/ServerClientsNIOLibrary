package chat;

import chat.inc.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainChatExample extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GUI.generateGUI();
	}

}
