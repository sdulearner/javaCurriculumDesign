package Socket;

import java.util.ArrayList;

public class Manager_File implements  Runnable
{
    private ArrayList<Socket_Util> sockets;
    private Socket_Util cs;
    private int flag;

    public Manager_File(Socket_Util socket, ArrayList<Socket_Util> socketList, int flag)
    {
        cs = socket;
        sockets = socketList;
        this.flag = flag;
    }

    @Override
    public void run()
    {
        switch (flag)
        {

            case '1'://传文件
            {
                for (int i = 0; i < sockets.size(); i++)
                {
                    if (sockets.get(i) != cs)
                    {
                        sockets.get(i).outUpload(cs.getFileName());
                    }
                }
            }
            break;
            case '2'://删文件
            {

                for (int i = 0; i < sockets.size(); i++)
                {
                    if (sockets.get(i) != cs)
                    {
                        sockets.get(i).outDeleteFile(cs.getFileNo());
                    }
                }

            }
        }
    }
}
