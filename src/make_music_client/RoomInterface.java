package make_music_client;

import java.io.IOException;
import java.net.UnknownHostException;

public class RoomInterface extends SocketInterface {

   public RoomInterface(String address, int port) throws UnknownHostException, IOException {
      super(address, port);
   }
   
   // Construct interfaces between Room and Client.
   public void sendSoundToRoom(String sound){
      this.sendMessageToServer("/sound "+sound);
   }
   
   public void sendShowMemberListToRoom(){
	   this.sendMessageToServer("/showMemberList");
   }
   
   public void sendExitToRoom(){
      this.sendMessageToServer("/exit");
   }
   
   public void sendQuitToRoom(){
	   this.sendMessageToServer("/quit");
   }
}