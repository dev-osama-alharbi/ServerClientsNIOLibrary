package sa.osama_alharbi.serverClients.clients;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sa.osama_alharbi.serverClients.event.ServerClientEvent;
import sa.osama_alharbi.serverClients.io.ByteGenerator;
import sa.osama_alharbi.serverClients.io.FileCache;
import sa.osama_alharbi.serverClients.io.PacketSender;
import sa.osama_alharbi.serverClients.io.PacketSenderHandling;
import sa.osama_alharbi.serverClients.io.PacketReceiverHandling;
import sa.osama_alharbi.serverClients.io.Protocol;
import sa.osama_alharbi.serverClients.model.ModelClient;
import sa.osama_alharbi.serverClients.model.ProtocolMode;
import sa.osama_alharbi.serverClients.model.Setting;
import sa.osama_alharbi.serverClients.server.ClientsInfo;
import sa.osama_alharbi.serverClients.server.Server;

public class Client {

	private int packetTraking = 0;
	private ModelClient modelClient;
	public HashMap<Integer, PacketReceiverHandling> packetReceiverHandling;
	private ArrayList<PacketSenderHandling> packetSenderHandling;
	public HashMap<String, Protocol> protocols;
	public ServerClientEvent clientEvent;
	public ClientsInfo clientInfo = null;

	public ArrayList<PacketSenderHandling> getPacketSenderHandling() {
		return packetSenderHandling;
	}

	public Client(String ip, int port, int packetSize,int applcationPlatform) {
		this.modelClient = new ModelClient(ip, port);
		this.modelClient.setPacketSize(packetSize);
		this.packetReceiverHandling = new HashMap<Integer, PacketReceiverHandling>();
		this.packetSenderHandling = new ArrayList<PacketSenderHandling>();
		this.protocols = new HashMap<String, Protocol>();
		this.clientInfo = new ClientsInfo(ip, port, "server", false);
		Setting.setThreadType(applcationPlatform);
		this.clientEvent = new ServerClientEvent().defaultEvents();
	}

	public Client(SocketChannel acceptedChannel, SelectionKey readKey, int packetSize, ClientsInfo clientsInfo,int applcationPlatform,ServerClientEvent events) {
		this.modelClient = new ModelClient(acceptedChannel, readKey);
		this.modelClient.setPacketSize(packetSize);
		this.packetReceiverHandling = new HashMap<Integer, PacketReceiverHandling>();
		this.packetSenderHandling = new ArrayList<PacketSenderHandling>();
		this.protocols = new HashMap<String, Protocol>();
		this.clientInfo = clientsInfo;
		this.clientInfo.setServer(true);
		Setting.setThreadType(applcationPlatform);
		this.clientEvent = events;
	}

	public void addProtocol(Protocol protocol) {
		this.protocols.put(protocol.getLvl(), protocol);
	}

	public void removeProtocol(Protocol protocol) {
		this.protocols.remove(protocol.getLvl(), protocol);
	}

	public void removeProtocol(String lvl) {
		this.protocols.remove(lvl);
	}

