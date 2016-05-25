package make_music_client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerInterface extends SocketInterface {
	private Boolean sendFlag;

	public ServerInterface(String address, int port, Boolean sendFlag) throws UnknownHostException, IOException {
		super(address, port);
		this.sendFlag = sendFlag;
	}
	
	// Construct interfaces between Server and Client.
	public void sendAddressToServer() throws UnknownHostException {
		this.sendMessageToServer(InetAddress.getLocalHost().getHostAddress());
		sendFlag = Boolean.FALSE;
	}

	public void sendAddRoomSignalToServer(String roomName) {
		this.sendMessageToServer("@addRoom " + roomName);
		sendFlag = Boolean.FALSE;
	}

	public void sendRemoveRoomSignalToServer() {
		this.sendMessageToServer("@removeRoom");
		sendFlag = Boolean.FALSE;
	}

	public void sendShowUserListSignalToServer() {
		this.sendMessageToServer("@showUserList");
		sendFlag = Boolean.FALSE;
	}

	public void sendShowRoomListSignalToServer() {
		this.sendMessageToServer("@showRoomList");
		sendFlag = Boolean.FALSE;
	}

	public void sendQuitSignalToServer() {
		this.sendMessageToServer("@quit");
		sendFlag = Boolean.FALSE;
	}

}