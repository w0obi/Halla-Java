//package chapter05;
///* 자바는 다중 상속 안 됨.
// * 자바에서 다중 상속이 되게하려면 추상클래스를 이용하면 된다.
// * public abstract String  fail() { return "Good Bye"; } // 추상메소드 아님. 컴파일 오류.
//
// * 추상메소드를 가진 추상클래스가 있고,
// * 추상메소드가 없는 추상클래스 있음.
//
// * 추상클래스의 인스턴스(객체) 생성이 불가함. 인스턴스(객체)를 만들려고 하면 컴파일 오류.
//
// * 추상클래스의 구현
// * 중요! c.average(new int [] {2,3,4}) */
//
//abstract class Calculator {
//public abstract int add(int a, int b);
//public abstract int substract(int a, int b);
//public abstract double average(int[]a);
//}
//
//	public class GoodCalc_ex5no5 extends Calculator {
//	@Override
//	public int add(int a, int b) {
//	   return a + b;
//	}
//	@Override
//	public int substract(int a, int b) {
//	   return a - b;
//	}
//	@Override
//	public double average(int[]a) {
//	   double sum = 0;
//	   for (int i = 0; i < a.length; i++) 
//	      sum += a[i];
//	   return sum/a.length;
//	}
//	public static void main(String[] args) {
//	   GoodCalc_ex5no5 c = new GoodCalc_ex5no5();
//	   System.out.println(c.add(2, 3));
//	   System.out.println(c.substract(2,3));
//	   System.out.println(c.average(new int [] {2,3,4} ));
//	}
//}
