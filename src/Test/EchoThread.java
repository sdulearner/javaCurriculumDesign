package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoThread  extends Thread

{
    Socket socket;

    public EchoThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String input;
            while ((input = reader.readLine()) != null)
            {
                System.out.println(input);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
