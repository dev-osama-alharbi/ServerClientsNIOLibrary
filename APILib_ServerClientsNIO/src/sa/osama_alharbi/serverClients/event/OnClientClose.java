package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnClientClose {
	public abstract void onClientClose();
	public void call(){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onClientClose();
			}
		});
	}
}
