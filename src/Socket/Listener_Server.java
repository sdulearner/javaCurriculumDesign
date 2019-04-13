package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Listener_Server extends Thread
{

    //    static ArrayList<Socket_Util> list = new ArrayList<Socket_Util>();
    public static Manager_Announcement managerAnnouncement = new Manager_Announcement();
//    static Manager_Voting managerVoting = new Manager_Voting();
//    static Manager_Chat managerChat =new Manager_Chat();
    static ArrayList<Long> idList = new ArrayList<>();

    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8108);
            while (true)
            {
//                                managerVoting.add(cs);
//               managerChat.add(cs);
                Socket socket = serverSocket.accept();
                System.out.println("succeed!" + socket.getPort());
//                JOptionPane.showMessageDialog(null, "有客户端连到本机8018端口");
                Socket_Util cs = new Socket_Util(socket);
                cs.start();
                managerAnnouncement.add(cs);

                System.out.println("建立成功");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}
