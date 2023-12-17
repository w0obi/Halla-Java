package server;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Server1216 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JTextField port_tf;
   private JTextArea textArea = new JTextArea();
   private JButton startBtn = new JButton("서버 실행");
   private JButton stopBtn = new JButton("서버 중지");

   // 소켓 생성 및 연결 부분
   private ServerSocket serverSocket; // 서버 소켓
   private Socket cs; // 클라이언트 소켓
   private int port = 12345; // 기본 포트 번호

   // 기타 변수 관리
   private Vector<ClientInfo> clientVC = new Vector<ClientInfo>();
   private Vector<RoomInfo> roomVC = new Vector<RoomInfo>();

   public Server1216() {
      initializeGUI();
      setupActionListeners();
   }

   public void initializeGUI() {
      setTitle("Server Application");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(30, 100, 321, 370);

      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setContentPane(contentPane);
      contentPane.setLayout(new BorderLayout()); // 레이아웃 변경

      JPanel topPanel = new JPanel();
      contentPane.add(topPanel, BorderLayout.NORTH);

      JLabel lblNewLabel_2 = new JLabel("포트 번호");
      topPanel.add(lblNewLabel_2);

      port_tf = new JTextField();
      port_tf.setColumns(20);
      topPanel.add(port_tf);

      JPanel bottomPanel = new JPanel();
      contentPane.add(bottomPanel, BorderLayout.SOUTH);

      startBtn.setBounds(12, 286, 138, 23);
      bottomPanel.add(startBtn);

      stopBtn.setBounds(155, 286, 138, 23);
      bottomPanel.add(stopBtn);
      stopBtn.setEnabled(false);

      JScrollPane scrollPane = new JScrollPane();
      contentPane.add(scrollPane, BorderLayout.CENTER);

      textArea.setEditable(false);
      scrollPane.setViewportView(textArea);

      this.setVisible(true); // 화면 보이기
   }

   void setupActionListeners() {
      startBtn.addActionListener(e -> startServer());
      stopBtn.addActionListener(e -> stopServer());
   }

   @Override
   public void actionPerformed(ActionEvent e) { // 삭제 가능
      if (e.getSource() == startBtn) {
         startServer(); // 11-13

      } else if (e.getSource() == stopBtn) {
         stopServer();
      }
   }

   private void startServer() {
      try {
         port = Integer.parseInt(port_tf.getText().trim());
         serverSocket = new ServerSocket(port);
         textArea.append("서버가 포트 " + port + "에서 시작되었습니다.\n");
         startBtn.setEnabled(false);
         port_tf.setEditable(false);
         stopBtn.setEnabled(true);
         waitForClientConnection();
      } catch (NumberFormatException e) {
         textArea.append("잘못된 포트 번호입니다.\n");
      } catch (IOException e) {
         textArea.append("서버 시작 오류: " + e.getMessage() + "\n");
      }
   }

   private void stopServer() {
      // 연결된 모든 클라이언트에게 서버 종료 알림
      for (ClientInfo c : clientVC) {
         c.sendMsg("ServerShutdown/Bye");
         try {
            c.closeStreams();
         } catch (IOException e) {
            e.printStackTrace(); // 스트림 닫기 실패 시 예외 출력
         }
      }

      // 서버 소켓 닫기
      try {
         if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
         }
         roomVC.removeAllElements();
      } catch (IOException e) {
         e.printStackTrace(); // serverSocket 닫기 실패 시 예외 출력
      }

      // UI 컴포넌트 상태 업데이트
      startBtn.setEnabled(true);
      port_tf.setEditable(true);
      stopBtn.setEnabled(false);
   }

   private void waitForClientConnection() {
      new Thread(() -> {
         try {
            while (!serverSocket.isClosed()) { // serverSocket이 닫힐 때까지 반복
               textArea.append("클라이언트 Socket 접속 대기중\n");
               Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 수락
               textArea.append("클라이언트 Socket 접속 완료\n");

               ClientInfo client = new ClientInfo(clientSocket);
               client.start();
            }
         } catch (IOException e) {
            if (!serverSocket.isClosed()) {
               textArea.append("클라이언트 연결 수락 중 오류 발생: " + e.getMessage() + "\n");
            }
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
            dupAndInitNewClient();
         } catch (IOException e) {
            textArea.append("Error in communication: " + e.getMessage() + "\n");
         }

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

      private void dupAndInitNewClient() {
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
                     // textArea.append("OldClient: " + c.clientID + "\n");
                     sendMsg("OldClient/" + c.clientID);
                  }

                  // 새로운 클라이언트 정보가 중복되지 않는 경우, 해당 정보를 기존 클라이언트들에게 알림니다. (가입인사)
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
               textArea.append("통신 중 오류 발생: " + e.getMessage() + "\n");
               break; // 오류 발생 시 반복문 종료
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

         textArea.append(clientID + " 사용자로부터 수신한 메시지: " + str + "\n");
         System.out.println(clientID + " 사용자로부터 수신한 메시지: " + str);
         StringTokenizer st = new StringTokenizer(str, "/");
         String protocol = st.nextToken();
         String message = "";
         if (st.hasMoreTokens()) {
            message = st.nextToken();
         }

         switch (protocol) {
         case "Note":
            handleNoteProtocol(st, message);
            break;
         case "CreateRoom":
            handleCreateRoomProtocol(message);
            break;
         case "JoinRoom":
            handleJoinRoomProtocol(st, message);
            break;
         case "SendMsg":
            handleSendMsgProtocol(st, message);
            break;
         case "ClientExit":
            handleClientExitProtocol();
            break;
         case "ExitRoom":
            handleExitRoomProtocol(message);
            break;
         default:
            log("알 수 없는 프로토콜: " + protocol);
            break;
         }
      }

      private void handleNoteProtocol(StringTokenizer st, String recipientID) {
         String note = st.nextToken();

         // 해당 사용자에게 쪽지 전송
         for (ClientInfo c : clientVC) {
            if (c.clientID.equals(recipientID)) {
               c.sendMsg("Note/" + clientID + "/" + note);
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
            closeStreams(); // 스트림을 닫습니다.
            clientVC.remove(this); // 클라이언트 벡터에서 현재 클라이언트를 제거합니다.
            if (clientSocket != null && !clientSocket.isClosed()) {
               clientSocket.close(); // 클라이언트 소켓이 열려있는 경우, 닫습니다.
               textArea.append("Client socket closed.\n");
            }

            broadCast("ClientExit/" + clientID); // 모든 클라이언트에게 클라이언트의 종료를 알립니다.
            broadCast("ClientJlistUpdate/Update"); // 클라이언트 목록 업데이트를 알립니다.

         } catch (IOException e) {
            logError("사용자 로그아웃 중 오류 발생", e); // 로그아웃 중 발생한 오류를 기록합니다.
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
      new Server1216();
   }
}