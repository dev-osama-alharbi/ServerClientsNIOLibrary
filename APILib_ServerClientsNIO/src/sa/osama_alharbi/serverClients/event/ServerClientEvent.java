package sa.osama_alharbi.serverClients.event;

import java.util.HashMap;

public class ServerClientEvent {
	/*
	 * on server start
	 * on server stop
	 * on server new client
	 * on server close client
	 * on server send packet with %
	 * on server get packet with %
	 * on server comblite packet
	 * on server client attack
	 * on server 
	 */
	/*
	 * on client start
	 * on client stop
	 * on client server stop
	 * on client send packet with %
	 * on client get packet with %
	 * on client comblite packet
	 * on client client attack
	 * on client 
	 */

	private OnServerStart onServerStart;
	public void setOnServerStart(OnServerStart onServerStart) {
		this.onServerStart = onServerStart;
	}
	public OnServerStart getOnServerStart() {
		return onServerStart;
	}

	private OnServerClose onServerClose;
	public void setOnServerStart(OnServerClose onServerClose) {
		this.onServerClose = onServerClose;
	}
	public OnServerClose getOnServerClose() {
		return onServerClose;
	}

	private OnClientStart onClientStart;
	public void setOnClientStart(OnClientStart onClientStart) {
		this.onClientStart = onClientStart;
	}
	public OnClientStart getOnClientStart() {
		return onClientStart;
	}

	private OnClientClose onClientClose;
	public void setOnClientClose(OnClientClose onClientClose) {
		this.onClientClose = onClientClose;
	}
	public OnClientClose getOnClientClose() {
		return onClientClose;
	}

	private OnEndReceivePacket onEndReceivePacket;
	public void setOnEndReceivePacket(OnEndReceivePacket onEndReceivePacket) {
		this.onEndReceivePacket = onEndReceivePacket;
	}
	public OnEndReceivePacket getOnEndtReceivePacket() {
		return onEndReceivePacket;
	}
	
	private OnServerNewClient onServerNewClient;
	public void setOnServerNewClient(OnServerNewClient onServerNewClient) {
		this.onServerNewClient = onServerNewClient;
	}
	public OnServerNewClient getOnServerNewClient() {
		return onServerNewClient;
	}
	
	private OnServerCloseClient onServerCloseClient;
	public void setOnServerCloseClient(OnServerCloseClient onServerCloseClient) {
		this.onServerCloseClient = onServerCloseClient;
	}
	public OnServerCloseClient getOnServerCloseClient() {
		return onServerCloseClient;
	}
	
	private OnStartReceivePacket onStartReceivePacket;
	public void setOnStartReceivePacket(OnStartReceivePacket onStartReceivePacket) {
		this.onStartReceivePacket = onStartReceivePacket;
	}
	public OnStartReceivePacket getOnStartReceivePacket() {
		return onStartReceivePacket;
	}
	
	private OnStartSendPacket onStartSendPacket;
	public void setOnStartSendPacket(OnStartSendPacket onStartSendPacket) {
		this.onStartSendPacket = onStartSendPacket;
	}
	public OnStartSendPacket getOnStartSendPacket() {
		return onStartSendPacket;
	}
	
	private OnEndSendPacket onEndSendPacket;
	public void setOnEndSendPacket(OnEndSendPacket onEndSendPacket) {
		this.onEndSendPacket = onEndSendPacket;
	}
	public OnEndSendPacket getOnEndSendPacket() {
		return onEndSendPacket;
	}
	
	
	
	public ServerClientEvent defaultEvents(){
		this.onClientClose = new OnClientClose() {
			@Override
			public void onClientClose() {
			}
		};
		this.onClientStart = new OnClientStart() {
			@Override
			public void onClientStart() {
			}
		};
		this.onEndReceivePacket = new OnEndReceivePacket() {
			@Override
			public void onEndReceivePacket(String packetName, long length, HashMap<String, Integer> keysLingth,
					HashMap<String, String> keysTypes, String senderName) {
			}
		};
		this.onServerClose = new OnServerClose() {
			@Override
			public void onServerClose() {
			}
		};
		this.onServerStart = new OnServerStart() {
			@Override
			public void onServerStart() {
			}
		};
		this.onServerNewClient = new OnServerNewClient() {
			@Override
			public void onServerNewClient(String clientName,String ipAddress,int port,int numbersOfClients) {
			}
		};
		this.onServerCloseClient = new OnServerCloseClient() {
			@Override
			public void onServerCloseClient(String clientName, String ipAddress, int port,int numbersOfClients) {
			}
		};
		this.onStartReceivePacket = new OnStartReceivePacket() {
			@Override
			public void onStartReceivePacket(String lvl, String packetName) {
			}
		};
		this.onStartSendPacket = new OnStartSendPacket() {
			@Override
			public void onStartSendPacket(String lvl, String packetName) {
			}
		};
		this.onEndSendPacket = new OnEndSendPacket() {
			@Override
			public void onEndSendPacket(String lvl, String packetName) {
			}
		};
		return this;
	}
	
	
}
