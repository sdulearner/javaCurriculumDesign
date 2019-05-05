package Socket;

public class Main
{
    public static void main(String[] args)
    {
        new Listener_File().start();
        new Listener_Server().start();
    }
}
