package Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test
{
    public static void main(String[] args) throws ParseException
    {
       String a="2019-03-31 20:46:25.0";
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm;ss");
        Date b=fmt.parse(a);
        System.out.println();
    }
}