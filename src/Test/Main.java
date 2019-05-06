package Test;

import Entity.Announcement;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-05 11:55
 **/

public class Main
{

    public static void main(String[] args)
    {
        Announcement a = new Announcement("aha1", "title1", "text1", new Timestamp(System.currentTimeMillis()));
        Announcement b = new Announcement("aha2", "title2", "text2", new Timestamp(System.currentTimeMillis()));
        Announcement c = new Announcement("aha3", "title3", "text3", new Timestamp(System.currentTimeMillis()));

        Announcement[] announcements = {a, b, c};
        ArrayList<Announcement> announceList = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {

            announceList.add(announcements[i]);
            System.out.println(announceList.get(i));
        }
        Map map=new HashMap();


    }
}
