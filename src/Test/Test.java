package Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Test extends Thread
{
    private static Socket socket;

    public static void main(String[] args) throws IOException
    {
         socket = new Socket("127.0.0.1", 5555);
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        String line;
        while (scanner.hasNext())
        {
            line = scanner.nextLine();
//            System.out.println(line);
            writer.println(line);
            System.out.println(br.readLine());
        }

    }
}