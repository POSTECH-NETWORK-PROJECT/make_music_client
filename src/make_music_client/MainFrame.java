package make_music_client;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
	public static MainFrame frame;
	
	public static void main(String[] args) {
		frame = new MainFrame();
	}
	
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);
		
		MainPanel main = new MainPanel();
		
		this.getContentPane().add(main.panel);
		setVisible(true);
	}
}
