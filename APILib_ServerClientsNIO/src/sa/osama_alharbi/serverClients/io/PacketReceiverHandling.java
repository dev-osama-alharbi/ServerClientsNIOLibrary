package sa.osama_alharbi.serverClients.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import sa.osama_alharbi.serverClients.server.ClientsInfo;

public class PacketReceiverHandling {

	public HashMap<String, byte[]> packets;
	public HashMap<String, String> packetsTypes;
	private String packetName;
	public PacketReceiverHandling(String packetName){
		this.packets = new HashMap<String, byte[]>();
		this.packetsTypes = new HashMap<String, String>();
		this.packetName = packetName;
	}
	
	public void end(Protocol protocol,ClientsInfo clientInfo){
		protocol.callPacket(this.packetName,this,clientInfo);
	}
	
	public void apend(String key, byte[] value, int partFrom,int partOf,int length ,int serverPacketSize ,String type){
		if(!this.packets.containsKey(key)){
			byte[] data = new byte[length];
			this.packets.put(key, data);
			this.packetsTypes.put(key, type);
		}

		System.arraycopy(value, 0, this.packets.get(key), ((serverPacketSize * partFrom) - serverPacketSize),value.length);
	}
	
	public long getLength(){
		long count = 0;
		for (Entry<String, byte[]> ent : packets.entrySet()) {
			String type = this.packetsTypes.get(ent.getKey());
			if(type.equals("File")){
				count = count + new File(new String(ent.getValue()).replaceAll("file://", "")).length();

			}else{
				count = count + ent.getValue().length;
			}
		}
		return count;
	}
	
	public void addFile(String key, String filePath){
		this.packets.put(key, filePath.getBytes());
		this.packetsTypes.put(key, "File");
	}
	
	public String getPacketName(){
		return packetName;
	}
	
	public HashMap<String, Integer> getKeysLingth(){
		HashMap<String, Integer> keysLingth = new HashMap<String, Integer>();
		for (Entry<String, byte[]> ent : packets.entrySet()) {
			keysLingth.put(ent.getKey(), ent.getValue().length);
		}
		return keysLingth;
	}
}
