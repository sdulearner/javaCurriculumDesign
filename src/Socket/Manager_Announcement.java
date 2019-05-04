package Socket;

import Database.JDBC_Students;

import java.util.ArrayList;

public class Manager_Announcement implements Runnable
{

    private ArrayList<Socket_Util> socketList = new ArrayList<>();
    private ArrayList<Long> idlist = new ArrayList<>();
    private Socket_Util cs;

    public ArrayList<Socket_Util> getSocketList()
    {
        return socketList;
    }


       public void add(Socket_Util socket)
    {
        socketList.add(socket);
    }

    public void subtract(Socket_Util socket)
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
                        socketList.get(i).outAnnouncement(cs.getName(), cs.getTitle(), cs.getText(), cs.getTime());
                }
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
