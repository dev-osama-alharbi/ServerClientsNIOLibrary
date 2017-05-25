package chat.c;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ResourceBundle;

import chat.c.CChat.ChatItems;
import chat.inc.ChatCore;
import chat.inc.ServerClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import sa.osama_alharbi.serverClients.io.PacketReceiver;
import sa.osama_alharbi.serverClients.io.PacketSender;
import sa.osama_alharbi.serverClients.server.ClientsInfo;

public class CChat implements Initializable {

	@FXML
	public ListView<String> listOnline;
	public ObservableList<String> obsListOnline;
	@FXML
	public ListView<ChatItems> listChat;
	public ObservableList<ChatItems> obsListChat;
	@FXML
	public TextArea txtSend;

	@FXML
	public void onClickSend() {
		ServerClient.client.send(new PacketSender("sendMsg",ServerClient.chatProtocol.toString()) {
			@Override
			public void sender() {
				put("msg",txtSend.getText());
				put("username",ChatCore.myUsername);
			}
		});
		addNewMsg(ChatCore.myUsername, txtSend.getText());
		txtSend.setText("");
	}

	private static CChat cchat;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		obsListOnline = FXCollections.observableArrayList();
		obsListChat = FXCollections.observableArrayList();
		listOnline.setItems(obsListOnline);
		listChat.setItems(obsListChat);
		RProtocolLogin();
		RProtocolChat();
		onListChatDrugPhoto();
		cchat = this;
	}
	
	private void onListChatDrugPhoto(){
		listChat.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});
		listChat.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (event.getDragboard().hasFiles()) {
					for (File file : db.getFiles()) {
						ServerClient.client.send(new PacketSender("sendPhoto",ServerClient.chatProtocol.toString()) {
							@Override
							public void sender() {
								put("username", ChatCore.myUsername);
								put("photo",file);
							}
						});
						addNewPhoto(ChatCore.myUsername,file);
					}
					event.setDropCompleted(true);
					event.consume();
				}
			}
		});
	}


	private void RProtocolLogin() {
		
	}

	private void RProtocolChat() {
		ServerClient.chatProtocol.add(new PacketReceiver("onlienUsers") {
			@Override
			public void onResave(ClientsInfo clientsInfo) {
				refrishOnlienUsers(getStringArray("usersNames"));
			}
		});
		
		ServerClient.chatProtocol.add(new PacketReceiver("sendMsg") {
			@Override
			public void onResave(ClientsInfo clientsInfo) {
				addNewMsg(getString("username"),getString("msg"));
			}
		});
		
		ServerClient.chatProtocol.add(new PacketReceiver("sendPhoto") {
			@Override
			public void onResave(ClientsInfo clientsInfo) {
				String username = getString("username");
				File file = getFile("photo");
				System.out.println("Cphoto => "+file.toURI().toString());
				addNewPhoto(username,file);
			}
		});
	}
	
	public void refrishOnlienUsers(String[] users){
		obsListOnline.clear();
		for (String string : users) {
			
			obsListOnline.add(string);
		}
	}

	public static void addNewUser(String user) {
		cchat.obsListOnline.add(user);
	}


	private void addNewPhoto(String user, File photo) {
		obsListChat.add(new ChatItems(user, photo));
	}
	private void addNewMsg(String user, String msg) {
		obsListChat.add(new ChatItems(user, msg));
	}

	public class ChatItems extends VBox {
		private ImageView img;

		public ChatItems(String username, String data) {
			addUsername(username);
			addMsg(data);
			addEnd();
		}

		public ChatItems(String username, File data) {
			addUsername(username);
			addImg(data);
			addEnd();
		}

		private void addUsername(String username) {
			this.getChildren().add(new Label(username));
		}

		private void addImg(File url) {
			String u = url.toString();
			if(!u.toLowerCase().startsWith("file:///")){
				if(!u.toLowerCase().startsWith("file")){
					u = "file://" + u;
				}else{
					u = u.replaceAll("file:/", "file:///");
				}
			}
			img = new ImageView(new Image(u));
			img.setFitWidth(200.0);
			img.setFitHeight(200.0);
			this.getChildren().add(img);
		}

		private void addMsg(String msg) {
			Label mshLbl = new Label(msg);
			mshLbl.setWrapText(true);
			this.getChildren().add(mshLbl);
		}

		private void addEnd() {
			this.getChildren().add(new Label("----------------------"));
		}

	}

}
