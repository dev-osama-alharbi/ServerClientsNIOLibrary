package sa.osama_alharbi.serverClients.io;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import sa.osama_alharbi.serverClients.server.ClientsInfo;

public abstract class PacketReceiver {

	/*
	 * get ip is vistor or user or admin
	 * 
	 */
	private String packetName;
	public HashMap<String, byte[]> packets;

	public PacketReceiver(String packetName) {
		this.packetName = packetName;
		this.packets = new HashMap<String, byte[]>();
	}

	public abstract void onResave(ClientsInfo clientInfo);

	protected String getString(String key) {
		return ByteGenerator.toString(this.packets.get(key));
	}

	protected String[] getStringArray(String key) {
		return ByteGenerator.toStringArray(this.packets.get(key));
	}

	protected int getInt(String key) {
		return ByteGenerator.toInt(this.packets.get(key));
	}

	protected double getDouble(String key) {
		return ByteGenerator.toDouble(this.packets.get(key));
	}

	protected char getChar(String key) {
		return ByteGenerator.toChar(this.packets.get(key));
	}

	protected char[] getCharArray(String key) {
		return ByteGenerator.toCharArray(this.packets.get(key));
	}

	protected long getLong(String key) {
		return ByteGenerator.toLong(this.packets.get(key));
	}

	protected File getFile(String key) {
		String fileP = ByteGenerator.toString(this.packets.get(key));//.replaceAll("%20", "\\ ");
		return new File(fileP);
	}

	public String getPacketName() {
		return packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}

	public void addPacket(String key, byte[] value, String type) {
		this.packets.put(key, value);
	}

	public void clear() {
		this.packets.clear();
	}

}
