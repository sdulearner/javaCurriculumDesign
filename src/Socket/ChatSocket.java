package Socket;

import Database.JDBC_Announcement;
import Database.JDBC_Students;
import Entity.Announcement;
import Entity.Student;

import java.io.*;

import java.net.Socket;
import java.sql.Timestamp;

public class ChatSocket extends Thread
{

    private JDBC_Students jdbc_students = new JDBC_Students();
    private JDBC_Announcement jdbc_announcement = new JDBC_Announcement();
    private Student student;
    private Announcement announcement;
    private long Id;
    private String Name;
    private String Nickname;
    private String Sex;
    private String Password;
    private int Administrator;
    private String Title;
    private String Text;

    @Override
    public long getId()
    {
        return Id;
    }

    private Timestamp Time;
    Socket socket;


    public String get_Name()
    {
        return Name;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getText()
    {
        return Text;
    }

    public Timestamp getTime()
    {
        return Time;
    }

    public ChatSocket(Socket socket)
    {
        this.socket = socket;

    }

    public void outAnnouncemt(String Name, String Title, String Text, Timestamp Time)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(Name);
            writer.println(Title);
            writer.println(Text);
            writer.println(Time);
            writer.flush();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String input;
            while ((input = reader.readLine()) != null)
            {
                if (input.equals("quit"))
                {
                    ServerListener.announcementManager.subtract(this);
                    break;
                }
                switch (input.charAt(0))
                {
                    case '1'://注册
                    {
                        input = reader.readLine();
                        Id = Long.parseLong(input);

                        input = reader.readLine();
                        Name = input.trim();

                        input = reader.readLine();
                        Nickname = input.trim();

                        input = reader.readLine();
                        Sex = input.trim();

                        input = reader.readLine();
                        Password = input.trim();

                        input = reader.readLine();
                        Administrator = Integer.parseInt(input);

                        student = new Student(Id, Name, Nickname, Sex, Password, Administrator);
                        if (jdbc_students.insert(student))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                        } else
                        {
                            System.out.println("NO");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '2'://登录
                    {
                        System.out.println("正在登陆");
                        input = reader.readLine();
                        Id = Long.parseLong(input);
                        if (jdbc_students.judgeId(Id))
                        {
                            input = reader.readLine();
                            Password = input.trim();
                            if (jdbc_students.judgePassword(Id, Password))
                            {
                                System.out.println("OK");
                                writer.println("OK");

                                //这里要加载信息
                            } else
                            {
                                System.out.println("NO2");
                                writer.println("NO2");//false2密码错误
                            }
                        } else
                        {
                            System.out.println("NO1");
                            writer.println("NO1");//false1是Id不存在
                        }
                    }
                    break;
                    case '3'://发公告
                    {
                        System.out.println("正在发公告");
//                        input = reader.readLine();
//                        NO = Integer.parseInt(input);
                        input = reader.readLine();
                        Name = input.trim();
                        input = reader.readLine();
                        Text = input.trim();
                        input = reader.readLine();
                        Title = input.trim();
                        Time = new Timestamp(System.currentTimeMillis());
                        announcement = new Announcement(Name, Title, Text, Time);
                        if (jdbc_announcement.insert(announcement))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            new Thread(ServerListener.announcementManager).start();

                        } else
                        {
                            System.out.println("NO");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '4'://查公告
                    {
                        StringBuilder builder = new StringBuilder();
                        System.out.println("正在查公告");
                        input = reader.readLine();
                        int no = Integer.parseInt(input);
                        announcement = jdbc_announcement.query(no);
//                        builder.append(announcement.getNO()+"\t");
                        builder.append(announcement.getName() + "\t");
                        builder.append(announcement.getTitle() + "\t");
                        builder.append(announcement.getText() + "\t");
                        builder.append(announcement.getTime());
                        writer.println(builder);
                        System.out.println("OK");
                    }
                    break;
                }
            }
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (socket != null)
            {
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

//    Socket socket;
//
//    public ChatSocket(Socket socket)
//    {
//        this.socket = socket;
//    }
//
//    public void out(String out)
//        {
//            try
//        {
//            PrintWriter pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
//            pw.print(out);
//            pw.flush();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void run()
//    {
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//            String str;
//            while ((str = reader.readLine()) != null)
//            {
//                System.out.println("收到！");
//                System.out.println(str);
//                ChatManager.getChatManager().publish(this, str);
//
//            }
//            reader.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
