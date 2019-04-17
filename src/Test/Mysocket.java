package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Mysocket extends Thread
{
    Socket socket;

    public Mysocket(Socket socket)
    {
        this.socket = socket;
    }

    public void out(String out) throws IOException
    {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        writer.println(out);
    }

    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String input;
            while ((input = reader.readLine()) != null)
            {
                writer.println(input);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
