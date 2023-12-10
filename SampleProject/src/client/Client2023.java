package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Client2023 extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 2L;
	// Login GUI 변수
	private JFrame loginGUI = new JFrame("Login"); // 11-19
	private JPanel loginJpanel;
	private JTextField ip_tf;
	private JTextField port_tf;
	private JTextField id_tf; // 클라이언트 ID
	private JLabel img_Label;
	private JButton loginBtn; // 11-13
	private String serverIP; // 11-14
	private int serverPort; // 11-14
	private String clientID; // 11-20 //클라이언트 ID

	// Main GUI 변수
	private JPanel contentPane;
	private JList<String> clientJlist = new JList(); // 전체 접속자 명단, 첫번째는 자기 자신 //11-20
	private JList<String> roomJlist = new JList(); // 11-21
	private JTextField msgTf;
	private JTextArea chatArea = new JTextArea(); // 채팅창 변수
	private JButton noteBtn = new JButton("쪽지 보내기"); // 11-27
	private JButton joinRoomBtn = new JButton("채팅방 참여");
	private JButton exitRoomBtn = new JButton("탈퇴");
	private JButton createRoomBtn = new JButton("방 만들기");
	private JButton sendBtn = new JButton("전송");
	private JButton clientExitBtn = new JButton("채팅종료");

	// 클라이언트 관리
	private Vector<String> clientVC = new Vector<>(); // 11-20
	private Vector<String> roomVC = new Vector<>(); // 11-21
	private String myRoom = ""; // 내가 참여한 채팅방 11-28

	// network 변수
	private Socket socket; // 11-14
	private DataInputStream dis;
	private DataOutputStream dos;

	// 기타
	StringTokenizer st;
	//private boolean stopped = false;
	private boolean socketEstablished = false;

	public Client2023() {
		initializeLoginGUI();
		initializeMainGUI();
		addActionListeners(); // 11-13
	}

	void initializeLoginGUI() {
		loginGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
		loginGUI.setBounds(100, 100, 385, 541); // 1
		loginJpanel = new JPanel();
		loginJpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		loginGUI.setLocationRelativeTo(null);
		loginGUI.setContentPane(loginJpanel); // 1
		loginJpanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Server IP");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
		lblNewLabel.setBounds(12, 244, 113, 31);
		loginJpanel.add(lblNewLabel);

		ip_tf = new JTextField();
		ip_tf.setBounds(135, 245, 221, 33);
		loginJpanel.add(ip_tf);
		ip_tf.setColumns(10);

		JLabel lblServerPort = new JLabel("Server Port");
		lblServerPort.setFont(new Font("굴림", Font.BOLD, 20));
		lblServerPort.setBounds(12, 314, 113, 31);
		loginJpanel.add(lblServerPort);

		port_tf = new JTextField();
		port_tf.setColumns(10);
		port_tf.setBounds(135, 312, 221, 33);
		loginJpanel.add(port_tf);

		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("굴림", Font.BOLD, 20));
		lblId.setBounds(12, 376, 113, 31);
		loginJpanel.add(lblId);

		id_tf = new JTextField();
		id_tf.setColumns(10);
		id_tf.setBounds(135, 377, 221, 33);
		loginJpanel.add(id_tf);

		loginBtn = new JButton("Login"); // 11-13
		loginBtn.setFont(new Font("굴림", Font.BOLD, 20));
		loginBtn.setBounds(12, 450, 344, 44);
		loginJpanel.add(loginBtn);

		try {
		    ImageIcon im = new ImageIcon("images/다람쥐.jpg");
		    img_Label = new JLabel(im);
		    img_Label.setBounds(12, 23, 344, 154);
		    loginJpanel.add(img_Label);
		} catch (Exception e) {
		    // 이미지 로딩에 실패한 경우 예외 처리
			JOptionPane.showMessageDialog(this, "image.", "Error", JOptionPane.ERROR_MESSAGE);
		    // 이 부분에 적절한 오류 처리를 추가하세요.
		}
		
		loginGUI.setVisible(true); // 1
	}

	void initializeMainGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 100, 510, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel 접속자 = new JLabel("전체 접속자");
		접속자.setBounds(12, 20, 73, 15);
		contentPane.add(접속자);
		clientJlist.setBounds(12, 45, 108, 107);
		contentPane.add(clientJlist); // 접속자 목록 JLIST

		clientExitBtn.setBounds(12, 162, 108, 23);
		contentPane.add(clientExitBtn); // 채팅 종료
		noteBtn.setBounds(12, 192, 108, 23);
		contentPane.add(noteBtn); // 쪽지 보내기

		JLabel 채팅방 = new JLabel("채팅방목록");
		채팅방.setBounds(12, 225, 97, 15);
		contentPane.add(채팅방);
		roomJlist.setBounds(12, 240, 108, 107);
		contentPane.add(roomJlist); // 채팅방 목록 JLIST

		joinRoomBtn.setBounds(6, 357, 60, 23);
		contentPane.add(joinRoomBtn); // 채팅방 참여
		joinRoomBtn.setEnabled(false); // 버튼 비활성화
		exitRoomBtn.setBounds(68, 357, 60, 23);
		contentPane.add(exitRoomBtn); // 채팅방 나감
		exitRoomBtn.setEnabled(false);
		createRoomBtn.setBounds(12, 386, 108, 23);
		contentPane.add(createRoomBtn); // 채팅방 생성

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(142, 16, 340, 363);
		contentPane.add(scrollPane);
		scrollPane.setColumnHeaderView(chatArea); // 채팅창
		chatArea.setEditable(false);

		msgTf = new JTextField();
		msgTf.setBounds(144, 387, 268, 21);
		contentPane.add(msgTf); // 대화 입력창
		msgTf.setColumns(10);
		msgTf.setEditable(false);
		sendBtn.setBounds(412, 386, 70, 23);
		contentPane.add(sendBtn);
		sendBtn.setEnabled(false); // 메시지 전송
		this.setVisible(false);
	}

	void addActionListeners() { // 11-13
		loginBtn.addActionListener(this);
		noteBtn.addActionListener(this); // 11-27
		joinRoomBtn.addActionListener(this);
		createRoomBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		exitRoomBtn.addActionListener(this); // 채팅방탈퇴 리스너
		msgTf.addKeyListener(this); // 메시지 전송 리스너
		clientExitBtn.addActionListener(this); // 채팅 종료 리스너
	}

	public void connectToServer() {
		if (!socketEstablished) {
			try {
				serverIP = ip_tf.getText().trim();
				serverPort = Integer.parseInt(port_tf.getText().trim());
				socket = new Socket(serverIP, serverPort);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
						JOptionPane.ERROR_MESSAGE);
			}
			if (socket != null) {
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					socketEstablished = true;

				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		sendMyClientID();
	}

	void sendMyClientID() {

		String msg = "";

		clientID = id_tf.getText().trim(); // 11-20
		// 처음 접속시에 자신의 ID를 서버에게 전송
		sendMsg(clientID);
		try {
			msg = dis.readUTF();
			if (msg.equals("DuplicateClientID")) {
				// ClientID 중복
				JOptionPane.showMessageDialog(this, "DuplicateClientID", "ID 중복", JOptionPane.ERROR_MESSAGE);
				id_tf.setText("");
				id_tf.requestFocus();
				return;
			}

		} catch (IOException e) {
		}
		InitializeAndRecvMsg();
	}

	void InitializeAndRecvMsg() {
		// Main GUI 표시 및 Login GUI 숨기기
		this.setVisible(true);
		this.loginGUI.setVisible(false);

		// clientListVC에 자신을 등록
		clientVC.add(clientID);
//		clientJlist.setListData(clientVC); // JLIST로 화면에 출력
		setTitle(clientID); // 11-28

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String msg = "";
					while (true) {
						msg = dis.readUTF();
						System.out.println("From Server: " + msg);
						recvMsg(msg);
					}
				} catch (IOException e) {
					// 클라이언트와의 연결이 끊어짐
					handleServerShutdown();
				}
			}
		});
		th.start();
	}
	

	
	

	void sendMsg(String msg) {
		try {
			dos.writeUTF(msg);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error sending message.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void recvMsg(String msg) { // 11-21

		st = new StringTokenizer(msg, "/");
		String protocol = st.nextToken();
		String message = st.nextToken();

		switch (protocol) {
		case "NewClient":
		case "OldClient":
			addClientToList(message); // 서버가 등록할 정보만 전송한다.
			break;
		case "NoteS": // 11-28
			String note = st.nextToken();
			showMessageBox(note, message + "님으로부터 쪽지");
			break;
		case "CreateRoom":
			handleCreateRoom(message);
			break;
		case "NewRoom":
		case "OldRoom": // 12-01
			handleAddRoomJlist(message);
			break;
		case "CreateRoomFail": // 12-01
			showErrorMessage("CreateRoomFail", "알림");
			break;
		case "JoinRoomMsg": // 12-01-1
			String msg2 = st.nextToken();
			appendToChatArea(message + " : " + msg2);
			break;
		case "JoinRoom":
			handleJoinRoom(message);
			break;
		case "SendMsg":
			String chatMsg = st.nextToken();
			appendToChatArea(message + "님이 전송 : " + chatMsg);
			break;
		case "ClientJlistUpdate": // 12-05
			xxxupdateClientJlist();
		case "RoomJlistUpdate": // 12-05
			System.out.println("RoomJlistUpdate");
			xxxupdateRoomJlist();
		case "ClientExit":
			removeClientFromJlist(message);
			break;
		case "ServerShutdown":
			handleServerShutdown();
			break;
		case "RoomOut":
			handleRoomOut(message);
			break;
		case "ExitRoomMsg":
			String exitMsg = st.nextToken();
			appendToChatArea(message + " : " + exitMsg);
			break;
		default:
			// 처리되지 않은 프로토콜에 대한 처리
			break;
		}

	}

	private void showMessageBox(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.CLOSED_OPTION);
	}

	private void addClientToList(String clientID) {
		clientVC.add(clientID);
		// clientJlist.setListData(clientVC);
	}

	private void xxxupdateClientJlist() {
		clientJlist.setListData(clientVC);
	}

	private void handleCreateRoom(String roomName) {
		myRoom = roomName;
		joinRoomBtn.setEnabled(false);
		createRoomBtn.setEnabled(false);
		exitRoomBtn.setEnabled(true);
		msgTf.setEditable(true);
		sendBtn.setEnabled(true);
		setTitle("사용자: " + clientID + "  채팅방: " + myRoom);
		chatArea.append(clientID + "님이 " + myRoom + "생성 및 가입\n");
	}

	private void handleAddRoomJlist(String roomName) {
		if (myRoom.equals("")) {
			joinRoomBtn.setEnabled(true);
		}
		roomVC.add(roomName);
		roomJlist.setListData(roomVC); // ZZZ1
	}

	private void xxxupdateRoomJlist() {
		// System.out.println("updateRoomJlist");
		roomJlist.setListData(roomVC); // ZZZ2
	}

	private void handleJoinRoom(String roomName) {
		myRoom = roomName;
		joinRoomBtn.setEnabled(false);
		createRoomBtn.setEnabled(false);
		exitRoomBtn.setEnabled(true);
		msgTf.setEditable(true);
		sendBtn.setEnabled(true);
		setTitle("사용자: " + clientID + "   채팅방: " + myRoom);
		chatArea.append(clientID + "님이 " + myRoom + " join.\n");
		showInfoMessage("joinRoom success", "알림");
	}

	private void removeClientFromJlist(String clientID) {
		clientVC.remove(clientID);
		// clientJlist.setListData(clientVC);
	}

	private void handleServerShutdown() {
		//stopped = true;
		try {
			socket.close();
			clientVC.removeAllElements();
			if (!myRoom.isEmpty()) {
				roomVC.removeAllElements();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private void handleRoomOut(String roomName) {
		System.out.println("remove Room " + roomName);
		roomVC.remove(roomName);
		if (roomVC.isEmpty()) {
			joinRoomBtn.setEnabled(false);
		}
		exitRoomBtn.setEnabled(false);
	}

	private void showErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void appendToChatArea(String message) {
		chatArea.append(message + "\n");
	}

	private void showInfoMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginBtn) {
			System.out.println("login button clicked");
			connectToServer(); // 11-19
		} else if (e.getSource() == noteBtn) { // 11-27
			System.out.println("note button clicked");
			handleNoteSendButtonClick();
		} else if (e.getSource() == createRoomBtn) {
			handleCreateRoomButtonClick();
		} else if (e.getSource() == joinRoomBtn) { // 12-01-1
			handleJoinRoomButtonClick();
		} else if (e.getSource() == sendBtn) { // 12-02
			handleSendButtonClick();
		} else if (e.getSource() == clientExitBtn) {
			handleClientExitButtonClick();
		} else if (e.getSource() == exitRoomBtn) {
			System.out.println("ExitRoomButtonClick");
			handleExitRoomButtonClick();
		}
	}

	public void handleNoteSendButtonClick() { // 11-28
		System.out.println("noteBtn clicked");
		String dstClient = (String) clientJlist.getSelectedValue();

		String note = JOptionPane.showInputDialog("보낼 메시지");
		if (note != null) {
			sendMsg("Note/" + dstClient + "/" + note);
			System.out.println("receiver : " + dstClient + " | 전송 노트 : " + note);
		}
	}

	private void handleCreateRoomButtonClick() {
		System.out.println("createRoomBtn clicked");

		String roomName = JOptionPane.showInputDialog("Enter Room Name:");
		if (roomName == null || roomName.trim().isEmpty()) {
			System.out.println("Room creation cancelled or no name entered");
			return;
		}
		sendMsg("CreateRoom/" + roomName.trim());
	}

	private void handleJoinRoomButtonClick() { // 12-01-1
		System.out.println("joinRoomBtn clicked");
		String roomName = (String) roomJlist.getSelectedValue();
		if (roomName != null) {
			sendMsg("JoinRoom/" + roomName);
		}
	}

	private void handleSendButtonClick() {
		if (!myRoom.isEmpty()) {
			sendMsg("SendMsg/" + myRoom + "/" + msgTf.getText().trim());
			msgTf.setText("");
			msgTf.requestFocus();
		}
	}

	private void handleClientExitButtonClick() {
		if (!myRoom.isEmpty())
			sendMsg("ExitRoom/" + myRoom); // 먼저 자기가 가입 채팅방 탈퇴
		sendMsg("ClientExit/Bye");
		System.out.println("ClientExit/Bye" + clientID);
		clientVC.removeAllElements();
		if (!myRoom.isEmpty()) {
			roomVC.removeAllElements();
		}
		closeSocket();
		System.exit(0);
	}

	private void closeSocket() {
		try {
			dos.close();
			dis.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void handleExitRoomButtonClick() {
		System.out.println("exitRoomBtn clicked");
		sendMsg("ExitRoom/" + myRoom);
		myRoom = "";
		exitRoomBtn.setEnabled(false);
		joinRoomBtn.setEnabled(roomVC.size() > 0);
		createRoomBtn.setEnabled(true);
		msgTf.setEditable(false);
		sendBtn.setEnabled(false);
		setTitle("사용자: " + clientID);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			if (!myRoom.isEmpty()) {
				sendMsg("SendMsg/" + myRoom + "/" + msgTf.getText().trim());
				msgTf.setText("");
				msgTf.requestFocus();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		new Client2023();
	}

}
