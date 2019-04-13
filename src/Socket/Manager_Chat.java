package Socket;

import java.util.ArrayList;

public class Manager_Chat //implements  Runnable
{
    Manager_Chat()
    {
    }

    private static final Manager_Chat cm = new Manager_Chat();

    public static Manager_Chat getChatManager()
    {
        return cm;
    }

    static ArrayList<ChatSocket> socketList = new ArrayList<>();

    public void add(ChatSocket socket)
    {
        socketList.add(socket);
    }

    public void subtract(ChatSocket socket)
    {
        socketList.remove(socket);
    }

    public void publishVote()
    {
    }

    public void publishAnnouncement(ChatSocket cs)
    {
        for (int i = 0; i < socketList.size(); i++)
        {
            ChatSocket csTemp = socketList.get(i);
            if (!cs.equals(csTemp))
            {
                csTemp.outAnnouncemt(cs.get_Name(),cs.getTitle(),cs.getText(),cs.getTime());//不用发送给自己。
            }
        }

    }

//    @Override
//    public void run()
//    {
//        for (int i = 0; i < socketList.size(); i++)
//        {
//            ChatSocket csTemp = socketList.get(i);
//            if (!cs.equals(csTemp))
//            {
//                csTemp.outAnnouncemt(cs.get_Name(),cs.getTitle(),cs.getText(),cs.getTime());//不用发送给自己。
//            }
//        }
//
//    }
}