//package chapter04;
///* 오버로딩이 가능하기 때문에 클래스 중복이 된다! return 값을 가질 수 없다! 
// * 생성자는 객체가 생성될 때 초기화를 위해 실행되는 메소드이다. */ 
//public class Circle_ex4no3 {
//	int radius;
//	String name;
//	
//	public Circle_ex4no3() {				// 매개변수 없는 생성자
//		radius = 1; name =" ";				// radius의 초기값은 1
//	}
//	
//	public Circle_ex4no3(int r, String n) { // 매개 변수를 가진 생성자
//		radius = r; name = n;
//	}
//	
//	public double getArea() {
//		return 3.14*radius*radius;
//	}
//	
//	public static void main(String[] args) {
//		Circle_ex4no3 pizza = new Circle_ex4no3(10, "자바피자");// Circle_ex4no3 객체 생성, 반지름 10
//		double area = pizza.getArea();
//		System.out.println(pizza.name + "의 면적은 " + area);
//		
//		Circle_ex4no3 donut = new Circle_ex4no3();// Circle_ex4no3 객체 생성, 반지름 1
//		donut.name = "도넛피자";
//		area = donut.getArea();
//		System.out.println(donut.name + "의 면적은 " + area);
//	}
//}
