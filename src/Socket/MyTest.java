package Socket;

import java.io.IOException;

public class MyTest
{
    public static void main(String[] args) throws IOException
    {
        String a="C:\\Program Files (x86)\\MySQL\\Connector J 8.0\\src";
        System.out.println(a.lastIndexOf('\\'));
        System.out.println(a.substring(a.lastIndexOf('\\')+1));
    }

}
