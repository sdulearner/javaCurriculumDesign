package Socket;

import Database.JDBC_Vote;
import Entity.Result;
import Entity.Voting;

import java.io.*;
import java.net.Socket;

public class VotingThread extends Thread
{
    private Result result;
    private JDBC_Vote jdbc_vote = new JDBC_Vote();
    private Voting voting = new Voting();
    private int no;
    Socket socket;
    String input;
    private int[] array;

    public VotingThread(Socket socket, int no)
    {
        this.socket = socket;
        this.no = no;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            result = jdbc_vote.calculate(no);
            writer.println(result.getTitle());
            writer.println(result.getName());
            //选项内容
            writer.println(result.getOptions().length);
            for (int i = 0; i < result.getOptions().length; i++)
            {
                writer.println(result.getOptions()[i]);
            }
            //补充意见
            writer.println(result.getOpinions().length);
            for (int i = 0; i < result.getOpinions().length; i++)
            {
                writer.println(result.getOpinions()[i]);
                writer.println(result.getTime()[i]);
            }
            //投票结果
            array = new int[result.getOptions().length];
            for (int i = 0; i < result.getOptions().length; i++)
            {
                array[i] = Integer.parseInt(reader.readLine());
            }

            input = reader.readLine();
            、、//未完待续
            voting.setNO(no);
            voting.setVotes(array);
            voting.setOpinion(input);
            jdbc_vote.voting(voting);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
