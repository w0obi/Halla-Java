//package chapter08;
//
//import java.awt.*;
//import javax.swing.*;
//
//public class BorderLayoutEx extends JFrame {
//	public BorderLayoutEx() {
//		setTitle("BorderLayout 예제");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Container contentPane = getContentPane();
//		
//		contentPane.setLayout(new BorderLayout(30, 20));
//		
//		contentPane.add(new JButton("Calcuate"), BorderLayout.CENTER);
//		contentPane.add(new JButton("add"), BorderLayout.NORTH);
//		contentPane.add(new JButton("sub"), BorderLayout.SOUTH);
//		contentPane.add(new JButton("mul"), BorderLayout.EAST);
//		contentPane.add(new JButton("div"), BorderLayout.WEST);
//	}
//}
