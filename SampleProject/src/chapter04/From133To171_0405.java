/*
 * package chapter04;

public class From133To171_0405 {
	public static void main(String[] args) {
		객체 지향과 자바.
		
		클래스class ;객체 모양을 선언한 틀		c언어에서는 structure라고한다.
		인스턴스instance ;생성된 실체
		멤버는 두가지로 나뉘는데 필드field와 메소드method로 구분.
		메소드는 객체를 수정 및 변경하는 역할을 함
		
		%상속inheritance ;상위 클래스의 멤버를 하위 클래스가 물려받음.
						상위클래스는 수퍼클래스, 하위클래스는 코드재사용.
		/*class Animal {
        	String name = "lion"; /* 필드(field) //
        	int age = 4;
        	
        	void eat( ) {}
        	void speak() {}
        	void love() {}
        }
        
        class Human extends Animal {/* Animal클래스를 상속받은 Human클래스와 객체 관계 //
        	String hobby;
        	String job;
        	
        	void work() {}
        	void cry() {}
        	void laugh() {}
        }
			
		%다형성polymorphism ;같은 메소드가 클래스에 따라 다른 역할을 하는 것. 예를 들어 cout.
		
		%객체지향 언어의 목적 ;소프트웨어의 생산성 향상: 디버깅하기 쉽다.
		 			   
		%절차지향프로그래밍과 객체지향프로그래밍 ;객체지향에서 객체는 클래스에 선언된 동일한 속성을 가지지만 속성 값은 서로 다르다.
		
		
		
		4.2 자바 클래스 선언과 구성
			public class New {
				int radius;
				String name;
				
				public double getArea() {
					return 3.14*radius*radius;
				}
			}
			
			%객체가 생성되는 과정
			-레퍼런스 변수 선언
			-new연산자로 객체 생성
			-객체 멤버 접근
				객체레퍼런스.멤버
		
		
		ex4no2 ;객체는 new연산자로 생성하고, .연산자로 멤버를 접근.
		Rectangle rect = new Rectangle(); 	// Rectangle이란 클래스에서 객체 rect를 생성
		rect.width = scanner.nextInt();
		rect.height = scanner.nextInt();
		System.out.println("사각형의 면적은 "+rect.getArea());
		
		%기본 생성자
		매개변수없이 아무일도 하지 않고 단순 리턴하는 생성자이다.
		
		%this(레퍼런스, 메소드)
		생성할 때 호출, 호츨 한 뒤 생성 - 이 둘의 차이점 알고 있기! 서로 다른 생성자임! 
		동작 중에도 값을 변경할 수 있다.
		
			static메소드에서는 this를 사용할 수 없다는 사실 이해하기!
		
		%객체 배열 생성 3단계
			배열에 대한 레퍼런스 변수를 선언
			레퍼런스 배열을 생성
			각 원소 객체를 생성
			new Circle[5];
		%메소드
			자바의 모든 메소드는 반드시 클래스 내부에 있어야 함.(캡슐화원칙)
			
		배열 또한 레퍼런스다.
		 
		%객체 치환 시 주의할 점
			객체의 치환은 객체 복사가 아니라 객체 레퍼런스이다.
			
		%가비지 컬렉션
			-자바가상기계가 가비지 자동 회수
				;가용메모리 공간이 일정 이하로 부족해질 때
				;가비지를 수거하여 가용메모리 공간으로 확보
			-가비지 컬렉터에 의해 자동 수행
		
		%접근지정자
		privite(=외부로부터 완전 차단)<default(=동일한 패키지에 허용)
		<protect(=동일패키지와 자식클래스에 접근 허용)
		<public(=모든 클래스에 허용)
	}
}*/