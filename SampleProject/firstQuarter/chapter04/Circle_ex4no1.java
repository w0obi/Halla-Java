//package chapter04;
///* 반지름과 이름을 가진 Circle클래스를 작성하고, Circle클래스의 객체를 생성해라. */
//public class Circle_ex4no1 {
//	int radius; 	//원의 반지름 필드			 		//필드(변수) 작성
//	String name; 	//원의 이름 필드
//	
//	public double getArea() { // 메소드(함수) 작성
//		return 3.14*radius*radius;
//	}
//	
//	public static void main(String[] args) {
//		Circle_ex4no1 pizza;		 		 		//객체에 대한 레퍼런스 변수 pizza 선언
//		pizza = new Circle_ex4no1();
//		pizza.radius = 10;							//pizza객체의 radius 값을 10으로 설정
//		pizza.name = "자바피자";
//		double area = pizza.getArea();		 		//pizza 객체의 getArea() 메소드 호출
//		System.out.println(pizza.name + "의 면적은 " + area);
//		
//		Circle_ex4no1 donut = new Circle_ex4no1();	//객체에 대한 레퍼런스 변수 donut 선언과 생성
//		donut.radius = 2;							//도넛의 반지름을 2로 설정
//		donut.name = "자바도넛";
//		area = donut.getArea();						//donut 객체의 getArea() 메소드 호출
//		System.out.println(donut.name + "의 면적은 " + area);
//	}
//}
