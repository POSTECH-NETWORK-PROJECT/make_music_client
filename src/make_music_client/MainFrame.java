package make_music_client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	enum State {HOST, PARTICIPANT};
	
	public static MainFrame frame;
	public static String id;
	public static RoomInterface sock;
	public static ServerInterface server;
	public static Boolean sendFlag;
	public static State state;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		sendFlag = new Boolean(true);
		server = new ServerInterface("141.223.204.46", 10002, sendFlag);
		
		frame = new MainFrame();
	}
	
	public MainFrame() {
		setSize(450, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		//MainPanel main = new MainPanel();
		
		LoginPanel login = new LoginPanel();
		
		getContentPane().add(login.panel);
		setVisible(true);
	}
}

