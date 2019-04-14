package Test;


import java.io.IOException;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        int[] a = {42342, 42342, 4324};
        String b = Arrays.toString(a);
        System.out.println(b);
        b = b.replace("[", "");
        b = b.replace("]", "");

        String[] c = b.split(", ");
        for (String d : c)
        {
            System.out.println(Long.parseLong(d));
        }

    }
}