package Socket;

import Database.JDBC_Documents;
import Entity.Document;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class FileSocket extends Thread
{
    Socket socket;
    private int flag;
    private String fileName;
    private int no;

    public FileSocket(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            flag = Integer.parseInt(reader.readLine());
            if (flag == 1)
            {
                fileName = reader.readLine();
            } else
            {
                no = Integer.parseInt(reader.readLine());
            }


            switch (flag)
            {
                case '1'://上传文件
                {
                    if (JDBC_Documents.insert(fileName))
                    {
                        byte[] inputByte;
                        int length;
                        DataInputStream dis = null;
                        FileOutputStream fos = null;
                        String filePath = "D:/课设专用/" + fileName;
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
                                writer.println("OK");
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
                    } else
                    {
                        System.out.println("重名");
                        writer.println("NO");
                    }
                }
                break;
                case '2'://下载文件
                {
                    int length;
                    double sumL = 0;
                    byte[] sendBytes;
//                Socket socket = null;
                    DataOutputStream dos = null;
                    FileInputStream fis = null;
                    boolean bool = false;
                    try
                    {
                        try
                        {
                            File file = new File("D:/课设专用/" + JDBC_Documents.query(no).getName()); //要传输的文件路径
                            long l = file.length();
//                        socket = new Socket();
//                        socket.connect(new InetSocketAddress("127.0.0.1", 48123));
                            dos = new DataOutputStream(socket.getOutputStream());
                            fis = new FileInputStream(file);
                            sendBytes = new byte[1024];
                            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0)
                            {
                                sumL += length;
                                writer.println((sumL / l) * 100);
                                dos.write(sendBytes, 0, length);
                                dos.flush();
                            }
                            //虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
                            if (sumL == l)
                            {
                                bool = true;
                            }
                        } finally
                        {
                            if (dos != null)
                                dos.close();
                            if (fis != null)
                                fis.close();
                            if (socket != null)
                                socket.close();
                        }
                    } catch (Exception e)
                    {
                        writer.println("NO");
                        System.out.println("客户端文件传输异常");
                        bool = false;
                        e.printStackTrace();
                    }
                    writer.println(bool ? "成功" : "失败");
                    System.out.println(bool ? "成功" : "失败");

                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
