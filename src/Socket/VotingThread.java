package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class VotingThread extends Thread
{
    Socket socket;
    public VotingThread(Socket socket)
    {
        this.socket=socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
