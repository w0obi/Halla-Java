//package chapter03;
//import java.util.Scanner;
////try-catch-finally 블록을 이용하여 예제 3-12를 수정하여, 정수를 0으로 나누는 경우에  "0으로 나눌 수 없습니다!"를 출력하는 프로그램.
//public class DevideByZeroHandling_ex3no13 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		int dividend;	// 나뉨수
//		int divisor;	// 나눗수
//		
//		System.out.print("나뉨수를 입력하시오:");
//		dividend = scanner.nextInt();
//		System.out.print("나눗수를 입력하시오:");
//		divisor = scanner.nextInt();
//		
//		try {
//			System.out.print(dividend+"를 "+divisor+ "로 나누면 " 
//			+ dividend/divisor+ " 입니다.");
//		}
//		catch(ArithmeticException e) {
//			System.out.print("0으로 나눌 수 없습니다!");
//		}
//		finally {
//		scanner.close();
//		}
//	}
//}
