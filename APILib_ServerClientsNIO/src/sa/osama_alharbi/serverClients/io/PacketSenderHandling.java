package sa.osama_alharbi.serverClients.io;

public class PacketSenderHandling {
	private int infoPacketHeaderlength;
	private String infoPacketHeader;
	private byte[] packet;
	private PacketSender p;
	
	public PacketSenderHandling(int infoPacketHeaderlength, String infoPacketHeader, byte[] packet){
		this.infoPacketHeaderlength = infoPacketHeaderlength;
		this.infoPacketHeader = infoPacketHeader;
		this.packet = packet;
		
	}
	
	public PacketSenderHandling(PacketSender p){
		this.p = p;
	}

	public int getInfoPacketHeaderlength() {
		return infoPacketHeaderlength;
	}

	public String getInfoPacketHeader() {
		return infoPacketHeader;
	}

	public PacketSender getPacketSender() {
		return p;
	}

	public byte[] getPacket() {
		return packet;
	}
	
}
