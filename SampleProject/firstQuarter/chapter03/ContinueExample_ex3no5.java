//package chapter03;
//import java.util.Scanner;
//public class ContinueExample_ex3no5 {
//	// continue문을 이용하여 양수 합 구하는 프로그램. 정수 5개를 입력받아 양수 합을 구하는 출력하는 프로그램.
//	public static void main(String[] args) {
//
//		int integer, sum=0;
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("정수를 입력하시오: ");
//	
//		for(int i = 0; i < 5; i++) {
//			integer = scanner.nextInt();
//			
//			if(integer > 0) {						// 양수인 경우
//				sum += integer;
//			}
//			else  									// 음수인 경우, 더하지 않고 다음 반복으로 진행 
//				continue;
////			System.out.println(sum);
//		}
//		System.out.println("\n총 합은 " +sum+" 입니다.");
//		scanner.close();
//	}
//}
