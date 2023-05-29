///* %boolean equal(Object)
// * -두 객체의 내용을 비교
// * -객체의 내용물을 비교하기 위해 클래스의 멤버로 작성
// * 
// * 예제 6-3 다운캐스팅 */
//package chapter06;
//
//class Point {
//	private int x, y;
//	public Point(int x, int y) { this.x = x; this.y = y; }
//	public boolean equals(Object obj) {
//		Point p = (Point)obj;					// obj를 Point 타입을 다운캐스팅.
//		if(x == p.x && y == p.y) return true;
//		else return false;
//	}
//}
//
//public class EqualsEx_ex6no3 {
//	public static void main(String[] args) {
//		Point a = new Point(2,3);
//		Point b = new Point(2,3);
//		Point c = new Point(3,4);
//		if(a == b) {
//			System.out.println("a==b");
//		}
//		if(a.equals(b)) System.out.println("a is equal to b");
//		if(a.equals(c)) System.out.println("a is equal to c");
//	}
//}
