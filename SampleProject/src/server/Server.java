package server;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;

public class Server extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;
   private JFrame frame;
   private JTextField port_tf;
   private JTextArea textArea; // 11-13
   private JButton stop_btn; // 11-13
   private JButton start_btn; // 11-13
   private int port = 12345; // 11-13
   private ServerSocket ss; // 11-13
   private Socket cs; // 11-13 client socket
   private Vector<ClientInfo> userVC = new Vector<ClientInfo>();  // 네트워크 변수

   public Server() {
      initializeGUI();
      setupActionListeners(); // 11-13
   }

   public void initializeGUI() {
      setTitle("Server Application");
      setBounds(100, 100, 330, 365);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      getContentPane().setLayout(null);

      textArea = new JTextArea(); // 11-13
      textArea.setBounds(22, 18, 272, 191);
      getContentPane().add(textArea);

      JLabel lblNewLabel = new JLabel("Port No");
      lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 15));
      lblNewLabel.setBounds(22, 240, 71, 23);
      getContentPane().add(lblNewLabel);

      port_tf = new JTextField();
      port_tf.setBounds(96, 237, 198, 26);
      getContentPane().add(port_tf);
      port_tf.setColumns(10);

      stop_btn = new JButton("Stop button"); // 11-13
      stop_btn.setFont(new Font("굴림", Font.PLAIN, 15));
      stop_btn.setBounds(164, 287, 130, 26);
      getContentPane().add(stop_btn);

      start_btn = new JButton("Start button"); // 11-13
      start_btn.setFont(new Font("굴림", Font.PLAIN, 15));
      start_btn.setBounds(22, 287, 130, 26);
      getContentPane().add(start_btn);
      setVisible(true);
   }

   void setupActionListeners() { // 11-13
      start_btn.addActionListener(this);
      stop_btn.addActionListener(this);
   }

   private void startServer() { // 11-13
      try {
         port = Integer.parseInt(port_tf.getText().trim());
         ss = new ServerSocket(port);
         textArea.append("Server started on port: " + port + "\n");
         waitForClientConnection();
      } catch (NumberFormatException e) {
         textArea.append("Invalid port number.\n");
      } catch (IOException e) {
         textArea.append("Error starting server: " + e.getMessage() + "\n");
      }
   }

   private void waitForClientConnection() {
      Thread clientWaitThread = new Thread(() -> {
         try {
            textArea.append("Waiting for client connections...\n");
            cs = ss.accept();
            textArea.append("Client connected.\n");
            ClientInfo client = new ClientInfo(cs);
               client.start();

         } catch (IOException e) {
            textArea.append("Error accepting client connection: " + e.getMessage() + "\n");
         }
      });
      clientWaitThread.start();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      if (e.getSource() == start_btn) {
         System.out.println("start button clicked");
         startServer(); // 11-13
      }
      else if (e.getSource() == stop_btn) {
         System.out.println("stop button clicked");
      }
   }

   class ClientInfo extends Thread {
      private InputStream is;
      private DataInputStream dis;
      private OutputStream os;
      private DataOutputStream dos;
      private Socket userSocket;
      private String clientID = "";
      
      public ClientInfo(Socket socket) {
         this.userSocket = socket;
         ClientCom();
      }
      
      private void ClientCom() {
         try {
            is = cs.getInputStream();
            dis = new DataInputStream(is);

            os = cs.getOutputStream();
            dos = new DataOutputStream(os);

            clientID = dis.readUTF(); // 클라이언트로 부터 메시지 수신
            textArea.append("new Client: " + clientID + "\n");             
            
            // New Customer( new Client information -> broadCast -> pre Clients
            broadCast("NewUser/" + clientID);
            
            //기존 client 정보를 새로운 가입자에게 알림
            for(int i = 0; i < userVC.size(); i++) {
               ClientInfo c = (ClientInfo) userVC.elementAt(i);
               c.sendMsg("OldUser/" + clientID);
            }
            
            userVC.add(this);   //신규 가입자를 등록
                  
         } catch (IOException e) {
            textArea.append("Error in communication: " + e.getMessage() + "\n");
         }
      }
      
      private void broadCast(String str) {
         for(int i =0;i<userVC.size(); i++) {
            ClientInfo c = (ClientInfo) userVC.elementAt(i);
            c.sendMsg(str);
         }
      }
      
      void sendMsg(String str) {
         try {
            dos.writeUTF(str);
         } catch (IOException e) { }
      }

      // recieve Message from Client
      public void run() {
         while(true) {
            try {
               String msg = dis.readUTF();
               textArea.append(clientID + "receive from Client msg : " + msg + "\n");
            } catch (IOException e) { }
         }
      }
   }
   
   public static void main(String[] args) {
      new Server();
   }
}