package sa.osama_alharbi.serverClients.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import sa.osama_alharbi.serverClients.clients.Client;
import sa.osama_alharbi.serverClients.event.ServerClientEvent;
import sa.osama_alharbi.serverClients.io.PacketSender;
import sa.osama_alharbi.serverClients.io.Protocol;
import sa.osama_alharbi.serverClients.model.ModelServer;
import sa.osama_alharbi.serverClients.model.Setting;

public class Server {
	private static ModelServer modelServer;
	
	private HashMap<String,Protocol> ServerProtocols;
	private ServerClientEvent event = null;
	
	private int clientNameCounter = 0;
	
	private synchronized int getNewClientNameCounter(){
		this.clientNameCounter++;
		return this.clientNameCounter;
	}

	public static ModelServer getModelServer() {
		return modelServer;
	}
	
	public Server(String ip, int port, int packetSize ,int applcationPlatform){
		modelServer = new ModelServer(ip,port);
		modelServer.setPacketSize(packetSize);
		this.ServerProtocols = new HashMap<String,Protocol>();
		Setting.setThreadType(applcationPlatform);
		this.event = new ServerClientEvent().defaultEvents();
	}
	
	public void addProtocol(Protocol protocol){
		this.ServerProtocols.put(protocol.getLvl(), protocol);
	}
	
	public void removeProtocol(Protocol protocol){
		this.ServerProtocols.remove(protocol.getLvl(), protocol);
	}
	
	public void removeProtocol(String lvl){
		this.ServerProtocols.remove(lvl);
	}
	
	
	public ServerClientEvent events(){
		return this.event;
	}
	ScheduledFuture<?> ex = null;
	public void start(){
		try{
			getModelServer().setServerSocketChannel(ServerSocketChannel.open());
			getModelServer().getServerSocketChannel().configureBlocking(false);
			getModelServer().setSelector(Selector.open());
			getModelServer().setSelectionKey(getModelServer().getServerSocketChannel().register(getModelServer().getSelector(), SelectionKey.OP_ACCEPT));
			getModelServer().getServerSocketChannel().bind(getModelServer().getInetSocketAddress());
			getModelServer().setTimeForClientLoop(1);
			getModelServer().setRunning(true);
			
			
			ex = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			try {

				loop();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}, 1,getModelServer().getTimeForClientLoop(), TimeUnit.NANOSECONDS);
			
		}catch (IOException e) {
		}
		this.event.getOnServerStart().call();
	}
	
	public void close(){
		
		try {
			getModelServer().setRunning(false);
			for (Entry<SelectionKey, Client> ent : getModelServer().getClients().entrySet()) {
				ent.getValue().close();
			}
			getModelServer().getServerSocketChannel().close();
			ex.cancel(true);
			this.event.getOnServerClose().call();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.event.getOnServerClose().call();
	}
	
	private void loop() throws Throwable{
		getModelServer().getSelector().selectNow();
		
		Set<SelectionKey> selectedKeys = getModelServer().getSelector().selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
		while(keyIterator.hasNext()) {
			
			SelectionKey key = keyIterator.next();
			
			if (!key.isValid()){
				continue;
			}
			
			
			if(key.isAcceptable()){
				SocketChannel acceptedChannel =getModelServer().getServerSocketChannel().accept();

				if (acceptedChannel == null){
					continue;
				}
				acceptedChannel.configureBlocking(false);
				SelectionKey readKey = acceptedChannel.register(getModelServer().getSelector(), SelectionKey.OP_READ);
				String[] remoteAddress = acceptedChannel.getRemoteAddress().toString().replaceAll("/", "").split(":");
				acceptedChannel.socket().setSoTimeout(50);
				acceptedChannel.socket().setTcpNoDelay(true);
				
				ClientsInfo clientsInfo = new ClientsInfo(remoteAddress[0], Integer.parseInt(remoteAddress[1]), "client-"+this.getNewClientNameCounter(),true);
				Client client = new Client(acceptedChannel, readKey, modelServer.getPacketSize(),clientsInfo,Setting.getThreadType(),this.event);
				getModelServer().getClients().put(readKey, client);
				getModelServer().getClientsName().put(clientsInfo.getClientName(), client);
				
				for (Entry<String, Protocol> ent : ServerProtocols.entrySet()) {
					getModelServer().getClients().get(readKey).addProtocol(ent.getValue());
				}
				
				
				client.getModelClient().setSelection(getModelServer().getSelector());
				
				this.event.getOnServerNewClient().call(clientsInfo.getClientName(), clientsInfo.getIpAddress(), clientsInfo.getPort(), getModelServer().getClients().size());
			}
			
			if(key.isReadable()){
				Client client = getModelServer().getClients().get(key);
				
				if(client == null){
					continue;
				}
				client.serverRead();
				
			}
			if(key.isWritable()){
				Client client = getModelServer().getClients().get(key);
				if(client == null){
					continue;
				}
			}

			keyIterator.remove();
			
		}
		getModelServer().getSelector().selectedKeys().clear();
	}
	public void send(PacketSender p) {
		for (Entry<SelectionKey, Client> client : getModelServer().getClients().entrySet()) {
			client.getValue().send(p);
		}
	}	
	public void sendTo(PacketSender p, String clientName) {
		Client client = getModelServer().getClientsName().get(clientName);
		client.send(p);
	}	
	public void sendToAllNot(PacketSender p, String clientName) {
		for (Entry<String, Client> client : getModelServer().getClientsName().entrySet()) {
			if(!client.getKey().equals(clientName)){
				client.getValue().send(p);
			}
			System.out.println("Server --> "+client.getKey() +"  |=|=|=|  "+clientName);
		}
	}	
}