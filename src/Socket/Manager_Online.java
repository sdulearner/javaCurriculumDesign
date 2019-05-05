package Socket;

import java.util.ArrayList;

public class Manager_Online implements Runnable
{

    private Manager_Online()
    {
    }

    public static final Manager_Online MANAGER_ONLINE = new Manager_Online();

    public static Manager_Online getManagerOnline()
    {
        return MANAGER_ONLINE;
    }

    private ArrayList<Socket_Util> socketList = new ArrayList<>();
    private Socket_Util cs;

    public void add(Socket_Util socket_util)
    {
        socketList.add(socket_util);
    }

    public void setSocket(Socket_Util socket)
    {
        this.cs = socket;
    }

//    public static void main(String[] args)
//    {
//        new Thread(Manager_Online.getManagerOnline()).start();
//    }

    @Override
    public void run()
    {
        for (int i = 0; i < socketList.size(); i++)
        {
            if (socketList.get(i) != cs)
            {
                socketList.get(i).outOnline(cs.getId(), cs.getName(), cs.getNickname());
            }
        }
    }
}
