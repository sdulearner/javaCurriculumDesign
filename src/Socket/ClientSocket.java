package Socket;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientSocket
{
    static int port = 8080;
    static String host = "10.27.211.8";
    Socket socket;

    public ClientSocket() throws IOException
    {
        socket = new Socket(host, port);
    }

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        new Client().send();
    }

    public void send() throws IOException
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(bw, true);
            /*这里读入用户数据*/

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
