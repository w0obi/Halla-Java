//package chapter05;
///* 예제 5-2 생성자 추가, super()를 이용하여 ColorPoint클래스의 생성자에서 슈퍼 클래스 Point의 생성자를 호출 */
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
//public class SuperEx_ex5no2 {
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
///* %서븐클래스와 슈퍼클래스의 생성자 호출 및 실행
// * 	호출과 실행의 순서는 다르다. 호출은 자녀 먼저, 실행은 부모 먼저! 
// * 
// * %서브클래스에서 슈퍼클래스 생성자 선택
// *  -컴파일러
// *   
// *  -개발자의 명시적 지시
// *   super()로 슈퍼 클래스의 생성자를 명시적으로 선택
// *  */