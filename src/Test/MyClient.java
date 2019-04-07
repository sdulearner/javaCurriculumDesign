package Test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MyClient extends Thread
{
    Socket socket;
    String input;

    public void run()
    {
        try
        {
            socket = new Socket("127.0.0.1", 8888);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            while (scanner.hasNext())
            {
                input = scanner.nextLine();
                System.out.println(input);
                writer.println(input);
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
