package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// A panel for showing other clients and the list of rooms
public class ListPanel {
	JPanel panel;
	DefaultListModel<String> rooms, users; // contains only room name and client name
	ArrayList<String> roomNames; // contains full string: ID and IP address
	JList<String> roomList, userList; // list component which will be added to the panel
	JScrollPane roomScroll, userScroll; // when there are many list entries larger than size, make it 
	
	// update room list and client list
	public void refresh() throws IOException {
		String roomMsg, userMsg;
		
		// clear to get up-to-data info.
		rooms.clear();
		roomNames.clear();
		users.clear();
		
		// send room list data request to the server, and parse it to room string
		MainFrame.server.sendShowRoomListSignalToServer();
		while ((roomMsg=MainFrame.server.getMessageFromServer()) != null) {
			if (roomMsg.indexOf("@END") == 0)
				break;
			
			roomNames.add(roomMsg);
			rooms.addElement(roomMsg.substring(0, roomMsg.indexOf(":")));
		}
		
		// send user list data request to the server, and parse it to user string
		MainFrame.server.sendShowUserListSignalToServer();
		while ((userMsg=MainFrame.server.getMessageFromServer()) != null) {
			if (userMsg.indexOf("@END") == 0)
				break;
			
			users.addElement(userMsg);
		}
	}
	
	public ListPanel() throws IOException {
		// create instances for variables required
		panel = new JPanel();
		panel.setLayout(null);
		
		rooms = new DefaultListModel<String>();
		roomNames = new ArrayList<String>();
		
		users = new DefaultListModel<String>();
		
		refresh();
		
		JLabel roomLabel = new JLabel("Room List:");
		roomLabel.setBounds(25, 25, 400, 15);
		
		// create room list component
		roomList = new JList<String>(rooms);
		roomScroll = new JScrollPane();
		roomScroll.setViewportView(roomList);
		roomScroll.setBounds(25, 40, 400, 275);
		
		JLabel userLabel = new JLabel("User List:");
		userLabel.setBounds(25, 335, 400, 15);
		
		// create other clients list component
		userList = new JList<String>(users);
		userScroll = new JScrollPane();
		userScroll.setViewportView(userList);
		userScroll.setBounds(25, 350, 400, 125);
		
		// join button for entering the room
		JButton btnJoin = new JButton("Join Room");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (roomList.getSelectedIndex() == -1) return; // when no entry is selected
				
				try {
					MainFrame.state = MainFrame.State.PARTICIPANT;
					
					// get selected entry
					System.out.println(roomNames.get(roomList.getSelectedIndex()).toString());
					String roomName = roomNames.get(roomList.getSelectedIndex());
					
					// panel switching
					RoomPanel room = new RoomPanel(roomName.substring(roomName.indexOf(":") + 1));
					
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(room.panel);
					room.panel.requestFocus(); // to give focus to keyboard
					MainFrame.frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});		
		btnJoin.setBounds(25, 500, 120, 50);
		
		// refresh button for refreshing room list and client list
		JButton btnRef = new JButton("Refresh");
		btnRef.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRef.setBounds(165, 500, 120, 50);
		
		// back button for returning to the main
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// panel switching
				MainPanel main = new MainPanel();
				MainFrame.frame.getContentPane().remove(panel);
				MainFrame.frame.getContentPane().add(main.panel);
				MainFrame.frame.setVisible(true);
			}
		});
		btnBack.setBounds(305, 500, 120, 50);
		
		// add components
		panel.add(roomLabel);
		panel.add(userLabel);
		panel.add(btnJoin);
		panel.add(btnRef);
		panel.add(btnBack);
		panel.add(roomScroll);
		panel.add(userScroll);
	}
}
