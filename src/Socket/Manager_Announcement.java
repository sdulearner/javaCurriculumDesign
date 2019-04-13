package Socket;

import Database.JDBC_Students;

import java.util.ArrayList;

public class Manager_Announcement implements Runnable
{

    private ArrayList<ChatSocket> socketList = new ArrayList<>();
    private ArrayList<Long> idlist = new ArrayList<>();
    private ChatSocket cs;

    public Manager_Announcement()
    {
    }



    public void add(ChatSocket socket)
    {
        socketList.add(socket);
    }

    public void subtract(ChatSocket socket)
    {
        socketList.remove(socket);
    }
    @Override
    public void run()
    {
        try
        {
            while (idlist.size() < JDBC_Students.count())
            {
                Thread.sleep(500);

                for (int i = 0; i < JDBC_Students.count(); i++)
                {
                    if (!idlist.contains(socketList.get(i).getId()))
                        socketList.get(i).outAnnouncemt(cs.getName(), cs.getTitle(), cs.getText(), cs.getTime());
                }
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
