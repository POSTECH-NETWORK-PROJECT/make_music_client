package make_music_client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class ManageRoomThread extends Thread{
   private ServerSocket room;
   private HashMap<String, String> memberList;
   private HashMap<String, PrintWriter> outputStreamList;
   
   public ManageRoomThread(ServerSocket room){
      this.room = room;
   }
   
   public void run(){
      try{
         System.out.println("[ROOM SERVER] 접속을 기다립니다. IP: "+InetAddress.getLocalHost().getHostAddress());
         // memberList and outputStreamList manage member and each outputStream.
         memberList = new HashMap<String, String>();
         outputStreamList = new HashMap<String, PrintWriter>();
         while(!Thread.currentThread().isInterrupted()){
            Socket sock = room.accept();
            // Multithread logic
            MusicThread t = new MusicThread(sock, outputStreamList, memberList);
            t.start();
         }
      } catch(SocketException e2){
         
      } catch(IOException e1){
         e1.printStackTrace();
      } finally{
         System.out.println("방장이 나갔습니다. 방이 폭파됩니다.");
         try{
            if(room != null)
               room.close();
         } catch(Exception ex){
            ex.printStackTrace();
         }
      }
   }
   
   public ServerSocket getRoom(){
      return room;
   }
}