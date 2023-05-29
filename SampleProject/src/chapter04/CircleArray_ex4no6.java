//package chapter04;
//
///* 반지름이 0~4인 Circle 객체 5개를 가지는 배열을 생성하고, 배열에 있는 모든 Circle 객체의 면적을 출력하라. */
//class Circle {
//	int radius;
//
//	public Circle(int radius) {
//		this.radius = radius;
//	}
//
//	public double getArea() {
//		return 3.14 * radius * radius;
//	}
//}
//
//public class CircleArray_ex4no6 {
//	public static void main(String[] args) {
//		Circle[] c; // Circle 배열에 대한 레퍼런스 c를 선언
//		c = new Circle[5];
//
//		for (int i = 0; i < c.length; i++) {
//			c[i] = new Circle(i);
//		}
//
//		for (int i = 0; i < c.length; i++) {
//			System.out.print((int) (c[i].getArea()) + " ");
//		}
//	}
//}