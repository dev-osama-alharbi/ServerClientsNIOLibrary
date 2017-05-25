package sa.osama_alharbi.serverClients.server;

public class ClientsInfo {
	private String ipAddress;
	private int port;
	private String clientName;
	private boolean isServer = false;
	
	public ClientsInfo(String ipAddress, int port, String clientName, boolean isServer) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.clientName = clientName;
		this.isServer = isServer;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public int getPort() {
		return port;
	}
	public String getClientName() {
		return clientName;
	}

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}
	
}
