package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

public class ListPanel {
	JPanel panel;
	DefaultListModel<String> rooms;
	ArrayList<String> roomNames;
	JList<String> roomList;
	
	public void refresh() throws IOException {
		String msg;
		rooms.clear();
		roomNames.clear();
		
		MainFrame.server.sendShowRoomListSignalToServer();
		while ((msg=MainFrame.server.getMessageFromServer()) != null) {
			if (msg.indexOf("@END") == 0)
				break;
			
			roomNames.add(msg);
			rooms.addElement(msg.substring(0, msg.indexOf(":")));
		}
	}
	
	public ListPanel() throws IOException {
		panel = new JPanel();
		rooms = new DefaultListModel<String>();
		roomNames = new ArrayList<String>();
		
		panel.setLayout(null);
		
		refresh();
		roomList = new JList<String>(rooms);
		roomList.setBounds(25, 25, 400, 475);
		
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
		
		panel.add(btnJoin);
		panel.add(btnRef);
		panel.add(btnBack);
		panel.add(roomList);
	}
}
