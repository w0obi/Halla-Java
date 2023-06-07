package chapter08;

import javax.swing.JFrame;

public class MyFrame_ex8no1 extends JFrame {
	public MyFrame_ex8no1() {
		setTitle("300x300 스윙 프레임 만들기");
		setSize(300,300);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		MyFrame_ex8no1 frame = new MyFrame_ex8no1();
	}
}