package Test;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class FileUp
{
    // 上传的文件路径
    private String filePath;
    // socket服务器地址和端口号
    private String host;
    private int port;

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public static void main(String[] args)
    {
        FileUp fu = new FileUp();
        fu.setHost("127.0.0.1");
        fu.setPort(9005);
        fu.setFilePath("D:\\课设专用\\");
        fu.uploadFile("qq.txt");
    }

    /**
     * 客户端文件上传
     *
     * @param fileName 文件名
     */
    public void uploadFile(String fileName)
    {
        Socket s = null;
        try
        {
            s = new Socket(host, port);

            // 选择进行传输的文件
            File fi = new File(filePath + fileName);
            System.out.println("文件长度:" + (int) fi.length());

            DataInputStream fis = new DataInputStream(new FileInputStream(filePath + fileName));
            DataOutputStream ps = new DataOutputStream(s.getOutputStream());
            // 将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In
            // Java
            // 4th里有现成的代码。
            //design pattern 设计模式  <<Head First Design Pattern>><<Head First 设计模式>><<Head First Java>>
            ps.writeUTF(fi.getName());
            ps.flush();
            ps.writeLong((long) fi.length());
            ps.flush();

            int bufferSize = 8192;
            byte[] buf = new byte[bufferSize];

            while (true)
            {
                int read = 0;
                if (fis != null)
                {
                    read = fis.read(buf);
                }

                if (read == -1)
                {
                    break;
                }
                ps.write(buf, 0, read);
            }
            ps.flush();
            // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
            // 直到socket超时，导致数据不完整。
            fis.close();
            ps.close();
            s.close();
            System.out.println("文件传输完成");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}