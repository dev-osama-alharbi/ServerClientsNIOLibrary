package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnClientStart {
	public abstract void onClientStart();
	public void call(){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onClientStart();
			}
		});
	}
}
