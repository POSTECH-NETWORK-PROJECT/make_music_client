package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel {
	JPanel panel;
	
	public LoginPanel() {
		panel = new JPanel();
		panel.setLayout(null);
		
		JLabel label = new JLabel("사용할 ID를 입력해주세요");
		
		JTextField input = new JTextField();
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (input.getText() == null) {
					
				} else if (input.getText().equals("")) {
					
				} else {
					MainFrame.id = input.getText();
					
					MainFrame.server.sendMessageToServer(MainFrame.id);
					
					MainPanel main = new MainPanel();
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(main.panel);
					MainFrame.frame.setVisible(true);
				}
			}
		});
		
		label.setBounds(70, 20, 150, 15);
		input.setBounds(20, 65, 150, 18);
		btnLogin.setBounds(190, 59, 80, 30);
		
		panel.add(label);
		panel.add(input);
		panel.add(btnLogin);
	}
}
