package server;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class serverStudy extends JFrame implements ActionListener {

   private static final long serialVersionUID = 2L;
   private JPanel contentPane;
   private JTextField port_tf;

   private JTextArea textArea = new JTextArea();
   private JButton start_btn = new JButton("서버 실행");
   private JButton stop_btn = new JButton("서버 중지");

   // socket 생성 연결 부분
   private ServerSocket ss; // server socket
   private Socket cs;         //client socket
   int port = 12345;

   // 기타 변수 관리
   private Vector<UserInfo> clientVC = new Vector<UserInfo>();
   private Vector<RoomInfo> roomVC = new Vector<RoomInfo>();
   StringTokenizer st;
   private boolean stopServer = true;

   serverStudy() {
      initGUI(); // GUI 초기화, 화면 생성
      connectListener(); // 리스너 설정 메소드
   }

   private void initGUI() {
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

   private void connectListener() {
      start_btn.addActionListener(this);
      stop_btn.addActionListener(this);
   }

   private boolean serverStart() {
      try {
         port = Integer.parseInt(port_tf.getText().trim());
         ss = new ServerSocket(port);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림", JOptionPane.ERROR_MESSAGE);
         stopServer = true;
      }
      if (ss != null) {
         stopServer = false;
         connection();
      }
      return stopServer;
   }

   public void connection() {

      new Thread(new Runnable() {
         public void run() {
            while (stopServer != true) {
               try {
                  textArea.append("사용자 접속 대기중\n");
                  cs = ss.accept();
                  textArea.append("클아이언트 접속 완료\n");
                  UserInfo user = new UserInfo(cs);
                  user.start();

               } catch (IOException e) {

               }
            }

         }
      }).start();
   }

   public class UserInfo extends Thread {
      private InputStream is;
      private DataInputStream dis;
      private OutputStream os;
      private DataOutputStream dos;
      private Socket cs;
      private String userID = null;
      private String roomID = null;

      UserInfo(Socket socket) {
         this.cs = socket;
         UserNetwork();
      }

      private void UserNetwork() {
         try {
            is = cs.getInputStream();
            dis = new DataInputStream(is);
            os = cs.getOutputStream();
            dos = new DataOutputStream(os);

            userID = dis.readUTF();
            log("클라이언트 접속했습니다");

            // 연결된 클라이언트에게 가입자 정보 전달
            for (UserInfo u : clientVC) {
               u.sendMsg("NewUser/" + userID);
            }

            // 새로 접속한 클라이언트에게 기존 사용자 정보 전송
            for (UserInfo u : clientVC) {
               sendMsg("OldUser/" + u.userID);
            }

            // 자신에게 기존의 개설된 채팅 방 정보 전송
            for (RoomInfo r : roomVC) {
               sendMsg("Old_Room/" + r.roomName);
            }

            sendMsg("room_list_update/update");

            // 새로 접속한 클라이언트 등록
            clientVC.add(this);

            // 기존 사용자들에게 새로운 사용자 알림
            broadCast("user_list_update/update");

         } catch (IOException e) {
            logError("Stream 설정 중 오류 발생", e);
         }
      }

      @Override
      public void run() {
         log("스레드 시작");
         while (true) {
            try {
               String msg = dis.readUTF();
               log("수신한 메시지: " + msg);
               recvMsg(msg);
            } catch (IOException e) {
               logError("클라이언트와의 접속이 끊어졌습니다.", e);
               userOut();
               break;
            }
         }
      }

      public void userOut() {
         log(userID + " 사용자 접속 끊어짐");
         try {
            closeStreams();
            // 벡터의 요소 삭제 ###
            clientVC.remove(this);

            // 방에서 탈퇴
            for (RoomInfo r : roomVC) {
               if (r.roomName.equals(roomID)) {
                  r.RoomClientVC.remove(this);
                  if (r.RoomClientVC.isEmpty()) {
                     roomVC.remove(r);
                     broadCast("Room_out/" + roomID);
                     broadCast("room_list_update/");
                  }
                  break;
               }
            }
         } catch (IOException e) {
            logError("사용자 로그아웃 중 오류 발생", e);
         }
      }

      public void recvMsg(String str) {
         StringTokenizer st = new StringTokenizer(str, "/");
         String protocol = st.nextToken();
         String message = st.nextToken();

         log("프로토콜: " + protocol);
         log("내용: " + message);

         try {
            // 전송받은 프로토콜이 Note ###
            if ("Note".equals(protocol)) {
               handleNoteProtocol(st, message);
            }
            else if ("CreateRoom".equals(protocol)) {
               handleCreateRoomProtocol(message);
            }
            else if ("Join_Room".equals(protocol)) {
               handleJoinRoomProtocol(st, message);
            }
            else if ("Chatting".equals(protocol)) {
               handleChattingProtocol(st, message);
            }
            else if ("chatQuit".equals(protocol)) {
               handleChatQuitProtocol();
            }
            else if ("Exit_Room".equals(protocol)) {
               handleExitRoomProtocol(message);
            }
            else {
               log("알 수 없는 프로토콜: " + protocol);
            }
         } catch (Exception e) {
            logError("프로토콜 처리 중 오류 발생", e);
         }
      }

      private void handleNoteProtocol(StringTokenizer st, String message) {
         String note = st.nextToken();
         log("받는 사람: " + message);
         log("보낼 쪽지: " + note);

         // 해당 사용자에게 쪽지 전송
         for (UserInfo u : clientVC) {
            if (u.userID.equals(message)) {
               u.sendMsg("NoteS/" + userID + "/" + note);
               break;
            }
         }
      }

      private void handleCreateRoomProtocol(String roomName) {

         // 방 이름이 이미 존재하는지 확인
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
            sendMsg("CreateRoom/" + roomName);
            broadCast("New_Room/" + roomName);
         }
      }

      private void handleJoinRoomProtocol(StringTokenizer st, String roomName) {
         for (RoomInfo r : roomVC) {
            if (r.roomName.equals(roomName)) {
               r.broadcastRoomMsg("Join_Room_Msg/가입/***" + userID + "님이 입장하셨습니다.********");
               r.RoomClientVC.add(this);
               roomID = roomName;
               sendMsg("Join_Room/" + roomName);
               break;
            }
         }
      }

      private void handleChattingProtocol(StringTokenizer st, String roomName) {
         String chatting_msg = st.nextToken();
         for (RoomInfo r : roomVC) {
            if (r.roomName.equals(roomName)) {
               r.broadcastRoomMsg("Chatting/" + userID + "/" + chatting_msg);
            }
         }
      }

      private void handleChatQuitProtocol() {
         userOut();
      }

      private void handleExitRoomProtocol(String roomName) {
         roomID = roomName;
         log(userID + " 사용자가 " + roomName + " 방에서 나감");

         for (RoomInfo r : roomVC) {
            if (r.roomName.equals(roomName)) {
               r.broadcastRoomMsg("Exit_Room_Msg/탈퇴/***" + userID + "님이 채팅방에서 나갔습니다.********");
               r.RoomClientVC.remove(this);
               if (r.RoomClientVC.isEmpty()) {
                  roomVC.remove(r);
                  broadCast("Room_out/" + roomName);
                  broadCast("room_list_update/");
               }
               break;
            }
         }
      }

      private void broadCast(String str) {
         for (UserInfo u : clientVC) {
            u.sendMsg(str);
         }
      }

      // 내용 전송시 호출 ###
      public void sendMsg(String msg) {
         try {
            dos.writeUTF(msg);
         } catch (IOException e) {
            logError("메시지 전송 중 오류 발생", e);
         }
      }

      // 로그 출력
      private void log(String message) {
         System.out.println(userID + ": " + message);
      }

      // 오류 로그 출력
      private void logError(String message, Exception e) {
         System.err.println(userID + ": " + message);
         e.printStackTrace();
      }

      public void closeStreams() throws IOException {
         if (is != null) {
            is.close();
         }
         if (os != null) {
            os.close();
         }
         if (dos != null) {
            dos.close();
         }
         if (dis != null) {
            dis.close();
         }
         if (cs != null) {
            cs.close();
         }
      }
   }

   class RoomInfo {
      private String roomName = "";
      private Vector<UserInfo> RoomClientVC = new Vector<UserInfo>();

      public RoomInfo(String name, UserInfo u) {
         this.roomName = name;
         this.RoomClientVC.add(u);
      }

      public void broadcastRoomMsg(String message) {
         for (UserInfo u : RoomClientVC) {
            u.sendMsg(message);
         }
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == start_btn) {
         System.out.println("Server Start button Click");
         if (serverStart()) { // 소켓 생성 및 사용자 대기
            start_btn.setEnabled(false);
            port_tf.setEditable(false);
            stop_btn.setEnabled(true);
         }
      } else if (e.getSource() == stop_btn) {
         System.out.println("Server Stop Button Click");

         // 서버 종료 시 모든 클라이언트에게 알림
         for (UserInfo u : clientVC) {
            u.sendMsg("Server_Out/Bye");
            try {
               u.closeStreams();
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }

         try {
            ss.close();
            stopServer = true;
            // 벡터의 모든 성분 제거, 크기 0으로 설정 ###
            roomVC.removeAllElements();
         } catch (IOException e1) {
            e1.printStackTrace();
         }

         start_btn.setEnabled(true);
         port_tf.setEditable(true);
         stop_btn.setEnabled(false);
      }
   }// 액션 이벤트 끝

   public static void main(String[] args) {
      new serverStudy();
   }
}