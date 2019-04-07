package Socket;

import Database.JDBC_Students;
import Entity.Voting;

import java.util.ArrayList;

public class VotingManager implements Runnable
{


    private ArrayList<ChatSocket> socketList = new ArrayList<>();
    private ArrayList<Long> idlist = new ArrayList<>();
    private ChatSocket cs;
    private long[] array;
    private VotingThread votingThread;
    public VotingManager()
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
                array = JDBC_Students.getId();
                Thread.sleep(500);
                for (int i = 0; i < JDBC_Students.count(); i++)
                {

                    if (!idlist.contains(array[i]))
                    {
                        socketList.get(i).outVoting();
                        votingThread=socketList.get(i).getVotingThread();
                        votingThread.join();
                    }
                }

            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
