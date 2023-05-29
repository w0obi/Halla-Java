//package chapter05;
///* 예제 5-4 */
//
//class Shape {
//   public void draw() {
//      System.out.println("Shape");
//   }
//}
//
//class Line extends Shape {
//   public void draw() {
//      System.out.println("Line");
//   }
//}
//
//class Rect extends Shape {
//   public void draw() {
//      System.out.println("Rect");
//   }
//}
//
//class Circle extends Shape {
//   public void draw() {
//      System.out.println("Circle");
//   }
//}
//public class MethodOverridingEx_ex5no4 {
//   static void paint(Shape p) {	//Shape을 상속받은 모든 객체들이 매개 변수로 넘어올 수 있음
//      p.draw();					//p가 가리키는 객체에 오버라이딩한 draw() 호출. 동적바인딩
//   }
//   public static void main(String[] args) {
//      Line line = new Line();
//      paint(line);
//   	
//		// 다음 호출들은 모두 paint() 메소드 내에서 Shape에 대한 레퍼렌스 p로 업캐스팅 됨.
//   	paint(new Shape());
//   	paint(new Line());
//   	paint(new Rect());
//   	paint(new Circle());
//   
//   }
//}
