package Socket;

import ClientSocket.ClientSocket_Util;

import java.io.IOException;
import java.net.Socket;

public class MyTest
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 6666);
        new ClientSocket_Util(socket).start();
    }

}
