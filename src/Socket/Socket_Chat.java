package Socket;

import java.io.*;
import java.net.Socket;

public class Socket_Chat extends Thread
{
    private Socket socket;

    public Socket_Chat(Socket socket)
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

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
