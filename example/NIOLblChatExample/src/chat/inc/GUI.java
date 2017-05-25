package chat.inc;

import java.io.IOException;

import chat.MainChatExample;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI {
	
	public static Stage stage;
	public static Scene scene;
	
	public static BorderPane root;
	public static AnchorPane mainButton, login, logout, chat, details, fileSender;
	
	public static void generateGUI(){
		try {
			FXMLLoader rootLlader = new FXMLLoader(MainChatExample.class.getResource("v/Root.fxml"));
			root = rootLlader.load();
			
			FXMLLoader chatLlader = new FXMLLoader(MainChatExample.class.getResource("v/Chat.fxml"));
			chat = chatLlader.load();
			
			FXMLLoader mainButtonLlader = new FXMLLoader(MainChatExample.class.getResource("v/MainButton.fxml"));
			mainButton = mainButtonLlader.load();

			FXMLLoader loginLlader = new FXMLLoader(MainChatExample.class.getResource("v/Login.fxml"));
			login = loginLlader.load();

			FXMLLoader logoutLlader = new FXMLLoader(MainChatExample.class.getResource("v/Logout.fxml"));
			logout = logoutLlader.load();

			FXMLLoader detailsLlader = new FXMLLoader(MainChatExample.class.getResource("v/Details.fxml"));
			details = detailsLlader.load();

			FXMLLoader fileSenderLlader = new FXMLLoader(MainChatExample.class.getResource("v/FileSender.fxml"));
			fileSender = fileSenderLlader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		root.setLeft(mainButton);
		root.setCenter(login);
		
		stage = new Stage();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
