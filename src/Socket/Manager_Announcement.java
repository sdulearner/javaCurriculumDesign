package Socket;

import Database.JDBC_Students;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Manager_Announcement implements Runnable
{
    private static final ArrayList<Manager_Announcement> manager_announcements = new ArrayList<>();
    private Map<Socket_Util, Socket_Util> socketList = new LinkedHashMap<>();
    private int NO;

    //已经收到公告的用户
    private ArrayList<Long> usersRead = new ArrayList<>();

    private Manager_Announcement(int no)
    {
        this.NO = no;
    }

    private int getNO()
    {
        return this.NO;
    }

    private void setSockets(Map<Socket_Util, Socket_Util> map)
    {
        this.socketList = map;
    }


    public static void addManager(int no)
    {
        manager_announcements.add(new Manager_Announcement(no));
    }

    public static void subtractManager(int no)
    {
        for (int i = 0; i < manager_announcements.size(); i++)
        {
            if (manager_announcements.get(i).getNO() == no) manager_announcements.remove(i);
        }
    }

    public static void setAllSocketLists(Map<Socket_Util, Socket_Util> socketList1)
    {
        for (int i = 0; i < manager_announcements.size(); i++)
        {
            manager_announcements.get(i).setSockets(socketList1);
        }
    }

    //根据NO返回Manager_Announcement
    public static Manager_Announcement getManagerAnnouncement(int no)
    {
        Manager_Announcement manager_announcement = null;
        for (Manager_Announcement managerAnnouncement : manager_announcements)
        {
            if (managerAnnouncement.getNO() == no) manager_announcement = managerAnnouncement;
        }
        return manager_announcement;
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
                Thread.sleep(1000);
//                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
//                {
//
//                    entry.getValue().outAnnouncement(cs.get_Name(), cs.getTitle(), cs.getText(), new Timestamp(System.currentTimeMillis()));
//                }
                System.out.println("公告时间" + NO + "又过去了1秒");
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

                for (Long aLong : usersRead)
                {
                    users.remove(aLong);
                }

                System.out.println("用户" + users.toString() + "未读此公告");
                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    System.out.println("用户" + entry.getKey().getId() + "在线");
                    if (users.contains(entry.getKey().getId()))//如果在线的学生里有没收到公告的
                    {
                        entry.getValue().outAnnouncement(NO);
                        this.usersRead.add(entry.getKey().getId());
                        System.out.println("已经向" + entry.getKey().getId() + "发送公告");
                        System.out.println("已读用户：" + usersRead.toString());
                    }
                }
                System.out.println("\n");
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("所有人已经收到了公告");
        Manager_Announcement.subtractManager(NO);
        System.out.println("投票线程" + NO + "已结束");
    }
}
