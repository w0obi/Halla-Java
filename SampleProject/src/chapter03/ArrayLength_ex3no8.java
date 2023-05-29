//package chapter03;
//import java.util.Scanner;
//public class ArrayLength_ex3no8 {
//	public static void main(String[] args) {
//		/* 배열의 length 필드를 이용하여 배열의 크기만큼 정수를 입력받고 평균 출력. */		
//		Scanner a = new Scanner(System.in);			//Scanner 객체 선언과 생성
//		
//		System.out.println("5개의 정수를 입력하세요.");	//사용자로부터 정수 5개를 입력받음
//		
//		int integer[] = new int[5];					//배열의 선언과 생성
//		double sum = 0.0;
//		
//		for(int i=0; i < integer.length; i++) {		//배열의 크기만큼 반복
//			integer[i] = a.nextInt();				//인덱스 0~4까지 입력받음
//		}
//
//		for(int i=0; i < integer.length; i++) {		//배열의 크기만큼 반복
//			sum += integer[i];						//배열에 저장된 정수 값 더하기
//		}
//		
//		System.out.println("평균은 " + sum/integer.length+ "입니다.");
//		
//		a.close();
//	}
//}
