package chapter06;
/* java의 string은 null을 포함하지 않음. */
public class StringEx_ex6no6 {
	public static void main(String[] args) {
		String a = new String(" C#");
		String b = new String(",C++ ");
		
		System.out.println(a+ "의 길이는 " + a.length());
		System.out.println(a.contains("#"));
		
		a = a.concat(b);
		System.out.println(a);
		
		a = a.trim();
		System.out.println(a);
		
		a = a.replace("C#", "Java");
		
	}
}
