package Socket;

import Database.JDBC_Documents;

import java.io.*;
import java.net.Socket;

public class Socket_File extends Thread
{
    private Socket socket;
    private int flag;
    private String fileName;
    private int no;

    public Socket_File(Socket socket)
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
                            fos = new FileOutputStream(new File(filePath));
                            File f = new File("D:/课设专用");
                            if (!f.exists())
                            {
                                f.mkdir();
                            }
                            /*
                             * 文件存储位置
                             */
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
                }
                break;
                case '2'://下载文件
                {
                    int length;
                    double sumL = 0;
                    byte[] sendBytes;
                    DataOutputStream dos = null;
                    FileInputStream fis = null;
                    boolean bool = false;
                    try
                    {
                        try
                        {
                            File file = new File("D:/课设专用/" + JDBC_Documents.query(no).getName()); //要传输的文件路径
                            long l = file.length();
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
                        System.out.println("客户端文件传输异常");
                        bool = false;
                        e.printStackTrace();
                    }
                    writer.println(bool ? "OK" : "NO");
                    System.out.println(bool ? "成功" : "失败");

                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
