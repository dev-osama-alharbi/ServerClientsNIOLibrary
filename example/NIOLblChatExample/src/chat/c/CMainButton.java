package chat.c;

import chat.inc.GUI;
import chat.inc.Setting;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CMainButton {
	
	@FXML public Button btnChat;
	
	@FXML public void onClickLoginOut(){
		if(Setting.isLogin){
			GUI.root.setCenter(GUI.logout);
		}else{
			GUI.root.setCenter(GUI.login);
		}
	}
	
	@FXML public void onClickChat(){
		GUI.root.setCenter(GUI.chat);
	}
	
	@FXML public void onClickFile(){
		GUI.root.setCenter(GUI.fileSender);
	}
	
	@FXML public void onClickDetails(){
		GUI.root.setCenter(GUI.details);
	}
}
