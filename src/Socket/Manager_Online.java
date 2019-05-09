package Socket;

import java.util.HashMap;
import java.util.Map;

public class Manager_Online implements Runnable
{


    private char flag;

    private Manager_Online()
    {
    }

    private static final Manager_Online MANAGER_ONLINE = new Manager_Online();

    public static Manager_Online getManagerOnline()
    {
        return MANAGER_ONLINE;
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

    public Map<Socket_Util, Socket_Util> getSocketList()
    {
        return socketList;
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
            case '1'://上线提示
            {

                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    if (!entry.getKey().equals(cs))
                    {
                        entry.getValue().outOnline(cs.getId());
                    }
                }
            }
            break;
            case '2'://下线提示
            {
                for (Map.Entry<Socket_Util, Socket_Util> entry : socketList.entrySet())
                {
                    if (!entry.getKey().equals(cs))
                    {
                        entry.getValue().outOffLine(cs.getId());
                    }
                }

            }
        }

    }
}
