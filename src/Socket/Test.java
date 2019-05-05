package Socket;

import ClientSocket.ClientSocket_Util;
import ClientSocket.SignIn;

import java.io.IOException;
import java.net.Socket;

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

        SignIn signIn = new ClientSocket_Util(socket).signIn(201800301165L, "88888888");

        System.out.println(signIn.toString());
    }
}
