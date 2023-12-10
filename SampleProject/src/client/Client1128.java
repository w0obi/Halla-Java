package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class Client1128 extends JFrame implements ActionListener {

   private static final long serialVersionUID = 2L;
   // ** Login GUI 변수 **
   private JFrame Login_GUI = new JFrame("Login");
   private JPanel login_pane;
   // 서버 IP주소 입력 필드
   private JTextField ip_tf;
   // 포트 번호 입력 필드
   private JTextField port_tf;
   // 유저 입력 필드
   private JTextField id_tf;
   private JLabel img_Label;
   private JButton login_btn;
   private String serverIP;
   private int serverPort;
   private String clientID; //클라이언트 ID

   // ** Main GUI 변수 **
   private JPanel contentPane;
   private JList<String> clientList = new JList(); // 전체 접속자 명단, 첫번째는 자기 자신
   private JList<String> roomList = new JList();
   // 쪽지 입력 필드
   private JTextField msg_tf;
   private JTextArea chatArea = new JTextArea(); // 채팅창 변수
   JButton noteBtn = new JButton("쪽지 보내기");
   JButton joinRoomBtn = new JButton("채팅방 참여");
   JButton createRoomBtn = new JButton("방 만들기");
   JButton sendBtn = new JButton("전송");

   // ** 클라이언트 관리 **
   private Vector<String> clientListVC = new Vector<>();
   private Vector<String> roomListVC = new Vector<>();
   private String myRoom = ""; // 내가 참여한 채팅방

   // ** network 변수 **
   private Socket socket; // 11-14
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;

   // ** 기타 **
   StringTokenizer st;

   public Client1128() {
      initializeLoginGUI();
      initializeMainGUI();
      addActionListeners();
   }

   void initializeLoginGUI() {
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Login_GUI.setBounds(100, 100, 385, 341);
      login_pane = new JPanel();
      login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));

      Login_GUI.setContentPane(login_pane);
      login_pane.setLayout(null);

      JLabel lblNewLabel = new JLabel("Server IP");
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
      lblNewLabel.setBounds(12, 44, 113, 31);
      login_pane.add(lblNewLabel);

      ip_tf = new JTextField();
      ip_tf.setBounds(135, 45, 221, 33);
      login_pane.add(ip_tf);
      ip_tf.setColumns(10);

      JLabel lblServerPort = new JLabel("Server Port");
      lblServerPort.setFont(new Font("굴림", Font.BOLD, 20));
      lblServerPort.setBounds(12, 114, 113, 31);
      login_pane.add(lblServerPort);

      port_tf = new JTextField();
      port_tf.setColumns(10);
      port_tf.setBounds(135, 112, 221, 33);
      login_pane.add(port_tf);

      JLabel lblId = new JLabel("ID");
      lblId.setFont(new Font("굴림", Font.BOLD, 20));
      lblId.setBounds(12, 176, 113, 31);
      login_pane.add(lblId);

      id_tf = new JTextField();
      id_tf.setColumns(10);
      id_tf.setBounds(135, 177, 221, 33);
      login_pane.add(id_tf);

      login_btn = new JButton("Login");
      login_btn.setFont(new Font("굴림", Font.BOLD, 20));
      login_btn.setBounds(12, 250, 344, 44);
      login_pane.add(login_btn);

      ImageIcon im = new ImageIcon("다람쥐.jpg");

      img_Label = new JLabel(im);
      img_Label.setBounds(12, 23, 344, 154);
      login_pane.add(img_Label);

      Login_GUI.setVisible(true);
      this.setVisible(true);
   }

   void initializeMainGUI() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 440, 618, 565);
      contentPane = new JPanel();
      // 가장 자리 여백 설정
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);
      contentPane.setLayout(null);

      JLabel 접속자 = new JLabel("전체 접속자");
      접속자.setBounds(25, 25, 120, 20);
      contentPane.add(접속자);

      JLabel 채팅방 = new JLabel("채팅방 목록");
      채팅방.setBounds(25, 241, 120, 20);
      contentPane.add(채팅방);

      clientList.setBounds(12, 55, 123, 147);
      contentPane.add(clientList);

      roomList.setBounds(12, 287, 123, 147);
      contentPane.add(roomList);

      noteBtn.setBounds(0, 208, 135, 23);
      contentPane.add(noteBtn);

      joinRoomBtn.setBounds(12, 458, 123, 23);
      contentPane.add(joinRoomBtn);

      createRoomBtn.setBounds(12, 491, 123, 23);
      contentPane.add(createRoomBtn);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(157, 23, 427, 456);
      contentPane.add(scrollPane);
      scrollPane.setColumnHeaderView(chatArea); // 채팅창, (구성요소)-열 머리글을 설정함
      chatArea.setEditable(false);

      msg_tf = new JTextField();
      msg_tf.setBounds(180, 492, 301, 21);
      contentPane.add(msg_tf);
      msg_tf.setColumns(10);

      sendBtn.setBounds(493, 491, 91, 23);
      contentPane.add(sendBtn);

      this.setVisible(true);
   }

   void addActionListeners() {
      login_btn.addActionListener(this);
      noteBtn.addActionListener(this);
      joinRoomBtn.addActionListener(this);
      createRoomBtn.addActionListener(this);
      sendBtn.addActionListener(this);

   }

   public void connectToServer() {
      try {
         serverIP = ip_tf.getText().trim();
         serverPort = Integer.parseInt(port_tf.getText().trim());
         // 클라이언트 소켓 생성하여 서버에 접속을 지시
         socket = new Socket(serverIP, serverPort);
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }
      if (socket != null)
         System.out.println("connection established!!!");
      connection();
   }

   void connection() {
      try {
         dis = new DataInputStream(socket.getInputStream());
         dos = new DataOutputStream(socket.getOutputStream());
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }
      clientID = id_tf.getText().trim();

      // 처음 접속시에 자신의 ID를 서버에게 전송
      sendMsg(clientID);

      // clientListVC에 자신을 등록
      clientListVC.add(clientID);
      clientList.setListData(clientListVC); // JLIST로 화면에 출력
      setTitle(clientID);

      Thread th = new Thread(new Runnable() {

         @Override
         public void run() {
            while (true) {
               recvMsg();
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

   void recvMsg() { // 11-21
      String msg = "";
      try {
         msg = dis.readUTF();
         System.out.println("From Server: " + msg);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error receiving message.", "Error", JOptionPane.ERROR_MESSAGE);
      } // 서버로 부터 메시지 수신

      st = new StringTokenizer(msg, "/");
      String protocol = st.nextToken();
      String message = st.nextToken();

      switch (protocol) {
      case "NewClient":
      case "OldClient":
         addClientToList(message); // 서버가 등록할 정보만 전송한다.
         break;
      case "NoteS":
         String note = st.nextToken();
         showMessageBox(note, message + "'note");
         break;
      case "CreateRoom":
         handleCreateRoom(message);
         break;
      case "New_Room":
         handleRoomListUpdate(message);
         break;
      }
   }

   private void showMessageBox(String message, String title) {
      // parentComponent가 null이면 중앙에 출력
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.CLOSED_OPTION);
   }
   private void addClientToList(String clientID) {
      clientListVC.add(clientID);
      clientList.setListData(clientListVC);
   }
   
   private void handleCreateRoom(String roomName) {
      myRoom = roomName;
      // 채팅방 참여 버튼 비활성화
      joinRoomBtn.setEnabled(false);
      // 방 만들기 버튼 비활성화
      createRoomBtn.setEnabled(false);
      setTitle("사용자: " + clientID + "  채팅방: " + myRoom);
      
      chatArea.append(clientID + "님이 " + myRoom + "생성 및 가입\n");
   }
   
   private void handleRoomListUpdate(String roomName) {
      roomListVC.add(roomName);
      roomList.setListData(roomListVC);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == login_btn) {
         System.out.println("login button clicked");
         connectToServer();
      } else if (e.getSource() == noteBtn) {
         System.out.println("note button clicked");
         handleNoteSendButtonClick();
      }else if (e.getSource() == createRoomBtn) {
         handleCreateRoomButtonClick();
      }
   }

   public void handleNoteSendButtonClick() {
      System.out.println("noteBtn clicked");
      
      String client = (String) clientList.getSelectedValue();

      String note = JOptionPane.showInputDialog("보낼 메시지");
      if (note != null) {
         sendMsg("Note/" + client + "/" + note);
         System.out.println("receiver : " + client + " | send data : " + note);
      }
   }
   
   private void handleCreateRoomButtonClick() {
      System.out.println("createRoomBtn clicked");
      boolean createRoom = false;
      String room_name = "";

      room_name = JOptionPane.showInputDialog("방 이름");
      if (room_name == null) {
         return;
      }

      sendMsg("CreateRoom/" + room_name);
   }
/*
 * 11. 28 과제
 * 같은 방을 요청했을 때, (못 만들게)다시 만들라고 알림 (다음주 월요일까지)
 * jList 문제 해결하기
 * 방 정보 알려주기
 * 방에서 클라이언트가 나갔을 때 채팅 종료하는 기능 구현하기 "User_Out/" + UserID
 * 업데이트하는 횟수 줄여서 오류 발생 감소시키기 (백터로 구현), 바뀐 명단을 JList 한번만! Vector는 여러번 변경해도 상관 X
 * 
 */
   public static void main(String[] args) {
      new Client1128();
   }

}