package ClientSocket;

import java.io.*;
import java.net.Socket;

public class ClientSocket_File extends Thread
{
    private char a;
    private Socket socket;
    private DataOutputStream dos;
    private FileInputStream fis;
    private DataInputStream dis;
    private FileOutputStream fos;
    private byte[] bytes;
    private long sender;
    private long receiver;
    private File photo;
    private int no;
    private String fileName;
    private String filePath;
    private File file;

    /**
     * @Description: 上传图片时用的构造方法
     * @Parameters: [socket, a=1, sender, receiver, photo]
     * @return:
     * @date: 2019/5/3
     * @time: 21:22
     */
    public ClientSocket_File(Socket socket, char a, long sender, long receiver, File photo)
    {
        this.socket = socket;
        this.a = a;
        this.sender = sender;
        this.receiver = receiver;
        this.photo = photo;
    }

    /**
     * @Description: 下载图片时用的构造方法
     * @Parameters: [a=2, socket， no]
     * @return:
     * @date: 2019/5/3
     * @time: 21:24
     */
    public ClientSocket_File(char a, Socket socket, long sender, long receiver, int no)
    {
        this.a = a;
        this.socket = socket;
        this.sender = sender;
        this.receiver = receiver;
        this.no = no;
    }

    /**
     * @Description: 上传文件时用的构造方法
     * @Parameters: [a=3, socket, fileName, file]
     * @return:
     * @date: 2019/5/3
     * @time: 21:26
     */
    public ClientSocket_File(char a, Socket socket, String fileName, File file)
    {
        this.a = a;
        this.socket = socket;
        this.fileName = fileName;
        this.file = file;
    }

    /**
     * @Description: 下载文件时使用的构造方法
     * @Parameters: [a=4, socket, no]
     * @return:
     * @date: 2019/5/3
     * @time: 21:28
     */
    public ClientSocket_File(char a, Socket socket, String path)
    {
        this.a = a;
        this.socket = socket;
        this.filePath = path;
    }

