package Socket;

import java.util.HashMap;
import java.util.Map;

public class Manager_File implements Runnable
{

    private char flag;
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

    private  static final Manager_File MANAGER_FILE = new Manager_File();

    public static Manager_File getManagerFile()
    {
        return MANAGER_FILE;
    }

    private Map<Socket_Util, Socket_Util> socketList = new HashMap<>();
    private Socket_Util cs;

    public void add(Socket_Util socket_util1, Socket_Util socket_util)
    {
        socketList.put(socket_util1, socket_util);
    }

    public void subtract(Socket_Util socket_util)
    {
        socketList.remove(socket_util);
    }

    public void setSocket(Socket_Util socket)
    {
        this.cs = socket;
    }

    public void setFlag(char flag)
    {
        this.flag = flag;
    }

    @Override
    public void run()
    {
        switch (flag)
        {

            case '1'://传文件
            {
                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    entry.getValue().outUpload(cs.getFileName(), cs.getFileSize());
                }
            }
            break;
            case '2'://删文件
            {

                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    entry.getValue().outDeleteFile(cs.getFileNo());
                }

            }
        }
    }
}
