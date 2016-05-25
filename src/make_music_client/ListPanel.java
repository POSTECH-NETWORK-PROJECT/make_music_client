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

public class ListPanel {
	JPanel panel;
	DefaultListModel<String> rooms, users;
	ArrayList<String> roomNames;
	JList<String> roomList, userList;
	JScrollPane roomScroll, userScroll;
	
	public void refresh() throws IOException {
		String roomMsg, userMsg;
		rooms.clear();
		roomNames.clear();
		users.clear();
		
		MainFrame.server.sendShowRoomListSignalToServer();
		while ((roomMsg=MainFrame.server.getMessageFromServer()) != null) {
			if (roomMsg.indexOf("@END") == 0)
				break;
			
			roomNames.add(roomMsg);
			rooms.addElement(roomMsg.substring(0, roomMsg.indexOf(":")));
		}
		
		MainFrame.server.sendShowUserListSignalToServer();
		while ((userMsg=MainFrame.server.getMessageFromServer()) != null) {
			if (userMsg.indexOf("@END") == 0)
				break;
			
			users.addElement(userMsg);
		}
	}
	
	public ListPanel() throws IOException {
		panel = new JPanel();
		panel.setLayout(null);
		
		rooms = new DefaultListModel<String>();
		roomNames = new ArrayList<String>();
		
		users = new DefaultListModel<String>();
		
		refresh();
		
		JLabel roomLabel = new JLabel("Room List:");
		roomLabel.setBounds(25, 25, 400, 15);
		
		roomList = new JList<String>(rooms);
		roomScroll = new JScrollPane();
		roomScroll.setViewportView(roomList);
		roomScroll.setBounds(25, 40, 400, 275);
		
		JLabel userLabel = new JLabel("User List:");
		userLabel.setBounds(25, 335, 400, 15);
		
		userList = new JList<String>(users);
		userScroll = new JScrollPane();
		userScroll.setViewportView(userList);
		userScroll.setBounds(25, 350, 400, 125);
		
		JButton btnJoin = new JButton("Join Room");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (roomList.getSelectedIndex() == -1) return;
				
				try {
					System.out.println(roomNames.get(roomList.getSelectedIndex()).toString());
					String roomName = roomNames.get(roomList.getSelectedIndex());
					
					RoomPanel room = new RoomPanel(roomName.substring(roomName.indexOf(":") + 1));
					
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(room.panel);
					room.panel.requestFocus();
					MainFrame.frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});		
		btnJoin.setBounds(25, 500, 120, 50);
		
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
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainPanel main = new MainPanel();
				MainFrame.frame.getContentPane().remove(panel);
				MainFrame.frame.getContentPane().add(main.panel);
				MainFrame.frame.setVisible(true);
			}
		});
		btnBack.setBounds(305, 500, 120, 50);
		
		panel.add(roomLabel);
		panel.add(userLabel);
		panel.add(btnJoin);
		panel.add(btnRef);
		panel.add(btnBack);
		panel.add(roomScroll);
		panel.add(userScroll);
	}
}
