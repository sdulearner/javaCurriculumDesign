package Socket;

import Database.JDBC_Students;

import java.sql.Timestamp;
import java.util.*;

public class Manager_Announcement implements Runnable
{
    private Manager_Announcement()
    {
    }

    private static final Manager_Announcement MANAGER_ANNOUNCEMENT = new Manager_Announcement();

    public static Manager_Announcement getManagerAnnouncement()
    {
        return MANAGER_ANNOUNCEMENT;
    }

    private Map<Socket_Util, Socket_Util> socketList = new HashMap<>();
    private Socket_Util cs;
    private static ArrayList<Long> usersRead = new ArrayList<>();

    public void add(Socket_Util socket_util1, Socket_Util socket_util)
    {
        socketList.put(socket_util1, socket_util);
    }

    public void subtract(Socket_Util socket_util)
    {
        socketList.remove(socket_util);
    }

    public void setSocket(Socket_Util socket)
    {
        this.cs = socket;
    }

    @Override
    public void run()
    {
//        ArrayList idlist = new ArrayList();//已经收到的用户
//            while (idlist.size() < JDBC_Students.count())
//            {
//                Thread.sleep(500);
//
//                for (int i = 0; i < JDBC_Students.count(); i++)
//                {
//                    if (!idlist.contains(socketList.get(i).getId()))
//                    {
//                        socketList.get(i).outAnnouncement(cs.getName(), cs.getTitle(), cs.getText(), cs.getTime());
//                    }
//                }
//            }
        //直到所有用户接收一遍公告才会结束该线程
        while (usersRead.size() < JDBC_Students.count())
        {
            try
            {
                Thread.sleep(300);
//                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
//                {
//
//                    entry.getValue().outAnnouncement(cs.get_Name(), cs.getTitle(), cs.getText(), new Timestamp(System.currentTimeMillis()));
//                }

                JDBC_Students jdbc_students = new JDBC_Students();

                int count = JDBC_Students.count();//现在的学生数

                long[] array = jdbc_students.getId();

                Long[] idList = new Long[count];
                for (int i = 0; i < count; i++)
                {
                    idList[i] = array[i];
                }
                //所有学生的Id
                ArrayList<Long> users = new ArrayList<Long>(count);
                Collections.addAll(users, idList);

                Iterator<Long> iterator = usersRead.iterator();
                while (iterator.hasNext())
                {
                    users.remove(iterator);
                }
                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    if (users.contains(entry.getKey().getId()))//如果在线的学生里有没收到公告的
                    {
                        entry.getValue().outAnnouncement(cs.get_Name(), cs.getTitle(), cs.getText(), new Timestamp(System.currentTimeMillis()));
                        users.remove(entry.getKey().getId());
                    }
                }


            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
