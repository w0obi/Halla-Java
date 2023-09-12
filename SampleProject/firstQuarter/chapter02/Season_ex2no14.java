//package chapter02;
//
//import java.util.Scanner;
///* 1~12 사이의 월을 입력받아 봄, 여름, 가을, 겨울을 판단하여 출력하라. */
//public class Season_ex2no14 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		
//		System.out.println("월을 입력하세요:(1~12)");
//		int Month = scanner.nextInt();
//		
//		switch(Month) {
//		case 3: case 4: case 5: 
//			System.out.println("봄");
//			break;
//		
//		case 6: case 7: case 8:
//			System.out.println("여름");
//			break;
//			
//		case 9: case 10: case 11:
//			System.out.println("가을");
//			break;
//		
//		case 12: case 1: case 2:
//			System.out.println("겨울");
//			break;
//		
//		}
//
//		scanner.close();
//		
//	}	
//}
