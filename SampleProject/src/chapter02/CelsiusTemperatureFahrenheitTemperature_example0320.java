//package chapter02;
//import java.util.Scanner;
//public class CelsiusTemperatureFahrenheitTemperature_example0320 {
//	// 사용자로부터 섭씨 온도(℃)를 입력받아 화씨 온도(℉)로 변환하여 출력하고, 다시 섭씨 온도로 바꾸어 출력하는 프로그램 
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		double CelsiusTemp, FahrenheitTemp = 0, Constant = 1.8;
//		System.out.println("\t < 섭씨온도를 입력하시오: >");
//		Scanner scanner = new Scanner(System.in);
//		
//		CelsiusTemp = scanner.nextDouble();
//		FahrenheitTemp = (CelsiusTemp * Constant) + 32;
//		System.out.println("\t 섭씨온도 " +CelsiusTemp+ " ℃는" + " 화씨온도 " +(long)FahrenheitTemp+ " ℉ 입니다.");
//		
//		Scanner sc = new Scanner(System.in);
//		
//		FahrenheitTemp = scanner.nextDouble();
//		CelsiusTemp = (FahrenheitTemp - 32) / 1.8;
//		System.out.println("\t 화씨온도 " +FahrenheitTemp+ " ℉는" + " 섭씨온도 " +(long)CelsiusTemp+ " ℃ 입니다.");	
//		
//		scanner.close();
//		sc.close();
//	}
//}
