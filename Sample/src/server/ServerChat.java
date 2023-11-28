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

public class ServerChat extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private JTextField port_tf;
   private JTextArea textArea = new JTextArea();
   private JButton start_btn = new JButton("서버 실행");
   private JButton stop_btn = new JButton("서버 중지");

   // socket 생성 연결 부분
   private ServerSocket ss; // server socket
   private Socket cs;      //client socket
   int port = 12345;

   // 기타 변수 관리
   private Vector<ClientInfo> clientVC = new Vector<ClientInfo>();

   public ServerChat() {
      initializeGUI();
      setupActionListeners(); // 11-13
   }

   public void initializeGUI() {
      setTitle("ServerChat Application");

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(30, 100, 321, 370);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
         textArea.append("ServerChat started on port: " + port + "\n");
         waitForClientConnection();
      } catch (NumberFormatException e) {
         textArea.append("Invalid port number.\n");
      } catch (IOException e) {
         textArea.append("Error starting server: " + e.getMessage() + "\n");
      }
   }

   private void stopServer() { // 11-21
      try {
         if (ss != null && !ss.isClosed()) {
            ss.close();
            textArea.append("ServerChat stopped.\n");
         }
      } catch (IOException e) {
         textArea.append("Error stopping server: " + e.getMessage() + "\n");
      }
   }

   private void waitForClientConnection() {
      new Thread(() -> {
         try {
            while (true) {            //11-22
               textArea.append("Waiting for client connections...\n");
               cs = ss.accept();        // cs를 분실하면 클라이언트와 통신이 불가능함.
               textArea.append("Client connected.\n");
               ClientInfo client = new ClientInfo(cs);
               client.start();          //ClientInfo에 대한 start
            }
         } catch (IOException e) {
            textArea.append("Error accepting client connection: " + e.getMessage() + "\n");
         }
      }).start();
   }
    /**
     * | 연결시 개별 스레드 실행   |
     * | 각각 개체를 Vector에 저장 |
     *
     * == 가입자 관리 절차 ==
     * 클라이언트 - 클라이언트 통신 불가, 서버 - 클라이언트 통신 O
     * 
     */
   class ClientInfo extends Thread { // 11-21
      private DataInputStream dis;
      private DataOutputStream dos;

      private Socket clientSocket;
      private String clientID = ""; // client ID

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
               sendMsg("OldClient/" + c.clientID);
            }

            // 새 클라이언트 정보를 기존 클라이언트들에게 알림 (가입인사)
            broadcastMsg("NewClient/" + clientID);

            clientVC.add(this); // 신규 클라이언트 등록
         } catch (IOException e) {
            textArea.append("Error in communication: " + e.getMessage() + "\n");
         }
      }

      private void broadcastMsg(String msg) {
         for (ClientInfo client : clientVC) {   //Object List
            client.sendMsg(msg);
         }
      }

      void sendMsg(String msg) {
         try {
            dos.writeUTF(msg);
         } catch (IOException e) {
            }
      }

      // 클라이언트로부터 데이터 수신 (항시 대기)
      public void run() {
         while (true) {
            recvMsg();
         }
      }

      public void recvMsg() { // 11-21
         String msg = "";
         try {
            msg = dis.readUTF();
         } catch (IOException e) {
            e.printStackTrace();
         }
         textArea.append(clientID + "사용자로부터 수신한 메시지: " + msg + "\n");

         StringTokenizer st = new StringTokenizer(msg, "/");    // /로 토큰을 분리
         String protocol = st.nextToken();                      //첫번째 토큰
         String message = st.nextToken();                       //두번째 토큰

         // log("프로토콜: " + protocol);
         // log("내용: " + message);

         // sendMsg("Note/" + dstClient + "/" + note);
         if("Note".equals(protocol)) {
            handleNoteProtocol(st, message);     //message는 쪽지 수신자
         }

      }
      private void handleNoteProtocol(StringTokenizer st, String dstClient) {
         String note = st.nextToken();

         //쪽지 수신자에게 쪽지를 전송하는 과정
         for(ClientInfo c : clientVC) {            
            if(c.clientID.equals(dstClient)) {
               c.sendMsg("NoteS/" + clientID + "/" + note);
               break;
            }
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

   public static void main(String[] args) {
      new ServerChat();
   }

}