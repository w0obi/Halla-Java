//package chapter05;
//
///* %업캐스팅 ;그림 5-13 레퍼랜스 p생성, 객체 student 생성 
// * 					학생은 모두 사람이지만, 사람은 모두 학생일 수 없다.
// * %다운캐스팅은 반드시 ' 명시적 '으로! ;객체 생성가 동시에 바로 업캐스팅.. 
// * 								그림 5-13 s는 모두 볼 수 있으나 p는 자기가 만든 것만을 볼 수 있음.
// * %업캐스팅 레퍼런스로 객체 구별?
// *	person타입의 레퍼런스 person이 어떤 클래스 타입의 객체를 가르키는지 알 수 있음(=>매개변수를 하나로 통일해서 쓸 수 있다)
// *	객체레퍼렌스 instanceof 연산자타입
// *	
// *	
// *	*/
///* 예제 5-3 instanceof 연산자를 이용하여 그림5-15의 상속관계에 따라 레퍼런스가 가리키는 객체의 타입을 알아본다. 실행결과는 무엇인가? */
//class Person {}
//class Student extends Person {}
//class Researcher extends Person {}
//class Professor extends Researcher {}
//
//public class InstanceOfEx_ex5no3 {
//	static void print(Person p) {
//		if(p instanceof Person) {
//			System.out.print("Person ");
//		}
//		if(p instanceof Student) {
//			System.out.print("Student ");
//		}
//		if(p instanceof Researcher) {
//			System.out.print("Researcher ");
//		}
//		if(p instanceof Professor) {
//			System.out.print("Professor ");
//		}
//		System.out.println();
//	}
//	public static void main(String[] args) {
//		System.out.print("new Student() -> "); print(new Student());
//		System.out.print("new Researcher() -> "); print(new Researcher());
//		System.out.print("new Professor() -> "); print(new Professor());
//	}
//}
