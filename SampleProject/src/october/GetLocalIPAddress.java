package october;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetLocalIPAddress {
    public static void main(String[] args) {
        try {
            InetAddress local = InetAddress.getLocalHost();
            byte[] address = local.getAddress();    // IP주소가 바이트 배열 저장됨
            System.out.println("사용 중인 호스트의 IP주소는 ");
            for(int i = 0; i<address.length; i++) {
                int unsigned = address[i] < 0 ? address[i]+256 : address[i];
                System.out.print(unsigned+".");
            }
        } catch (UnknownHostException e) {
            System.out.println("로컬 호스트의 IP 주소를 알 수 없습니다.");
        }
    }
}
