package Socket;

import Database.*;
import Entity.Announcement;
import Entity.Result;
import Entity.Student;

import java.io.*;

import java.net.Socket;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Socket_Util extends Thread
{
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
                    Listener_Server.managerAnnouncement.subtract(this);
                    Listener_Server.idList.remove(this.Id);
                    break;
                }
                switch (input.charAt(0))
                {
                    case '1'://注册
                    {
                        Id = Long.parseLong(reader.readLine());

                        Name = reader.readLine().trim();

                        Nickname = reader.readLine().trim();

                        Sex = reader.readLine().trim();

                        Password = reader.readLine().trim();

                        Administrator = Integer.parseInt(reader.readLine());

                        student = new Student(Id, Name, Nickname, Sex, Password, Administrator);
                        if (jdbc_students.insert(student))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            new Thread(new Manager_Online(this, Listener_Server.managerAnnouncement.getSocketList())).start();

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
                            Password = reader.readLine().trim();
                            if (jdbc_students.judgePassword(Id, Password))
                            {
                                Name = jdbc_students.getInformation(Id).getName();
                                Nickname = jdbc_students.getInformation(Id).getNickname();
                                System.out.println("OK");
                                writer.println("OK");
                                writer.println(jdbc_students.getInformation(Id).getAdministrator());//告诉你是不是管理员
                                Listener_Server.idList.add(Id);
                                //告诉其他人我上线了
                                new Thread(new Manager_Online(this, Listener_Server.managerAnnouncement.getSocketList())).start();
                                //载入信息
                                int n = Listener_Server.idList.size();//在线人数
                                int num = JDBC_Students.count();//总人数
                                long[] array = JDBC_Students.getId();
                                writer.println(n);
                                for (int i = 0; i < n; i++)
                                {
                                    writer.println(Listener_Server.idList.get(i));
                                    writer.println(jdbc_students.getInformation(Listener_Server.idList.get(i)).getName());
                                    writer.println(jdbc_students.getInformation(Listener_Server.idList.get(i)).getNickname());
                                }
                                writer.println(num - n);
                                for (int i = 0; i < num; i++)
                                {
                                    if (!Listener_Server.idList.contains(array[i]))
                                    {
                                        writer.println(array[i]);
                                        writer.println(jdbc_students.getInformation(array[i]).getName());
                                        writer.println(jdbc_students.getInformation(array[i]).getNickname());
                                    }
                                }
                                //公告信息
                                writer.println(JDBC_Announcement.count());
                                for (int i = 0; i < JDBC_Announcement.count(); i++)
                                {
                                    writer.println(jdbc_announcement.query(i).getName());
                                    writer.println(jdbc_announcement.query(i).getTitle());
                                    writer.println(jdbc_announcement.query(i).getTime().getTime());
                                }
                                //文件信息
                                writer.println(JDBC_Documents.count());
                                for (int i = 0; i < JDBC_Documents.count(); i++)
                                {
                                    writer.println(JDBC_Documents.query(i));
                                }
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
                        Name = reader.readLine().trim();

                        Text = reader.readLine().trim();

                        Title = reader.readLine().trim();

                        Time = new Timestamp(System.currentTimeMillis());

                        announcement = new Announcement(Name, Title, Text, Time);

                        if (jdbc_announcement.insert(announcement))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            new Thread(Listener_Server.managerAnnouncement).start();//告诉其他人我发公告了
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
                        builder.append(announcement.getTime().getTime());
                        writer.println(builder);
                        System.out.println("OK");
                    }
                    break;
                    case '5'://删公告
                    {
                        System.out.println("Deleting the announcement");
                        int temp = Integer.parseInt(reader.readLine());//公告序号
                        jdbc_announcement.delete(temp);
                        System.out.println("OK");
                        writer.println("OK");
                    }
                    break;

                    case '6'://发起投票
                    {
                        System.out.println("正在发起投票");
                        Title = reader.readLine().trim();
                        int num = reader.readLine().charAt(0);
                        options = new String[num];
                        for (int i = 0; i < num; i++)
                        {
                            options[i] = reader.readLine();
                        }
                        if (jdbc_vote.start(Name, Title, options))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            //开启投票线程
                            new Thread(new Manager_Voting(Listener_Server.managerAnnouncement.getSocketList())).start();

                        } else
                        {
                            System.out.println("标题重复");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '7'://查询投票结果
                    {
                        System.out.println("正在查询投票结果");
                        int a = Integer.parseInt(reader.readLine());
                        result = jdbc_vote.calculate(a);

                        writer.println(result.getName());
                        writer.println(result.getTitle());
                        writer.println(result.getTime()[0].getTime());
                        writer.println(result.getOptions().length);
                        for (int i = 0; i < result.getOptions().length; i++)
                        {
                            writer.println(result.getOptions()[i]);
                            writer.println(result.getVotes()[i]);
                        }
                        int counter = 0;
                        for (String temp : result.getOpinions())
                        {
                            if (temp != null) counter++;
                        }
                        writer.println(counter);
                        for (String temp : result.getOpinions())
                        {
                            if (temp != null) writer.println(temp);
                        }
                        System.out.println("OK");
                    }
                    break;
                    case '8'://上传文件
                    {
                        System.out.println("正在判断文件名是否重复");
                        fileName = reader.readLine();
                        if (JDBC_Documents.insert(fileName))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            //告诉其他人我传了文件
                            new Thread(new Manager_File(this, Listener_Server.managerAnnouncement.getSocketList(), 1)).start();
                        } else
                        {
                            System.out.println("NO");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '9'://删除某文件
                    {
                        fileNo = Integer.parseInt(reader.readLine());
                        File file = new File("D:/课设专用/" + JDBC_Documents.query(fileNo).getName());
                        file.delete();
                        JDBC_Documents.delete(fileNo);
                        //告诉其他人我删了文件
                        new Thread(new Manager_File(this, Listener_Server.managerAnnouncement.getSocketList(), 2)).start();
                        writer.println("OK");
                    }
                    break;
                    case '0'://给某人单独发消息
                    {
                        boolean flag = false;
                        long temp = Long.parseLong(reader.readLine());//接收者的Id
                        String message = reader.readLine();//接收者的信息
                        Socket_Util temp_socket;
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            temp_socket = Listener_Server.managerAnnouncement.getSocketList().get(i);
                            if (temp_socket.getId() == temp)
                            {
                                temp_socket.outSending(this.Id, message, null);
                                JDBC_Messages.insert(this.Id, temp_socket.getId(), message, null, true);
                                flag = true;
                                break;
                            }
                        }
                        JDBC_Messages.insert(this.Id, temp, message, null, flag);
                    }
                    break;
                    case 'a'://向某群发送消息
                    {
                        int temp = Integer.parseInt(reader.readLine());//发送群组的序号
//                        String temp=reader.readLine();
                        long[] id = JDBC_Groups.query(temp).getId();
                        ArrayList<Long> Id = new ArrayList<>();
                        for (long a : id)
                        {
                            Id.add(a);
                        }
                        String message = reader.readLine();
                        Socket_Util temp_socket;
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            temp_socket = Listener_Server.managerAnnouncement.getSocketList().get(i);
                            if (Id.contains(temp_socket.getId()))
                            {
                                temp_socket.outSending(this.Id, message, null);
                                JDBC_Messages.insert(this.Id, temp_socket.getId(), message, JDBC_Groups.query(temp).getName(), true);//这里尚有问题
                                Id.remove(temp_socket.getId());
                                break;
                            }
                        }
                        for (int i = 0; i < Id.size(); i++)
                        {
                            JDBC_Messages.insert(this.Id, Id.get(i), message, JDBC_Groups.query(temp).getName(), false);//这里也有问题

                        }

                    }
                    break;
                    case 'b'://


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

    private static DecimalFormat fmt = new DecimalFormat("#.0");
    private JDBC_Students jdbc_students = new JDBC_Students();
    private JDBC_Announcement jdbc_announcement = new JDBC_Announcement();
    private JDBC_Vote jdbc_vote = new JDBC_Vote();
    private Announcement announcement;
    private Student student;
    private String Name;
    private String Nickname;
    private String fileName;
    private String Sex;
    private String Password;
    private String Title;
    private String Text;
    private String[] options;
    private int Administrator;
    private long Id;

    private VotingThread votingThread;
    private Result result = new Result();
    private Timestamp Time;
    private int fileNo;
    Socket socket;


    public int getFileNo()
    {
        return fileNo;
    }

    public String getFileName()
    {
        return fileName;
    }

    public VotingThread getVotingThread()
    {
        return votingThread;
    }

    public String getNickname()
    {
        return Nickname;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public long getId()
    {
        return Id;
    }

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

    public Socket_Util(Socket socket)
    {
        this.socket = socket;

    }

    public void outOnline(long id, String name, String nickname)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(id);
            writer.println(name);
            writer.println(nickname);
            writer.flush();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void outVoting()
    {
        try
        {
            votingThread = new VotingThread(this.socket, JDBC_Vote.count());
            votingThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

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

    public void outUpload(String name)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(name);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void outDeleteAnnouncement()
    {
    }

    public void outDeleteFile(int no)//删除某文件时
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(no);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void outSending(long sender, String text, String group)//发消息用
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(sender);
            writer.println(text);
            writer.println(group);

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
