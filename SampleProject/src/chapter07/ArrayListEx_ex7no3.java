//package chapter07;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//
//class ArrayListEx_ex7no3 {
//	public static void main(String[] args) {
//		ArrayList<String> a = new ArrayList<String>();
//		
//		Scanner sc = new Scanner(System.in);
//		for(int i = 0; i < 4; i++) {
//			System.out.println("이름을 입력하세요>>");
//			String s = sc.next();
//			a.add(s);
//		}
//		
//		for(int i = 0; i < 4; i++) {
//			String name = a.get(i);
//			System.out.println(name + " ");
//		}
//		
//		int longestIndex = 0;
//		for(int i = 0; i < a.size(); i++) {
//			if(a.get(longestIndex).length() < a.get(i).length())
//				longestIndex = i;
//		}
//		
//		System.out.println("\n가장 긴 이름은 : " +a.get(longestIndex));
//		sc.close();
//	}
//}
