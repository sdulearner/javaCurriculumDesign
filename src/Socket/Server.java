package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    ServerSocket server;
    private int port = 8888;

    public Server() throws IOException
    {
        server = new ServerSocket(port);
        System.out.println("启动");
    }

    public void service()
    {
        Socket socket = null;
        while (true)
        {
            try
            {
                socket = server.accept();
                System.out.println("address:" + socket.getInetAddress() + ":" + socket.getPort());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                PrintWriter printWriter = new PrintWriter(writer, true);
                String info = null;
                while ((info = reader.readLine()) != null)
                {
                    System.out.println(info); //输出用户发送的消息
                    printWriter.println("you said:" + info); //向客户端返回用户发送的消息，println输出完后会自动刷新缓冲区
                    if (info.equals("quit"))
                    { //如果用户输入“quit”就退出
                        break;
                    }
                }


            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (socket != null)
                {
                    try
                    {
                        socket.close();

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws IOException
    {
        new Server().service();

    }


}
