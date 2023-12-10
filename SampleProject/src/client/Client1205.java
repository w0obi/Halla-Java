package client;

// 1. 과제 같은 방을 만들려고 한다면 못만들게 하기   
// 2. 데이터 추가시에 JList의 문제 해결해보기(새로고침이 아니라 뒤에 붙히는 경우의 발생) -> 이건 수업시간에 진행함.
// 3. 새로 가입한 클라이언트에게 방 정보를 알려주기.

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
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

public class Client1205 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 2L;
   
   // Login GUI 변수
   private JFrame Login_GUI = new JFrame("Login"); // 11-19
   private JPanel login_pane;               // 컴포넌트이면서 컨테이너
   private JTextField ip_tf;               // Server IP
   private JTextField port_tf;               // Server Port
   private JTextField id_tf;                // 클라이언트 ID
   private JLabel img_Label;               // ? 
   private JButton login_btn;                // Login Button
   private String serverIP;                // 
   private int serverPort;                   // 11-14
   private String clientID;                // 11-20 //클라이언트 ID

   // Main GUI 변수
   private JPanel contentPane;
   private JList<String> clientList = new JList();    // 전체 접속자 명단, 첫번째는 자기 자신 //11-20
   private JList<String> roomList = new JList();    // 채팅방 목록 
   private JTextField msg_tf;                  // 전송 옆에 텍스트 필드
   private JTextArea chatArea = new JTextArea();    // 채팅창 변수
   private boolean createRoom = false;
   private JButton noteBtn = new JButton("쪽지 보내기");       // 쪽지 보내기
   private JButton joinRoomBtn = new JButton("채팅방 참여");   // 채팅방 참여
   private JButton exitroom_btn = new JButton("탈퇴");
   private JButton createRoomBtn = new JButton("방 만들기");   // 방 만들기
   private JButton sendBtn = new JButton("전송");            // 전송 버튼

   // 클라이언트 관리
   private Vector<String> clientListVC = new Vector<>();// 접속자 관리 이름만을
   private Vector<String> roomListVC = new Vector<>();    // 생성된 방 관리 이름만을
   private String myRoom = "";                      // 내가 참여한 채팅방

   // network 변수
   private Socket socket;                        // 접속할 클라이언트 변수
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;

   // 기타
   StringTokenizer st;

   public Client1205() {
      initializeLoginGUI();
      initializeMainGUI();
      addActionListeners();
   }

   void initializeLoginGUI() {
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Login_GUI.setBounds(100, 100, 385, 341);
      login_pane = new JPanel();
      login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));
      // 화면 중앙에 배치 ###
      Login_GUI.setLocationRelativeTo(null);
      Login_GUI.setContentPane(login_pane);
      login_pane.setLayout(null);

      JLabel lblNewLabel = new JLabel("Server IP");
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));
      lblNewLabel.setBounds(12, 44, 113, 31);
      login_pane.add(lblNewLabel);

      ip_tf = new JTextField();   
      ip_tf.setBounds(135, 45, 221, 33);
      login_pane.add(ip_tf);
      ip_tf.setColumns(10);            // JTextField : 가로 최댓값

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

      login_btn = new JButton("Login"); // 11-13
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
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      setLocationRelativeTo(null);
      setContentPane(contentPane);
      contentPane.setLayout(null);

      JLabel 접속자 = new JLabel("전체 접속자"); // 11-21
      접속자.setBounds(25, 25, 120, 20);
      contentPane.add(접속자);

      JLabel 채팅방 = new JLabel("채팅방 목록"); // 11-21
      채팅방.setBounds(25, 241, 120, 20);
      contentPane.add(채팅방);

      clientList.setBounds(12, 55, 123, 147);
      contentPane.add(clientList);

      roomList.setBounds(12, 287, 123, 147);
      contentPane.add(roomList);

      noteBtn.setBounds(12, 208, 123, 23);
      contentPane.add(noteBtn);

      joinRoomBtn.setBounds(6, 357, 60, 23);
      contentPane.add(joinRoomBtn);

      exitroom_btn.setBounds(68, 357, 60, 23);
      contentPane.add(exitroom_btn); // 채팅방 나감 ###
      exitroom_btn.setEnabled(false);

      createRoomBtn.setBounds(12, 491, 123, 23);
      contentPane.add(createRoomBtn);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(157, 23, 427, 456);
      contentPane.add(scrollPane);
      scrollPane.setViewportView(chatArea); // 채팅창
      chatArea.setEditable(false);

      msg_tf = new JTextField();
      msg_tf.setBounds(180, 492, 301, 21);
      contentPane.add(msg_tf);
      msg_tf.setColumns(10);

      sendBtn.setBounds(493, 491, 91, 23);
      contentPane.add(sendBtn);

      this.setVisible(false);
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
         socket = new Socket(serverIP, serverPort);
      }
      catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);
      } 
      catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }
      if (socket != null) { System.out.println("connection established!!!"); }
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
      clientID = id_tf.getText().trim(); // 11-20

      // 처음 접속시에 자신의 ID를 서버에게 전송
      sendMsg(clientID);

      // clientListVC에 자신을 등록
      clientListVC.add(clientID);
      clientList.setListData(clientListVC); // JLIST로 화면에 출력
      
      // Main GUI 표시 및 Login GUI 숨기기 ###
      this.setVisible(true);
      this.Login_GUI.setVisible(false);
      setTitle(clientID);
      
      Thread th = new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) { recvMsg(); }
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

   void recvMsg() {
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
         addClientToList(message);    // 서버가 등록할 정보만 전송한다.
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
      case "preRoom":
         handleRoomListUpdate(message);
         break;
      }
   }

   private void showMessageBox(String message, String title) {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.CLOSED_OPTION);
   }
   
   private void addClientToList(String clientID) {
      clientListVC.add(clientID);         // 새로운 클라이언트의 ID를 벡터의 형태로 저장하고
      clientList.setListData(clientListVC); // 저장된 해당 벡터를 이용하여 JList를 업데이트함.
   }
   private void handleCreateRoom(String roomName) {
      myRoom = roomName;
      joinRoomBtn.setEnabled(true);
      createRoomBtn.setEnabled(true);
      setTitle("사용자: " + clientID + "  채팅방: " + myRoom);
      chatArea.append(' ' + clientID + "님이 " + myRoom + "생성 및 가입\n");
   }
   
   private void handleRoomListUpdate(String roomName) {
         // 향후 사용자 수가 커짐을 대비하여 이분탐색 알고리즘 적용
       Collections.sort(roomListVC); // 이분탐색을 위하여 벡터 정렬
      
       int left = 0;
       int right = roomListVC.size() - 1;
       boolean flag = false;
       
       while (left <= right) {
           int mid = left + (right - left) / 2;
           int compareResult = roomName.compareTo(roomListVC.elementAt(mid));
           
           if (compareResult == 0) {
               flag = true; break;
           }
           
           else if (compareResult < 0) { right = mid - 1; } 
           else { left = mid + 1; }
       }

       if (!flag) {
           roomListVC.add(roomName);
           Collections.sort(roomListVC);
           roomList.setListData(roomListVC);
       }
       
       else if (createRoom) {
           JOptionPane.showMessageDialog(this, "다른 방제를 사용하십쇼!", "Warning", JOptionPane.CLOSED_OPTION);
       }
       
       createRoom = false;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == login_btn) {
         System.out.println("login button clicked");
         connectToServer();
      }
      else if (e.getSource() == noteBtn) {
         System.out.println("note button clicked");
         handleNoteSendButtonClick();
      }
      else if (e.getSource() == createRoomBtn) {
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
      createRoom = true;
      String room_name = "";
      room_name = JOptionPane.showInputDialog("방 이름");
      if (room_name == null) { return; }
      sendMsg("CreateRoom/" + room_name);
   }

   public static void main(String[] args) {
      new Client1205();
   }
}