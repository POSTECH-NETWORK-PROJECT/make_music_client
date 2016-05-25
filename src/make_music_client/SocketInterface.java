package make_music_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketInterface {
   private Socket sock;
   private BufferedReader inputStream;
   private PrintWriter outputStream;
   
   // Socket setting.
   protected SocketInterface(String address, int port) throws UnknownHostException, IOException{
      sock = new Socket(address, port);
      inputStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      outputStream = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
   }
   
   // Basic send method from client to server.
   protected void sendMessageToServer(String msg){
      outputStream.println(msg);
      outputStream.flush();
   }
   
   // Basic receive method from server to client.
   protected String getMessageFromServer() throws IOException{
      return inputStream.readLine();
   }
   
   // Socket close method.
   protected void close() throws IOException{
      inputStream.close();
      outputStream.close();
      sock.close();
   }
}