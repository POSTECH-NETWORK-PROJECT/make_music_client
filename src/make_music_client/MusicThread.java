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
   private BufferedReader br;
   private HashMap<String, PrintWriter> hm;
   private boolean initFlag = false;
   
   public MusicThread(Socket sock, HashMap<String, PrintWriter> hm) throws IOException{
      this.sock = sock;
      this.hm = hm;
      PrintWriter pw = new PrintWriter(
            new OutputStreamWriter(sock.getOutputStream()));
      br = new BufferedReader(
            new InputStreamReader(sock.getInputStream()));
      id = br.readLine();
         
      broadcast("/notice "+id+"님이 접속했습니다.");
      System.out.println("접속한 사용자의 아이디는 "+id+"입니다.");
         
      synchronized(hm){
         hm.put(this.id, pw);
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
            } else if (line.equals("/quit")) {
            	broadcast("/quit");
            }
         }
      } catch(Exception e){
         System.out.println(e);
      } finally{
         synchronized(hm){
            hm.remove(id);
         }
         broadcast("/notice "+id+" 님이 접속 종료했습니다.");
         try{
            if(sock != null)
               sock.close();
         } catch(Exception ex){
         }
      }
   }
   
   public void sendmsg(String msg){
      int start = msg.indexOf(" ")+1;
      int end = msg.indexOf(" ", start);
      
      if(end != -1){
         String to = msg.substring(start, end);
         String msg2 = msg.substring(end+1);
         Object obj = hm.get(to);
         if(obj != null){
            PrintWriter pw = (PrintWriter)obj;
            pw.println(id+"님이 다음의 귓속말을 보냈습니다. :"+msg2);
            pw.flush();
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