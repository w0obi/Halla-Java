//package chapter02;
//
//import java.util.Scanner;
///* 다중 if-else문을 이용하여 입력받은 성적에 대해 학점을 부여하는 프로그램을 작성하라. */
//public class Grading_ex2no12 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		
//		System.out.println("성적을 입력하세요:(0~100)");
//		int Score = scanner.nextInt();
//		char Grande;
//		if(Score >= 90) {
//			Grande = 'A';
//		}
//		else if(Score >= 80) {
//			Grande = 'B';
//		}
//		else if(Score >= 70) {
//			Grande = 'C';
//		}
//		else if(Score >= 60) {
//			Grande = 'D';
//		}
//		else
//			Grande = 'F';
//		
//		System.out.println("학점은 " +Grande+ "입니다.");
//		scanner.close();
//	}
//}