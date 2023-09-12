//package chapter04;
///* 예제4-5 Book 클래스의 생성자를 this()를 이용하여 수정 
// * <출력>생성자 호출됨
// * 		춘향전 작자미상 */
//public class Book_ex4no5 {
//	String Title;
//	String Author;
//	
//	void show() { System.out.println(Title +", "+ Author); }
//	
//	public Book_ex4no5() {
//		this("", "");
//		System.out.println("생성자 호출됨");
//	}
//	public Book_ex4no5(String Title) {
//		this(Title, "작자미상");
//		
//	}	
//	public Book_ex4no5(String Title, String Author) {
//		this.Title = Title;
//		this.Author = Author;
//	}
//	public static void main(String[] args) {
//		Book_ex4no5 littlePrince = new Book_ex4no5("어린왕자", "생텍쥐페리");
//		Book_ex4no5 loveStory = new Book_ex4no5("춘향전");
//		Book_ex4no5 emptyBook = new Book_ex4no5();
//		loveStory.show();
//	}
//}
