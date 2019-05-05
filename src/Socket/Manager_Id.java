package Socket;

import java.util.ArrayList;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-04 21:18
 **/

public class Manager_Id
{
    private Manager_Id()
    {
    }

    private static final Manager_Id MANAGER_ID = new Manager_Id();

    public static Manager_Id getManagerId()
    {
        return MANAGER_ID;
    }

    ArrayList<Long> idList = new ArrayList<>();

    public ArrayList<Long> getIdList()
    {
        return idList;
    }

    public void add(long id)
    {
        idList.add(id);
    }
}
