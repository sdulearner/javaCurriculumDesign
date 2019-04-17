package Socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MyTest
{
    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("10.27.211.8", 5555);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext())
        {
            String line = scanner.nextLine();
        }
    }

}
