package make_music_client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
		MainFrame.frame.setSize(1000, 600);
		
		panel = new JPanel();
		panel.setLayout(null);
		
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
		
		JButton btnTest = new JButton("CHANGE");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = JOptionPane.showInputDialog(null, "Type the code of an instrument you want");
				changeInstrument(Integer.parseInt(code));
				panel.requestFocus();
			}
		});
		
		JButton btnBack = new JButton("BACK");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (MainFrame.state == MainFrame.State.HOST) {
					try {
						roomInterface.sendMessageToServer("/quit");
						MainPanel.manageRoomThread.getRoom().close();
						MainFrame.server.sendRemoveRoomSignalToServer();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					
				} else {
					roomInterface.sendExitToRoom();
				}
			}
		});
		
		memberLabel.setBounds(25, 25, 400, 15);
		memberList.setBounds(25, 40, 400, 75);
		btnTest.setBounds(25, 500, 180, 50);
		btnBack.setBounds(245, 500, 180, 50);
		noticeLabel.setBounds(25, 135, 400, 15);
		noticeScroll.setBounds(25, 150, 450, 230);
		
		initKeyMap();
		panel.addKeyListener(new CustomKeyListener());
		panel.add(memberLabel);
		panel.add(btnTest);
		panel.add(noticeLabel);
		panel.add(btnBack);
		panel.add(memberList);
		panel.add(noticeScroll);
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
	            } else if (line.equals("/exit")) {
	            	MainPanel main = new MainPanel();
					MainFrame.frame.getContentPane().remove(panel);
					MainFrame.frame.getContentPane().add(main.panel);
					MainFrame.frame.setVisible(true);
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

