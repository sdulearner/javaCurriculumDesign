package Socket;

import Database.JDBC_Announcement;
import Database.JDBC_Documents;
import Database.JDBC_Students;
import Database.JDBC_Vote;
import Entity.Announcement;
import Entity.Result;
import Entity.Student;

import java.io.*;

import java.net.Socket;
import java.sql.Timestamp;
import java.text.DecimalFormat;

public class ChatSocket extends Thread
{
    private static DecimalFormat fmt = new DecimalFormat("#.0");
    private JDBC_Students jdbc_students = new JDBC_Students();
    private JDBC_Announcement jdbc_announcement = new JDBC_Announcement();
    private JDBC_Vote jdbc_vote = new JDBC_Vote();
    private Announcement announcement;
    private Student student;
    private String Name;
    private String Nickname;
    private String Sex;
    private String Password;
    private String Title;
    private String Text;
    private String[] options;
    private int Administrator;
    private long Id;
    private boolean flag = false;
    private VotingThread votingThread;
    private Result result = new Result();

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

    @Override
    public long getId()
    {
        return Id;
    }

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


    private Timestamp Time;

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
    public void outDelete(){



    }

//    public boolean outChat()
//    {
//
//    }

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
                    Listener_Server.managerVoting.subtract(this);
                    Listener_Server.managerChat.subtract(this);
                    Listener_Server.idList.remove(this);
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
                            new Thread(new Manager_Online(this, Manager_Chat.socketList)).start();

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
                                writer.println(jdbc_students.getInformation(Id).getAdministrator());
                                Listener_Server.idList.add(Id);
                                new Thread(new Manager_Online(this, Manager_Chat.socketList)).start();

                                int n = Listener_Server.idList.size();
                                int num = JDBC_Students.count();
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
                                for (int i = 0; i < JDBC_Announcement.count(); i++)
                                {
                                    writer.println(jdbc_announcement.query(i).getName());
                                    writer.println(jdbc_announcement.query(i).getTitle());
                                    writer.println(jdbc_announcement.query(i).getTime());
                                }


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
                        Name = reader.readLine().trim();

                        Text = reader.readLine().trim();

                        Title = reader.readLine().trim();

                        Time = new Timestamp(System.currentTimeMillis());

                        announcement = new Announcement(Name, Title, Text, Time);

                        if (jdbc_announcement.insert(announcement))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            new Thread(Listener_Server.managerAnnouncement).start();
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

                    case '5'://发起投票
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
                            new Thread(Listener_Server.managerVoting).start();

                        } else
                        {
                            System.out.println("标题重复");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '6'://查询投票结果
                    {
                        System.out.println("正在查询投票结果");
                        int a = Integer.parseInt(reader.readLine());
                        result = jdbc_vote.calculate(a);

                        writer.println(result.getName());
                        writer.println(result.getTitle());
                        writer.println(result.getTime()[0]);

                        for (int i = 0; i < result.getOptions().length; i++)
                        {
                            writer.println(result.getOptions()[i]);
                            writer.println(result.getVotes()[i]);
                        }
                        for (String temp : result.getOpinions())
                        {
                            writer.println(temp);
                        }
                        System.out.println("OK");

                    }
                    break;
//                    case '7'://下载文件
//                    {
//
//
//                    }
//                    break;
//                    case '8'://上传文件
//                    {
//                        String fileName =reader.readLine();
//                    }
//                    break;
                    case '7'://删除某文件
                    {
                        int temp = Integer.parseInt(reader.readLine());
                        File file = new File("D:/课设专用/" + JDBC_Documents.query(temp).getName());
                        file.delete();
                        JDBC_Documents.delete(temp);
                        writer.println("OK");
                    }
                    break;
//                    case '9'://请求与某人建立即时通讯
//                    {
//                        long temp = Long.parseLong(reader.readLine());
//                        for (int i = 0; i < Listener_Server.idList.size(); i++)
//                        {
//                            if (Manager_Chat.socketList.get(i).Id == temp)
//                            {
//                                if (outChat())
//                                {
//
//                                } else
//                                {
//                                    System.out.println("拒绝");
//                                    writer.println("NO");
//                                }
//                            }
//                            break;
//                        }
//
//                    }
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
//                Manager_Chat.getChatManager().publish(this, str);
//
//            }
//            reader.close();
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
