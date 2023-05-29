//package chapter03;
//import java.util.Scanner;
//
//public class StringCount_example0329 {
//	/* 202121027 김현정 */
//	public static void main(String[] args) {
//	Scanner n = new Scanner(System.in);
//	System.out.print("문자열을 입력 : ");
//	String Arr = n.nextLine();				// 공백을 포함한 문자열을 입력받기 위해 nextLine()
//	int Big=0, Small=0, Number=0;
//	
//	for(int i =0; i < Arr.length(); i++) {
//		if(Arr.charAt(i) >= 97 && Arr.charAt(i) <= 122) {
//			Small++;
//		}
//		if(Arr.charAt(i) >= 65 && Arr.charAt(i) <= 92) {
//			Big++;
//		}
//		if(Arr.charAt(i) >= 48 && Arr.charAt(i) <= 57) {
//			Number++;
//		}
//	}
//	System.out.print("대문자 : " + Big + "개," + " 소문자 : " 
//						+ Small + "개," + " 숫자 : " + Number + "개");
// }
//}
