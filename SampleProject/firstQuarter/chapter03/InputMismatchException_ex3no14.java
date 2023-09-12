//package chapter03;
//import java.util.InputMismatchException;
//import java.util.Scanner;
///* Scanner 클래스를 입력하여 3개의 정수를 입력받아 합을 구하는 프로그램을 작성하라.
// * 사용자가 정수가 아닌 문자를 입력할 때 발생하는 InputMismatchException 예외를 처리하여 다시 입력받도록 하라.*/
//public class InputMismatchException_ex3no14 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("정수 3개를 입력하세요");
//		int integer = 0 , sum = 0;
//
//		for(int i = 0; i < 3; i++) {
//			System.out.print(i+">>");
//			// integer = scanner.nextInt();
//			try {
//				integer = scanner.nextInt();
//			}
//			catch(InputMismatchException e) {	/* catch에서 출력할 때, ln붙이기! */
//				System.out.println("정수가 아닙니다. 다시 입력하세요!");
//				scanner.next();					/* 입력스트림에 있는 정수가 아닌 토큰을 버린다. */ 
//				i--;
//				continue;
//			}
//		sum += integer;
//		}
//		System.out.println(sum);
//		scanner.close();
//	}	
//}
