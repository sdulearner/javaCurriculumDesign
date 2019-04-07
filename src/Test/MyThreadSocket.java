package Test;

import java.io.*;
import java.net.Socket;

public class MyThreadSocket extends Thread
{
    Socket socket;
    EchoThread echoThread;

    public Socket getSocket()
    {
        return socket;
    }

    public MyThreadSocket(Socket socket)
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
                if (input.equals("1"))
                {
                    System.out.println("输入了1");
                    echoThread=new EchoThread(socket);
                    echoThread.setPriority(MAX_PRIORITY);
                    echoThread.start();
                    echoThread.join();
                }
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
