package chapter14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class CClient {
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		Socket client = null;
		Scanner scanner = new Scanner(System.in);
		
		try {
			client = new Socket("127.0.0.1",9999);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			System.out.println("클라이언트는 서버와의 연결을 원합니다");
			System.out.println("서버와의 연결이 완료되었습니다.");
			while(true) {
				System.out.print("클라이언트 : ");
				String sendMsg = scanner.nextLine();
				if(sendMsg.equalsIgnoreCase("Bye")) {
					System.out.println("클라이언트가 종료를 원함");
					break;
				}
				out.write(sendMsg + '\n');
				out.flush();
				
				String sendedMsg = in.readLine();
				if(sendedMsg.equalsIgnoreCase("Bye")) {
					System.out.println("클라이언트가 종료를 원함");
					break;
				}
				System.out.println("서버 : " + sendedMsg);
			}
			
		} catch (IOException e) { e.printStackTrace(); }
		finally {
				try {
					in.close();
					out.close();
					scanner.close();
					client.close();	
					System.out.println("연결이 끊겼습니다.");
			} catch (Exception e2) { e2.printStackTrace(); }
		}
	}
}

