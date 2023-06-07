//package chapter07;
///* 점(x,y)를 표현하는 Point클래스만 다루는 Vector<Point> 컬렉션 활용 */
//import java.util.Vector;
//
//class Point {
//	private int x,y;
//	public Point(int x, int y) {
//		this.x = x; this.y = y;
//	}
//	public String toString() {
//		return "(" +x+ "," +y+ ")";
//	}
//}
//
//class PointVectorEx_ex7no2 {
//	public static void main(String[] args) {
//		Vector<Point> v = new Vector<Point>();
//		
//		//3개의 객체 삽입
//		v.add(new Point(2,3));
//		v.add(new Point(-5,20));
//		v.add(new Point(30,-8));
//		
//		v.remove(1);
//		
//		//벡터에 있는 Point객체를 모두 검색하여 출력
//		for(int i = 0; i < v.size(); i++) {
//			Point p = v.elementAt(i);
//			System.out.println(p);
//		}
//	}
//}