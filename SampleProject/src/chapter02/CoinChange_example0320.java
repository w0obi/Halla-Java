//package chapter02;
//import java.util.Scanner;
//public class CoinChange_example0320 {
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		// 사용자로부터 입력을 받아 500원, 100원, 50원, 10원으로 교환해주는 프로그램
//		int FiveHundred, Hundred, Fifty, Ten;
//		System.out.println("\t < 액수를 입력하시오: >");	
//		int Input = scanner.nextInt();
//		
//		if(Input > 0) {
//			FiveHundred = Input / 500;
//			
//			Hundred = (Input % 500) / 100;
//			
//			Fifty = ((Input % 500) % 100) / 50;
//			
//			Ten = (((Input % 500) % 100) % 50) / 10;
//			
//			System.out.print("\t " + Input + "원 은\n" + "\t 500원 " + (int)FiveHundred + "개,\n" + "\t 100원 " + (int)Hundred + "개,\n" + "\t 50원 " + (int)Fifty + "개,\n" + "\t 10원 " + (int)Ten + "개 입니다.");
//			
//		}
//		else {	// 입력이 0 이하일 때 
//			System.out.println("다시 입력해주세요.");
//		}
//		
//		scanner.close();
//	}
//}
