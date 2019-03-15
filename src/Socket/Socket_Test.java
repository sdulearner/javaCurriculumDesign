package Socket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Socket_Test
{

    public static void main(String[]args) throws UnknownHostException
    {
        InetAddress a= InetAddress.getLocalHost();
        String x=a.getHostAddress();
        System.out.println(a.getHostAddress()+"\t"+a.getHostName());
     }
}
