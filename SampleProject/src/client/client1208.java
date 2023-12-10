package client;

//import
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
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

//클라이언트 GUI, 네트워크
public class client1208 extends JFrame implements ActionListener {
   private static final long serialVersionUID = 2L;   //직렬화 버전 명시
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
   private String clientID; // 11-20 //클라이언트 ID

   // Main GUI 변수
   private JPanel contentPane;   //로그인 후 이동 창
   private JList<String> clientList = new JList(); // 전체 접속자 명단, 첫번째는 자기 자신 //11-20
   private JList<String> roomList = new JList(); // 채팅방 목록
   private JTextField msg_tf;
   private JTextArea chatArea = new JTextArea(); // 채팅창 변수
   private boolean createRoom = false;
   JButton noteBtn = new JButton("쪽지 보내기");
   JButton joinRoomBtn = new JButton("채팅방 참여");
   JButton createRoomBtn = new JButton("방 만들기");
   JButton sendBtn = new JButton("전송");

   // 클라이언트 관리
   private Vector<String> clientListVC = new Vector<>(); // 11-20
   private Vector<String> roomListVC = new Vector<>(); // 11-21
   private String myRoom = ""; // 내가 참여한 채팅방      11-28

   // network 변수
   private Socket socket; // 11-14
   private InputStream is;
   private DataInputStream dis;
   private OutputStream os;
   private DataOutputStream dos;


   // 기타
   StringTokenizer st;

   public client1208() {   //생성
      initializeLoginGUI();   //login GUI 생성
      initializeMainGUI();   //main GUI 생성
      addActionListeners(); // 액션 리스너   11-13
   }

