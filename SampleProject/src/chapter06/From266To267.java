package chapter06;

public class From266To267 {
	/* String의 생성과 특징
	 * -스트링 리터럴 ;응용프로그램에서 공유됨
	 * -new string()으로 생성된 스트링 ;스트링은 공유 불가
	 * -스트링객체는 수정 불가능 ;한번 생성한 스트링은 수정이 불가
	 * -스트링 비교 ;두 스트링을 비교하기 위해서는 ==대신 eqauals() 사용. 
	 * 
	 * 주요 메소드
	 * int length() ;스트링의 길이(문자 개수)를 리턴. --->(주의!) 배열에선 필드, 스트링에선 메소드! 헷갈리지 않기
	 * 
	 * String 활용
	 * -스트링비교, equals()와 compareTo()
	 * ;스트링 비교에 == 연산자 절대 사용 금지
	 * ;equals()스트링이 같은면 true, 아니면  false를 리턴.
	 * -230508-
	 * 
	 * 
	 * 
	 * 공백제거 String trim()
	 * 
	 * collection특징
	 * 1.컬렉션은 제네릭기법으로 구현
	 * -제네릭은 특정타입만 다루지 않고, 여러 종류의 타입으로 사용할 수 있다.
	 * -컬렉션의 요소는 객체만 가능.
	 * 
	 * Vector<Integer> v = new Vector<Integer>(7);	//용량이 7인 벡터
	 * v.add(5);	//자동 언박싱
	 * v.add(4);
	 * 
	 * ArrayList<E>클래스의 주요 메소드에 capacity()가 없다는 것 알고있기.
	 * -230510-
	 *  
	 * */
}
