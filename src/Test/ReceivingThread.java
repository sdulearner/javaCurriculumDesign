package Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;


public class ReceivingThread extends Thread
{
    private Socket socket;

    public ReceivingThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        byte[] inputByte = null;
        int length = 0;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String filePath = "D:/课设专用/" + "???" + ".txt";
        try
        {
            try
            {
                dis = new DataInputStream(socket.getInputStream());
                File f = new File("D:/课设专用");
                if (!f.exists())
                {
                    f.mkdir();
                }
                /*
                 * 文件存储位置
                 */
                fos = new FileOutputStream(new File(filePath));
                inputByte = new byte[1024];
                System.out.println("开始接收数据...");
                while ((length = dis.read(inputByte, 0, inputByte.length)) > 0)
                {
                    fos.write(inputByte, 0, length);
                    fos.flush();
                }
                System.out.println("完成接收：" + filePath);
            } finally
            {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                if (socket != null)
                    socket.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
