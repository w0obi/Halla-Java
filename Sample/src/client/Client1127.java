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

public class Client1127 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 2L;
   // Login GUI 변수
   private JFrame Login_GUI = new JFrame("Login"); // 11-19
   private JPanel login_pane;
   private JTextField ip_tf;
   private JTextField port_tf;
   private JTextField id_tf; // 클라이언트 ID
   private JLabel img_Label;
   private JButton login_btn; // 11-13
   private String serverIP; // 11-14
   private int serverPort; // 11-14
   private String clientID; // 11-20      //클라이언트 ID

   // Main GUI 변수
   private JPanel contentPane;
   private JList<String> clientList = new JList(); // 전체 접속자 명단, 첫번째는 자기 자신 //11-20 
   private JList<String> roomList = new JList(); //11-21
   private JTextField msg_tf;
   private JTextArea chatArea = new JTextArea(); // 채팅창 변수

   // 클라이언트 관리
   private Vector<String> clientListVC = new Vector(); // 11-20
   private Vector<String> roomListVC = new Vector<>(); // 11-21

   // network 변수
   private Socket socket; // 11-14
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;

   // 기타   
   StringTokenizer st;

   public Client1127() {
      initializeLoginGUI();
      initializeMainGUI();
      addActionListeners(); // 11-13
   }

   void initializeLoginGUI() {
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 1
      Login_GUI.setBounds(100, 100, 385, 341); // 1
      login_pane = new JPanel();
      login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));

      Login_GUI.setContentPane(login_pane); // 1
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

      login_btn = new JButton("Login"); // 11-13
      login_btn.setFont(new Font("굴림", Font.BOLD, 20));
      login_btn.setBounds(12, 250, 344, 44);
      login_pane.add(login_btn);

      ImageIcon im = new ImageIcon("다람쥐.jpg");

      img_Label = new JLabel(im);
      img_Label.setBounds(12, 23, 344, 154);
      login_pane.add(img_Label);

      Login_GUI.setVisible(true); // 1
      this.setVisible(true);
   }

   void initializeMainGUI() {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 440, 618, 565);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

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

      JButton btnNewButton = new JButton("쪽지 보내기");
      btnNewButton.setBounds(0, 208, 135, 23);
      contentPane.add(btnNewButton);

      JButton joinRoomBtn = new JButton("채팅방 참여");
      joinRoomBtn.setBounds(12, 458, 123, 23);
      contentPane.add(joinRoomBtn);

      JButton createRoomBtn = new JButton("방 만들기");
      createRoomBtn.setBounds(12, 491, 123, 23);
      contentPane.add(createRoomBtn);

      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(157, 23, 427, 456);
      contentPane.add(scrollPane);
      scrollPane.setColumnHeaderView(chatArea); // 채팅창
      chatArea.setEditable(false);

      msg_tf = new JTextField();
      msg_tf.setBounds(180, 492, 301, 21);
      contentPane.add(msg_tf);
      msg_tf.setColumns(10);

      JButton sendBtn = new JButton("전송");
      sendBtn.setBounds(493, 491, 91, 23);
      contentPane.add(sendBtn);

      this.setVisible(true);
   }

   void addActionListeners() { // 11-13
      login_btn.addActionListener(this);
   }

   public void connectToServer() {
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
      clientID = id_tf.getText().trim(); // 11-20

      // 처음 접속시에 자신의 ID를 서버에게 전송
      sendMsg(clientID);

      // clientList에 자신을 등록
      clientListVC.add(clientID);
      clientList.setListData(clientListVC); // JLIST로 화면에 출력

      Thread th = new Thread(new Runnable() {

         @Override
         public void run() {
            while(true){        
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

      /**
       * (기존)1,2,3 (신규)4
       * 기존 클라이언트에게 새로운 클라이언트를 알림
       * 신규 - 기존 가입자 정보 전부 등록
       */
      switch (protocol) {
      case "NewClient":
      case "OldClient":
         addClientToList(message);    // 서버가 등록할 정보만 전송한다. 필요없는 정보는 보내지 않기 때문임
         break;
      }
   }

   private void addClientToList(String clientID) {
      clientListVC.add(clientID);
      clientList.setListData(clientListVC);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == login_btn) {
         System.out.println("login button clicked");
         connectToServer(); // 11-19
      }
   }

   public static void main(String[] args) {
      new Client1127();
   }

}