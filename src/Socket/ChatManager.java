package Socket;

import java.util.ArrayList;
import java.util.List;

public class ChatManager
{
    private ChatManager()
    {
    }

    private static final ChatManager cm = new ChatManager();

    public static ChatManager getChatManager()
    {
        return cm;
    }

    static List<ChatSocket> socketList = new ArrayList<>();

    public void add(ChatSocket socket)
    {

        socketList.add(socket);

    }
//
//    public void publish(ChatSocket cs, String msg)
//    {
//        for (int i = 0; i < socketList.size(); i++)
//        {
//            ChatSocket csTemp = socketList.get(i);
//            if (!cs.equals(csTemp))
//            {
//                csTemp.out(msg + "\n");//不用发送给自己。
//            }
//        }
//
//    }
}