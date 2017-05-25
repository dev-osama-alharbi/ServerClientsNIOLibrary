package tt;

import sa.osama_alharbi.serverClients.clients.Client;
import sa.osama_alharbi.serverClients.event.OnServerNewClient;
import sa.osama_alharbi.serverClients.io.PacketReceiver;
import sa.osama_alharbi.serverClients.io.PacketSender;
import sa.osama_alharbi.serverClients.io.Protocol;
import sa.osama_alharbi.serverClients.model.AppType;
import sa.osama_alharbi.serverClients.server.ClientsInfo;
import sa.osama_alharbi.serverClients.server.Server;

public class ServerExample {
	
	public static String ipAddress = "127.0.0.1";
	public static int port = 12332;
	public static int packetSize = 50000; //byte[]
	public static int appType = AppType.CONSOLE;
	
	public static void main(String[] args) {
		Server server = new Server(ipAddress, port, packetSize, appType);
		Client client = new Client(ipAddress, port, packetSize, appType);
		
		
		server.start();
		client.start();
		
		client.close();
		server.close();
		
		
		String protocolLvl = "LoginLvl";
		Protocol loginProtocol = new Protocol(protocolLvl);
		server.addProtocol(loginProtocol);
		
		String packetName = "userLogin";
		PacketReceiver packetLogin = new PacketReceiver(packetName) {
			@Override
			public void onResave(ClientsInfo clientInfo) {
				String username = getString("username");
				String password = getString("password");
			}
		};
		loginProtocol.add(packetLogin);
		
		String packetName_2 = "userLogin";
		PacketSender sendPacketLogin = new PacketSender(packetName_2, loginProtocol.getLvl()) {
			@Override
			public void sender() {
				put("username","Osama");
				put("password","123123");
			}
		};
		server.send(sendPacketLogin);
		
		server.events().setOnServerNewClient(new OnServerNewClient() {
			@Override
			public void onServerNewClient(String clientName, String ipAddress, int port, int numbersOfClients) { 
				// TODO Auto-generated method stub
				System.out.println("New client name "+clientName+", Ip Address "+ipAddress);
			}
		});
		
		
	}

}


