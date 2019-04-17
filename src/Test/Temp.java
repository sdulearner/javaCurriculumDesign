package Test;

import java.io.IOException;

public class Temp
{
    private Temp()
    {
    }

    private static final Temp temp = new Temp();

    public static Temp getChatManager()
    {
        return temp;
    }

    public void add(Mysocket main)
    {
        this.main = main;
    }

    private static Mysocket main;

    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(1000);
                main.out("我是来捣乱的");
            } catch (InterruptedException | IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