	public void enTestS() {
		try {
			this.modelClient.getSocketChannel().configureBlocking(false);
			this.modelClient.setSelection(Selector.open());
			this.modelClient.setSelectionKey(modelClient.getSocketChannel().register(this.modelClient.getSelector(), SelectionKey.OP_WRITE));
			this.modelClient.getSocketChannel().socket().setSoTimeout(50);
			this.modelClient.getSocketChannel().socket().setTcpNoDelay(true);

			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				try {
					if(!isHaveSamethingHandling() && !haveSamethingHandlingFromOther){
						synchronized (this) {
							this.wait(timeSetWait);
						}
					}
					enTestE();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}, 1, timeSet, mainTimeUnit);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private TimeUnit mainTimeUnit = TimeUnit.NANOSECONDS;
	private Integer timeSet = 1;
	private Integer timeSetWait = 250;

	public ModelClient getModelClient() {
		return modelClient;
	}

	public void enTestE() {
		try {
			this.modelClient.getSelector().selectNow();
			for (SelectionKey key : this.modelClient.getSelector().selectedKeys()) {
				if (!key.isValid()) {
					continue;
				}

				if (key.isReadable()) {
					readWrite("r");
				}else if (key.isWritable()) {
					 readWrite("w");
				 }

			}

			this.modelClient.getSelector().selectedKeys().clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean start() {
		
		try {
			this.modelClient.setSocketChannel(SocketChannel.open(this.modelClient.getInetSocketAddress()));
			this.modelClient.setRunning(true);
			enTestS();
			this.clientEvent.getOnClientStart().call();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			this.modelClient.setRunning(false);
			return false;
		}

	}

	public boolean close() {
		disconnect();
		if(clientInfo.isServer()){
			this.clientEvent.getOnServerCloseClient().call(clientInfo.getClientName(), clientInfo.getIpAddress(), clientInfo.getPort(),Server.getModelServer().getClients().size());
		}else{
			this.clientEvent.getOnClientClose().call();
		}
		return true;
	}

	public void read() {
		try {
			System.gc();
			byte[] dataStart = read(10);
			if (dataStart == null) {
				return;
			}
			
			int xx = ByteGenerator.toInt(dataStart);
			byte[] dataHeader = read(xx);
			if (dataHeader == null) {
				return;
			}

			String dataHeaderString = ByteGenerator.toString(dataHeader);
			String[] dataHeaderSplit = dataHeaderString.split("\n");
			if (dataHeaderSplit.length > 6) {
				int packetSize = Integer.parseInt(dataHeaderSplit[13]);
				byte[] packet = read(packetSize);
				int trackingNumber = Integer.parseInt(dataHeaderSplit[15]);
				int partFrom = Integer.parseInt(dataHeaderSplit[9]);
				int partOf = Integer.parseInt(dataHeaderSplit[11]);
				int length = Integer.parseInt(dataHeaderSplit[7]);
				String type = dataHeaderSplit[5];
				if (type.equals("File")) {

					if (partFrom != 1) {
						FileCache.insertIntoFilecashe(
								Setting.getFolderPath() + clientInfo.getClientName() + "_" + dataHeaderSplit[1] + "_"
										+ dataHeaderSplit[3] + "_" + trackingNumber,
								packet, partFrom, packetSize, length);
					} else {
						FileCache.createNewFilecashe(Setting.getFolderPath() + clientInfo.getClientName() + "_"
								+ dataHeaderSplit[1] + "_" + dataHeaderSplit[3] + "_" + trackingNumber);
						FileCache.insertIntoFilecashe(
								Setting.getFolderPath() + clientInfo.getClientName() + "_" + dataHeaderSplit[1] + "_"
										+ dataHeaderSplit[3] + "_" + trackingNumber,
								packet, partFrom, packetSize, length);
						packetReceiverHandling.get(trackingNumber).addFile(dataHeaderSplit[3],
								Setting.getFolderPath() + clientInfo.getClientName() + "_" + dataHeaderSplit[1] + "_"
										+ dataHeaderSplit[3] + "_" + trackingNumber);
					}
				} else {
					packetReceiverHandling.get(trackingNumber).apend(dataHeaderSplit[3], packet, partFrom, partOf,
							length, modelClient.getPacketSize(), type);
				}

			} else {
				read(1);
				if (dataHeaderSplit[0].equals("End")) {
					PacketReceiverHandling p = packetReceiverHandling.get(Integer.parseInt(dataHeaderSplit[3]));
					clientEvent.getOnEndtReceivePacket().call(p.getPacketName(), p.getLength(), p.getKeysLingth(),
							p.packetsTypes, clientInfo.getClientName());
					p.end(protocols.get(dataHeaderSplit[5]),this.clientInfo);

					packetReceiverHandling.remove(Integer.parseInt(dataHeaderSplit[3]));
				} else if (dataHeaderSplit[0].equals("Start")) {
					
					packetReceiverHandling.put(Integer.parseInt(dataHeaderSplit[3]),
							new PacketReceiverHandling(dataHeaderSplit[1]));
					this.events().getOnStartReceivePacket().call(dataHeaderSplit[5], dataHeaderSplit[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
	}
	

	private byte[] read(int capacity) throws IOException {
		int amount_read = -1;
		byte[] data = new byte[0];
		do {
			ByteBuffer bb = ByteBuffer.allocateDirect(capacity);

			amount_read = modelClient.getSocketChannel().read(bb);
			
			if (amount_read == -1) {
				disconnect();
				return null;
			}

			bb.flip();
			byte[] socend = new byte[amount_read];

			bb.get(socend, 0, amount_read);
			bb.clear();
			bb = null;
			capacity = capacity - amount_read;
			data = byteArrayAddition(data, socend);
		} while (capacity > 0);

		return data;
	}

	public void asdasdasd() {
		try {
			int amount_read = -1;

			ByteBuffer bb = ByteBuffer.allocate(2);
			amount_read = modelClient.getSocketChannel().write(bb);

			if (amount_read == -1) {
				disconnect();
			}

			if (amount_read < 1) {
				return; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private byte[] byteArrayAddition(byte[] first, byte[] socend) {
		byte[] data = new byte[first.length + socend.length];
		System.arraycopy(first, 0, data, 0, first.length);
		System.arraycopy(socend, 0, data, first.length, socend.length);
		return data;
	}

	private void writeStart(int length) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(length));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeHeader(String data) {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(data));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writePacket(byte[] data) {
		System.gc();
		ByteBuffer bb = ByteBuffer.wrap(data);
		try {
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		bb.clear();


	}

	public void ceepItReadable() {
		modelClient.getSelectionKey().interestOps(SelectionKey.OP_READ).selector().wakeup();
	}

	public void ceepItWriteable() {
		modelClient.getSelectionKey().interestOps(SelectionKey.OP_WRITE).selector().wakeup();
	}



	void disconnect() {
		if(this.clientInfo.isServer()){
			Server.getModelServer().getClients().remove(this.modelClient.getSelectionKey());
		}

		try {
			if (this.modelClient.getSelectionKey() != null) {
				this.modelClient.getSelectionKey().cancel();
			}

			if (this.modelClient.getSocketChannel() == null) {
				return;
			}

			this.modelClient.getSocketChannel().close();
		} catch (Throwable t) {
		}
	}

	public void send(PacketSender p) {
		Thread T = new Thread(new Runnable() {

			@Override
			public void run() {
				writeSendTest(p);
			}
		});
		T.start();
	}
	
	public class HandlingSendData{
		
		private String packetHeader;
		private int packetHeaderLength = 0;
		private long cbacityRead = 0;
		private String filePath;
		private byte[] data;
		private boolean isStartEnd = false, isFile = false, isByteArray = false; 
		
		public HandlingSendData(String packetHeader){
			this.packetHeader = packetHeader;
			this.packetHeaderLength = packetHeader.length();
			this.isStartEnd = true;
		}
		
		public HandlingSendData(String packetHeader, String filePath,long cbacityRead){
			this.packetHeader = packetHeader;
			this.packetHeaderLength = packetHeader.length();
			this.filePath = filePath;
			this.cbacityRead = cbacityRead;
			this.isFile = true;
		}
		
		public HandlingSendData(String packetHeader, byte[] data){
			this.packetHeader = packetHeader;
			this.packetHeaderLength = packetHeader.length();
			this.data = data;
			this.isByteArray = true;
		}
		
		public int getLength(){
			return this.packetHeaderLength;
		}
		
		public String getPacketHeader(){
			return this.packetHeader;
		}
		
		public byte[] getData(){
			byte[] rData = null;
			if(this.isByteArray){
				rData = this.data;
			}else if(this.isStartEnd){
				rData = new byte[1];
			}else if(this.isFile){
				try {
					FileChannel channel = FileChannel.open(new File(this.filePath).toPath(), StandardOpenOption.READ);
					ByteBuffer bb = ByteBuffer.allocateDirect(modelClient.getPacketSize());
					bb.clear();
					channel.read(bb, cbacityRead);
					bb.flip();
					rData = new byte[bb.limit()];
					bb.get(rData);
					bb.clear();
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return rData;
		}
		
	}
	
	private ArrayList<ArrayDeque<HandlingSendData>> arrayHandlingSendDatas = new ArrayList<ArrayDeque<HandlingSendData>>();
	private void writeSendTest(PacketSender p) {
		ArrayDeque<HandlingSendData> arr = new ArrayDeque<HandlingSendData>();
		int packetTracking = getNewPacketTraking();
		String ss = "Start\n" + p.getPacketName() + "\npacketTracking\n" + packetTracking + "\nlvl\n" + p.getLvl();
		modelClient.packetWriterCounter++;
		arr.add(new HandlingSendData(ss));
		this.events().getOnStartSendPacket().call(p.getLvl(), p.getPacketName());
		for (Entry<String, byte[]> ent : p.packets.entrySet()) {
			byte[] packet = ent.getValue();
			int lengthPacket = packet.length;
			int part = 1;
			String[] sArr = p.packetsHeader.get(ent.getKey()).split("\n");

			if (sArr[5].equals("File")) {
				String filePath = new String(packet);
				System.out.println("f -> "+filePath);
				filePath = filePath.replaceAll("%20", "\\ ");
				filePath = filePath.replaceAll("file://", "");
//				System.out.println("t -> "+filePath);
				Path path = Paths.get(filePath); 
//				File file = FileCache.readFilecashe(filePath);
				try {
					
					FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
					ByteBuffer bb = ByteBuffer.allocateDirect(modelClient.getPacketSize());
					bb.clear();
					long cbacity = channel.size();
					long cbacityRead = 0;
					long cbacityReadAdd = 0;
					
					while((cbacityReadAdd = cbacityReadAdd + modelClient.getPacketSize()) < cbacity){
						String infoPacketHeader = p.infoPartPacketHeaderForFile(ent.getKey(), part,
								modelClient.getPacketSize(), cbacity, modelClient.getPacketSize(), packetTracking);
						arr.add(new HandlingSendData(infoPacketHeader, filePath, cbacityRead));
						cbacityRead = cbacityReadAdd;
						part++;
					}
					long lastSize = cbacity - cbacityRead;
					String infoPacketHeader = p.infoPartPacketHeader(ent.getKey(), part, modelClient.getPacketSize(),
							(int) cbacity, (int) lastSize, packetTracking);
					arr.add(new HandlingSendData(infoPacketHeader, filePath, cbacityRead));
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {

				while (true) {
					byte[] data = p.getPart(packet, part, modelClient.getPacketSize());
					if (data == null) {
						break;
					}
					String infoPacketHeader = p.infoPartPacketHeader(ent.getKey(), part, modelClient.getPacketSize(),
							lengthPacket, data.length, packetTracking);
					modelClient.packetWriterCounter++;
					arr.add(new HandlingSendData(infoPacketHeader, data));
					part++;
				}

			}
		}
		ss = "End\n" + p.getPacketName() + "\npacketTracking\n" + packetTracking + "\nlvl\n" + p.getLvl();
		arr.add(new HandlingSendData(ss));
		arrayHandlingSendDatas.add(arr);
	}

	public synchronized void send(int infoPacketHeaderLength, String infoPacketHeader, byte[] packet) {
		writeStart(infoPacketHeaderLength);
		writeHeader(infoPacketHeader);
		writePacket(packet);
	}

	public int counter = 0;

	private synchronized int getNewPacketTraking() {
		this.packetTraking++;
		return this.packetTraking;
	}


	public ServerClientEvent events() {
		return this.clientEvent;
	}

	
	private boolean isHaveSamethingHandling(){
			if(arrayHandlingSendDatas.size() > 0){
				return true;
			}else{
				return false;
			}
	}
	
	private boolean haveSamethingHandlingFromOther = false;
	
	public synchronized void readWrite(String rw){
		switch (rw) {
		case "r":
			serverRead();
			break;
		case "w":
			clientWrite();
			break;
		}
	}

	public synchronized void clientWrite() {
		if (isHaveSamethingHandling()) {
			sendHaveSomething();
		}else{
			sendPing();
		}
		ceepItReadable();
	}
	
	public synchronized void serverRead() {
		try {
			byte[] data = read(10);
			if (data == null) {
				return;
			}
			int lvl_1 = ByteGenerator.toInt(data);
			switch (lvl_1) {
			case ProtocolMode.PING:
				if(isHaveSamethingHandling()){
					sendHaveSomething();
				}else{
					if(clientInfo.isServer()){
						sendPingBack();	
					}else{
						sendPing();
						
					}
				}
				break;
			case ProtocolMode.PING_BACK:
				haveSamethingHandlingFromOther = false;
				if(!clientInfo.isServer()){
					ceepItWriteable();
				}
				return;
			case ProtocolMode.READY:
				HandlingSendData hsd = getPollHandlingSendData();
				send(hsd.getLength(),hsd.getPacketHeader(),hsd.getData());
				break;
			case ProtocolMode.HAVE_SOMETHING:
				haveSamethingHandlingFromOther = true;
				sendReady();
				read();
				if(isHaveSamethingHandling()){
					sendHaveSomething();
				}else{
						sendPing();	
				}
				break;

			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int handlingSendDataConter = 0;
	
	private HandlingSendData getPollHandlingSendData(){
		HandlingSendData handl = null;
		if(arrayHandlingSendDatas.size() > handlingSendDataConter){
			ArrayDeque<HandlingSendData> arrHandl = arrayHandlingSendDatas.get(handlingSendDataConter);
			if(arrHandl != null){
				handl = arrHandl.poll();
				if(arrHandl.size() == 0){
					arrayHandlingSendDatas.remove(handlingSendDataConter);
					String[] arr = handl.getPacketHeader().split("\n");
					this.events().getOnEndSendPacket().call(arr[5], arr[1]);
				}
				handlingSendDataConter++;
			}
		}else{
			handlingSendDataConter = 0;
			ArrayDeque<HandlingSendData> arrHandl = arrayHandlingSendDatas.get(handlingSendDataConter);
			if(arrHandl != null){
				handl = arrHandl.poll();
				if(arrHandl.size() == 0){
					arrayHandlingSendDatas.remove(handlingSendDataConter);
					String[] arr = handl.getPacketHeader().split("\n");
					this.events().getOnEndSendPacket().call(arr[5], arr[1]);
				}
				handlingSendDataConter++;
			}
		}
		
		return handl;
	}

	private void sendPing() {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(ProtocolMode.PING));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendPingBack() {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(ProtocolMode.PING_BACK));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendHaveSomething() {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(ProtocolMode.HAVE_SOMETHING));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendReady() {
		try {
			ByteBuffer bb = ByteBuffer.wrap(ByteGenerator.generat(ProtocolMode.READY));
			while (bb.hasRemaining()) {
				modelClient.getSocketChannel().write(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
