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

class MusicThread extends Thread {
	private Socket sock;
	private String id;
	private String ipAddress;
	private BufferedReader inputStream;
	private PrintWriter outputStream;
	private HashMap<String, String> memberList;
	private HashMap<String, PrintWriter> outputStreamList;

	public MusicThread(Socket sock, HashMap<String, PrintWriter> outputStreamList, HashMap<String, String> memberList)
			throws IOException {
		this.sock = sock;
		this.outputStreamList = outputStreamList;
		this.memberList = memberList;
		this.outputStream = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.inputStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		id = inputStream.readLine();
		ipAddress = sock.getInetAddress().getHostAddress();

		synchronized (this.memberList) {
			this.memberList.put(ipAddress, id);
		}
		broadcast("/notice " + id + "님이 접속했습니다.");
		System.out.println("접속한 사용자의 아이디는 " + id + "입니다.");

		synchronized (outputStreamList) {
			outputStreamList.put(ipAddress, outputStream);
		}
	}

	public void run() {
		try {
			String line = null;

			while ((line = inputStream.readLine()) != null) {
				System.out.println(line);
				if (line.equals("/exit"))
					break;
				else if (line.indexOf("/sound") == 0) {
					int start = line.indexOf(" ") + 1;
					broadcast(line.substring(start));
				} else if (line.equals("/showMemberList")) {
					Iterator<String> it = memberList.values().iterator();
					String ids = it.next();
					while(it.hasNext())
						ids.concat(":"+it.next());
					outputStream.println("/member "+ids);
					outputStream.flush();
				} else if (line.equals("/quit")) {
					broadcast("/quit");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			synchronized (memberList) {
				memberList.remove(ipAddress);
			}
			synchronized (outputStreamList) {
				outputStreamList.remove(ipAddress);
			}
			broadcast("/notice " + id + " 님이 접속 종료했습니다.");
			try {
				if (sock != null)
					sock.close();
			} catch (Exception ex) {
			}
		}
	}

	public void broadcast(String msg) {
		synchronized (outputStreamList) {
			Collection collection = outputStreamList.values();
			Iterator iter = collection.iterator();
			PrintWriter pw;
			while (iter.hasNext()) {
				pw = (PrintWriter) iter.next();
				pw.println(msg);
				pw.flush();
			}

		}
	}
}