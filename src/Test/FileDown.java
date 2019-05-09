
package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDown extends Thread
{
    // 文件的保存路径
    private String fileDir;
    // socket服务器端口号
    private int port;
    // 是否停止
    private boolean stop;

    public String getFileDir()
    {
        return fileDir;
    }

    public void setFileDir(String fileDir)
    {
        this.fileDir = fileDir;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public boolean isStop()
    {
        return stop;
    }

    public void setStop(boolean stop)
    {
        this.stop = stop;
    }

    public static void main(String[] args)
    {
        FileDown fd = new FileDown();
        fd.setFileDir("d:\\");
        fd.setPort(9005);
        fd.start();
    }

    /**
     * 文件下载
     */
    @Override
    public void run()
    {
        Socket socket = null;
        try
        {
            ServerSocket ss = new ServerSocket(port);
            do
            {
                socket = ss.accept();

                // public Socket accept() throws
                // IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。
                System.out.println("建立socket链接");
                DataInputStream inputStream = new DataInputStream(
                        new BufferedInputStream(socket.getInputStream()));

                // 本地保存路径，文件名会自动从服务器端继承而来。
                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];
                long passedlen = 0;
                long len = 0;

                // 获取文件名
                String file = fileDir + inputStream.readUTF();
                DataOutputStream fileOut = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(file)));
                len = inputStream.readLong();

                System.out.println("文件的长度为:" + len + "\n");
                System.out.println("开始接收文件!" + "\n");

                while (true)
                {
                    int read = 0;
                    if (inputStream != null)
                    {
                        read = inputStream.read(buf);
                    }
                    passedlen += read;
                    if (read == -1)
                    {
                        break;
                    }
                    // 下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
                    System.out.println("文件接收了" + (passedlen * 100 / len)
                            + "%\n");
                    fileOut.write(buf, 0, read);
                }
                System.out.println("接收完成，文件存为" + file + "\n");

                fileOut.close();
            } while (!stop);
        } catch (Exception e)
        {
            System.out.println("接收消息错误" + "\n");
            e.printStackTrace();
            return;
        }
    }
}
 
