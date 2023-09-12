//package chapter07;
//
//import java.util.ArrayList;
//import java.util.Scanner;
///* ArrayList<E>에는 capacity()메소드가 없으므로 주의!, 배열을 가변적크기로 관리. 
// * 문자열만 다루는 ArrayList<String> 활용,
// * 이름을 4개 입력받아 ArrayList에 저장하고, ArrayList에 저장된 이름을 모두 출력한 후, 제일 긴 이름을 출력.
// * */
//class ArrayListEx_ex7no3 {
//	public static void main(String[] args) {
//		//문자열만 삽입가능한 ArrayList 컬렉션 생성
//		var <String> a = new ArrayList<String>();
//		
//		Scanner sc = new Scanner(System.in);
//		for(int i = 0; i < 4; i++) {
//			System.out.println("이름을 입력하세요>>");
//			String s = sc.next();
//			a.add(s);							 //ArrayList컬렉션에 삽입
//		}
//		
//		for(int i = 0; i < a.size(); i++) {
//			String name = a.get(i);				 //ArrayList의 i번째 문자열 얻어오기
//			System.out.println(name + " ");
//		}
//		
//		int longestIndex = 0;		//현재 가장 긴 이름이 있는 ArrayList 내의 인덱스
//		for(int i = 0; i < a.size(); i++) {
//			if(a.get(longestIndex).length() < a.get(i).length())
//				longestIndex = i;
//		}
//		
//		System.out.println("\n가장 긴 이름은 : " +a.get(longestIndex));
//		sc.close();
//	}
//}
