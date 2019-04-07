package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener extends Thread
{

    //    static ArrayList<ChatSocket> list = new ArrayList<ChatSocket>();
    static AnnouncementManager announcementManager = new AnnouncementManager();
    static VotingManager votingManager = new VotingManager();
    static ChatManager chatManager=new ChatManager();
    static ArrayList<Long> idList = new ArrayList<>();

    public void run()
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8108);
            while (true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("succeed!" + socket.getPort());
//                JOptionPane.showMessageDialog(null, "有客户端连到本机8018端口");
                ChatSocket cs = new ChatSocket(socket);

                cs.start();
                announcementManager.add(cs);
                votingManager.add(cs);

                chatManager.add(cs);
                System.out.println("建立成功");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}
