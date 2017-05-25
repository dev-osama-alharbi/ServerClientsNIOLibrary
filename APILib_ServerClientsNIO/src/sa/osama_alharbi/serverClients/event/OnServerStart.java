package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnServerStart {
	public abstract void onServerStart();
	public void call(){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onServerStart();
			}
		});
	}
}
