package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * 231212 서버IP주소를 Wi-Fi 번호로 이용시 서로 통신이 가능함
 */
public class TestClient1210 extends JFrame implements ActionListener, KeyListener {
   private static final long serialVersionUID = 2L;
   // Login GUI 변수
   private JFrame loginGUI = new JFrame("Login"); // 11-19
   private JPanel loginJpanel;      // 컴포넌트이면서 컨테이너 ###
   private JTextField ip_tf;        // Server IP              ###
   private JTextField port_tf;      // Server Port            ###
   private JTextField id_tf;        // Client ID              ###
   private JLabel backgroundLabel;
   private JButton loginBtn;        // Login Button // 11-13  ###
   private String serverIP;         // 11-14
   private int serverPort;          // 11-14
   private String clientID;         //클라이언트 ID // 11-20 

   // Main GUI 변수
   private JPanel contentPane;
   private JList<String> clientJlist = new JList<>(); // 전체 접속자 명단, 첫번째는 자기 자신 //11-20 
   private JList<String> roomJlist = new JList<>();   // 채팅방 목록 11-21           ###
   private JTextField msgTf;                        // 전송 옆에 텍스트 필드       ###
   private JTextArea chatArea = new JTextArea();    // 채팅창 변수                 ###
   private JScrollBar verticalScrollBar = new JScrollBar();     // 채팅창 스크롤   ###
   private JButton noteBtn = new JButton("쪽지 보내기");    // 쪽지 보내기    ###
   private JButton joinRoomBtn = new JButton("참여");       // 채팅방 참여    ###
   private JButton createRoomBtn = new JButton("방 만들기");// 방 만들기      ###
   private JButton sendBtn = new JButton("전송");           // 전송 버튼      ###
   private JButton exitRoomBtn = new JButton("탈퇴");       // 채팅방 탈퇴    ###
   private JButton clientExitBtn = new JButton("채팅종료"); // 채팅 종료      ###

   // 클라이언트 관리
   private Vector<String> clientVC = new Vector<>();  // 접속자 목록 // 11-20     ###
   private Vector<String> roomVC = new Vector<>();    // 채팅방 목록 // 11-21     ###
   private String myRoom = "";                        // 내가 참여한 채팅방 11-28 ###

   // network 변수
   private Socket socket;                             // 접속할 클라이언트 변수 // 11-14 ###
   private DataInputStream dis;
   private DataOutputStream dos;

   // 기타
   StringTokenizer st;
   // private boolean stopped = false;
   private boolean socketEstablished = false;

   public TestClient1210() {
      initializeLoginGUI();
      initializeMainGUI();
      addActionListeners(); // 11-13
   }

