package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Vector;

public class Client extends JFrame implements ActionListener {
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
   private String id;

   // Main GUI 변수
   private JPanel contentPane;
   private JTextField msg_tf;

   // Networking 변수
   private Socket socket; // 11-14
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;
   private JList<Vector<String>> userListVc = new JList<Vector<String>>();

   public Client() {
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

      JLabel lblNewLabel = new JLabel("전체 접속자");
      lblNewLabel.setBounds(25, 25, 120, 20);
      contentPane.add(lblNewLabel);

      JLabel lblNewLabel_1 = new JLabel("전체 접속자");
      lblNewLabel_1.setBounds(25, 241, 120, 20);
      contentPane.add(lblNewLabel_1);

      JTextArea textArea = new JTextArea();
      textArea.setBounds(12, 55, 123, 147);
      contentPane.add(textArea);

      JTextArea textArea_1 = new JTextArea();
      textArea_1.setBounds(12, 287, 123, 147);
      contentPane.add(textArea_1);

      JButton btnNewButton = new JButton("쪽지 보내기");
      btnNewButton.setBounds(0, 208, 135, 23);
      contentPane.add(btnNewButton);

      JButton btnNewButton_1 = new JButton("채팅방 참여");
      btnNewButton_1.setBounds(12, 458, 123, 23);
      contentPane.add(btnNewButton_1);

      JButton btnNewButton_2 = new JButton("방 만들기");
      btnNewButton_2.setBounds(12, 491, 123, 23);
      contentPane.add(btnNewButton_2);

      JTextArea textArea_2 = new JTextArea();
      textArea_2.setBounds(157, 23, 427, 456);
      contentPane.add(textArea_2);

      msg_tf = new JTextField();
      msg_tf.setBounds(180, 492, 301, 21);
      contentPane.add(msg_tf);
      msg_tf.setColumns(10);

      JButton btnNewButton_3 = new JButton("전송");
      btnNewButton_3.setBounds(493, 491, 91, 23);
      contentPane.add(btnNewButton_3);

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
         JOptionPane.showMessageDialog(this, "Invalid port number.", "Error",
               JOptionPane.ERROR_MESSAGE);
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
         is = socket.getInputStream();
         dis = new DataInputStream(is);
         os = socket.getOutputStream();
         dos = new DataOutputStream(os);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }
      id = id_tf.getText().trim();

      // 처음 접속시에 자신의 ID를 서버에게 전송
      sendMsg(id);

      recvMsg();
   }

   void sendMsg(String str) {
      try {
         dos.writeUTF(str);
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error sending message.", "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

   void recvMsg() {
      try {
         String msg = dis.readUTF();
//       chatArea.append("Server: " + msg + "\n");
         System.out.println("From Server: " + msg);    // 서버로 부터 메시지 수신

      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error receiving message.", "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == login_btn) {
         System.out.println("login button clicked");
         connectToServer(); // 11-19
      }
   }

   public static void main(String[] args) {
      new Client();
   }
}