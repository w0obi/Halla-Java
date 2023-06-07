//package chapter08;
//
//import java.awt.Container;
//import java.awt.GridLayout;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//
//public class GridLayoutEx extends JFrame {
//	public GridLayoutEx() {
//		super("GridLayout 예제");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//프레임 윈도우를 닫으면 프로그램 종료 
//		Container contentPane = getContentPane();		//컨텐트펜을 알아낸다.
//		
//		contentPane.setLayout(new GridLayout(1,10));	//1행10열
//		
//		for(int i = 0; i < 10; i++) {
//			String text = Integer.toString(i);			//정수i를 문자열로 반환
//			JButton button = new JButton(text);			//버튼 컴포넌트 생성
//			contentPane.add(button);					//컨텐트펜에 버튼 부착
//		}
//		setSize(500,200);
//		setVisible(true);
//	}
//	public static void main(String[] args) {
//		new GridLayoutEx();
//	}
//}
