package make_music_client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RoomPanel {
	final private int SYNTH_NOTE_VELOCITY = 120;
	
	JPanel panel;
	JScrollPane noticeScroll;
	DefaultListModel<String> members, notices;
	JList<String> memberList, noticeList;
	
	private Sequencer sequencer;
	private Synthesizer synth;
	private MidiChannel synthChannel;
	private int instruCode = 0;
	
	private Map<Integer, Integer> keyMap;
	
	private RoomInterface roomInterface;
	private	RoomInputThread mainInputThread;
	
	public RoomPanel(String address) {
		MainFrame.frame.setSize(700, 600);
		
		panel = new JPanel();
		panel.setLayout(null);
		panel.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				panel.requestFocus();
			}
		});
		JLabel memberLabel = new JLabel("Members:");
		
		members = new DefaultListModel<String>();
		memberList = new JList<String>(members);
		
		JLabel noticeLabel = new JLabel("Announcement:");
		
		noticeScroll = new JScrollPane();
		notices = new DefaultListModel<String>();
		noticeList = new JList<String>(notices);
		noticeScroll.setViewportView(noticeList);
		noticeScroll.setBounds(25, 150, 400, 230);
		
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
	
			synth = MidiSystem.getSynthesizer();
			synth.open();
			
			MidiChannel[] channels = synth.getChannels();
			synthChannel = channels[channels.length - 1];
			synthChannel.programChange(instruCode);
			
			roomInterface = new RoomInterface(address, 10001);
			mainInputThread = new RoomInputThread(roomInterface, synthChannel, panel);
			mainInputThread.start();
			roomInterface.sendMessageToServer(MainFrame.id);
		} catch (MidiUnavailableException e) {
			sequencer = null;
			return;
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		SpinnerModel instrument = new SpinnerNumberModel(0, 0, 127, 1);
		JSpinner spinner = new JSpinner(instrument);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeInstrument((int)((JSpinner)e.getSource()).getValue());
			}
		});
		spinner.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int temp = new Integer(((Integer)spinner.getValue()).intValue() - e.getWheelRotation());
				if(temp < 0) temp = 0;
				else if(temp > 127) temp = 127;
				spinner.setValue(temp);
			}
		});
		((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
		((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setFont(new Font("SansSerif", Font.BOLD, 50));
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MainFrame.state == MainFrame.State.HOST) {
					try {
						roomInterface.sendQuitToRoom();
						MainPanel.manageRoomThread.getRoom().close();
						MainFrame.server.sendRemoveRoomSignalToServer();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					MainPanel main = new MainPanel();
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(main.panel);
					MainFrame.frame.setVisible(true);
				} else {
					roomInterface.sendExitToRoom();
					
					MainPanel main = new MainPanel();
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(main.panel);
					MainFrame.frame.setVisible(true);
				}
			}
		});
		
		JButton btnZ = new JButton("Z");
		btnZ.setBackground(Color.WHITE);
		JButton btnS = new JButton("S");
		btnS.setBackground(Color.BLACK);
		btnS.setForeground(Color.WHITE);
		JButton btnX = new JButton("X");
		btnX.setBackground(Color.WHITE);
		JButton btnD = new JButton("D");
		btnD.setBackground(Color.BLACK);
		btnD.setForeground(Color.WHITE);
		JButton btnC = new JButton("C");
		btnC.setBackground(Color.WHITE);
		
		JButton btnV = new JButton("V");
		btnV.setBackground(Color.WHITE);
		JButton btnG = new JButton("G");
		btnG.setBackground(Color.BLACK);
		btnG.setForeground(Color.WHITE);
		JButton btnB = new JButton("B");
		btnB.setBackground(Color.WHITE);
		JButton btnH = new JButton("H");
		btnH.setBackground(Color.BLACK);
		btnH.setForeground(Color.WHITE);
		JButton btnN = new JButton("N");
		btnN.setBackground(Color.WHITE);
		JButton btnJ = new JButton("J");
		btnJ.setBackground(Color.BLACK);
		btnJ.setForeground(Color.WHITE);
		JButton btnM = new JButton("M");
		btnM.setBackground(Color.WHITE);
		
		JButton btnQ = new JButton("Q");
		btnQ.setBackground(Color.WHITE);
		JButton btn2 = new JButton("2");
		btn2.setBackground(Color.BLACK);
		btn2.setForeground(Color.WHITE);
		JButton btnW = new JButton("W");
		btnW.setBackground(Color.WHITE);
		JButton btn3 = new JButton("3");
		btn3.setBackground(Color.BLACK);
		btn3.setForeground(Color.WHITE);
		JButton btnE = new JButton("E");
		btnE.setBackground(Color.WHITE);
		
		JButton btnR = new JButton("R");
		btnR.setBackground(Color.WHITE);
		JButton btn5 = new JButton("5");
		btn5.setBackground(Color.BLACK);
		btn5.setForeground(Color.WHITE);
		JButton btnT = new JButton("T");
		btnT.setBackground(Color.WHITE);
		JButton btn6 = new JButton("6");
		btn6.setBackground(Color.BLACK);
		btn6.setForeground(Color.WHITE);
		JButton btnY = new JButton("Y");
		btnY.setBackground(Color.WHITE);
		JButton btn7 = new JButton("7");
		btn7.setBackground(Color.BLACK);
		btn7.setForeground(Color.WHITE);
		JButton btnU = new JButton("U");
		btnU.setBackground(Color.WHITE);
		
		JButton btnI = new JButton("I");
		btnI.setBackground(Color.WHITE);
		JButton btn9 = new JButton("9");
		btn9.setBackground(Color.BLACK);
		btn9.setForeground(Color.WHITE);
		JButton btnO = new JButton("O");
		btnO.setBackground(Color.WHITE);
		JButton btn0 = new JButton("0");
		btn0.setBackground(Color.BLACK);
		btn0.setForeground(Color.WHITE);
		JButton btnP = new JButton("P");
		btnP.setBackground(Color.WHITE);
		
		JButton btn10 = new JButton("[");
		btn10.setBackground(Color.WHITE);
		JButton btn11 = new JButton("=");
		btn11.setBackground(Color.BLACK);
		btn11.setForeground(Color.WHITE);
		JButton btn12 = new JButton("]");
		btn12.setBackground(Color.WHITE);
		JButton btn13 = new JButton("\\");
		btn13.setBackground(Color.BLACK);
		btn13.setForeground(Color.WHITE);
		
		int left = 175;
		int top = 325;
		
		btnZ.setBounds(left + 0, top + 50, 50, 50);
		btnS.setBounds(left + 25, top, 50, 50);
		btnX.setBounds(left + 50, top + 50, 50, 50);
		btnD.setBounds(left + 75, top, 50, 50);
		btnC.setBounds(left + 100, top + 50, 50, 50);
	
		btnV.setBounds(left + 150, top + 50, 50, 50);
		btnG.setBounds(left + 175, top, 50, 50);
		btnB.setBounds(left + 200, top + 50, 50, 50);
		btnH.setBounds(left + 225, top, 50, 50);
		btnN.setBounds(left + 250, top + 50, 50, 50);
		btnJ.setBounds(left + 275, top, 50, 50);
		btnM.setBounds(left + 300, top + 50, 50, 50);
		
		int left2 = 37;
		int top2 = 175;
		
		btnQ.setBounds(left2, top2 + 50, 50, 50);
		btn2.setBounds(left2 + 25, top2, 50, 50);
		btnW.setBounds(left2 + 50, top2 + 50, 50, 50);
		btn3.setBounds(left2 + 75, top2, 50, 50);
		btnE.setBounds(left2 + 100, top2 + 50, 50, 50);
		
		btnR.setBounds(left2 + 150, top2 + 50, 50, 50);
		btn5.setBounds(left2 + 175, top2, 50, 50);
		btnT.setBounds(left2 + 200, top2 + 50, 50, 50);
		btn6.setBounds(left2 + 225, top2, 50, 50);
		btnY.setBounds(left2 + 250, top2 + 50, 50, 50);
		btn7.setBounds(left2 + 275, top2, 50, 50);
		btnU.setBounds(left2 + 300, top2 + 50, 50, 50);
		
		btnI.setBounds(left2 + 350, top2 + 50, 50, 50);
		btn9.setBounds(left2 + 375, top2, 50, 50);
		btnO.setBounds(left2 + 400, top2 + 50, 50, 50);
		btn0.setBounds(left2 + 425, top2, 50, 50);
		btnP.setBounds(left2 + 450, top2 + 50, 50, 50);
		
		btn10.setBounds(left2 + 500, top2 + 50, 50, 50);
		btn11.setBounds(left2 + 525, top2, 50, 50);
		btn12.setBounds(left2 + 550, top2 + 50, 50, 50);
		btn13.setBounds(left2 + 575, top2, 50, 50);
		
		btnZ.setEnabled(false);
		btnS.setEnabled(false);
		btnX.setEnabled(false);
		btnD.setEnabled(false);
		btnC.setEnabled(false);
		
		btnV.setEnabled(false);
		btnG.setEnabled(false);
		btnB.setEnabled(false);
		btnH.setEnabled(false);
		btnN.setEnabled(false);
		btnJ.setEnabled(false);
		btnM.setEnabled(false);
		
		btnQ.setEnabled(false);
		btn2.setEnabled(false);
		btnW.setEnabled(false);
		btn3.setEnabled(false);
		btnE.setEnabled(false);
		
		btnR.setEnabled(false);
		btn5.setEnabled(false);
		btnT.setEnabled(false);
		btn6.setEnabled(false);
		btnY.setEnabled(false);
		btn7.setEnabled(false);
		btnU.setEnabled(false);
		
		btnI.setEnabled(false);
		btn9.setEnabled(false);
		btnO.setEnabled(false);
		btn0.setEnabled(false);
		btnP.setEnabled(false);
		btn10.setEnabled(false);
		btn11.setEnabled(false);
		btn12.setEnabled(false);
		btn13.setEnabled(false);
		
		panel.add(btnZ);
		panel.add(btnS);
		panel.add(btnX);
		panel.add(btnD);
		panel.add(btnC);
		panel.add(btnV);
		panel.add(btnG);
		panel.add(btnB);
		panel.add(btnH);
		panel.add(btnN);
		panel.add(btnJ);
		panel.add(btnM);
		panel.add(btnQ);
		panel.add(btn2);
		panel.add(btnW);
		panel.add(btn3);
		panel.add(btnE);
		panel.add(btnR);
		panel.add(btn5);
		panel.add(btnT);
		panel.add(btn6);
		panel.add(btnY);
		panel.add(btn7);
		panel.add(btnU);
		panel.add(btnI);
		panel.add(btn9);
		panel.add(btnO);
		panel.add(btn0);
		panel.add(btnP);
		panel.add(btn10);
		panel.add(btn11);
		panel.add(btn12);
		panel.add(btn13);
		
		memberLabel.setBounds(25, 25, 300, 15);
		memberList.setBounds(25, 40, 300, 75);
		
		noticeLabel.setBounds(375, 25, 300, 15);
		noticeScroll.setBounds(375, 40, 300, 75);
		
		spinner.setBounds(145, 500, 180, 50);
		btnBack.setBounds(375, 500, 180, 50);
		
		initKeyMap();
		panel.addKeyListener(new CustomKeyListener());
		panel.add(memberLabel);
		panel.add(noticeLabel);
		panel.add(btnBack);
		panel.add(memberList);
		panel.add(noticeScroll);
		panel.add(spinner);
	}
	
	private void changeInstrument(int code) {
		if (code < 0 || code >= 128) return;
		
		instruCode = code;		
		synthChannel.programChange(instruCode);	
	}
	
	protected void finalize() throws Throwable {
		if (synth != null)
			synth.close();
		if (sequencer != null)
			sequencer.close();
		super.finalize();
	}
	
	public void play(Sequence sequence) {
		if (sequencer == null) return;
		
		sequencer.stop();
		sequencer.close();
		try {
			sequencer.open();
		} catch (MidiUnavailableException e) {
			return;
		}
		
		try {
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(0);
			sequencer.start();
		} catch (InvalidMidiDataException e) {

		}
	}
	
	public void stop() {
		if (sequencer == null) return;
		sequencer.stop();
	}
	
	private void initKeyMap() {
		keyMap = new HashMap<Integer, Integer>();
		keyMap.put(KeyEvent.VK_Z, 12 * 0 + 0); // C4
		keyMap.put(KeyEvent.VK_S, 12 * 0 + 1);
		keyMap.put(KeyEvent.VK_X, 12 * 0 + 2);
		keyMap.put(KeyEvent.VK_D, 12 * 0 + 3);
		keyMap.put(KeyEvent.VK_C, 12 * 0 + 4);
		keyMap.put(KeyEvent.VK_V, 12 * 0 + 5);
		keyMap.put(KeyEvent.VK_G, 12 * 0 + 6);
		keyMap.put(KeyEvent.VK_B, 12 * 0 + 7);    
		keyMap.put(KeyEvent.VK_H, 12 * 0 + 8);
		keyMap.put(KeyEvent.VK_N, 12 * 0 + 9);
		keyMap.put(KeyEvent.VK_J, 12 * 0 + 10);
		keyMap.put(KeyEvent.VK_M, 12 * 0 + 11);
		keyMap.put(KeyEvent.VK_Q, 12 * 1 + 0); // C5
		keyMap.put(KeyEvent.VK_2, 12 * 1 + 1);
		keyMap.put(KeyEvent.VK_W, 12 * 1 + 2);
		keyMap.put(KeyEvent.VK_3, 12 * 1 + 3);
		keyMap.put(KeyEvent.VK_E, 12 * 1 + 4);
		keyMap.put(KeyEvent.VK_R, 12 * 1 + 5);
		keyMap.put(KeyEvent.VK_5, 12 * 1 + 6);
		keyMap.put(KeyEvent.VK_T, 12 * 1 + 7);
		keyMap.put(KeyEvent.VK_6, 12 * 1 + 8);
		keyMap.put(KeyEvent.VK_Y, 12 * 1 + 9);
		keyMap.put(KeyEvent.VK_7, 12 * 1 + 10);
		keyMap.put(KeyEvent.VK_U, 12 * 1 + 11);
		keyMap.put(KeyEvent.VK_I, 12 * 2 + 0); // C6
		keyMap.put(KeyEvent.VK_9, 12 * 2 + 1);
		keyMap.put(KeyEvent.VK_O, 12 * 2 + 2);
		keyMap.put(KeyEvent.VK_0, 12 * 2 + 3);
		keyMap.put(KeyEvent.VK_P, 12 * 2 + 4);
		keyMap.put(KeyEvent.VK_OPEN_BRACKET, 12 * 2 + 5);
		keyMap.put(KeyEvent.VK_EQUALS, 12 * 2 + 6);
		keyMap.put(KeyEvent.VK_CLOSE_BRACKET, 12 * 2 + 7);
		keyMap.put(KeyEvent.VK_BACK_SPACE, 12 * 2 + 8);
		keyMap.put(KeyEvent.VK_BACK_SLASH, 12 * 2 + 9);
	}
	
	class CustomKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			if (keyMap.containsKey(keyCode)) {
				roomInterface.sendSoundToRoom(Integer.toString(60 + keyMap.get(keyCode)) + ":" + instruCode);
				//synthChannel.noteOn(60 + keyMap.get(keyCode), SYNTH_NOTE_VELOCITY);
			}
		}
		
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			if (keyMap.containsKey(keyCode)) {
				roomInterface.sendSoundToRoom("/mute " + Integer.toString(60 + keyMap.get(keyCode)));
				//synthChannel.noteOff(60 + keyMap.get(keyCode), 127);
			}
		}
	}
	
	/* RoomInputThread
	 * Print the message received by server. */
	class RoomInputThread extends Thread{
	   private RoomInterface sock = null;
	   private MidiChannel synth = null;
	   private JPanel panel = null;
	   private int instru = 0;
	   
	   public RoomInputThread(RoomInterface sock, MidiChannel synth, JPanel panel){
	      this.sock = sock;
	      this.synth = synth;
	      this.panel = panel;
	   }
	   
	   public void run(){
	      try{
	         String line = null;
	         while((line=sock.getMessageFromServer()) != null){
	            // line is message from server.
	            System.out.println(line);
	            if (line.indexOf("/notice") == 0) {
	            	members.clear();
	            	sock.sendShowMemberListToRoom();
	            	int start = line.indexOf(" ") + 1;
	            	System.out.println(line.substring(start));
	            	notices.addElement(line.substring(start));
	            } else if (line.indexOf("/mute") == 0) {
	            	int start = line.indexOf(" ") + 1;
	            	synth.noteOff(Integer.parseInt(line.substring(start)), 127);
	            } else if (line.indexOf("/member") == 0) {
	            	int start = line.indexOf(" ") + 1;
	            	String[] arr = line.substring(start).split(":");
	            	
	            	for (int i = 0; i < arr.length; i++)
	            		members.addElement(arr[i]);
	            } else if (line.equals("/quit")) {
	            	if (MainFrame.state != MainFrame.State.HOST) {
		            	MainPanel main = new MainPanel();
						MainFrame.frame.getContentPane().remove(panel);
						MainFrame.frame.getContentPane().add(main.panel);
						MainFrame.frame.setVisible(true);
					
						JOptionPane.showMessageDialog(null, "Host가 연결을 종료했습니다");
	            	}
					
	            	break;
	            } else {
		            String[] info = line.split(":");
		            
		            changeChannel(Integer.parseInt(info[1]));
		            playNote(Integer.parseInt(info[0]));
	            }
	         }
	      } catch(Exception ex){
	    	  ex.printStackTrace();
	      } finally{
	         try{
	            if(sock != null)
	               sock.close();
	         } catch(Exception ex){
	        	 ex.printStackTrace();
	         }
	      }
	   }
	   
	   public void changeChannel(int program) {
		   instru = program;
		   synth.programChange(instru);
	   }
	   
	   public void playNote(int pitch) {
		   synth.noteOn(pitch, 120);
	   }
	}

}

