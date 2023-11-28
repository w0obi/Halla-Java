package chapter14;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class CServer {
	public static void main(String[] args) {
		BufferedReader in = null;
		BufferedWriter out = null;
		ServerSocket server = null;
		Socket socket = null;
		Scanner scanner = new Scanner(System.in);
		try {
			server = new ServerSocket(9999);
			System.out.println("서버 구축 성공!");
			socket = server.accept();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("서버가 클라이언트의 요청을 기다리는중입니다.");
			System.out.println("클라이언트와 연결 성공!!");
			while(true) {
				String sendedMsg = in.readLine(); // 클라이언트가 보낸 메세지
				if(sendedMsg.equalsIgnoreCase("Bye")) {
					System.out.println("클라이언트가 연결을 끊기를 원함.");
					break;
				}
				System.out.println("클라이언트 : " + sendedMsg);
				System.out.print("서버 : ");
				String sendMsg = scanner.nextLine(); // 내가 클라이언트 한테 보낼 메세지
				if(sendMsg.equalsIgnoreCase("Bye")) {
					System.out.println("서버가 연결을 끊기를 원함.");
					break;
				}
				out.write(sendMsg + '\n');
				out.flush();
			}
		} catch (IOException e) { e.printStackTrace(); }
		finally {
			try {
				in.close();
				out.close();
				scanner.close();
				socket.close();
				server.close();
				System.out.println("모든 자원 반환함");
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}