package chat.c;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import chat.inc.ChatCore;
import chat.inc.GUI;
import chat.inc.ServerClient;
import chat.inc.Setting;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import sa.osama_alharbi.serverClients.clients.Client;
import sa.osama_alharbi.serverClients.io.PacketSender;
import sa.osama_alharbi.serverClients.model.AppType;
import sa.osama_alharbi.serverClients.server.Server;

public class CLogin implements Initializable {

	@FXML
	public TextField txtHost, txtPort, txtPacketSize, txtUsername;

	@FXML
	public void onClickLoginAsClient() {
		if (chekIfIsNotEmpty()) {
			ServerClient.client = new Client(txtHost.getText(), Integer.parseInt(txtPort.getText()),
			Integer.parseInt(txtPacketSize.getText()), AppType.JAVAFX);
			ServerClient.isServer = false;
			sa.osama_alharbi.serverClients.model.Setting.setFolderPath(Paths.get("").toUri().toString()+"cash/");
			ServerClient.onStartClient();
			Setting.isLogin = true;
			GUI.root.setCenter(GUI.logout);

			ChatCore.ClientsNames.put(ServerClient.client.clientInfo.getClientName(), txtUsername.getText());
			CChat.addNewUser(txtUsername.getText());
			ChatCore.myUsername = txtUsername.getText();
			ServerClient.client.send(new PacketSender("newUser", ServerClient.logInProtocol.toString()) {
				@Override
				public void sender() {
					put("username", txtUsername.getText());
				}
			});
			ServerClient.client.send(new PacketSender("getOnline", ServerClient.chatProtocol.toString()) {
				@Override
				public void sender() {
					put("tst", "tst");
				}
			});

		}
	}

	private boolean chekIfIsNotEmpty() {
		if (txtHost.getText().isEmpty() & txtPort.getText().isEmpty() & txtPacketSize.getText().isEmpty()
				& txtUsername.getText().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtHost.setText("192.168.1.101");
		txtPort.setText("23234");
		txtPacketSize.setText("500000");
	}

}
