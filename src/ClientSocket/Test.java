package ClientSocket;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-04 22:29
 **/

public class Test
{

    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket("127.0.0.1", 8018);
        File file = new File("D:\\迅雷下载\\蜘蛛侠：平行宇宙HD1080P高清中英双字.mp4");
//        new ClientSocket_File('3', socket, "蜘蛛侠：平行宇宙HD1080P高清中英双字.mp4", file);
    }
}
