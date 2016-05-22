package make_music_client;

public class ServerInputThread extends Thread {
   private SocketInterface sock;
   private Boolean sendFlag;
   
   public ServerInputThread(SocketInterface sock, Boolean sendFlag){
      this.sock = sock;
      this.sendFlag = sendFlag;
   }
   
   public void run(){
      try{
         String line = null;
         while((line=sock.getMessageFromServer()) != null){
            // line is message from server.
            if(line.indexOf("@END") == 0)
               sendFlag = Boolean.TRUE;
            else
               System.out.println(line);
         }
      } catch(Exception ex){
      } finally{
         try{
            if(sock != null)
               sock.close();
         } catch(Exception ex){
         }
      }
   }
}