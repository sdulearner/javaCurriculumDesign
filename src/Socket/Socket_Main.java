package Socket;


public class Socket_Main extends Thread
{
    private int countDown = 5;
    private static int threadCount = 0;
    private int threadNumber = ++threadCount;

    public Socket_Main()
    {
        System.out.println("Making" + threadNumber);
    }

    public void run()
    {
        while (true)
        {
            System.out.println("Thread" + threadNumber + "(" + countDown + ")");
            if (--countDown == 0) return;
        }

    }

    public static void main(String[] args)
    {
        for (int i = 0; i < 5; i++)
        {
            new Socket_Main().start();
        }
        System.out.println("Finished");
    }

}