    @Override
    public void run()
    {
        switch (a)
        {
            case '1':
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    writer.println("photo");
                    writer.println(1);
                    writer.println(sender);
                    writer.println(receiver);
                    String filePath = photo.getCanonicalPath();
                    int i = filePath.lastIndexOf('.');
                    writer.println(filePath.substring(i + 1));//输出扩展名

                    int length;
                    double sumL = 0;
                    long l = photo.length();
                    dos = new DataOutputStream(socket.getOutputStream());
                    fis = new FileInputStream(photo);
                    bytes = new byte[1024];
                    while ((length = fis.read(bytes, 0, bytes.length)) > 0)
                    {
                        sumL += length;
//                System.out.println((sumL / l) * 100);//传输进度 单位%
//                System.out.println((length/(endTime-startTime)*1000)/(1024*1024));//传输速度  单位MB/S
                        long startTime = System.currentTimeMillis();
                        dos.write(bytes, 0, length);
                        dos.flush();
                        long endTime = System.currentTimeMillis();
                    }
                    System.out.println(reader.readLine());

                    dos.close();
                    fis.close();
                    socket.close();

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            break;
            case '2':
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    writer.println("photo");
                    writer.println(2);
                    writer.println(sender);
                    writer.println(receiver);
                    writer.println(no);
                    String extension = reader.readLine();
                    int length;
                    File f = new File("C:/课设专用存图片");
                    if (!f.exists())
                    {
                        f.mkdir();
                    }
                    dis = new DataInputStream(socket.getInputStream());
                    String filePath = "C:/课设专用存图片/" + sender + receiver +
                            no + extension;
                    fos = new FileOutputStream(new File(filePath));
                    bytes = new byte[1024];
                    while ((length = dis.read(bytes, 0, bytes.length)) > 0)
                    {
                        fos.write(bytes, 0, length);
                        fos.flush();
                    }
                    System.out.println(reader.readLine());

                    dos.close();
                    fis.close();
                    socket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
            break;
            case '3':
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    writer.println("not-photo");
                    writer.println(1);
                    writer.println(fileName);

                    int length;
                    double sumL = 0;
                    long l = file.length();
                    dos = new DataOutputStream(socket.getOutputStream());
                    fis = new FileInputStream(file);
                    bytes = new byte[1024];
                    while ((length = fis.read(bytes, 0, bytes.length)) > 0)
                    {
                        sumL += length;
//                System.out.println((sumL / l) * 100);//传输进度 单位%
//                System.out.println((length/(endTime-startTime)*1000)/(1024*1024));//传输速度  单位MB/S
                        long startTime = System.currentTimeMillis();
                        dos.write(bytes, 0, length);
                        dos.flush();
                        long endTime = System.currentTimeMillis();
                    }
                    System.out.println(reader.readLine());

                    dos.close();
                    fis.close();
                    socket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
            break;
            case '4':
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    writer.println("not-photo");
                    writer.println(2);
                    writer.println(no);


                    int length;
                    dis = new DataInputStream(socket.getInputStream());
                    fos = new FileOutputStream(new File(filePath));
                    bytes = new byte[1024];
                    while ((length = dis.read(bytes, 0, bytes.length)) > 0)
                    {
                        fos.write(bytes, 0, length);
                        fos.flush();
                    }
                    dos.close();
                    fis.close();
                    socket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
//    public void upLoadPhoto(long sender, long receiver, File photo)
//    {
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//            writer.println("photo");
//            writer.println(1);
//            writer.println(sender);
//            writer.println(receiver);
//            String filePath = photo.getCanonicalPath();
//            int i = filePath.lastIndexOf('.');
//            writer.println(filePath.substring(i + 1));//输出扩展名
//
//            int length;
//            double sumL = 0;
//            long l = photo.length();
//            dos = new DataOutputStream(socket.getOutputStream());
//            fis = new FileInputStream(photo);
//            bytes = new byte[1024];
//            while ((length = fis.read(bytes, 0, bytes.length)) > 0)
//            {
//                sumL += length;
////                System.out.println((sumL / l) * 100);//传输进度 单位%
////                System.out.println((length/(endTime-startTime)*1000)/(1024*1024));//传输速度  单位MB/S
//                long startTime = System.currentTimeMillis();
//                dos.write(bytes, 0, length);
//                dos.flush();
//                long endTime = System.currentTimeMillis();
//            }
//            System.out.println(reader.readLine());
//
//            dos.close();
//            fis.close();
//            socket.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void downLoadPhoto(long sender, long receiver, int no)
//    {
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//            writer.println("photo");
//            writer.println(2);
//            writer.println(sender);
//            writer.println(receiver);
//            writer.println(no);
//            String extension = reader.readLine();
//            int length;
//            File f = new File("C:/课设专用存图片");
//            if (!f.exists())
//            {
//                f.mkdir();
//            }
//            dis = new DataInputStream(socket.getInputStream());
//            String filePath = "C:/课设专用存图片/" + sender + receiver +
//                    no + extension;
//            fos = new FileOutputStream(new File(filePath));
//            bytes = new byte[1024];
//            while ((length = dis.read(bytes, 0, bytes.length)) > 0)
//            {
//                fos.write(bytes, 0, length);
//                fos.flush();
//            }
//            System.out.println(reader.readLine());
//
//            dos.close();
//            fis.close();
//            socket.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void upLoad(String fileName, File file)
//    {
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//            writer.println("not-photo");
//            writer.println(1);
//            writer.println(fileName);
//
//            int length;
//            double sumL = 0;
//            long l = file.length();
//            dos = new DataOutputStream(socket.getOutputStream());
//            fis = new FileInputStream(file);
//            bytes = new byte[1024];
//            while ((length = fis.read(bytes, 0, bytes.length)) > 0)
//            {
//                sumL += length;
////                System.out.println((sumL / l) * 100);//传输进度 单位%
////                System.out.println((length/(endTime-startTime)*1000)/(1024*1024));//传输速度  单位MB/S
//                long startTime = System.currentTimeMillis();
//                dos.write(bytes, 0, length);
//                dos.flush();
//                long endTime = System.currentTimeMillis();
//            }
//            System.out.println(reader.readLine());
//
//            dos.close();
//            fis.close();
//            socket.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void downLoad(int no)
//    {
//
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//            writer.println("not-photo");
//            writer.println(2);
//            writer.println(no);
//
//            String fileName = reader.readLine();
//
//            int length;
//            File f = new File("C:/课设专用");
//            if (!f.exists())
//            {
//                f.mkdir();
//            }
//            dis = new DataInputStream(socket.getInputStream());
//            String filePath = "C:/课设专用/" + fileName;
//            fos = new FileOutputStream(new File(filePath));
//            bytes = new byte[1024];
//            while ((length = dis.read(bytes, 0, bytes.length)) > 0)
//            {
//                fos.write(bytes, 0, length);
//                fos.flush();
//            }
//            System.out.println(reader.readLine());
//
//            dos.close();
//            fis.close();
//            socket.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }
}