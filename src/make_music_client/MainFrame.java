package make_music_client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame{
	enum State {HOST, PARTICIPANT};
	
	public static MainFrame frame;
	public static String id;
	public static RoomInterface sock;
	public static ServerInterface server;
	public static Boolean sendFlag;
	public static State state;
	private ImageIcon icon;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		sendFlag = new Boolean(true);
		server = new ServerInterface("141.223.204.46", 10002, sendFlag);
		
		frame = new MainFrame("Make Music");
	}
	
	public MainFrame(String title) {
		super(title);
		
		setSize(290, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		//MainPanel main = new MainPanel();
		
		LoginPanel login = new LoginPanel();
		icon = new ImageIcon("res/icon.png");
		this.setIconImage(icon.getImage());
		getContentPane().add(login.panel);
		setVisible(true);
	}
}

