package chapter10;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JComponent_ex10no1 extends JFrame {
	public JComponent_ex10no1() {
		super("JComonent의 공통 메소드 예제");
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		JButton b1 = new JButton("Magenta/Yellow Button");
		JButton b2 = new JButton("	 Disabled Button	");
		JButton b3 = new JButton("getX(), getY()");
		
		b1.setBackground(Color.YELLOW);						//배경색 설정
		b1.setForeground(Color.MAGENTA);					//전경색 설정
		b1.setFont(new Font("Arial", Font.ITALIC, 20));		//폰트 설정
		b2.setEnabled(false);								//컴포넌트 비활성화
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton)e.getSource();
				setTitle(b.getX() + "," + b.getY());		//타이틀에 버튼 좌표 출력
			}			
		});
		
		c.add(b1); c.add(b2); c.add(b3);					//컨텐트팬에 버튼 부착
		setSize(260, 200); setVisible(true);				//크기 지정, 컴포넌트 보이기
	
	}
	public static void main(String[] args) {
		new JComponent_ex10no1(); 
	}
}
