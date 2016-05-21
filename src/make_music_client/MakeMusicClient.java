package make_music_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/* This main stream handle the connection between server and client(both host and participant)
 * SERVER <--------------------------------------------------------- CLIENT(HOST, PARTICIPANT) */
public class MakeMusicClient {
	private enum State { SELECT, ROOM, LIST };
	private enum MemberType { HOST, PARTICIPANT, UNDECIDED };
	
	public static void main(String[] args){
		// Get my Internet address.
		InetAddress inetaddr = null;
		try{
			inetaddr = InetAddress.getLocalHost();
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
		
		// Socket connection between server and client.
		Socket sock = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		MainInputThread it = null;
		boolean endFlag = false;
		
		// Socket connection between host and participant.
		Socket user = null;
		BufferedReader userbr = null;
		PrintWriter userpw = null;
		SubInputThread userit = null;
		boolean joinFlag = false;
		
		try{
			// Socket setting.
			sock = new Socket("141.223.151.179",10001);
			pw = new PrintWriter(
					new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			
			// Keyboard buffer setting.
			BufferedReader keyboard = new BufferedReader(
					new InputStreamReader(System.in));
			
			// Send client IP to server. 
			pw.println(inetaddr.getHostAddress());
			pw.flush();
			
			// Client handle information from server to this thread.
			it = new MainInputThread(sock, br);
			it.start();
			
			// Make state machine.
			State state = State.SELECT;
			MemberType memberType = MemberType.UNDECIDED;
						
			String line = null;
			
			MakeRoomThread mrt = null;
			
			while((line=keyboard.readLine()) != null){
				// Client is not in the room.
				if(!joinFlag){
					// Command host : change this member to host
					if(line.equals("/host")){
						memberType = MemberType.HOST;
					
						// Run MakeRoomThread.
						mrt = new MakeRoomThread(inetaddr.getHostAddress());
						mrt.start();
						// And Enter the room.
						try{
							user = new Socket(inetaddr.getHostAddress(), 10002);
							userbr = new BufferedReader(
									new InputStreamReader(user.getInputStream()));
							userpw = new PrintWriter(
									new OutputStreamWriter(user.getOutputStream()));
							System.out.println("host join to the room");
							// SubInputThread handle the message from room(host).
							userit = new SubInputThread(user, userbr);
							userit.start();
						
							joinFlag = true;
						} catch(Exception e){
							e.printStackTrace();
						}
					
						// Send command createRoom to server so that server recognize and add this room to roomList.
						pw.println("/createRoom");
						pw.flush();
					}
					// Command participant : change this member to client.
					else if(line.equals("/participant")){
						memberType = MemberType.PARTICIPANT;
						pw.println("/showRoomList");
						pw.flush();
					}
					// Command refresh : request roomList.
					else if(line.equals("/refresh")){
						pw.println("/showRoomList");
						pw.flush();
					}
					// Command join <IP> : join that room.
					else if(line.indexOf("/join") == 0){
						int start = line.indexOf(" ")+1;
						try{
							user = new Socket(line.substring(start),10002);
							userbr = new BufferedReader(
									new InputStreamReader(user.getInputStream()));
							userpw = new PrintWriter(
									new OutputStreamWriter(user.getOutputStream()));
							
							// SubInputThread handle the message from room(host).
							userit = new SubInputThread(user, userbr);
							userit.start();
							
							joinFlag = true;
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					// Command quit : quit the game.
					else if(line.equals("/quit")){
						endFlag = true;
						break;
					}
				}
				// Client is in the room.
				else{
					// Command quitRoom : quit the room.
					if(line.equals("/quitRoom")){
						try{
							if(memberType == MemberType.HOST){
								// Connection close between host(server) and all member in the room(client).
								pw.println("/closeAll");
								pw.flush();
							}
							else if(memberType == MemberType.PARTICIPANT){
								pw.println("/closeMe");
								pw.flush();
								if(userbr != null)
									userbr.close();
								if(userpw != null)
									userpw.close();
								if(user != null)
									user.close();
							}
							joinFlag = false;
						} catch(Exception e){
							e.printStackTrace();
						}
					}
					// Maybe sound signal.
					else{
						userpw.println(line);
						userpw.flush();
					}
				}
			}
		} catch(Exception ex){
			if(!endFlag)
				ex.printStackTrace();
		}
		
	}
}

/* MainInputThread
 * Print the message received by server. */
class MainInputThread extends Thread{
	private Socket sock = null;
	private BufferedReader br = null;
	public MainInputThread(Socket sock, BufferedReader br){
		this.sock = sock;
		this.br = br;
	}
	
	public void run(){
		try{
			String line = null;
			while((line=br.readLine()) != null){
				// line is message from server.
				System.out.println(line);
			}
		} catch(Exception ex){
		} finally{
			try{
				if(br != null)
					br.close();
			} catch(Exception ex){
			}
			try{
				if(sock != null)
					sock.close();
			} catch(Exception ex){
			}
		}
	}
}

/* SubInputThread
 * Print the message received by host. */
class SubInputThread extends Thread{
	private Socket sock = null;
	private BufferedReader br = null;
	public SubInputThread(Socket sock, BufferedReader br){
		this.sock = sock;
		this.br = br;
	}
		
	public void run(){
		try{
			String line = null;
			while((line=br.readLine()) != null){
				// 여기가 진짜 방 안에서 host랑 participant간 소통하는 부분
				// host에서 participant로 보낸 것을 처리하는 부분.
				
				System.out.println(line);
			}
		} catch(Exception ex){
		} finally{
			try{
				if(br != null)
					br.close();
			} catch(Exception ex){
			}
			try{
				if(sock != null)
					sock.close();
			} catch(Exception ex){
			}
		}
	}
}