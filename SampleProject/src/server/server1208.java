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

//import chat.server.RoomInfo;
//import server.server.UserInfo;

//[JFrame]창 액션 리스너
public class server1208 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;   //직렬화 버전 명시
   //GUI 구성
   private JPanel contentPane;
   private JTextField port_tf;   //택스트 필드 - 포트번호 입력
   private JTextArea textArea = new JTextArea();   //텍스트 필드 - 서버 로그 표시
   private JButton start_btn = new JButton("서버 실행");   //서버실행 버튼
   private JButton stop_btn = new JButton("서버 중지");   //서버 중지 버튼

   // socket 생성 연결 부분
   private ServerSocket ss; // server socket
   private Socket cs;      //client socket
   int port = 12345;   //포트번호 12345

   //클라이언트 방 정보 관리
   private Vector<ClientInfo> clientVC = new Vector<ClientInfo>();   //클라이언트의 정보
   private Vector<RoomInfo> roomVC = new Vector<RoomInfo>();   //방 정보 관리
   
   
   public server1208() {   //생성자 객체 (초기화)
      initializeGUI();   //GUI를 초기화
      setupActionListeners(); //액션 리스너를 설정하는 초기화  11-13
   }

   public void initializeGUI() {
      setTitle("Server Application");   //프레임의 제목 = Server Application으로 설정

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //프레임을 닫을 때 기본 동작 설정
      setBounds(30, 100, 321, 370);   //프레임의 위치와 크기 설정

      contentPane = new JPanel();   //JPanel을 사용해 컨텐트 팬 생성
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));   //창의 5픽셀의 여백 지정
      setContentPane(contentPane);
      contentPane.setLayout(null);   //레이아웃 매니저 = null로 설정, 수동으로 컴포넌트의 위치를 조정

      JLabel lblNewLabel_2 = new JLabel("포트 번호");   //JLabel 객체를 생성 = lblNewLabel_2할당
      lblNewLabel_2.setBounds(12, 245, 57, 15);   //레이블 위치와 크기 설정
      contentPane.add(lblNewLabel_2);   //contentPane에 레이블 추가

      port_tf = new JTextField();   //JTextField를 생성 contentPane 내에서 위치와 크기 설정
      port_tf.setBounds(81, 242, 212, 21);
      contentPane.add(port_tf);   //contentPane에 추가 --> port_tf를 contentPane에 추가, 텍스트 필드를 GUI에 표시
      port_tf.setColumns(10);   //setColumns(10) 메서드 = 텍스트 필드의 선호 너비 설정

      start_btn.setBounds(12, 286, 138, 23);   //start 버튼 위치와 크기 설정
      contentPane.add(start_btn);   //start버튼을 contentPane에 추가

      JScrollPane scrollPane = new JScrollPane();   //스크롤 패널 생성
      scrollPane.setBounds(12, 10, 281, 210);
      contentPane.add(scrollPane);   //contantPane에 scrollPane생성

      scrollPane.setViewportView(textArea);   //JScrollPane에 표시 = textArea를 설정, JScrollPane 스크롤 기능 제공
      textArea.setEditable(false);   //JTextArea에 직접 텍스트를 입력하지 못하게 함

      stop_btn.setBounds(155, 286, 138, 23);   //버튼크기, 위치 설정
      contentPane.add(stop_btn);   //contentPane에 버튼 추가
      stop_btn.setEnabled(false);   //버튼 비활성화

      this.setVisible(true); // 화면 보이기
   }

   void setupActionListeners() { //setupActionListeners 매서드 11-13
      start_btn.addActionListener(this);   //start버튼 등록 --> actionPerformed 메서드가 호출
      stop_btn.addActionListener(this);   //stop버튼 등록 --> actionPerformed 메서드가 호출
      //this = 현재 클래스에서 ActionListener 인터페이스를 구현
   }

   @Override
   public void actionPerformed(ActionEvent e) {   //ActionListener 인터페이스를 구현 메소드
      // TODO Auto-generated method stub
      if (e.getSource() == start_btn) {   //만약 start버튼 클릭되었을때
        System.out.println("start button clicked");   //start button clicked을 콘솔에 출력
        startServer(); //startServer() 메서드 호출  11-13

      } 
      
      else if (e.getSource() == stop_btn) {   //만약 stop버튼 클릭되었을때
        System.out.println("stop button clicked");   //stop button clicked을 콘솔에 출력
        stopServer();   //stopServer 메서드 호출
      }

   }

   private void startServer() { //서버를 시작 11-13
      try {
         port = Integer.parseInt(port_tf.getText().trim());   //port_tf에서 입력받은 포트 번호 문자열을 정수로 변환 + 예외처리(NumberFormatException)
         ss = new ServerSocket(port);   //서버 소켓 생성, 클라이언트 연결 수락
         textArea.append("Server started on port: " + port + "\n");   //JTextArea인 textArea에 서버가 지정된 포트에서 시작되었다는 메시지
         waitForClientConnection();   //클라이언트의 연결 대기
         //예외문
      } catch (NumberFormatException e) {   //포트 번호를 정수로 변환할 때 유효하지 않은 포트 번호
         textArea.append("Invalid port number.\n");
      } catch (IOException e) {   //ServerSocket 생성 또는 클라이언트의 연결을 대기할 때 입출력 예외
         textArea.append("Error starting server: " + e.getMessage() + "\n");
      }
   }

   private void stopServer() { //서버를 중지하는데 사용  11-21
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
            while (true) {            //11-22
               textArea.append("Waiting for client connections...\n");
               cs = ss.accept();         //cs를 분실하면 클라이언트와 통신이 불가능
               textArea.append("Client connected.\n");
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
      private String roomID = "";      //11-28

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
            for (int i = 0; i < roomVC.size(); i++){
               RoomInfo r = roomVC.elementAt(i);
               textArea.append("preRoom/: " + r.roomName + "\n");
               sendMsg("preRoom/" + r.roomName);
            }

            // 새 클라이언트 정보를 기존 클라이언트들에게 알림 ( 가입인사)
            broadcastMsg("NewClient/" + clientID);

            clientVC.add(this); // 신규 클라이언트 등록
         } catch (IOException e) {
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
         } catch (IOException e) {
         }
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
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         textArea.append(clientID + "사용자로부터 수신한 메시지: " + str + "\n");

         StringTokenizer st = new StringTokenizer(str, "/");
         String protocol = st.nextToken();
         String message = st.nextToken();

         //log("프로토콜: " + protocol);
         //log("내용: " + message);
         
         if ("Note".equals(protocol)) {      //11-28
            handleNoteProtocol(st, message);
         } else if ("CreateRoom".equals(protocol)) {
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
         sendMsg("CreateRoom/" + roomName);
         broadCast("New_Room/" + roomName);
         
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
      // TODO Auto-generated method stub
      new server1208();
   }

}