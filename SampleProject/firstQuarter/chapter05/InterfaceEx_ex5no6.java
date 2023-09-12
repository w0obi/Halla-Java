//package chapter05;
///*예제 5-6을 인터페이스 구현*/
//interface PhoneInterface {
//	final int TIMEOUT = 10000;
//	void sendCall();
//	void receiveCall();
//	default void printLogo() {
//		System.out.println("**Phone**");
//	}
//}
//class SamsungPhone implements PhoneInterface {
//	public void sendCall() {
//		System.out.println("띠리링");
//	}
//	public void receiveCall() {
//		System.out.println("전화가 왔습니다.");
//	}
//	public void finish() {
//		System.out.println("전화기에 불이 켜졌습니다.");
//	}
//}
//
//public class InterfaceEx_ex5no6 {
//	public static void main(String[] args) {
//		SamsungPhone phone = new SamsungPhone();
//		phone.printLogo();
//		phone.sendCall();
//		phone.receiveCall();
//		phone.finish();
//	}
//}
//
