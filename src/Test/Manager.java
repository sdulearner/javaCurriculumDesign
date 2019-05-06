package Test;

import java.util.ArrayList;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-05 11:00
 **/

public class Manager implements  Runnable
{

    private Manager()
    {
    }

    private static final Manager MANAGER = new Manager();

    public static Manager getManager()
    {
        return MANAGER;
    }

    ArrayList<MySocket> mySockets = new ArrayList<>();

    public ArrayList<MySocket> getIdList()
    {
        return mySockets;
    }

    public void add(MySocket mySocket)
    {
        mySockets.add(mySocket);
    }

    public void remove(MySocket mySocket)
    {
        mySockets.remove(mySocket);
    }

    @Override
    public void run()
    {
        for (MySocket a : mySockets)
            System.out.println(a.getA());
    }
}
