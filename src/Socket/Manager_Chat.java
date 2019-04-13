package Socket;

import java.util.ArrayList;

public class Manager_Chat implements Runnable
{
    private ArrayList<Socket_Util> socketList = new ArrayList<>();


    @Override
    public void run()
    {
//        for (int i = 0; i < socketList.size(); i++)
//        {
//            Socket_Util csTemp = socketList.get(i);
//            if (!cs.equals(csTemp))
//            {
//                csTemp.outAnnouncemt(cs.get_Name(),cs.getTitle(),cs.getText(),cs.getTime());//不用发送给自己。
//            }
//        }

    }
}