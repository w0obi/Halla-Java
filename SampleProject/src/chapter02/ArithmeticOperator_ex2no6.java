//package chapter02;
//
//import java.util.Scanner;
///* %와 / 산술연산자 응용 
// * 초단위의 정수를 입력받고, 몇 시간, 몇 분, 몇 초인지 구하여 출력하는 프로그램을 작성하라. 
// * <입력>4000 <출력>정수를 입력하세요:\n4000초는 1시간, 6분, 40초입니다. */
//public class ArithmeticOperator_ex2no6 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		
//		System.out.println("정수를 입력하세요.");
//		int Time = scanner.nextInt();
//		int Second = Time % 60;
//		int Minute = (Time / 60) % 60;
//		int Hour = (Time / 60) / 60;
//		
//		System.out.println(Time+ "초는 " +Hour+ "시간, " +Minute+ "분, " +Second+ "초입니다.");
//		
//		scanner.close();
//	}	
//}