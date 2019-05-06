package Socket;

import ClientSocket.ClientSocket_Util;
import ClientSocket.SignIn;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * @description: Socket测试
 * @author: sdulearner
 * @create: 2019-05-05 07:33
 **/

public class Test
{

    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 5555);

        Map<String, String> map = new ClientSocket_Util(socket).selectLog();
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());

        }
        SignIn signIn = new ClientSocket_Util(socket).signIn(201800301165L, "88888888");
        new ClientSocket_Util(socket).closeConnection();
        System.out.println(signIn.toString());
        socket.close();
    }
}
