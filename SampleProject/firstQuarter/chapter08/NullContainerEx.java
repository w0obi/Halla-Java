//package chapter08;
//
//import java.awt.Container;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//
//public class NullContainerEx extends JFrame {
//	public NullContainerEx() {
//		setTitle("배치관리자 없이 절대 위치에 배치하는 예제");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Container contentPane = getContentPane();
//		
//		contentPane.setLayout(null); //배치관리자 제거
//		
//		JLabel la = new JLabel("Hello, Press Buttons!");
//		la.setLocation(130, 50);
//		la.setSize(200, 20);
//		contentPane.add(la);
//		
//		for(int i = 0; i < 10; i++) {
//			JButton b = new JButton(Integer.toString(i));
//			b.setLocation(i*15, i*15);
//			b.setSize(50, 20);
//			contentPane.add(b);
//		}
//		setSize(100, 200);
//		setVisible(true);
//	}
//	public static void main(String[] args) {
//		new NullContainerEx();
//	}
//}
