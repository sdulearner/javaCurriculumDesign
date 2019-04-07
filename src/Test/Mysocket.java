package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Mysocket  extends Thread
{
    private MyThreadSocket a;
    private  MyThreadSocket b;
    @Override
    public void run()
    {
        try
        {
            ServerSocket server=new ServerSocket(8888);
           Socket socket=server.accept();

            MyThreadSocket myThreadSocket=new MyThreadSocket(socket);
            a=myThreadSocket;
            b=myThreadSocket;

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
