package server;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server2023 extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField port_tf;
	private JTextArea textArea = new JTextArea();
	private JButton startBtn = new JButton("서버 실행");
	private JButton stopBtn = new JButton("서버 중지");

	// socket 생성 연결 부분
	private ServerSocket ss; // server socket
	private Socket cs; // client socket
	int port = 12345;

	// 기타 변수 관리
	private Vector<ClientInfo> clientVC = new Vector<ClientInfo>();
	private Vector<RoomInfo> roomVC = new Vector<RoomInfo>();

	public Server2023() {
		initializeGUI();
		setupActionListeners(); // 11-13
	}

	public void initializeGUI() {
		setTitle("Server Application");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(30, 100, 321, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("포트 번호");
		lblNewLabel_2.setBounds(12, 245, 57, 15);
		contentPane.add(lblNewLabel_2);

		port_tf = new JTextField();
		port_tf.setBounds(81, 242, 212, 21);
		contentPane.add(port_tf);
		port_tf.setColumns(10);

		startBtn.setBounds(12, 286, 138, 23);
		contentPane.add(startBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 281, 210);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		stopBtn.setBounds(155, 286, 138, 23);
		contentPane.add(stopBtn);
		stopBtn.setEnabled(false);

		this.setVisible(true); // 화면 보이기
	}

	void setupActionListeners() { // 11-13
		startBtn.addActionListener(this);
		stopBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == startBtn) {
			startServer(); // 11-13

		} else if (e.getSource() == stopBtn) {
			stopServer();
		}
	}

	private void startServer() { // 11-13
		try {
			port = Integer.parseInt(port_tf.getText().trim());
			ss = new ServerSocket(port);
			textArea.append("Server started on port: " + port + "\n");
			startBtn.setEnabled(false);
			port_tf.setEditable(false);
			stopBtn.setEnabled(true);
			waitForClientConnection();
		} catch (NumberFormatException e) {
			textArea.append("Invalid port number.\n");
		} catch (IOException e) {
			textArea.append("Error starting server: " + e.getMessage() + "\n");
		}
	}

	private void stopServer() { // 11-21

		// 서버 종료 시 모든 클라이언트에게 알림
		for (ClientInfo c : clientVC) {
			c.sendMsg("ServerShutdown/Bye");
			try {
				c.closeStreams();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			ss.close();
			roomVC.removeAllElements();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		startBtn.setEnabled(true);
		port_tf.setEditable(true);
		stopBtn.setEnabled(false);

	}

	private void waitForClientConnection() {
		new Thread(() -> {
			try {
				while (true) { // 11-22
					textArea.append("클라이언트 Socket 접속 대기중\n");
					cs = ss.accept(); // cs를 분실하면 클라이언트와 통신이 불가능
					textArea.append("클아이언트 Socket 접속 완료\n");
					ClientInfo client = new ClientInfo(cs);
					client.start();
				}
			} catch (IOException e) {
				textArea.append("Error accepting client connection: " + e.getMessage() + "\n");
			}
		}).start();
	}

	class ClientInfo extends Thread { // 11-21
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket clientSocket;
		private String clientID = ""; // client ID
		private String roomID = ""; // 11-28

		public ClientInfo(Socket socket) {
			try {
				this.clientSocket = socket;
				dis = new DataInputStream(clientSocket.getInputStream());
				dos = new DataOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				textArea.append("Error in communication: " + e.getMessage() + "\n");
			}
			clientCom();
		}

		// 클라이언트로부터 데이터 수신
		public void run() {
			try {
				String msg = "";
				while (true) {
					msg = dis.readUTF();
					recvMsg(msg);
				}
			} catch (IOException e) {
				// 클라이언트와의 연결이 끊어짐
				handleClientExitProtocol();
			}
		}

		private void clientCom() {
			while (true) {
				try {
					clientID = dis.readUTF(); // 클라이언트로부터 ID 수신

					// 중복 클라이언트 ID 검사
					boolean isDuplicate = false;
					for (int i = 0; i < clientVC.size(); i++) {
						ClientInfo c = clientVC.elementAt(i);
						if (c.clientID.equals(clientID)) {
							isDuplicate = true;
							break;
						}
					}

					if (isDuplicate) {
						// 중복된 클라이언트 ID인 경우 클라이언트에게 알림
						sendMsg("DuplicateClientID");
						// 클라이언트로부터 새로운 ID를 재전송받을 수 있도록 처리해야 합니다.
					} else {
						// 클라이언트 ID 중복 검사 패스
						sendMsg("GoodClientID");

						textArea.append("new Client: " + clientID + "\n");

						// 기존 클라이언트 정보를 새로운 클라이언트에게 알림
						for (int i = 0; i < clientVC.size(); i++) {
							ClientInfo c = clientVC.elementAt(i);
							//textArea.append("OldClient: " + c.clientID + "\n");
							sendMsg("OldClient/" + c.clientID);
						}

						// 중복이 아닌 경우 새 클라이언트 정보를 기존 클라이언트들에게 알림 (가입인사)
						broadCast("NewClient/" + clientID);

						// 자신에게 기존의 개설된 채팅 방 정보 전송
						for (RoomInfo r : roomVC) {
							sendMsg("OldRoom/" + r.roomName);
						}

						sendMsg("RoomJlistUpdate/Update"); // ZZZ1
						clientVC.add(this); // 신규 클라이언트 등록
						broadCast("ClientJlistUpdate/Update");
						break;
					}
				} catch (IOException e) {
					textArea.append("Error in communication: " + e.getMessage() + "\n");
				}
			}
		}

		void sendMsg(String msg) {

			try {
				dos.writeUTF(msg);
			} catch (IOException e) {
			}
		}

		public void recvMsg(String str) { // 11-21

			textArea.append(clientID + "사용자로부터 수신한 메시지: " + str + "\n");
			System.out.println(clientID + "사용자로부터 수신한 메시지: " + str);
			StringTokenizer st = new StringTokenizer(str, "/");
			String protocol = st.nextToken();
			String message = st.nextToken();

			if ("Note".equals(protocol)) { // 11-28
				handleNoteProtocol(st, message);
			} else if ("CreateRoom".equals(protocol)) {
				handleCreateRoomProtocol(message);

			} else if ("JoinRoom".equals(protocol)) {
				handleJoinRoomProtocol(st, message);
			} else if ("SendMsg".equals(protocol)) {
				handleSendMsgProtocol(st, message);
			} else if ("ClientExit".equals(protocol)) { // System.out.println("ClientExit/Bye" + clientID);
				handleClientExitProtocol();
			} else if ("ExitRoom".equals(protocol)) {
				handleExitRoomProtocol(message);
			} else {
				log("알 수 없는 프로토콜: " + protocol);
			}

		}

		private void handleNoteProtocol(StringTokenizer st, String message) {
			String note = st.nextToken();

			// 해당 사용자에게 쪽지 전송
			for (ClientInfo c : clientVC) {
				if (c.clientID.equals(message)) {
					c.sendMsg("NoteS/" + clientID + "/" + note);
					break;
				}
			}
		}

		private void handleCreateRoomProtocol(String roomName) {

			// 방 이름이 이미 존재하는지 확인 //12-01
			boolean roomExists = false;
			for (RoomInfo r : roomVC) {
				if (r.roomName.equals(roomName)) {
					roomExists = true;
					break;
				}
			}
			if (roomExists) {
				// 동일한 이름의 방이 이미 존재하는 경우 처리
				sendMsg("CreateRoomFail/OK");
			} else {
				RoomInfo r = new RoomInfo(roomName, this);
				roomVC.add(r);
				roomID = roomName;
				sendMsg("CreateRoom/" + roomName); // 생성 요청자에게 전송
				broadCast("NewRoom/" + roomName); // 모든 클라이언트들에게
				broadCast("RoomJlistUpdate/Update"); // zzz2

			}
			// sendMsg("RoomJlistUpdate/Update"); //12-5
		}

		private void handleJoinRoomProtocol(StringTokenizer st, String roomName) { // 12-01-1
			for (RoomInfo r : roomVC) {
				if (r.roomName.equals(roomName)) {
					r.broadcastRoomMsg("JoinRoomMsg/가입/***" + clientID + "님이 입장하셨습니다.********");
					r.RoomClientVC.add(this);
					roomID = roomName; // RoomName -->RoomID 변경 필요
					sendMsg("JoinRoom/" + roomName);
					break;
				}
			}
		}

		private void handleSendMsgProtocol(StringTokenizer st, String roomName) {
			String sendMsg = st.nextToken();
			for (RoomInfo r : roomVC) {
				if (r.roomName.equals(roomName)) {
					r.broadcastRoomMsg("SendMsg/" + clientID + "/" + sendMsg);
				}
			}
		}

		private void handleClientExitProtocol() {
			try {
				closeStreams();
				clientVC.remove(this);
				if (clientSocket != null && !clientSocket.isClosed()) {
					clientSocket.close();
					textArea.append("Client socket closed.\n");
				}

				broadCast("ClientExit/" + clientID);
				broadCast("ClientJlistUpdate/Update");

			} catch (IOException e) {
				logError("사용자 로그아웃 중 오류 발생", e);
			}
		}

		private void handleExitRoomProtocol(String roomName) {
			roomID = roomName;
			log(clientID + " 사용자가 " + roomName + " 방에서 나감");

			for (RoomInfo r : roomVC) {
				if (r.roomName.equals(roomName)) {
					r.broadcastRoomMsg("ExitRoomMsg/탈퇴/***" + clientID + "님이 채팅방에서 나갔습니다.********");
					r.RoomClientVC.remove(this);
					if (r.RoomClientVC.isEmpty()) {
						roomVC.remove(r);
						broadCast("RoomOut/" + roomName);
						broadCast("RoomJlistUpdate/Update"); // zzz4
					}
					break;
				}
			}
		}

		private void broadCast(String str) {
			for (ClientInfo c : clientVC) {
				c.sendMsg(str);
			}
		}

		private void log(String message) {
			System.out.println(clientID + ": " + message);
		}

		// 오류 로그 출력
		private void logError(String message, Exception e) {
			System.err.println(clientID + ": " + message);
			e.printStackTrace();
		}

		public void closeStreams() throws IOException {
			if (dos != null) {
				dos.close();
			}
			if (dis != null) {
				dis.close();
			}
			if (cs != null) {
				cs.close();
				textArea.append(clientID + " Client Socket 종료.\n");
			}
		}
	}

	class RoomInfo {
		private String roomName = "";
		private Vector<ClientInfo> RoomClientVC = new Vector<ClientInfo>();

		public RoomInfo(String name, ClientInfo c) {
			this.roomName = name;
			this.RoomClientVC.add(c);
		}

		public void broadcastRoomMsg(String message) {
			for (ClientInfo c : RoomClientVC) {
				c.sendMsg(message);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server2023();
	}

}
