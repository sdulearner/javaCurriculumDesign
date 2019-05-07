package Socket;

import Database.JDBC_Documents;
import Database.JDBC_Photos;

import java.io.*;
import java.net.Socket;

public class Socket_File extends Thread
{
    private Socket socket;
    private String fileName;
    private int no;
    private JDBC_Documents jdbc_documents = new JDBC_Documents();


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
            char flag;
            if (reader.readLine().equals("photo"))//发图片
            {
                System.out.println("图片操作");
                JDBC_Photos jdbc_photos = new JDBC_Photos();
                flag = reader.readLine().charAt(0);
                String extension = null;
                long sender = Long.parseLong(reader.readLine());
                long receiver = Long.parseLong(reader.readLine());
                if (flag == '2')
                {
                    no = Integer.parseInt(reader.readLine());
                } else
                {
                    extension = reader.readLine();
                }
                switch (flag)
                {
                    case '1'://上传图片
                    {
                        System.out.println("上传图片");
                        byte[] inputBytes;
                        int length;
                        DataInputStream dis = null;
                        FileOutputStream fos = null;
                        StringBuilder filePath = new StringBuilder("D:/课设专用存图片/").append(sender).append(receiver)
                                .append(jdbc_photos.getNO(sender, receiver)).append(".").append(extension);
//                        try
//                        {
//                            try
//                            {
                        dis = new DataInputStream(socket.getInputStream());
                        fos = new FileOutputStream(new File(filePath.toString()));

                        inputBytes = new byte[1024];

                        while ((length = dis.read(inputBytes, 0, inputBytes.length)) > 0)
                        {
                            fos.write(inputBytes, 0, length);
                            fos.flush();
                        }
                        writer.println("OK");
                        System.out.println("完成图片接收：" + filePath);

//                            } finally
//                            {
//                                if (fos != null)
//                                    fos.close();
//                                if (dis != null)
//                                    dis.close();
//                                if (socket != null)
//                                    socket.close();
//                            }
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                    case '2'://下载图片
                    {
                        System.out.println("下载图片");
                        int length;
                        double sumL = 0;
                        byte[] sendBytes;
                        DataOutputStream dos = null;
                        FileInputStream fis = null;
                        boolean bool = false;
//                        try
//                        {
//                            try
//                            {
                        extension = jdbc_photos.getExtension(no);
                        writer.println(extension);
                        String path = "D:/课设专用存图片/" + sender +
                                receiver + no + "." + extension;
                        File file = new File(path); //要传输的文件路径
                        long l = file.length();
                        dos = new DataOutputStream(socket.getOutputStream());
                        fis = new FileInputStream(file);
                        sendBytes = new byte[1024];
                        while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0)
                        {
                            sumL += length;
                            dos.write(sendBytes, 0, length);
                            dos.flush();
                        }
                        if (sumL == l)
                        {
                            bool = true;
                        }
//                            } finally
//                            {
//                                if (dos != null)
//                                    dos.close();
//                                if (fis != null)
//                                    fis.close();
//                                if (socket != null)
//                                    socket.close();
//                            }
//                        } catch (Exception e)
//                        {
//                            System.out.println("客户端文件传输异常");
//                            bool = false;
//                            e.printStackTrace();
//                        }
                        writer.println(bool ? "OK" : "NO");
                        System.out.println(bool ? "图片下载成功" : "图片下载失败");

                    }
                }


            } else//共享空间
            {
                System.out.println("共享空间操作");
                flag = reader.readLine().charAt(0);
                if (flag == '1')
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
                        System.out.println("上传文件");
                        byte[] inputByte;
                        int length;
                        DataInputStream dis = null;
                        FileOutputStream fos = null;
                        String filePath = "D:/课设专用/" + fileName;
//                        try
//                        {
//                            try
//                            {
                        dis = new DataInputStream(socket.getInputStream());
                        fos = new FileOutputStream(new File(filePath));
//                                File f = new File("D:/课设专用");
//                                if (!f.exists())
//                                {
//                                    f.mkdir();
//                                }

                        inputByte = new byte[1024];
                        while ((length = dis.read(inputByte, 0, inputByte.length)) > 0)
                        {
                            fos.write(inputByte, 0, length);
                            fos.flush();
                        }
//                        writer.println("OK");
                        System.out.println("完成接收：" + filePath);
//                            } finally
//                            {
//                                if (fos != null)
//                                    fos.close();
//                                if (dis != null)
//                                    dis.close();
//                                if (socket != null)
//                                    socket.close();
//                            }
//                        } catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                    case '2'://下载文件
                    {
                        System.out.println("下载文件");
                        int length;
                        double sumL = 0;
                        byte[] sendBytes;
                        DataOutputStream dos = null;
                        FileInputStream fis = null;
                        boolean bool = false;
//                        try
//                        {
//                            try
//                            {
                        String fileName = jdbc_documents.query(no).getName();

                        File file = new File("D:/课设专用/" + fileName); //要传输的文件路径
                        System.out.println(fileName);
                        long l = file.length();
                        dos = new DataOutputStream(socket.getOutputStream());
                        fis = new FileInputStream(file);
                        sendBytes = new byte[1024];
                        while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0)
                        {
//                            writer.println((sumL / l) * 100);
                            sumL += length;
                            dos.write(sendBytes, 0, length);
                            dos.flush();
                        }
                        //虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
                        if (sumL == l)
                        {
                            bool = true;
                        }
//                            } finally
//                            {
//                                if (dos != null)
//                                    dos.close();
//                                if (fis != null)
//                                    fis.close();
//                                if (socket != null)
//                                    socket.close();
//                            }
//                        } catch (Exception e)
//                        {
//                            System.out.println("客户端文件传输异常");
//                            bool = false;
//                            e.printStackTrace();
//                        }
//                        writer.println(bool ? "OK" : "NO");
                        System.out.println(bool ? "成功" : "失败");
                    }
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
