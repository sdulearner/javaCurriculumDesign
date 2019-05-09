package ClientSocket;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;

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
    private Socket clientSocket;
    private String nameOfPhoto;

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
    public ClientSocket_File(char a, Socket socket, String nameOfPhoto)
    {
        this.a = a;
        this.socket = socket;
        this.nameOfPhoto = nameOfPhoto;
    }

    /**
     * @Description: 上传文件时用的构造方法
     * @Parameters: [a=3, socket, fileName, file]
     * @return:
     * @date: 2019/5/3
     * @time: 21:26
     */
    public ClientSocket_File(char a, Socket socket, String fileName, File file, Socket clientSocket)
    {
        this.a = a;
        this.socket = socket;
        this.fileName = fileName;
        this.file = file;
        this.clientSocket = clientSocket;
    }

    /**
     * @Description: 下载文件时使用的构造方法
     * @Parameters: [a=4, socket, no]
     * @return:
     * @date: 2019/5/3
     * @time: 21:28
     */
    public ClientSocket_File(char a, Socket socket, int no)
    {
        this.a = a;
        this.socket = socket;
        this.no = no;
    }

    @Override
    public void run()
    {
        switch (a)
        {
            case '1'://上传图片
            {
                try
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
                            dos.write(bytes, 0, length);
                            dos.flush();
                        }
                        System.out.println(reader.readLine());
                    } finally
                    {
                        if (dos != null) dos.close();
                        if (fis != null) fis.close();

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
            case '2'://下载图片
            {
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    writer.println("photo");
                    writer.println(2);

                    writer.println(nameOfPhoto);
                    String extension = reader.readLine();
                    int length;
                    File f = new File("C:/课设专用存图片");
                    if (!f.exists())
                    {
                        f.mkdir();
                    }
                    dis = new DataInputStream(socket.getInputStream());
                    String filePath = "C:/课设专用存图片/" + nameOfPhoto;
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
            case '3'://上传文件
            {
                try
                {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("not-photo");
                    dataOutputStream.flush();
                    dataOutputStream.writeInt(1);
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF(fileName);
                    dataOutputStream.flush();

                    System.out.println("正在上传：" + fileName);
                    int length;
                    int sum = 0;
                    long l = file.length();
                    dos = new DataOutputStream(socket.getOutputStream());
                    fis = new FileInputStream(file);
                    bytes = new byte[1024];

                    DecimalFormat fmt = new DecimalFormat("#.##");
                    int MAX = 1024 * 100;//大约每100KB更新一次
                    int count = 0;
                    long startTime = System.currentTimeMillis();

                    while ((length = fis.read(bytes, 0, bytes.length)) > 0)
                    {
                        sum += length;
                        count += length;
                        dos.write(bytes, 0, length);
                        dos.flush();
                        if (count > MAX)
                        {

                            long endTime = System.currentTimeMillis();
                            double numerator = 1000 * count;//分子
                            double denominator = (endTime - startTime) * 1024 * 1024;//分母
                            String speed = fmt.format(numerator / denominator) + "MB/S";
                            //这里写更新方法

                            //这里写更新方法
                            count = 0;
                            startTime = System.currentTimeMillis();
                        }
                    }
                    if (sum == l)
                    {
                        System.out.println("上传成功");
//                        new ClientSocket_Util(clientSocket).uploadFile(fileName);
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
            case '4'://下载文件
            {
                try
                {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    dataOutputStream.writeUTF("not-photo");
                    dataOutputStream.flush();
                    dataOutputStream.writeInt(2);
                    dataOutputStream.flush();
                    dataOutputStream.writeInt(no);
                    dataOutputStream.flush();

                    System.out.println("下载文件的序号:" + no);
                    int sum = 0;
                    int length;
                    dis = new DataInputStream(socket.getInputStream());
                    fos = new FileOutputStream(new File(filePath));
                    bytes = new byte[1024];

                    DecimalFormat fmt = new DecimalFormat("#.##");
                    int MAX = 1024 * 100;//大约每100KB更新一次
                    int count = 0;
                    long startTime = System.currentTimeMillis();

                    while ((length = dis.read(bytes, 0, bytes.length)) > 0)
                    {
                        sum += length;
                        count += length;
                        fos.write(bytes, 0, length);
                        fos.flush();

                        if (count > MAX)
                        {
                            long endTime = System.currentTimeMillis();
                            double numerator = 1000 * count;//分子
                            double denominator = (endTime - startTime) * 1024 * 1024;//分母
                            String speed = fmt.format(numerator / denominator) + "MB/S";
                            //这里写更新方法

                            //这里写更新方法
                            count = 0;
                            startTime = System.currentTimeMillis();
                        }

                    }
                    System.out.println("下载完成");

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