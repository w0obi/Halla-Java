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

//import server.ServerBook.RoomInfo;
//import server.ServerBook.UserInfo;

public class Server1205 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JTextField port_tf;
   private JTextArea textArea = new JTextArea();
   private JButton start_btn = new JButton("서버 실행");
   private JButton stop_btn = new JButton("서버 중지");

   // socket 생성 연결 부분
   private ServerSocket ss;   // server socket
   private Socket cs;         // client socket
   int port = 12345;          // Default PortNum

   // 기타 변수 관리
   private Vector<ClientInfo> clientVC = new Vector<ClientInfo>();
   private Vector<RoomInfo> roomVC = new Vector<RoomInfo>();
   
   public Server1205() {
      initializeGUI();
      setupActionListeners();
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

      start_btn.setBounds(12, 286, 138, 23);
      contentPane.add(start_btn);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(12, 10, 281, 210);
      contentPane.add(scrollPane);

      scrollPane.setViewportView(textArea);
      textArea.setEditable(false);

      stop_btn.setBounds(155, 286, 138, 23);
      contentPane.add(stop_btn);
      stop_btn.setEnabled(false);

      this.setVisible(true); // 화면 보이기
   }

   void setupActionListeners() { // 11-13
      start_btn.addActionListener(this);
      stop_btn.addActionListener(this);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == start_btn) {
         System.out.println("start button clicked");
         startServer(); // 11-13

      } else if (e.getSource() == stop_btn) {
         System.out.println("stop button clicked");
         stopServer();
      }
   }

   private void startServer() { // 11-13
      try {
         port = Integer.parseInt(port_tf.getText().trim());
         ss = new ServerSocket(port);
         textArea.append("Server started on port: " + port + "\n");
         waitForClientConnection();
      } 
      catch (NumberFormatException e) {
         textArea.append("Invalid port number.\n");
      } 
      catch (IOException e) {
         textArea.append("Error starting server: " + e.getMessage() + "\n");
      }
   }

   private void stopServer() { // 11-21
      try {
         if (ss != null && !ss.isClosed()) {
            ss.close();
            textArea.append("Server stopped.\n");
         }
      } catch (IOException e) {
         textArea.append("Error stopping server: " + e.getMessage() + "\n");
      }
   }

   private void waitForClientConnection() {
      new Thread(() -> {
         try {
            while (true) {
               textArea.append("Waiting for client connections...\n");
               cs = ss.accept();         //cs를 분실하면 클라이언트와 통신이 불가능
               textArea.append("Client connected.\n");
               ClientInfo client = new ClientInfo(cs);
               client.start();
            }
         }
         
         catch (IOException e) {
            textArea.append("Error accepting client connection: " + e.getMessage() + "\n");
         }
      }).start();
   }

   class ClientInfo extends Thread {
      private DataInputStream dis;
      private DataOutputStream dos;

      private Socket clientSocket;
      private String clientID = "";    // client ID
      private String roomID = "";       //   11-28

      public ClientInfo(Socket socket) {
         this.clientSocket = socket;
         clientCom();
      }

      private void clientCom() {
         try {
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());

            clientID = dis.readUTF(); // 클라이언트로부터 ID 수신
            textArea.append("new Client: " + clientID + "\n");

            // 기존 클라이언트 정보를 새로운 클라이언트에게 알림
            for (int i = 0; i < clientVC.size(); i++) {
               ClientInfo c = clientVC.elementAt(i);
               textArea.append("OldClient/: " + c.clientID + "\n");
               sendMsg("OldClient/" + c.clientID);
            }
            // 기존 방의 정보를 새로운 클라이언트에게 알림
            for (int i = 0; i < roomVC.size(); i++) {
               RoomInfo r = roomVC.elementAt(i);
               textArea.append("preRoom/: " + r.roomName + "\n");
               sendMsg("preRoom/" + r.roomName);
            }

            // 새 클라이언트 정보를 기존 클라이언트들에게 알림 (가입인사)
            broadcastMsg("NewClient/" + clientID);
            clientVC.add(this); // 신규 클라이언트 등록
         } 
         catch (IOException e) {
            textArea.append("Error in communication: " + e.getMessage() + "\n");
         }
      }

      private void broadcastMsg(String msg) {
         for (ClientInfo client : clientVC) {
            client.sendMsg(msg);
         }
      }

      void sendMsg(String msg) {
         try {
            dos.writeUTF(msg);
         } 
         catch (IOException e) { }
      }

      // 클라이언트로부터 데이터 수신
      public void run() {
         while (true) {
            recvMsg();
         }
      }

      public void recvMsg() { // 11-21
         String str = "";
         try {
            str = dis.readUTF();
         } catch (IOException e) {
            e.printStackTrace();
         }
         textArea.append(clientID + "사용자로부터 수신한 메시지: " + str + "\n");

         StringTokenizer st = new StringTokenizer(str, "/");
         String protocol = st.nextToken();
         String message = st.nextToken();

         // log("프로토콜: " + protocol);
         // log("내용: " + message);
         
         if ("Note".equals(protocol)) {
            handleNoteProtocol(st, message);
         } 
         
         else if ("CreateRoom".equals(protocol)) {
            handleCreateRoomProtocol(message);
         }
      }
      
      private void handleNoteProtocol(StringTokenizer st, String message) {
         String note = st.nextToken();

         // 해당 사용자에게 쪽지 전송
         for (ClientInfo u : clientVC) {
            if (u.clientID.equals(message)) {
               u.sendMsg("NoteS/" + clientID + "/" + note);
               break;
            }
         }
      }

      private void handleCreateRoomProtocol(String roomName) {
         RoomInfo r = new RoomInfo(roomName, this);
         roomVC.add(r);
         roomID = roomName;
         sendMsg("CreateRoom/" + roomName);      // 생성 요청자에게 전송
         broadCast("New_Room/" + roomName);      // 모든 클라이언트에게 전송
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
      new Server1205();
   }
}