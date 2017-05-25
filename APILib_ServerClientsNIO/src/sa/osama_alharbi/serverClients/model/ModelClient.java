package sa.osama_alharbi.serverClients.model;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ModelClient {
	private int port;
	private String ip;
	private InetSocketAddress inetSocketAddress;
	private boolean isRunning = false;
	private SocketChannel socketChannel;
//	private Selector selector;
	private SelectionKey selectionKey;
	private int packetSize = 10 * 1024;
//	private int timeForClientLoop;
//	private HashMap<String, SocketChannel> clients = new HashMap<String, SocketChannel>();
	private Selector selector;
	public int packetWriterCounter = 0;

	public ModelClient(String ip, int port){
		this.ip = ip;
		this.port = port;
		this.setInetSocketAddress(new InetSocketAddress(this.ip, this.port));
	}
	public ModelClient(SocketChannel socketChannel, SelectionKey selectionKey){
		this.socketChannel = socketChannel;
		this.setSelectionKey(selectionKey);
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

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

//	public HashMap<String, SocketChannel> getClients() {
//		return clients;
//	}

//	public void setClients(HashMap<String, SocketChannel> clients) {
//		this.clients = clients;
//	}

//	public Selector getSelector() {
//		return selector;
//	}
//
//	public void setSelector(Selector selector) {
//		this.selector = selector;
//	}
//
//	public SelectionKey getSelectionKey() {
//		return selectionKey;
//	}
//
//	public void setSelectionKey(SelectionKey selectionKey) {
//		this.selectionKey = selectionKey;
//	}

	public InetSocketAddress getInetSocketAddress() {
		return inetSocketAddress;
	}

	public void setInetSocketAddress(InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	public SelectionKey getSelectionKey() {
		return this.selectionKey;
	}
	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}
	public void setSelection(Selector selector) {
		// TODO Auto-generated method stub
		this.setSelector(selector);
	}
	public Selector getSelector() {
		return selector;
	}
	public void setSelector(Selector selector) {
		this.selector = selector;
	}

//	public int getTimeForClientLoop() {
//		return timeForClientLoop;
//	}
//
//	public void setTimeForClientLoop(int timeForClientLoop) {
//		this.timeForClientLoop = timeForClientLoop;
//	}
}
