package Test;

import java.io.*;
import java.net.Socket;

/**
 * @description: 类似于Socket_Util
 * @author: sdulearner
 * @create: 2019-05-05 10:54
 **/

public class MySocket implements Runnable
{
    private int a;
    Socket socket;

    public int getA()
    {
        return a;
    }

    public MySocket(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String input;
            while ((input = reader.readLine()) != null)
            {
                if (input.equals("a"))
                {
                    this.a = 1;
                } else if (input.equals("b"))
                {

                    new Thread(Manager.getManager()).start();
                }else  {

//                    writer.println("aha");
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
