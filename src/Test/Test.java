package Test;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Test
{
    static ArrayList<Integer> list = new ArrayList<>();
    private static DecimalFormat fmt = new DecimalFormat("#.0");

    public static void main(String[] args)
    {
//     new MyClient().start();
        System.out.println(fmt.format(100.561616516161));


    }
}