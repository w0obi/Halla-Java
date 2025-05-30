package client;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientStudy extends JFrame implements ActionListener, KeyListener {
   private static final long serialVersionUID = 2L;

   // Login GUI 변수
   private JFrame Login_GUI = new JFrame("Login");
   private JPanel login_pane;                                     // 컴포넌트이면서 컨테이너
   private JTextField ip_tf;                                      // Server serverIP
   private JTextField port_tf;                                    // Server Port
   private JTextField id_tf;                                      // Client ID
   private JButton loginBtn;                                      // Login Button
   private String serverIP;
   private int serverPort;
   private String clientID;

   // Main GUI 변수
   private JPanel contentPane;
   private JTextField msg_tf;
   private JButton noteBtn = new JButton("쪽지보내기");
   private JButton joinRoomBtn = new JButton("참여");
   private JButton exitRoomBtn = new JButton("탈퇴");
   private JButton createRoomBtn = new JButton("방만들기");
   private JButton sendBtn = new JButton("전송");
   private JList<String> clientJlist = new JList();                // 전체 접속자 리스트
   private JList<String> roomJlist = new JList();                  // 전체 방 목록 리스트
   private JTextArea chatArea = new JTextArea();                   // 채팅창 변수
   private JButton chatQuitBtn = new JButton("채팅종료");
   
   // network 변수
   private Socket socket;
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;

   // 클라이언트 관리
   Vector<String> clientListVC = new Vector<>();                    // 가입자 목록
   Vector<String> roomListVC = new Vector<>();                      // 채팅방 목록
   private String myRoom = "";                                      // 내가 참여한 채팅방

   // 기타
   StringTokenizer st;
   private boolean createRoom = false;
   private boolean stopped = false;

   ClientStudy() {
      initLoginGUI();                                                // 로그인 메뉴 화면
      initMainGUI();                                                 // 메인 메뉴 화면
      connectListener();                                             // 리스너 연결
   }

   private void initLoginGUI() {
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Login_GUI.setBounds(300, 100, 295, 388);
      login_pane = new JPanel();
      login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
      Login_GUI.setLocationRelativeTo(null);
      Login_GUI.setContentPane(login_pane);
      login_pane.setLayout(null);

      JLabel 서버IP = new JLabel("Server IP");
      서버IP.setBounds(12, 165, 57, 15);
      this.login_pane.add(서버IP);

      JLabel 서버port = new JLabel("Server Port");
      서버port.setBounds(12, 202, 69, 15);
      login_pane.add(서버port);

      JLabel 사용자ID = new JLabel("ID");
      사용자ID.setBounds(12, 245, 57, 15);
      login_pane.add(사용자ID);

      ip_tf = new JTextField();
      ip_tf.setBounds(92, 162, 116, 21);
      login_pane.add(ip_tf);
      ip_tf.setColumns(10);

      port_tf = new JTextField();
      port_tf.setBounds(92, 199, 116, 21);
      login_pane.add(port_tf);
      port_tf.setColumns(10);

      id_tf = new JTextField();
      id_tf.setBounds(92, 242, 116, 21);
      login_pane.add(id_tf);
      id_tf.setColumns(10);

      loginBtn.setBounds(22, 291, 227, 23);
      login_pane.add(loginBtn);
      Login_GUI.setVisible(true);
      this.setVisible(true);
   }

   private void initMainGUI() {
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
      contentPane.add(clientJlist);                                      // 접속자 목록 JLIST

      chatQuitBtn.setBounds(12, 162, 108, 23);
      contentPane.add(chatQuitBtn);                                    // 채팅 종료
      noteBtn.setBounds(12, 192, 108, 23);
      contentPane.add(noteBtn);                                    // 쪽지 보내기

      JLabel 채팅방 = new JLabel("채팅방목록");
      채팅방.setBounds(12, 225, 97, 15);
      contentPane.add(채팅방);
      roomJlist.setBounds(12, 240, 108, 107);
      contentPane.add(roomJlist);                                      // 채팅방 목록 JLIST

      joinRoomBtn.setBounds(6, 357, 60, 23);
      contentPane.add(joinRoomBtn);                                    // 채팅방 참여
      joinRoomBtn.setEnabled(false);                                 // 버튼 비활성화
      exitRoomBtn.setBounds(68, 357, 60, 23);
      contentPane.add(exitRoomBtn);                                    // 채팅방 나감
      exitRoomBtn.setEnabled(false);
      createRoomBtn.setBounds(12, 386, 108, 23);
      contentPane.add(createRoomBtn);                                 // 채팅방 생성

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(142, 16, 340, 363);
      contentPane.add(scrollPane);
      scrollPane.setViewportView(chatArea);                             // 채팅창
      chatArea.setEditable(false);

      msg_tf = new JTextField();
      msg_tf.setBounds(144, 387, 268, 21);
      contentPane.add(msg_tf);                                          // 대화 입력창
      msg_tf.setColumns(10);
      msg_tf.setEditable(false);
      sendBtn.setBounds(412, 386, 70, 23);
      contentPane.add(sendBtn);
      sendBtn.setEnabled(false);                                     // 메시지 전송
      this.setVisible(false);
   }

   private void connectListener() {
      loginBtn.addActionListener(this);       // 로그인 리스너 연결
      noteBtn.addActionListener(this);        // 쪽지 전송 리스너
      joinRoomBtn.addActionListener(this);    // 채팅방참여 리스너
      exitRoomBtn.addActionListener(this);    // 채팅방탈퇴 리스너
      createRoomBtn.addActionListener(this);  // 방만들기 리스너
      sendBtn.addActionListener(this);        // 전송 버튼 리스너
      msg_tf.addKeyListener(this);            // 메시지 전송 리스너
      chatQuitBtn.addActionListener(this);    // 채팅 종료 리스너
   }

   public void actionPerformed(ActionEvent e) {
      // 로그인버튼 클릭 ###
      if (e.getSource() == loginBtn) {
         handleLoginButtonClick();
      }
      // 쪽지보내기버튼 클릭 ###
      else if (e.getSource() == noteBtn) {
         handleNoteSendButtonClick();
      }
      // 참여버튼 클릭 ###
      else if (e.getSource() == joinRoomBtn) {
         handleJoinRoomButtonClick();
      }
      // 방만들기버튼 클릭 ###
      else if (e.getSource() == createRoomBtn) {
         handleCreateRoomButtonClick();
      }
      // 전송버튼 클릭 ###
      else if (e.getSource() == sendBtn) {
         handleSendButtonClick();
      }
      // 채팅종료버튼 클릭 ###
      else if (e.getSource() == chatQuitBtn) {
         handleChatQuitButtonClick();
      }
      // 탈퇴버튼 클릭 ###
      else if (e.getSource() == exitRoomBtn) {
         handleExitRoomButtonClick();
      }
   }

   // 로그인버튼 클릭 ###
   private void handleLoginButtonClick() {
      System.out.println("loginBtn clicked");
      if (isEmpty(ip_tf)) {
         setFieldText(ip_tf, "IP를 입력해주세요");
         return;
      }
      if (isEmpty(port_tf)) {
         setFieldText(port_tf, "포트 번호를 입력해주세요");
         return;
      }
      if (isEmpty(id_tf)) {
         setFieldText(id_tf, "ID를 입력하세요");
         return;
      }

      serverIP = ip_tf.getText().trim();
      serverPort = Integer.parseInt(port_tf.getText().trim());
      clientID = id_tf.getText().trim();
      network();
   }

   private boolean isEmpty(JTextField field) {
      return field.getText().trim().isEmpty();
   }

   private void setFieldText(JTextField field, String text) {
      field.setText(text);
      // field에 포커스를 주어 키 입력을 받을 수 있게 함 ###
      field.requestFocus();
   }

   // 쪽지보내기버튼 클릭 ###
   private void handleNoteSendButtonClick() {
      System.out.println("noteBtn clicked");
      String user = (String) clientJlist.getSelectedValue();

      String note = JOptionPane.showInputDialog("보낼 메시지");
      if (note != null) {
         send_Msg("Note/" + user + "/" + note);
         System.out.println("receiver : " + user + " | send data : " + note);
      }
   }

   // 참여버튼 클릭 ###
   private void handleJoinRoomButtonClick() {
      System.out.println("joinRoomBtn clicked");
      String room = (String) roomJlist.getSelectedValue();
      if (room != null) {
         send_Msg("Join_Room/" + room);
      }
   }

   // 방만들기버튼 클릭 ###
   private void handleCreateRoomButtonClick() {
      System.out.println("createRoomBtn clicked");
      createRoom = true;
      String roomName = "";
      roomName = JOptionPane.showInputDialog("방 이름");
      if (roomName == null) { return; }
      createOrUpdateRoomList(roomName);
      send_Msg("CreateRoom/" + roomName);

  }
  
  private void createOrUpdateRoomList(String roomName) {
      Collections.sort(roomListVC); // 이분탐색을 위하여 벡터 정렬 ###
  
      int left = 0;
      int right = roomListVC.size() - 1;
      boolean flag = false;
  
      while (left <= right) {
          int mid = left + (right - left) / 2;
          int compareResult = roomName.compareTo(roomListVC.elementAt(mid));
  
          if (compareResult == 0) {
              flag = true; break;
          } else if (compareResult < 0) { right = mid - 1; }
           else { left = mid + 1; }
      }
  
      if (!flag) {
          roomListVC.add(roomName);
          Collections.sort(roomListVC);       // 새로운 요소를 추가하고 다시 정렬합니다 ###
          roomJlist.setListData(roomListVC); // 방 목록을 업데이트합니다 ###
      } else if (createRoom) {
          JOptionPane.showMessageDialog(this, "다른 방제를 사용하십쇼!", "Warning", JOptionPane.CLOSED_OPTION);
      }
      createRoom = false;
  }
  
   // 전송버튼 클릭 ###
   private void handleSendButtonClick() {
      if (!myRoom.isEmpty()) {
         send_Msg("Chatting/" + myRoom + "/" + msg_tf.getText().trim());
         msg_tf.setText("");
         // msg_tf에 포커스를 주어 키 입력을 받을 수 있게 함 ###
         msg_tf.requestFocus();
      }
   }

   // 채팅종료버튼 클릭 ###
   private void handleChatQuitButtonClick() {
      System.out.println("chatQuitBtn clicked");
      send_Msg("chatQuit/" + myRoom);
      clientListVC.removeAllElements();
      if (!myRoom.isEmpty()) {
         roomListVC.removeAllElements();
      }
      closeSocket();
      // 프로그램 강제 종료 ###
      System.exit(0);
   }

   private void closeSocket() {
      try {
         is.close();
         os.close();
         dos.close();
         dis.close();
         socket.close();
      } catch (IOException e1) {
         e1.printStackTrace();
      }
   }

   // 탈퇴버튼 클릭 ###
   private void handleExitRoomButtonClick() {
      System.out.println("exitRoomBtn clicked");
      send_Msg("Exit_Room/" + myRoom);
      myRoom = "";
      exitRoomBtn.setEnabled(false);
      joinRoomBtn.setEnabled(roomListVC.size() > 0);
      createRoomBtn.setEnabled(true);
      msg_tf.setEditable(false);
      sendBtn.setEnabled(false);
      setTitle("사용자: " + clientID);
   }

   public void keyPressed(KeyEvent e) {
   }

   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == 10) {
         if (!myRoom.isEmpty()) {
            send_Msg("Chatting/" + myRoom + "/" + msg_tf.getText().trim());
            msg_tf.setText("");
            // msg_tf에 포커스를 주어 키 입력을 받을 수 있게 함 ###
            msg_tf.requestFocus();
         }
      }
   }

   public void keyTyped(KeyEvent e) {
   }

   private void network() {
      try {
         socket = new Socket(serverIP, serverPort);
         if (socket != null)
            connectAndInitialize();
         System.out.println("Socket completed");
      } catch (IOException e) {
      }
   }

   // 연결 및 초기화 ###
   private void connectAndInitialize() {
      try {
         // 소켓과 데이터 입출력 스트림 초기화
         is = socket.getInputStream();
         dis = new DataInputStream(is);
         os = socket.getOutputStream();
         dos = new DataOutputStream(os);
      } catch (IOException e) {
         // 예외 처리: 입출력 스트림 초기화 실패
         e.printStackTrace();// 예외 처리에 따른 적절한 조치를 취하세요.
         return;             // 예를 들어, 사용자에게 오류 메시지를 표시하고 연결을 종료할 수 있습니다.
      }

      // Main GUI 표시 및 Login GUI 숨기기
      this.setVisible(true);
      this.Login_GUI.setVisible(false);
      setTitle("사용자: " + clientID);

      // 서버에 사용자 ID 알리기
      send_Msg(clientID);
      // 사용자 목록에 사용자 추가
      clientListVC.add(clientID);

      // 채팅 수신을 처리하는 스레드 시작
      startReceivingThread();
   }

   private void startReceivingThread() {
      new Thread(new Runnable() {
         public void run() {
            while (!stopped) {
               try {
                  String msg = dis.readUTF(); // 서버로부터 메시지 수신
                  recv_Msg(msg);
               } catch (IOException e) {
                  e.printStackTrace();
                  break;
               }
            }
            System.out.println("end of Chatting");
         }
      }).start();
   }

   private void send_Msg(String msg) {
      try {
         dos.writeUTF(msg);
      } catch (IOException e) { }
   }

   private void recv_Msg(String str) {
      System.out.println("recv_Msg  " + str);
      st = new StringTokenizer(str, "/");
      String protocol = st.nextToken();
      String message = st.nextToken();

      switch (protocol) {
         case "NewUser":
         case "OldUser":
            addUserToList(message);
            break;
         case "NoteS":
            String note = st.nextToken();
            showMessageBox(note, message + "'note");
            break;
         case "CreateRoomFail":
            showErrorMessage("createRoomFail", "알림");
            break;
         case "CreateRoom":
            handleCreateRoom(message);
            break;
         case "New_Room":
         case "Old_Room":
            handleRoomListUpdate(message);
            break;
         case "Join_Room":
            handleJoinRoom(message);
            break;
         case "Join_Room_Msg":
            String msg = st.nextToken();
            appendToChatArea(message + " : " + msg);
            break;
         case "User_out":
            removeUserFromList(message);
            break;
         case "Chatting":
            String chatMsg = st.nextToken();
            appendToChatArea(message + " : " + chatMsg);
            break;
         case "Server_Out":
            handleServerShutdown();
            break;
         case "user_list_update":
            updateUserList();
            break;
         case "room_list_update":
            updateRoomList();
            break;
         case "Room_out":
            handleRoomOut(message);
            break;
         case "Exit_Room_Msg":
            String exitMsg = st.nextToken();
            appendToChatArea(message + " : " + exitMsg);
            break;
         default:
            // 처리되지 않은 프로토콜에 대한 처리
            break;
      }
   }

   private void addUserToList(String clientID) {
      clientListVC.add(clientID);
      clientJlist.setListData(clientListVC);
   }

   private void removeUserFromList(String clientID) {
      clientListVC.remove(clientID);
      clientJlist.setListData(clientListVC);
   }

   private void handleCreateRoom(String roomName) {
      msg_tf.setEditable(true);
      sendBtn.setEnabled(true);
      myRoom = roomName;
      joinRoomBtn.setEnabled(false);
      createRoomBtn.setEnabled(false);
      exitRoomBtn.setEnabled(true);
      setTitle("사용자: " + clientID + "  채팅방: " + myRoom);
      chatArea.append(clientID + "님이 " + myRoom + " create and join.\n");
   }

   private void handleJoinRoom(String roomName) {
      msg_tf.setEditable(true);
      sendBtn.setEnabled(true);
      joinRoomBtn.setEnabled(false);
      createRoomBtn.setEnabled(false);
      myRoom = roomName;
      exitRoomBtn.setEnabled(true);
      setTitle("사용자: " + clientID + "   채팅방: " + myRoom);
      chatArea.append(clientID + "님이 " + myRoom + " join.\n");
      showInfoMessage("joinRoom success", "알림");
   }

   private void handleRoomListUpdate(String roomList) {
      if (myRoom.equals("")) {
         joinRoomBtn.setEnabled(true);
      }
      roomListVC.add(roomList);
      roomJlist.setListData(roomListVC);
   }

   private void handleServerShutdown() {
      stopped = true;
      try {
         socket.close();
         clientListVC.removeAllElements();
         if (!myRoom.isEmpty()) {
            roomListVC.removeAllElements();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      System.exit(0);
   }

   private void handleRoomOut(String roomName) {
      System.out.println("Room Out");
      roomListVC.remove(roomName);
      if (roomListVC.isEmpty()) {
         joinRoomBtn.setEnabled(false);
      }
   }

   private void showMessageBox(String message, String title) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.CLOSED_OPTION);
   }

   private void showErrorMessage(String message, String title) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
   }

   private void showInfoMessage(String message, String title) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
   }

   private void appendToChatArea(String message) {
      chatArea.append(message + "\n");
   }

   private void updateUserList() {
      clientJlist.setListData(clientListVC);
   }

   private void updateRoomList() {
      roomJlist.setListData(roomListVC);
   }

   public static void main(String[] args) {
      new ClientStudy();
   }
}