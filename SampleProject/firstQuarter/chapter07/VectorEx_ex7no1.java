//package chapter07;
//
//import java.util.Vector;
//
///* 정수만 다루는 Vector<Integer> 제네릭 벡터를 생성하고, 활용 */
//public class VectorEx_ex7no1 {
//	public static void main(String[] args) {
//		//정수 값만 다루는 제네릭 벡터 생성
//		Vector<Integer> v = new Vector<Integer>(10);
//		
//		//벡터 요소 삽입하기
//		v.add(5);
//		v.add(4);
//		v.add(-1);
//		
//		//벡터 중간에 삽입하기
//		v.add(2,100);
//		
//		System.out.println(
//				"벡터 내의 요소 객체 수 : " +v.size());
//		System.out.println(
//				"벡터의 현재 용량 : " +v.capacity());
//		
//		//요소 얻어내기
//		for(int i = 0; i < v.size(); i++) {
//			int n = v.get(i);						//벡터의 i번째 요소를 알아낸다.
//			System.out.println(i+"번째 요소 : "+n);
//		}
//		
//		int sum = 0;
//		for(int i = 0; i < v.size(); i++) {
//			int n = v.elementAt(i);					//벡터의 i번째 요소를 알아낸다.
//			sum += n;
//		}
//		System.out.println(
//				"벡터에 있는 정수 합 : " +sum);
//		
//		System.out.println();
//		System.out.println();
//		
//		Vector<Integer> v1 = new Vector<Integer>(3); //용량이 3인 벡터	
//		
//		v1.add(5);
//		v1.add(4);
//		v1.add(-1);
//		
//		v1.add(2, 100);
//		
//		int n1 = v1.size();
//		int c1 = v1.capacity();
//		
//		System.out.println(							//n1은 4, c1는 6
//				"n1 = " +n1+ "," + " c1 = " + c1);
//		
//		//다섯, 여섯 번째 요소 출력하기 -----> java.lang.ArrayIndexOutOfBoundsException: 5 >= 4
//		Integer obj = v1.elementAt(3);				//index 0~3
//		int a = obj.intValue();
//		System.out.println("a = "+a);	int alast = v1.lastElement(); 
//		System.out.println("alast = "+alast);		//마지막 요소
//		
//		//벡터에서 요소 삭제
//		v.remove(1);
//		int n2 = v1.size();
//		int c2 = v1.capacity();
//		System.out.println(
//				"n2 = " +n2+ "," + " c2 = " + c2);
//		
//		v.removeAllElements();
//		int n3 = v1.size();
//		int c3 = v1.capacity();
//		System.out.println(
//				"n3 = " +n3+ "," + " c3 = " + c3);
//		
//	}
//}
