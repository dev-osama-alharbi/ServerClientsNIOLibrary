package sa.osama_alharbi.serverClients.event;

import sa.osama_alharbi.serverClients.thread.MainThread;

public abstract class OnServerCloseClient {
	public abstract void onServerCloseClient(String clientName,String ipAddress, int port,int numbersOfClients);
	public void call(String clientName,String ipAddress, int port,int numbersOfClients){
		MainThread.getThread(new Runnable() {
			@Override
			public void run() {
				onServerCloseClient(clientName,ipAddress,port,numbersOfClients);
			}
		});
	}
}
