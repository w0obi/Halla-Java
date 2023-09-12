package chapter06;

import java.util.StringTokenizer;

/* "name=kitae&addr=seoul&age=21"를 '&'문자를 기준으로 분리하는 코드를 작성 */
public class StringTokenizer_ex6no7 {
	public static void main(String[] args) {
		
		String query = "name=kitae&addr=seoul&age=21";
		StringTokenizer st = new StringTokenizer(query, "&");
		
		int n = st.countTokens();
		System.out.println("토큰 개수 = " +n);
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			System.out.println(token);
		}
	}
}
