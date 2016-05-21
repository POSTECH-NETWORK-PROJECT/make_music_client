package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanel {
	JPanel panel;
	
	public MainPanel() {
		panel = new JPanel();
		
		JButton btnSolo = new JButton("SOLO");
		btnSolo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SoloPanel solo = new SoloPanel();
				MainFrame.frame.getContentPane().remove(panel);
				MainFrame.frame.getContentPane().add(solo.panel);
				MainFrame.frame.setVisible(true);
			}
		});
		
		JButton btnHost = new JButton("HOST");
		btnHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton btnParti = new JButton("PARTICIPANT");
		btnParti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton btnExit = new JButton("EXIT");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		panel.add(btnSolo);
		panel.add(btnHost);
		panel.add(btnParti);
		panel.add(btnExit);
	}
}
