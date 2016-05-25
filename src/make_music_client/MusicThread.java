package make_music_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

class MusicThread extends Thread{
   private Socket sock;
   private String id;
   private String ipAddress;
   private BufferedReader br;
   private HashMap<String, String> memberList;
   private HashMap<String, PrintWriter> hm;
   private boolean initFlag = false;
   
   public MusicThread(Socket sock, HashMap<String, PrintWriter> hm, HashMap<String, String> memberList) throws IOException{
      this.sock = sock;
      this.hm = hm;
      this.memberList = memberList;
      PrintWriter pw = new PrintWriter(
            new OutputStreamWriter(sock.getOutputStream()));
      br = new BufferedReader(
            new InputStreamReader(sock.getInputStream()));
      id = br.readLine();
      ipAddress = sock.getInetAddress().getHostAddress();
      
      synchronized(this.memberList){
    	  this.memberList.put(ipAddress, id);
      }
      broadcast("/notice "+id+"님이 접속했습니다.");
      System.out.println("접속한 사용자의 아이디는 "+id+"입니다.");
         
      synchronized(hm){
         hm.put(ipAddress, pw);
      }
      initFlag = true;
   }
   
   public void run(){
      try{
         String line = null;
         
         while((line = br.readLine()) != null){
            System.out.println(line);
            if(line.equals("/exit"))
               break;
            else if(line.indexOf("/sound") == 0){
               int start = line.indexOf(" ")+1;
               broadcast(line.substring(start));
            }
            else if (line.equals("/quit")) {
            	broadcast("/quit");
            }
         }
      } catch(Exception e){
         System.out.println(e);
      } finally{
    	 synchronized(memberList){
    		 memberList.remove(ipAddress);
    	 }
         synchronized(hm){
            hm.remove(ipAddress);
         }
         broadcast("/notice "+id+" 님이 접속 종료했습니다.");
         try{
            if(sock != null)
               sock.close();
         } catch(Exception ex){
         }
      }
   }
   
   public void broadcast(String msg){
      synchronized(hm){
         Collection collection = hm.values();
         Iterator iter = collection.iterator();
         PrintWriter pw;
         while(iter.hasNext()){
            pw = (PrintWriter)iter.next();
            pw.println(msg);
            pw.flush();   
         }
         
      }
   }
}