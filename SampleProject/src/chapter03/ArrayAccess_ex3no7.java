//package chapter03;
//
//import java.util.Scanner;
//
//public class ArrayAccess_ex3no7 {
//	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);	// 객체 선언과 생성
//		
//		int []intArray = new int[5];			// 배열의 선언과 생성
//		int max = 0;							// 지역 변수 초기화
//		System.out.println("양수 5개를 입력하세요.");
//		for(int i = 0; i < 5; i++) {			// 배열의 크기 만큼 반복
//			intArray[i] = sc.nextInt();			// 인덱스 0~4까지 입력받음
//			if(intArray[i] > max) {				// 입력받은 수가 제일 크면 
//				max = intArray[i];				// max 변경
//			}
//		}
//		System.out.println("가장 큰 수는 " + max +"입니다.");
//		
//		sc.close();
//	}
//}
