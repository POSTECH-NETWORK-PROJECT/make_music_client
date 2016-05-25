package make_music_client;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPanel {
	JPanel panel;
	public static ManageRoomThread manageRoomThread;
	private ImageIcon logo; // main logo
	
	public MainPanel() {
		MainFrame.frame.setSize(450, 700);
		
		// create instances for variables
		panel = new JPanel();
		panel.setLayout(null);
		
		logo = new ImageIcon("res/makemusic.png");
		Image temp = logo.getImage();
		temp = temp.getScaledInstance(400, 400, java.awt.Image.SCALE_SMOOTH);
		logo = new ImageIcon(temp);
		
		JLabel mainlogo = new JLabel();
		mainlogo.setIcon(logo);;
		
		// create room button for hosting a room
		JButton btnHost = new JButton("Create Room");
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get user input for the name of a room
				String roomName = JOptionPane.showInputDialog(null, "방 이름을 입력해주세요");
				
				if (roomName == null) {
					// do nothing
				} else if (roomName.equals("")) {
					JOptionPane.showMessageDialog(null, "유효한 입력이 아닙니다"); // when null string comes
				} else {
					try {
						// Making room
						manageRoomThread = new ManageRoomThread(new ServerSocket(10001));
						manageRoomThread.start();
						MainFrame.server.sendAddRoomSignalToServer(roomName);
						MainFrame.state = MainFrame.State.HOST;
						
						// panel switching
						RoomPanel room = new RoomPanel(InetAddress.getLocalHost().getHostAddress());
						
						MainFrame.frame.getContentPane().remove(panel);
						MainFrame.frame.getContentPane().add(room.panel);
						room.panel.requestFocus(); // give focus to keyboard
						MainFrame.frame.setVisible(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		// join room button for switch into list panel
		JButton btnRoom = new JButton("Join Room");
		btnRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel switching
				try {
					ListPanel list = new ListPanel();
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(list.panel);
					MainFrame.frame.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// Exit button for end program
		JButton btnExit = new JButton("EXIT");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.server.sendQuitSignalToServer();
				System.exit(0);
			}
		});
		
		mainlogo.setBounds(25, 25, 400, 400);
		btnHost.setBounds(25, 450, 400, 50);
		btnRoom.setBounds(25, 525, 400, 50);
		btnExit.setBounds(25, 600, 400, 50);

		panel.add(btnHost);
		panel.add(btnRoom);
		panel.add(btnExit);
		panel.add(mainlogo);
	}
}
