package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread
{
    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("succeed!"+socket.getPort());
//                JOptionPane.showMessageDialog(null, "有客户端连到本机8888端口");
                ChatSocket cs = new ChatSocket(socket);
                cs.start();
                ChatManager.getChatManager().add(cs);
                System.out.println("建立成功");

            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}
