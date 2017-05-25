package sa.osama_alharbi.serverClients.model;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

import sa.osama_alharbi.serverClients.clients.Client;

public class ModelServer {
	private int port;
	private String ip = "localhost";
	private InetSocketAddress inetSocketAddress;
	private boolean isRunning = false;
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private SelectionKey selectionKey;
	private int timeForClientLoop;
	private int packetSize ;
	private HashMap<SelectionKey, Client> clientsKey = new HashMap<SelectionKey, Client>();
	private HashMap<String, Client> clientsName = new HashMap<String, Client>();

	public ModelServer(String ip, int port){
		this.port = port;
		this.ip = ip;
		this.setInetSocketAddress(new InetSocketAddress(this.ip, this.port));
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public ServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}

	public void setServerSocketChannel(ServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}

	public HashMap<SelectionKey, Client> getClients() {
		return clientsKey;
	}

	public void setClients(HashMap<SelectionKey, Client> clients) {
		this.clientsKey = clients;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	public InetSocketAddress getInetSocketAddress() {
		return inetSocketAddress;
	}

	public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
	}

	public int getTimeForClientLoop() {
		return timeForClientLoop;
	}

	public void setTimeForClientLoop(int timeForClientLoop) {
		this.timeForClientLoop = timeForClientLoop;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}

	public HashMap<String, Client> getClientsName() {
		return clientsName;
	}

	public void setClientsName(HashMap<String, Client> clientsName) {
		this.clientsName = clientsName;
	}
}
