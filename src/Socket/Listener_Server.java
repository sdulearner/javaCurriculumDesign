package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener_Server extends Thread
{


    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(5555);
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("succeed!" + socket.getPort());
                Socket_Util cs = new Socket_Util(socket);
                cs.start();
//                Manager_Online.getManagerOnline().add(cs);
//                Manager_Announcement.getManagerAnnouncement().add(cs);
//                Manager_Voting.getManagerVoting().add(cs);
//                Manager_File.getManagerFile().add(cs);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}
