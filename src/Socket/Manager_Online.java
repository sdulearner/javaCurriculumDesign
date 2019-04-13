package Socket;

import java.util.ArrayList;

public class Manager_Online implements Runnable
{
    private ArrayList<ChatSocket> socketList;
    private ChatSocket cs;

    public Manager_Online(ChatSocket socket, ArrayList<ChatSocket> sockets)
    {
        this.cs = socket;
        this.socketList = sockets;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < socketList.size(); i++)
        {
            if (socketList.get(i) != cs)
            {
            socketList.get(i).outOnline(cs.getId(),cs.getName(),cs.getNickname());
            }
        }
    }
}
