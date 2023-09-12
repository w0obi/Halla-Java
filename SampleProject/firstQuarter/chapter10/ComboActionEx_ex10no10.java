package chapter10;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ComboActionEx_ex10no10 extends JFrame {
	private String[] fruits = {"apple", "banana", "mango"};
	private ImageIcon[] images = {new ImageIcon("images/kiwi.jpg"),
								  new ImageIcon("images/mango.jpg"),
								  new ImageIcon("images/flower.jpg") };
	private JLabel imgLabel = new JLabel(images[0]);
	
	public ComboActionEx_ex10no10() {
		setTitle("콤보박스 활용 예제");
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		JComboBox<String> combo = new JComboBox<String>(fruits);
		c.add(combo);	c.add(imgLabel);
		
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				
				int index = cb.getSelectedIndex();
				
				imgLabel.setIcon(images[index]);
			}
		});
		
		setSize(300, 250);
		setVisible(true);
	}
	public static void main(String[] args) {
		new ComboActionEx_ex10no10();
	}
}
