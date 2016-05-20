package make_music_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* This thread handle the connection between host and participant 
 * PARTICIPANT(client-side) <------------------ HOST(server-side) */
public class MakeRoomThread extends Thread{
	// Maximum number of member in the room is 5.
	private final int maxMemberNumber = 5;
	
	private String hostIP;
	private ServerSocket room;
	// 0 means Host, 1 means Participant.
	private HashMap<Integer, String> memberList;
	private HashMap<String, PrintWriter> outputStreamList;
	
	public MakeRoomThread(String hostIP){
		this.hostIP = hostIP;
	}
	
	public void run(){
		// Make connection between Host and Participant.
		try{
			this.room = new ServerSocket(10002);
			this.memberList = new HashMap<Integer, String>();
			this.outputStreamList = new HashMap<String, PrintWriter>();
			System.out.println("Creating Room is Complete.");
			
			System.out.println("Waiting...");
			while(true){
				// Make connection if there are empty seat.
				if(memberList.size() <= maxMemberNumber){
					Socket user = room.accept();
					if(user.getInetAddress().getHostAddress() == hostIP)
						memberList.put(new Integer(0), user.getInetAddress().getHostAddress());
					else
						memberList.put(new Integer(1), user.getInetAddress().getHostAddress());
					MusicThread t = new MusicThread(user, outputStreamList);
					t.start();
				}
				// If room is full, sleep 1 second and check whether there are empty seat.
				else{
					Thread.sleep(1000);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}

class MusicThread extends Thread{
	private Socket user;
	private BufferedReader br;
	private HashMap<String, PrintWriter> outputStreamList;
	
	public MusicThread(Socket user, HashMap<String, PrintWriter> outputStreamList){
		this.user = user;
		this.outputStreamList = outputStreamList;
		try{
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(user.getOutputStream()));
			br = new BufferedReader(
					new InputStreamReader(user.getInputStream()));
			
			// Manage all PrintWriter to outputStreamList
			synchronized(outputStreamList){
				outputStreamList.put(user.getInetAddress().getHostAddress(), pw);
			}
		} catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public void run(){
		try{
			String line = null;
			
			while((line = br.readLine()) != null){
				// participant가 host에게 보내는 것들에 대한 처리
				if(line.equals("/quitRoom"))
					break;
				else{
					broadcast(line);
				}
			}
		} catch(Exception e){
			System.out.println(e);
		} finally{
			synchronized(outputStreamList){
				outputStreamList.remove(user.getInetAddress().getHostAddress());
			}
			System.out.println("participant와 host 간 접속 종료");
			try{
				if(user != null)
					user.close();
			} catch(Exception ex){
			}
		}
	}
	
	public void broadcast(String msg){
		synchronized(outputStreamList){
			Collection collection = outputStreamList.values();
			Iterator iter = collection.iterator();
			while(iter.hasNext()){
				PrintWriter pw = (PrintWriter)iter.next();
				pw.println(msg);
				pw.flush();
			}
		}
	}
}
