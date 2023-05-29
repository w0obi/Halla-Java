//package chapter03;
//
//public class ScoreAverage_ex3no10 {
////2차원 배열에 학년별로 1, 2학기 성적을 저장하고, 4년 전체 평점 평균을 출력하라.
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		/*int intArray[][];
//		intArray = new int[2][5];*/
//		/* 선언과 생성 동시 예
//		 * int intArray[][] = new int[2][5];*/
//		double score[][] = {{3.3, 3.4},
//							{3.5, 3.6},
//							{3.7, 4.0},
//							{4.1, 4.2} };
//		double sum = 0;
//		for(int year = 0; year < score.length; year++) 
//			for(int term=0; term < score[year].length; term++)
//				sum += score[year][term];
//		
//		int n=score.length;
//		int m=score[0].length;
//		System.out.print("4년 전체 평점 평균은" + sum/(n*m) +"입니다.");
//	}
//}