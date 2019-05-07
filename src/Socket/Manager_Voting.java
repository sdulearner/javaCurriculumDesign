package Socket;

import Database.JDBC_Students;
import Database.JDBC_Vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Manager_Voting implements Runnable
{
    private static final ArrayList<Manager_Voting> manager_votings = new ArrayList<>();
    private Map<Socket_Util, Socket_Util> socketList = new LinkedHashMap<>();
    private int NO;
    private Socket_Util cs;

    //usersVoted:已经投票的用户
    private ArrayList<Long> usersVoted = new ArrayList<>();

    private Manager_Voting(Socket_Util socket_util, int no)
    {
        this.NO = no;
        this.cs = socket_util;
    }

    private int getNO()
    {
        return this.NO;
    }

    private void setSockets(Map<Socket_Util, Socket_Util> socketList)
    {
        this.socketList = socketList;
    }


    public static void addManager(int no, Socket_Util socket_util)
    {
        manager_votings.add(new Manager_Voting(socket_util, no));
    }

    public static void subtractManager(int no)
    {
        for (int i = 0; i < manager_votings.size(); i++)
        {
            if (manager_votings.get(i).getNO() == no) manager_votings.remove(i);
        }
    }

    public static void setAllSocketLists(Map<Socket_Util, Socket_Util> socketList1)
    {
        for (int i = 0; i < manager_votings.size(); i++)
        {
            manager_votings.get(i).setSockets(socketList1);
        }
    }

    //根据NO返回Manager_Voting
    public static Manager_Voting getManagerVoting(int no)
    {
        Manager_Voting manager_voting = null;
        for (Manager_Voting managerVoting : manager_votings)
        {
            if (managerVoting.getNO() == no) manager_voting = managerVoting;
        }
        return manager_voting;
    }

    @Override
    public void run()
    {
        JDBC_Students jdbc_students = new JDBC_Students();

        int count = JDBC_Students.count();//现在的学生数
        if (usersVoted.size() < count - 1)
        {
            long[] array = jdbc_students.getId();

            Long[] idList = new Long[count];
            for (int i = 0; i < count; i++)
            {
                idList[i] = array[i];
            }
            //所有学生的Id
            ArrayList<Long> users = new ArrayList<Long>(count);
            Collections.addAll(users, idList);

            for (Long aLong : usersVoted)
            {
                users.remove(aLong);
            }
            users.remove(cs.getId());//减去发布人
            System.out.println("用户" + users.toString() + "未投票");
            boolean flag = false;
            //直到找到一个在线且没有投过票的用户 while循环才会结束
            while (true)
            {
                try
                {
                    String name = null;
                    Thread.sleep(500);
                    System.out.println("投票时间已经过去了1秒");
                    System.out.println("已投票用户：" + usersVoted.toString() + "\n");
                    int random = (int) (1 + Math.random() * users.size());

                    for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                    {
                        System.out.println("用户" + entry.getKey().getId() + "在线");
                        if (entry.getKey().getId() == users.get(random - 1))
                        {
                            entry.getValue().outVoting(NO);
                            name = entry.getKey().get_Name();
                            this.usersVoted.add(entry.getKey().getId());
                            flag = true;
                            break;
                        }
                    }
                    if (flag)
                    {
                        System.out.println("已成功发给：" + name);
                        System.out.println("已投票用户：" + usersVoted.toString());
                        break;
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        } else
        {
            JDBC_Vote jdbc_vote = new JDBC_Vote();
            JDBC_Vote.setFlag(NO);
            System.out.println("投票[" + jdbc_vote.selectVotes().get(NO) + "]已完成");
            cs.votingResult(NO);
        }
    }
}