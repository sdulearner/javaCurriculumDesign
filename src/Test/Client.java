package Test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-05 11:08
 **/

public class Client
{
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("127.0.0.1", 8888);
            Scanner scanner = new Scanner(System.in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            while (scanner.hasNext())
            {
                writer.println(scanner.nextLine());
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
