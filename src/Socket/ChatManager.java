package Socket;

import java.util.ArrayList;
import java.util.List;

public class ChatManager //implements  Runnable
{
    ChatManager()
    {
    }

    private static final ChatManager cm = new ChatManager();

    public static ChatManager getChatManager()
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

//    public void publishAnnouncement(ChatSocket cs)
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