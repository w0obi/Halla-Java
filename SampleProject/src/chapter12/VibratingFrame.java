package chapter12;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

/* runnable 프레임에 run 메소드 반드시 작성 필요 
 * 생성자이므로 리턴 값 없음.
 * 
 * 컨텍스트 스위칭, 세마포어 언급.*/

public class VibratingFrame extends JFrame implements Runnable {
	private Thread th;		   // 진동하는 스레드
	public VibratingFrame() {
		setTitle("Made it (vibrating frame) ");
		setSize(200, 200);
		setLocation(300, 300); //프레임의 위치를 스크린의 (300, 300)에 설정
		setVisible(true);
		
		getContentPane().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(!th.isAlive()) return; // 이미 스레드가 종료했다면 그냥 리턴
				th.interrupt();			  // 진동스레드에게 예외를 보내서 강제 종료
			}
		});
		
		th = new Thread(this);
		th.start();
	}
	
	public void run() {		//프레임의 진동을 일으키기 위해 20ms마다 프레임의 위치를 랜덤하게 이동
		
		Random r = new Random();
		while(true) {
			try {
				Thread.sleep(20); 	//20ms
			}
			catch(InterruptedException e) {
				return;
			}
			int x = getX() + r.nextInt()%5;
			int y = getY() + r.nextInt()%5;
			setLocation(x, y);
		}
	}
	public static void main(String[] args) {
		new VibratingFrame();
	
	}
}