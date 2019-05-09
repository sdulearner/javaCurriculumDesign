package Test;

import java.text.DecimalFormat;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-05 11:06
 **/

public class Test
{

    public static void main(String[] args)
    {
        DecimalFormat fmt=new DecimalFormat("#.##");
        double a=10000;
        double b=34238;
        double c=b/a;
        System.out.println(fmt.format(a/b));
    }
}