   void initializeLoginGUI() {    //로그인 GUI 초기화
      Login_GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임이 닫힐 때 프로그램도 함께 종료   1 
      Login_GUI.setBounds(100, 100, 385, 341); // JFrame크기 설정   1
      login_pane = new JPanel();   //객체 생성, 할당
      login_pane.setBorder(new EmptyBorder(5, 5, 5, 5));   // 빈 테두리를 설정(여백 5)

      Login_GUI.setContentPane(login_pane); // 컨텐트 팬을 login_pane으로 설정   1
      login_pane.setLayout(null);    //JPanel의 레이아웃 매니저를 null로 설정

      JLabel lblNewLabel = new JLabel("Server IP");   //새로운 JLabel을 생성
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 20));   //JLabel의 폰트를 설정
      lblNewLabel.setBounds(12, 44, 113, 31);   //lblNewLabel 위치와 크기를 설정
      login_pane.add(lblNewLabel);   //JPanel에 lblNewLabel이라는 JLabel을 추가

      ip_tf = new JTextField();   //JTextField 객체를 생성, 이를 ip_tf 변수에 할당
      ip_tf.setBounds(135, 45, 221, 33);
      login_pane.add(ip_tf);
      ip_tf.setColumns(10);  //입력할 수 있는 텍스트 길이

      JLabel lblServerPort = new JLabel("Server Port");   //텍스트 레이블을 생성
      lblServerPort.setFont(new Font("굴림", Font.BOLD, 20));   //굴림체, 굵게, 크기 20
      lblServerPort.setBounds(12, 114, 113, 31);   //위치, 크기
      login_pane.add(lblServerPort);   // JPanel에 lblServerPort이름의 JLabel 추가

      port_tf = new JTextField();   //텍스트 필드 생성
      port_tf.setColumns(10);   //컬럼 수를 10으로 설정
      port_tf.setBounds(135, 112, 221, 33);
      login_pane.add(port_tf);   //login_pane(컨테이너)에 추가

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

      ImageIcon im = new ImageIcon("다람쥐.jpg");   //다람쥐 이미지 파일을 이용해 이미지 아이콘 생성

      img_Label = new JLabel(im);   //ImageIcom을 가진 레이블 생성해 이미지 생성
      img_Label.setBounds(12, 23, 344, 154);
      login_pane.add(img_Label);

      Login_GUI.setVisible(true); //JFrame을 화면에 표시  1
      this.setVisible(true);   //현 객체를 화면에 표시
   }

   void initializeMainGUI() {   //메인초기화
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

      noteBtn.setBounds(0, 208, 135, 23);
      contentPane.add(noteBtn);

      joinRoomBtn.setBounds(12, 458, 123, 23);
      contentPane.add(joinRoomBtn);

      createRoomBtn.setBounds(12, 491, 123, 23);
      contentPane.add(createRoomBtn);

      JScrollPane scrollPane = new JScrollPane();   //스크롤 패널 생성
      scrollPane.setBounds(157, 23, 427, 456);
      contentPane.add(scrollPane);   //contentPane에 스크롤 패널 추가
      scrollPane.setColumnHeaderView(chatArea); // 헤더 chatArea로 설정
      chatArea.setEditable(false);   //텍스트 편집 불가능

      msg_tf = new JTextField();
      msg_tf.setBounds(180, 492, 301, 21);
      contentPane.add(msg_tf);   //contentPane에 텍스트 필드 추가
      msg_tf.setColumns(10);   //컬럼 수 10으로 설정

      sendBtn.setBounds(493, 491, 91, 23);   //메시지 전송 버튼 위치와 크기
      contentPane.add(sendBtn);   //contentPane에 sendBtn 추가 --> 메시지 전송 버튼 화면에 표시

      this.setVisible(true);   //JFrame을 화면에 표시
   }

   void addActionListeners() { //액션 리스너 등록
    //이벤트 처리들
      login_btn.addActionListener(this);
      noteBtn.addActionListener(this); // 11-27
      joinRoomBtn.addActionListener(this);
      createRoomBtn.addActionListener(this);
      sendBtn.addActionListener(this);

   }

   public void connectToServer() {   //서버에 연결
      try {
        // 사용자가 입력한 서버 IP와 포트 번호를 가져옴
         serverIP = ip_tf.getText().trim();   // 서버 IP의 앞뒤공백을 제거 후 serverIP 변수에 할당
         serverPort = Integer.parseInt(port_tf.getText().trim());   //포트 번호를 문자열에서 정수로 변환해 serverPort 변수에 할당
         socket = new Socket(serverIP, serverPort);   //소켓을 생성하여 서버에 연결
         
         //포트 번호가 잘못된 경우
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(this, "Invalid port number.", "Error", JOptionPane.ERROR_MESSAGE);

         //서버에 연결할 수 없는 경우
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }

      //소켓이 정상적으로 생성시 연결 성공 메시지 출력, connection 메서드 호출
      if (socket != null)
         System.out.println("connection established!!!");
        connection();
   }

   void connection() {   //서버와의 연결 관리
      try {
         dis = new DataInputStream(socket.getInputStream());   //입력스트림 = dis
         dos = new DataOutputStream(socket.getOutputStream());   //출력스트림 = dos

         //서버와의 연결에 실패한 경우
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Cannot connect to server.", "Connection Error",
               JOptionPane.ERROR_MESSAGE);
      }
      //사용자가 입력한 클라이언트 ID(앞뒤 공백 제거) = 변수clientID
      clientID = id_tf.getText().trim(); // 11-20

      // 처음 접속시에 자신의 ID를 서버에게 전송
      sendMsg(clientID);

      // clientListVC에 자신을 등록
      clientListVC.add(clientID);
      clientList.setListData(clientListVC); // JLIST로 화면에 출력
      setTitle(clientID);             //클라이언트 프레임타이틀 = 자신의 클라이언트 타이틀  11-28

      //서버로부터 메시지를 수신하는 스레드 생성
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

   void sendMsg(String msg) {    //메세지 전송
      try {
        //서버에 메세지를 전송한다
         dos.writeUTF(msg);
         //메시지 전송 중 오류가 발생한 경우 예외
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error sending message.", "Error", JOptionPane.ERROR_MESSAGE);
      }
   }

   void recvMsg() { //메세지 수신
      String msg = "";
      try {
        //서버로 부터 메시지 수신
         msg = dis.readUTF();
         System.out.println("From Server: " + msg);   //수신한 메시지 콘솔에 출력
         //메시지 수신 중 오류가 발생한 경우 예외
      } catch (IOException e) {
         JOptionPane.showMessageDialog(this, "Error receiving message.", "Error", JOptionPane.ERROR_MESSAGE);
      } // 서버로 부터 메시지 수신

      st = new StringTokenizer(msg, "/");   //수신 메시지를 프로토콜과 메시지 분리를 위해 "/" 사용
      String protocol = st.nextToken();   //첫 번째 토큰 프로토콜로 추출
      String message = st.nextToken();   //두 번째 토큰 메시지로 추출

      switch (protocol) {
      case "NewClient":   //서버에 새로운 클라이언트 접속한 경우
      case "OldClient":   //서버에 기존 클라이언트 정보를 전송한 경우
         addClientToList(message); // 서버가 등록할 정보만 전송한다.
         break;

         //서버로부터 쪽지를 수신했을 때 동작을 처리
         //쪽지 내용, 메시지를 추출하여 showMessageBox 메서드 호출로 쪽지 내용을 알림
      case "NoteS":            //11-28
         String note = st.nextToken();
         showMessageBox(note, message + "'note");
         break;

         //새 채팅방이 생성되었을 때 동작처리
      case "CreateRoom":   //handleCreateRoom(message);호출해 새로운 채팅방 처리
         handleCreateRoom(message);
         break;
      case "New_Room":   //handleRoomListUpdate(message);호출해 채팅방 목록 업데이트
         handleRoomListUpdate(message);
         break;
      case "preRoom" :
         handleRoomListUpdate(message);
         break;
      }
   }

   //메세지 박스 표시
   private void showMessageBox(String message, String title) {
    //메시지 박스를 생성, 입력된 메시지와 제목을 표시
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.CLOSED_OPTION);
   }

   //접속자 목록에 클라이언트 추가
   private void addClientToList(String clientID) {
      clientListVC.add(clientID);   //클라이언트 목록(Vector)에 새로운 클라이언트 ID 추가
      clientList.setListData(clientListVC);   //목록을 clientList에 설정하여 화면에 업데이트
   }
   
   //채팅방 처리
   //새로운 채팅방이 생성되었을 때 호출, 생성된 채팅방의 정보(이름)를 받아옴
   private void handleCreateRoom(String roomName) {
      myRoom = roomName;   //변수 myRoom에 새로운 채팅방의 이름을 할당
      joinRoomBtn.setEnabled(false);   //채팅방 참여 버튼 비활성화
      createRoomBtn.setEnabled(false);   //방 만들기 버튼 비활성화
      setTitle("사용자: " + clientID + "  채팅방: " + myRoom);   //프레임의 타이틀 업데이트함 현재 사용자, 채팅방 정보 표시
      chatArea.append(clientID + "님이 " + myRoom + "생성 및 가입\n");   //채팅창에 메시지 추가
   }
   
   //채팅방 목록 업데이트
   private void handleRoomListUpdate(String roomName) {
      Collections.sort(roomListVC);   //이분 탐색을 위한 벡터 설정

      int left = 0;
      int right = roomListVC.size()- 1;
      boolean flag = false;

      while (left <= right){
         int mid = left + (right - left) / 2;
         int compareResult = roomName.compareTo(roomListVC.elementAt(mid));

         if (compareResult == 0) {
            flag = true;
            break;
            }
         else if (compareResult < 0) {right = mid - 1;}
         else {left = mid + 1;}
      }
      if (!flag){
         roomListVC.add(roomName);   //클라이언트 목록(Vector)에 새로운 방 이름 추가
         Collections.sort(roomListVC);
         roomList.setListData(roomListVC);
      }
      else if(createRoom) {
         JOptionPane.showMessageDialog(this, "다른 방제를 이용해 주십시오", "Warning", JOptionPane.CLOSED_OPTION);
      }
      createRoom = false;
      
   }


   //액션 이벤트 처리
   @Override
   public void actionPerformed(ActionEvent e) {

    //Login 버튼 클릭 --> connectToServer() 호출해 서버에 연결
      if (e.getSource() == login_btn) {
         System.out.println("login button clicked");
         connectToServer(); // 11-19
         //쪽지 보내기 버튼 클릭 --> handleNoteSendButtonClick() 호출 --> 쪽지 보내기 창 표시
      } else if (e.getSource() == noteBtn) { // 11-27
         System.out.println("note button clicked");
         handleNoteSendButtonClick();
         //방 만들기 버튼 클릭 --> handleCreateRoomButtonClick() 호출 새로운 채팅방 생성
      }else if (e.getSource() == createRoomBtn) {
         handleCreateRoomButtonClick();
      }
   }

   //버튼 클릭 - 쪽지 보내기
   public void handleNoteSendButtonClick() {   //11-28
      System.out.println("noteBtn clicked");   //[콘솔]noteBtn clicked 메시지 --> 해당 메서드가 실행됨
      String client = (String) clientList.getSelectedValue();   //채팅창에서 선택된 (쪽지를 보낼)클라이언트의 ID를 가져옴

      //사용자에게서 메시지를 입력 + 입력창
      String note = JOptionPane.showInputDialog("보낼 메시지");   //변수 note에 저장
      if (note != null) {   // 사용자가 취소 버튼을 누르지 않고 메시지를 입력시
         sendMsg("Note/" + client + "/" + note);   //서버로 메시지를 전송 --> Note/수신자ID/메시지의 내용
         System.out.println("receiver : " + client + " | send data : " + note);   //[콘솔]메시지를 보낼 대상 클라이언트와 전송한 데이터를 출력
      }
   }
   
   //방만들기 버튼 클릭 - 채팅방 만들기
   private void handleCreateRoomButtonClick() {   // 11-28
      System.out.println("createRoomBtn clicked");   //[콘솔]createRoomBtn clicked" 메시지 출력 --> 해당 메서드 실행
    //createRoom = 채팅방을 성공적으로 만들었는지의 여부
      boolean createRoom = true;   //createRoom이라는 boolean 변수 선언 --> 초기값 false
      String room_name = "";   //room_name이라는 빈 문자열 선언 = 채팅방 이름 저장

      room_name = JOptionPane.showInputDialog("방 이름");   //사용자가 채팅방의 이름을 입력
      if (room_name == null) {   //사용자가 취소 버튼을 누르면
         return;   //종료
      }

      sendMsg("CreateRoom/" + room_name);   //서버에 CreateRoom/채팅방이름 형식 메시지 전송
      //서버는 클라이언트에게 새로운 채팅방을 생성하라는 지시를 할 수 있음
   }

   //main 메서드
   public static void main(String[] args) {
      new client1208();
   }

}