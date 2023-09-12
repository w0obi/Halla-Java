//package chapter05;
///* 예제 5-1 (x,y)의 한 점을 표현하는 Point클래스와 이를 상속받아 점에 색을추가한 ColorPoint클래스를 만들어보자. */
//class Point {
//	private int x, y;
//	public void set(int x, int y) {		// 멤버 메소드 set
//		this.x = x; this.y = y;
//	}
//	public void showPoint() {			// 멤버 메소드 showPoint
//		System.out.println("(" + x + "," + y + ")");
//	}	
//}
//
//class ColorPoint extends Point {		// 부모클래스 Point를 물려받은 ColorPoint 자식클래스.
//	private String color;
//	public void setColor(String color) {
//		this.color = color; 
//	}
//	public void showColorPoint() {
//		System.out.print(color);
//		showPoint();
//	}
//}
//
//public class ColorPointEX_ex5no1 {
//	public static void main(String[] args) {
//		Point p = new Point();
//		p.set(1,2);
//		p.showPoint();
//		
//		ColorPoint cp = new ColorPoint();
//		cp.set(3, 4);
//		cp.setColor("red");
//		cp.showColorPoint();
//	}
//}