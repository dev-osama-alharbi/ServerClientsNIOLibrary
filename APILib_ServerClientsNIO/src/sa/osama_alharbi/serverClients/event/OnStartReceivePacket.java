package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnStartReceivePacket {
	public abstract void onStartReceivePacket(String lvl , String packetName);
	public void call(String lvl , String packetName){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onStartReceivePacket(lvl,packetName);
			}
		});
	}
}
