package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnEndSendPacket {
	public abstract void onEndSendPacket(String lvl , String packetName);
	public void call(String lvl , String packetName){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onEndSendPacket(lvl,packetName);
			}
		});
	}
}
