package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnServerClose {
	public abstract void onServerClose();
	public void call(){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onServerClose();
			}
		});
	}
}
