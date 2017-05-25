package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnStartSendPacket {
	public abstract void onStartSendPacket(String lvl , String packetName);
	public void call(String lvl , String packetName){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onStartSendPacket(lvl,packetName);
			}
		});
	}
}
