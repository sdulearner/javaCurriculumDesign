package Socket;

import ClientSocket.ClientSocket_Util;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description: Socket测试
 * @author: sdulearner
 * @create: 2019-05-05 07:33
 **/

public class Test
{

    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 5555);

        Scanner scanner = new Scanner(System.in);
        ClientSocket_Util clientSocket_util = new ClientSocket_Util(socket);
        String line = null;
        while (scanner.hasNext())
        {
            line = scanner.nextLine();
            switch (line)
            {
                case "2":
                {
                    System.out.println("请输入Id：");
                    clientSocket_util.signIn(Long.parseLong(scanner.nextLine()), "123456");
                }
                break;
                case "0":
                {
                    System.out.println("打开谁的私聊面板：");
                    System.out.println(clientSocket_util.openChattingWindow(Long.parseLong(scanner.nextLine())));
                    System.out.println("已经打开私聊面板");
                }
                break;
                case "a":
                {
                    System.out.println(clientSocket_util.openChattingWindow());
                    System.out.println("已经打开大群聊天面板");
                }
                break;
                case "f":
                {
                    System.out.println("向谁发消息：");
                    clientSocket_util.sendText(Long.parseLong(scanner.nextLine()), "啊哈", false);
                    System.out.println("已经发送私聊消息");
                }
                break;
                case "g":
                {
                    clientSocket_util.sendText("ahahhahahaha", false);
                    System.out.println("已经向大群发送消息");
                }
                break;
                case "quit":
                {
                    clientSocket_util.closeConnection();
                    break;
                }
                default:
                    break;
            }
            if (line.equals("quit")) break;
        }

    }
}
