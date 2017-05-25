package sa.osama_alharbi.serverClients.io;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;


public abstract class PacketSender {
	private String packetName,lvl;
	public String getLvl() {
		return lvl;
	}
	public void setLvl(String lvl) {
		this.lvl = lvl;
	}
	public String getPacketName() {
		return packetName;
	}
	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

	public HashMap<String, byte[]> packets;
	public HashMap<String, String> packetsHeader;
	public PacketSender(String packetName,String lvl){
		this.packetName = packetName;
		this.packets = new HashMap<String, byte[]>();
		this.packetsHeader = new HashMap<String, String>();
		this.lvl = lvl;
		this.sender();
	}
	public abstract void sender();
	
	protected void put(String key, String value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("String",key,data.length));
	}
	
	protected void put(String key, int value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("int",key,data.length));
	}
	
	protected void put(String key, char value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("char",key,data.length));
	}
	
	protected void put(String key, char[] value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("char[]",key,data.length));
	}
	
	protected void put(String key, long value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("long",key,data.length));
	}
	
	protected void put(String key, double value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("double",key,data.length));
	}
	
//	protected void put(String key, byte[] value){
//		byte[] data = value;
//		this.packets.put(key, data);
//		this.packetsHeader.put(key, createPacketHeader("byte[]",key,data.length));
//	}
	
	protected void put(String key, File value){
		byte[] data = null;
		try {
			data = value.toPath().toUri().toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("File",key,value.length()));
	}
	
	protected void put(String key, String[] value){
		byte[] data = ByteGenerator.generat(value);
		this.packets.put(key, data);
		this.packetsHeader.put(key, createPacketHeader("StringArray",key,data.length));
	}
	
	
	
	//String s = "packetName\nlogin\nkey\nusername\ntype\nString\nlength\n50\npart\n1\nof\n2";
	private String createPacketHeader(String type, String key, long length){
		return "packetName\n"+this.packetName+"\nkey\n"+key+"\ntype\n"+type+"\nlength\n"+length;
	}
	
	public byte[] getPart(byte[] data, int part, int size){
		int from = size * (part-1);
		int to = from+size;
		if(data.length > from && data.length > to){
			return Arrays.copyOfRange(data, from, to);
		}else if(data.length > from ){
			return Arrays.copyOfRange(data, from, data.length);
		}else{
			return null;
		}
	}
	
	public String infoPartPacketHeader(String key, int part, int size, int length, int packetLength,int trackingNumber){
		int of = 0;
		if(length%size == 0){
			of = length/size;
		}else{
			of = (length/size)+1;
		}
		String data = this.packetsHeader.get(key);
		data += "\npart\n"+part+"\nof\n"+of+"\npacketLength\n"+packetLength+"\ntrackingNumber\n"+trackingNumber+"\nlvl\n"+this.lvl;
		return data;
	}
	
	public String infoPartPacketHeaderForFile(String key, int part, int size, long length, int packetLength,int trackingNumber){
		int of = 0;
		if(length%size == 0){
			of = (int) (length/size);
		}else{
			of = (int) ((length/size)+1);
		}
		String data = this.packetsHeader.get(key);
		data += "\npart\n"+part+"\nof\n"+of+"\npacketLength\n"+packetLength+"\ntrackingNumber\n"+trackingNumber+"\nlvl\n"+this.lvl;
		return data;
	}
	
	public String toString(){
		String data = "";
		
		for (Entry<String, String> packets : this.packetsHeader.entrySet()) {
			String[] newData = packets.getValue().split("\n");
			data += "---------------------------\n";
			data += newData[0]+" = "+newData[1]+"\n";
			data += newData[2]+" = "+newData[3]+"\n";
			data += newData[4]+" = "+newData[5]+"\n";
			data += newData[6]+" = "+newData[7]+"\n";
		}
		data += "---------------------------";
		return data;
	}
	
}
