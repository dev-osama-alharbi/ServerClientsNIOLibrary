package chat.inc;

import sa.osama_alharbi.serverClients.clients.Client;
import sa.osama_alharbi.serverClients.io.Protocol;
import sa.osama_alharbi.serverClients.server.Server;

public class ServerClient {
	public static boolean isServer = false;
	public static Server server;
	public static Client client;

	public static Protocol logInProtocol = new Protocol("Login");
	public static Protocol chatProtocol = new Protocol("Chat");
	public static Protocol fileProtocol = new Protocol("File");

	public static void onStartClient() {
		client.addProtocol(logInProtocol);
		client.addProtocol(chatProtocol);
		client.addProtocol(fileProtocol);
		client.start();
	}
	
}
