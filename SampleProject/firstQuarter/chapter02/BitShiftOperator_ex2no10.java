//package chapter02;
//
//public class BitShiftOperator_ex2no10 {
//	public static void main(String[] args) {
//		byte flag = 0b00001010;
//		
//		if((flag & 0b00001010) == 0)
//		System.out.println("온도는 0도 이하");
//		else
//		System.out.println("온도는 0도 이상");
//		
//		System.out.println("flag = " + Integer.toBinaryString(flag));
//		System.out.println("flag << 2 -->" + Integer.toBinaryString(flag << 2));
//		
//		short a = (short) 0x55ff;
//		short b = (short) 0x00ff;
//		
//		// 비트 연산
//		System.out.println("[비트 연산 결과]");
//		System.out.println("xor " + Integer.toHexString(a^b));
//	}
//}
