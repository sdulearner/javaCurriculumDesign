package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Listener_File extends Thread
{
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8018);
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("succeed!" + socket.getPort());
                new FileSocket(socket).start();

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
