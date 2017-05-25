package sa.osama_alharbi.serverClients.io;

import java.util.HashMap;
import java.util.Map.Entry;

import sa.osama_alharbi.serverClients.server.ClientsInfo;
import sa.osama_alharbi.serverClients.thread.MainThread;

public class Protocol {
	private String lvl;
	private HashMap<String, PacketReceiver> packetReceivers = null;

	public Protocol(String lvl) {
		this.setLvl(lvl);
		this.packetReceivers = new HashMap<String, PacketReceiver>();
	}

	public void add(PacketReceiver packet) {
		this.packetReceivers.put(packet.getPacketName(), packet);
	}

	public void callPacket(String key, PacketReceiverHandling handling, ClientsInfo clientInfo) {
		this.packetReceivers.get(key).clear();

		for (Entry<String, byte[]> ent : handling.packets.entrySet()) {
			this.packetReceivers.get(key).addPacket(ent.getKey(), ent.getValue(),
					handling.packetsTypes.get(ent.getKey()));
		}
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				packetReceivers.get(key).onResave(clientInfo);
			}
		});

		

	}

	public String getLvl() {
		return lvl;
	}

	public void setLvl(String lvl) {
		this.lvl = lvl;
	}

	public String toString() {
		return this.lvl;
	}
}
