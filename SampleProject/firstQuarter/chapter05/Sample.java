//package chapter05;
///* <217page> super 키워드를 이용한 프로그램 
// * <실행결과> 예상 - "Sub"\n"Super"
// *  Sub
//	Exception in thread "main" java.lang.StackOverflowError
//	at extra.Super.draw(New.java:6)
//	draw()메소드를 호출함으로 재귀호출이 발생. 이 과정에서 무한루프가 발생하여 호출 스택이 쌓이다가 예외가 발생함.*/
//class Super {
//	public void paint() { draw(); }				// paint메소드에서 draw메소드 호출
//	public void draw() {						// draw메소드
//		draw();									// 재귀호출
//		System.out.println("Super");			// "Super" 출력
//	}
//}
//
//class Sub extends Super {						// Super클래스를 상속받는 Sub클래스 정의
//	public void paint() { super.draw(); }		// paint메소드에서 Super클래스의 draw메소드 호출 (super 키워드 호출)
//	public void draw() {						// draw 메소드 재정의
//		System.out.println("Sub");				// "Sub" 출력		
//	}
//}
//
//public class Sample {
//	public static void main(String[] args) {	
//		Super b = new Sub();					// Sub클래스의 인스턴스를 Super클래스 참조 변수 b에 대입 (업캐스팅) 
//		b.paint();								// ((오버라이딩된 메소드가 호출되면 동적바인딩이 발생한다.))b의 paint 메소드 호출 (동적바인딩) 
//	}
//}
