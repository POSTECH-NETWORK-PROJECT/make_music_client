package make_music_client;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

// Main frame for containing displays
public class MainFrame extends JFrame{
	enum State {HOST, PARTICIPANT};
	
	public static MainFrame frame; 
	public static String id; // ID of the client
	public static RoomInterface sock; // room interface for client
	public static ServerInterface server; // the server to connect
	public static Boolean sendFlag;
	public static State state; // State of client (whether HOST 
	private ImageIcon icon;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		sendFlag = new Boolean(true);
		server = new ServerInterface("141.223.204.46", 10002, sendFlag); // connect to the server
		
		frame = new MainFrame("Make Music");
	}
	
	public MainFrame(String title) {
		super(title);
		
		// set frame attributes
		setSize(290, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); 
		
		// Create a panel for login display
		LoginPanel login = new LoginPanel();
		icon = new ImageIcon("res/icon.png");
		this.setIconImage(icon.getImage());
		getContentPane().add(login.panel);
		setVisible(true); 
	}
}

