package Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 相与Listener_Server
 * @author: sdulearner
 * @create: 2019-05-05 10:57
 **/

public class Listener
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("succeed!" + socket.getPort());
                MySocket ms = new MySocket(socket);
                new Thread(ms).start();
                Manager.getManager().add(ms);
                System.out.println("建立成功");

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
