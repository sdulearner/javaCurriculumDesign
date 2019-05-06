package Socket;

import Database.JDBC_Students;

import java.util.*;

public class Manager_Voting implements Runnable
{

    private Manager_Voting()
    {
    }

    private void setNO(int NO)
    {
        this.NO = NO;
    }

    private int NO;

    private static ArrayList<Manager_Voting> manager_votings = new ArrayList<>();

    public static void addManager(int no)
    {
        Manager_Voting manager_voting = new Manager_Voting();
        manager_voting.setNO(no);
        manager_votings.add(manager_voting);
    }

    public int getNO()
    {
        return NO;
    }

    //根据NO返回Manager_Voting
    public static Manager_Voting getManagerVoting(int no)
    {
        Manager_Voting manager_voting = null;
        for (int i = 0; i < manager_votings.size(); i++)
        {
            if (manager_votings.get(i).getNO() == no) manager_voting = manager_votings.get(i);
        }
        return manager_voting;
    }

    private Map<Socket_Util, Socket_Util> socketList = new HashMap<>();
    //usersVoted:已经投票的用户

    private ArrayList<Long> usersVoted = new ArrayList<>();

    public void setSocketList(Map<Socket_Util, Socket_Util> socketList)
    {
        this.socketList = socketList;
    }

    //    public void setSocket(Socket_Util socket)
//    {
//        this.cs = socket;

//    }

    public void addList(long id)
    {

        this.usersVoted.add(id);
    }

    public static ArrayList<Manager_Voting> getManager_votings()
    {
        return manager_votings;
    }

    @Override
    public void run()
    {
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

        Iterator<Long> iterator = usersVoted.iterator();
        while (iterator.hasNext())
        {
            users.remove(iterator);
        }

        boolean flag = false;
        //直到找到一个在线且没有投过票的用户 while循环才会结束
        while (true)
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            int random = (int) (1 + Math.random() * users.size());
            for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
            {
                if (entry.getKey().getId() == users.get(random))
                {
                    entry.getValue().outVoting(NO);
                    flag = true;
                    break;
                }
            }
            if (flag)
            {
                break;
            }
        }
    }
}
