package Socket;

import java.util.HashMap;
import java.util.Map;

public class Manager_File implements Runnable
{

    private char flag;
    private int NO;
//
//    public Manager_File(Socket_Util socket, ArrayList<Socket_Util> socketList, int flag)
//    {
//        cs = socket;
//        sockets = socketList;
//        this.flag = flag;
//    }

    private Manager_File()
    {
    }

    private static final Manager_File MANAGER_FILE = new Manager_File();

    public static Manager_File getManagerFile()
    {
        return MANAGER_FILE;
    }

    private Map<Socket_Util, Socket_Util> socketList = new HashMap<>();

    public void add(Socket_Util socket_util1, Socket_Util socket_util)
    {
        socketList.put(socket_util1, socket_util);
    }

    public void subtract(Socket_Util socket_util)
    {
        socketList.remove(socket_util);
    }


    public void setFlag(char flag)
    {
        this.flag = flag;
    }

    public void setNO(int NO)
    {
        this.NO = NO;
    }

    @Override
    public void run()
    {
        switch (flag)
        {

            case '1'://传文件
            {
                System.out.println("正在更新在线同学的文件列表：ADD");
                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    entry.getValue().outUpload(NO);
                }

            }
            break;
            case '2'://删文件
            {
                System.out.println("正在更新在线同学的文件列表：DELETE");

                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    entry.getValue().outDeleteFile(NO);
                }

            }
        }
        System.out.println("更新完毕");
    }
}
