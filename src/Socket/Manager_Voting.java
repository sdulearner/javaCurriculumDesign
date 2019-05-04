package Socket;

import Database.JDBC_Students;

import java.util.ArrayList;
import java.util.Random;

public class Manager_Voting implements Runnable
{


    private ArrayList<Socket_Util> socketList;
    private ArrayList<Long> idlist = new ArrayList<>();//已经投票的用户
    private Socket_Util cs;
    private long[] array;
    private VotingThread votingThread;
    private JDBC_Students jdbc_students;

    public Manager_Voting(ArrayList<Socket_Util> managerAnnouncement)
    {
        socketList = managerAnnouncement;
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
                jdbc_students = new JDBC_Students();
                array = jdbc_students.getId();
                Thread.sleep(333);
                int i;
                Random random = new Random();
                do
                {
                    i = random.nextInt(array.length);
                } while (idlist.contains(array[i]));


                socketList.get(i).outVoting();
                idlist.add(array[i]);
//                if (!idlist.contains(array[i]))
//                {
//                }
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
