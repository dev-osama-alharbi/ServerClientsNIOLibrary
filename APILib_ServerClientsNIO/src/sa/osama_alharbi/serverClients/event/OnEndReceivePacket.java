package sa.osama_alharbi.serverClients.event;

import java.util.HashMap;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnEndReceivePacket {
	public abstract void onEndReceivePacket(String packetName, long length, HashMap<String, Integer> keysLingth, HashMap<String, String> keysTypes, String senderName);
	public void call(String packetName, long packetLength, HashMap<String, Integer> keysLingth, HashMap<String, String> keysTypes, String senderName){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onEndReceivePacket(packetName,packetLength,keysLingth,keysTypes, senderName);
			}
		});
	}
}
