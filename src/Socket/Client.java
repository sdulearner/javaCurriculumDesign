package Socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.System.*;

public class Client
{

    static final int PORT = 8888; //连接端口
    static final String HOST = "10.27.211.8"; //连接地址
    Socket socket;

    public Client() throws UnknownHostException, IOException
    {
        socket = new Socket(HOST, PORT); //创建客户端套接字
    }

//send()方法

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        new Client().send();
    }


    public void send()
    {
        try
        {
            //客户端输出流，向服务器发消息
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //客户端输入流，接收服务器消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(bw, true); //装饰输出流，及时刷新
            Scanner in = new Scanner(System.in); //接受用户信息
            String msg = null;
            while ((msg = in.next()) != null)
            {
                pw.println(msg); //发送给服务器端
                out.println(br.readLine()); //输出服务器返回的消息
                if (msg.equals("quit"))
                {
                    break; //退出
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (null != socket)
            {
                try
                {
                    socket.close(); //断开连接
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