   void initializeLoginGUI() {
      loginGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      loginGUI.setBounds(100, 100, 385, 541);
      loginJpanel = new JPanel();
      loginJpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      loginJpanel.setBackground(Color.white);
      loginGUI.setLocationRelativeTo(null); // 화면 중앙에 배치
      loginGUI.setContentPane(loginJpanel);   // 1
      loginJpanel.setLayout(null);
      ImageIcon backgroundImage = new ImageIcon("images/halla.jpg"); // 이미지 파일 경로로 변경
      backgroundLabel = new JLabel(backgroundImage);
      backgroundLabel.setBounds(0, 0, 380, 240);
      loginJpanel.add(backgroundLabel);
      loginGUI.setResizable(false); // 크기 고정

      JLabel lblNewLabel = new JLabel("Server IP");
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
      lblNewLabel.setBounds(12, 244, 113, 31);
      loginJpanel.add(lblNewLabel);

      ip_tf = new JTextField("IP를 입력해주세요");
      ip_tf.setBounds(135, 245, 221, 33);
      ip_tf.setColumns(10);      // JTextField : 가로 최댓값 ###
      ip_tf.setBackground(new Color(173, 216, 230)); // 하늘색
      ip_tf.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {
            if (ip_tf.getText().equals("")) {
               ip_tf.setText("IP를 입력해주세요");
            }
         }

         @Override
         public void focusGained(FocusEvent e) {
            if (ip_tf.getText().equals("IP를 입력해주세요")) {
               ip_tf.setText("");
            }
         }
      });

      loginGUI.add(ip_tf);

      JLabel lblServerPort = new JLabel("Server Port");
      lblServerPort.setFont(new Font("굴림", Font.BOLD, 20));
      lblServerPort.setBounds(12, 314, 113, 31);
      loginJpanel.add(lblServerPort);

      port_tf = new JTextField("포트번호를 입력해주세요");
      port_tf.setColumns(10);
      port_tf.setBounds(135, 312, 221, 33);
      port_tf.setBackground(new Color(173, 216, 230));
      port_tf.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {
            if (port_tf.getText().equals("")) {
               port_tf.setText("포트번호를 입력해주세요");
            }
         }

         @Override
         public void focusGained(FocusEvent e) {
            if (port_tf.getText().equals("포트번호를 입력해주세요")) {
               port_tf.setText("");
            }
         }
      });
      loginGUI.add(port_tf);

      JLabel lblId = new JLabel("ID");
      lblId.setFont(new Font("굴림", Font.BOLD, 20));
      lblId.setBounds(12, 376, 113, 31);
      loginJpanel.add(lblId);

      id_tf = new JTextField("ID를 입력해주세요");
      id_tf.setColumns(10);
      id_tf.setBounds(135, 377, 221, 33);
      id_tf.setBackground(new Color(173, 216, 230));
      id_tf.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {
            if (id_tf.getText().equals("")) {
               id_tf.setText("ID를 입력해주세요");
            }

         }

         @Override
         public void focusGained(FocusEvent e) {
            if (id_tf.getText().equals("ID를 입력해주세요")) {
               id_tf.setText("");
            }
         }
      });
      loginGUI.add(id_tf);

      loginBtn = new JButton("Login"); // 11-13
      loginBtn.setFont(new Font("굴림", Font.BOLD, 20));
      loginBtn.setBounds(12, 450, 344, 44);
      loginJpanel.add(loginBtn);

      loginGUI.setVisible(true); // 1
   }

   void initializeMainGUI() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(600, 100, 510, 460);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setLocationRelativeTo(null);
      contentPane.setLayout(null);
      setResizable(false);
      setContentPane(contentPane);

      JLabel 접속자 = new JLabel("전체 접속자");
      접속자.setBounds(12, 20, 73, 15);
      contentPane.add(접속자);
      JScrollPane clientScroll = new JScrollPane(clientJlist);
      clientScroll.setBounds(12, 45, 108, 107);
      contentPane.add(clientScroll);   // 접속자 목록 JLIST

      clientExitBtn.setBounds(12, 162, 108, 23);
      contentPane.add(clientExitBtn);  // 채팅 종료
      noteBtn.setBounds(12, 192, 108, 23);
      contentPane.add(noteBtn);        // 쪽지 보내기

      JLabel 채팅방 = new JLabel("채팅방목록");
      채팅방.setBounds(12, 225, 97, 15);
      contentPane.add(채팅방);
      JScrollPane chatScroll = new JScrollPane(roomJlist);
      chatScroll.setBounds(12, 240, 108, 107);
      contentPane.add(chatScroll);     // 채팅방 목록 JList

      joinRoomBtn.setBounds(6, 357, 60, 23);
      contentPane.add(joinRoomBtn);    // 채팅방 참여
      joinRoomBtn.setEnabled(false); // 버튼 비활성화
      exitRoomBtn.setBounds(68, 357, 60, 23);
      contentPane.add(exitRoomBtn);    // 채팅방 나감 ###
      exitRoomBtn.setEnabled(false);
      createRoomBtn.setBounds(12, 386, 108, 23);
      contentPane.add(createRoomBtn);  // 채팅방 생성

      chatArea.setLineWrap(true);      // 텍스트 영역이 가로 너비를 벗어나면 자동으로 줄 바꿈
      chatArea.setWrapStyleWord(true); // 단어 단위로 줄 바꿈
      chatArea.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(chatArea);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      // 채팅창 스크롤 ###
      verticalScrollBar = scrollPane.getVerticalScrollBar();
      verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
         @Override
         public void adjustmentValueChanged(AdjustmentEvent e) {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
         }
      });
      setLocationRelativeTo(null);    // 화면 중앙에 배치 ###
      scrollPane.setBounds(142, 16, 340, 363);
      contentPane.add(scrollPane);

      msgTf = new JTextField();
      msgTf.setBounds(144, 387, 268, 21);
      contentPane.add(msgTf);          // 대화 입력창
      msgTf.setColumns(10);
      msgTf.setEditable(false);
      sendBtn.setBounds(412, 386, 70, 23);
      contentPane.add(sendBtn);
      sendBtn.setEnabled(false);     // 메시지 전송
      this.setVisible(false);
   }

   void addActionListeners() {               // 11-13
      loginBtn.addActionListener(this);
      noteBtn.addActionListener(this);       // 11-27
      joinRoomBtn.addActionListener(this);
      createRoomBtn.addActionListener(this);
      sendBtn.addActionListener(this);
      exitRoomBtn.addActionListener(this);   // 채팅방탈퇴 리스너 // 1210 ###
      msgTf.addKeyListener(this);            // 메시지 전송 리스너// 1210 ###
      clientExitBtn.addActionListener(this); // 채팅 종료 리스너  // 1210 ###
   }

   public void connectToServer() {
      if (isEmpty(id_tf) || ip_tf.getText().equals("IP를 입력해주세요")) {
         JOptionPane.showMessageDialog(this, "IP를 입력해주세요", "Error", JOptionPane.ERROR_MESSAGE);
         return;
      } else if (isEmpty(port_tf) || port_tf.getText().equals("포트번호를 입력해주세요")) {
         JOptionPane.showMessageDialog(this, "포트번호를 입력해주세요", "Error", JOptionPane.ERROR_MESSAGE);
         return;
      } else if (isEmpty(id_tf) || id_tf.getText().equals("ID를 입력해주세요")) {
         JOptionPane.showMessageDialog(this, "ID를 입력해주세요", "Error", JOptionPane.ERROR_MESSAGE);
         return;
      } else if (!socketEstablished) {
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
      // JLIST로 화면에 출력
      // clientJlist.setListData(clientVC);
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

   void recvMsg(String msg) {          // 11-21

      st = new StringTokenizer(msg, "/");
      String protocol = st.nextToken();
      String message = st.nextToken();

      switch (protocol) {
         case "NewClient":
         case "OldClient":
            addClientToList(message); // 서버가 등록할 정보만 전송한다.
            break;
         case "NoteS":                // 11-28
            String note = st.nextToken();
            showMessageBox(note, message + "님으로부터 쪽지");
            break;
         case "CreateRoom":
            handleCreateRoom(message);
            break;
         case "NewRoom":
         case "OldRoom":        // 12-01
            handleAddRoomJlist(message);
            break;
         case "CreateRoomFail": // 12-01
            showErrorMessage("CreateRoomFail", "알림");
            break;
         case "JoinRoomMsg":    // 12-01-1
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
         case "RoomJlistUpdate":   // 12-05
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

   private boolean isEmpty(JTextField field) {
      return field.getText().trim().isEmpty();
   }

   private void showMessageBox(String msg, String title) {
      JOptionPane.showMessageDialog(null, msg, title, JOptionPane.CLOSED_OPTION);
   }

   private void addClientToList(String clientID) {
      clientVC.add(clientID);              // 새로운 클라이언트의 ID를 벡터의 형태로 저장하고
      // clientJlist.setListData(clientVC);// 저장된 해당 벡터를 이용하여 JList를 업데이트함.
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
      // stopped = true;
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
         connectToServer();                  // 11-19
      } else if (e.getSource() == noteBtn) { // 11-27
         System.out.println("note button clicked");
         handleNoteSendButtonClick();
      } else if (e.getSource() == createRoomBtn) {
         handleCreateRoomButtonClick();
      } else if (e.getSource() == joinRoomBtn) { // 12-01-1
         handleJoinRoomButtonClick();
      } else if (e.getSource() == sendBtn) {     // 12-02
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
         if (isEmpty(msgTf)) {
            JOptionPane.showMessageDialog(this, "전송시킬 메세지가 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         } else {
            sendMsg("SendMsg/" + myRoom + "/" + msgTf.getText().trim());
            msgTf.setText("");
            msgTf.requestFocus();
         }
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
      new TestClient1210();
   }
}