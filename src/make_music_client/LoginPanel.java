package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel {
   JPanel panel;
   
   public LoginPanel() { //login panel
      panel = new JPanel();
      panel.setLayout(null);
      
      JLabel label = new JLabel("사용할 ID를 입력해주세요"); //text to inform that this is id panel
      
      JTextField input = new JTextField(); //input field for id
      
      JButton btnLogin = new JButton("LOGIN"); //login button
      btnLogin.addActionListener(new ActionListener() { //executed when the login button is pressed
         public void actionPerformed(ActionEvent e) {
            if (input.getText() == null) { //nothing happens when the input is null
               
            } else if (input.getText().equals("")) { //nothing happens when the input is empty string
               
            } else { //when id input comes
               MainFrame.id = input.getText(); //save id in main frame
               
               MainFrame.server.sendMessageToServer(MainFrame.id); //let server know id
               
               MainPanel main = new MainPanel(); //make main panel
               MainFrame.frame.getContentPane().remove(panel); //remove login panel
               MainFrame.frame.getContentPane().add(main.panel); //put new main panel to main frame
               MainFrame.frame.setVisible(true); //show main frame
            }
         }
      });
      
      //position and size of each component
      label.setBounds(70, 20, 150, 15);
      input.setBounds(20, 65, 150, 18);
      btnLogin.setBounds(190, 59, 80, 30);
      
      //put every component to login panel
      panel.add(label);
      panel.add(input);
      panel.add(btnLogin);
   }
}