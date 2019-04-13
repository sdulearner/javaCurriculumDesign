package Test;



import java.io.IOException;
import java.util.Arrays;

public class Main{
    public static void main(String[]args) throws IOException
    {String a="123`456`789`";
        System.out.println(Arrays.toString(a.split("`")));


    }
}