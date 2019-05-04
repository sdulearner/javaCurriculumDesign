package Socket;

import Database.*;
import Entity.*;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Socket_Util extends Thread
{
    private String Password;
    private int Administrator;
    private long Id;
    private JDBC_Announcement jdbc_announcement;
    private JDBC_Documents jdbc_documents;
    private JDBC_Groups jdbc_groups;
    private JDBC_Texts jdbc_texts;
    private JDBC_Photos jdbc_photos;
    private JDBC_Students jdbc_students;
    private JDBC_Vote jdbc_vote;
    Socket socket;
    private int fileNo;
    private String fileName;
    private String Nickname;
    private String Name;
    private String Title;
    private Timestamp Time;
    private String Text;


    public int getFileNo()
    {
        return fileNo;
    }

    public String getFileName()
    {
        return fileName;
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

    //0上线提示
    public void outOnline(long id, String name, String nickname)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(0);
            writer.println(id);
            writer.println(name);
            writer.println(nickname);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //1弹出公告
    public void outAnnouncement(String Name, String Title, String Text, Timestamp Time)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(1);

            writer.println(Name);
            writer.println(Title);
            writer.println(Text);
            writer.println(Time);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //2弹出投票
    public void outVoting()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(2);

            VotingThread votingThread = new VotingThread(this.socket, JDBC_Vote.count());
            votingThread.join();//这里不太好
            System.out.println("收到投票结果");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //3有人上传了新文件
    public void outUpload(String name)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(3);

            writer.println(name);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    //4 有人删除了某文件
    public void outDeleteFile(int no)//删除某文件时
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(4);

            writer.println(no);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //5有人给你发消息
    public void outSending(long sender, String text, boolean group)//发消息用
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(5);

            writer.println(sender);
            writer.println(text);
            writer.println(group);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //6有人给你发图片
    private void outPhoto(long sender, int no, boolean group)//发图片
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(6);

            writer.println(sender);
            writer.println(no);
            writer.println(group);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    //7

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

                        String Sex = reader.readLine().trim();

                        Password = reader.readLine().trim();

                        Administrator = Integer.parseInt(reader.readLine());

                        Student student = new Student(Id, Name, Nickname, Sex, Password, Administrator);
                        jdbc_students = new JDBC_Students();
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
                        jdbc_students = new JDBC_Students();

                        if (jdbc_students.judgeId(Id))
                        {
                            writer.println("OK");
                            Password = reader.readLine().trim();
                            if (jdbc_students.judgePassword(Id, Password))
                            {
                                writer.println("OK");
                                Name = jdbc_students.getInformation(Id).getName();
                                Nickname = jdbc_students.getInformation(Id).getNickname();

                                writer.println(jdbc_students.getInformation(Id).getAdministrator());//告诉你是不是管理员
                                Listener_Server.idList.add(Id);

                                //告诉其他人我上线了
                                new Thread(new Manager_Online(this, Listener_Server.managerAnnouncement.getSocketList())).start();

                                //载入信息
                                int num = JDBC_Students.count();//总人数
                                long[] array = jdbc_students.getId();

                                int n = Listener_Server.idList.size();//在线人数
                                writer.println(n);
                                for (int i = 0; i < n; i++)
                                {
                                    writer.println(Listener_Server.idList.get(i));
                                    writer.println(jdbc_students.getInformation(Listener_Server.idList.get(i)).getName());
                                    writer.println(jdbc_students.getInformation(Listener_Server.idList.get(i)).getNickname());
                                }
                                //不在线的人
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
                                jdbc_announcement = new JDBC_Announcement();
                                int countOfAnnouncements = JDBC_Announcement.count();

                                if (countOfAnnouncements != 0)
                                {
                                    ArrayList<Announcement> arrayList = new ArrayList<>();
                                    Announcement announcement;
                                    Timestamp now = new Timestamp(System.currentTimeMillis());
                                    int count = 1;

                                    announcement = jdbc_announcement.query(count);
                                    while ((now.getTime() - announcement.getTime().getTime()) < 604800000)//604800000是一周的毫秒数
                                    {
                                        arrayList.add(announcement);
                                        count++;
                                        announcement = jdbc_announcement.query(count);
                                    }

                                    writer.println(arrayList.size());
                                    for (int i = 1; i <= arrayList.size(); i++)
                                    {
                                        writer.println(arrayList.get(i).getNO());
                                        writer.println(arrayList.get(i).getName());
                                        writer.println(arrayList.get(i).getTitle());
                                        writer.println(arrayList.get(i).getTime().getTime());
                                    }

                                } else
                                {
                                    writer.println("No_Announcement");
                                }


                                //文件信息
                                jdbc_documents = new JDBC_Documents();
                                writer.println(JDBC_Documents.count());
                                for (int i = 1; i <= JDBC_Documents.count(); i++)
                                {
                                    writer.println(i);
                                    writer.println(jdbc_documents.query(i));
                                }
                                //未读消息 的数目
                                jdbc_photos = new JDBC_Photos();
                                jdbc_texts = new JDBC_Texts();

                                Map map1 = jdbc_texts.register(this.Id);
                                Map map2 = jdbc_photos.register(this.Id);

                                for (Object o : map2.entrySet())
                                {
                                    Map.Entry entry = (Map.Entry) o;
                                    if (map1.containsKey(entry.getKey()))
                                    {
                                        int temp = (Short) map1.get(entry.getKey()) + (Short) entry.getValue();

                                        map1.put(entry.getKey(), (short) temp);
                                    } else
                                    {
                                        map1.put(entry.getKey(), entry.getValue());
                                    }
                                }


                                int numOfMessages = map1.size();
                                writer.println(numOfMessages);
                                long[] id = jdbc_students.getId();

                                for (int i = 0; i < JDBC_Students.count(); i++)
                                {
                                    if (map1.containsKey(id[i]))
                                    {
                                        writer.println(id[i] + ":" + map1.get(id[i]));
                                    }
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


                        Title = reader.readLine().trim();

                        Text = reader.readLine().trim();

                        Time = new Timestamp(System.currentTimeMillis());

                        Announcement announcement = new Announcement(Name, Title, Text, Time);

                        if (jdbc_announcement.insert(announcement))
                        {
                            JDBC_Log.insert(this.Id, this.Name, "发布了公告:" + Title);
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
                        Announcement announcement = jdbc_announcement.query(no);
//                        builder.append(announcement.getNO()+"\t");
                        builder.append(announcement.getName() + "\t");
                        builder.append(announcement.getTitle() + "\t");
                        builder.append(announcement.getText() + "\t");
                        builder.append(announcement.getTime().getTime());
                        writer.println(builder);
                        System.out.println("OK");
                    }
                    break;
                    case '5'://修改个人信息
                    {
                        System.out.println("修改个人信息");
                        jdbc_students = new JDBC_Students();

                        String nickname = reader.readLine();
                        String password = reader.readLine();
                        Student student = new Student();
                        student.setNickname(nickname);
                        student.setPassword(password);
                        jdbc_students.update(student);
                    }
                    break;
                    case '6'://发起投票
                    {
                        System.out.println("正在发起投票");
                        String title = reader.readLine().trim();
                        int num = reader.readLine().charAt(0);
                        String[] options = new String[num];
                        for (int i = 0; i < num; i++)
                        {
                            options[i] = reader.readLine();
                        }
                        if (jdbc_vote.start(this.Name, title, options))
                        {
                            System.out.println("OK");
                            writer.println("OK");
                            //开启投票线程
                            JDBC_Log.insert(this.Id, this.Name, "发起了投票:" + title);
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
                        Result result = jdbc_vote.calculate(a);

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
                        jdbc_documents = new JDBC_Documents();
                        if (jdbc_documents.insert(fileName))
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
                        jdbc_documents = new JDBC_Documents();
                        String fileName = jdbc_documents.query(fileNo).getName();
                        File file = new File("D:/课设专用/" + fileName);
                        file.delete();
                        jdbc_documents = new JDBC_Documents();
                        jdbc_documents.delete(fileNo);
                        //告诉其他人我删了文件
                        JDBC_Log.insert(this.Id, this.Name, "删除了文件:" + fileName);
                        new Thread(new Manager_File(this, Listener_Server.managerAnnouncement.getSocketList(), 2)).start();

                    }
                    break;
                    case '0'://打开与某人的聊天面板
                    {
                        jdbc_texts = new JDBC_Texts();
                        jdbc_photos = new JDBC_Photos();
                        long temp = Long.parseLong(reader.readLine());
                        ArrayList<Entity.Message> arrayList = jdbc_texts.queryId(temp, this.Id);
                        ArrayList<Entity.Photo> arrayList1 = jdbc_photos.queryId(temp, this.Id);
                        Iterator<Photo> iterator = arrayList1.iterator();
                        while (iterator.hasNext())
                        {
                            for (int i = 0; i < arrayList.size(); i++)
                            {
                                if (arrayList.get(i).compareTo(iterator) > 0)
                                {
                                    arrayList.add(i, (Photo) iterator);
                                    break;
                                }
                            }
                        }
                        //输出未读消息的个数
                        writer.println(arrayList.size());
                        //依次输出图片以及文字的消息
                        for (int i = 0; i < arrayList.size(); i++)
                        {
                            Message a = arrayList.get(i);
                            if (arrayList.get(i) instanceof Text)
                            {
                                Text b = (Text) a;
                                writer.println(b.getText());
                            } else
                            {
                                Photo b = (Photo) a;
                                writer.println("`" + b.getNO());
                            }
                        }
                    }
                    break;
                    case 'a'://打开大群的聊天面板
                    {
                        jdbc_texts = new JDBC_Texts();
                        jdbc_photos = new JDBC_Photos();

                        ArrayList<Entity.Message> arrayList = jdbc_texts.queryGroup(this.Id);
                        ArrayList<Entity.Photo> arrayList1 = jdbc_photos.queryGroup(this.Id);
                        Iterator<Photo> iterator = arrayList1.iterator();
                        while (iterator.hasNext())
                        {
                            for (int i = 0; i < arrayList.size(); i++)
                            {
                                if (arrayList.get(i).compareTo(iterator) > 0)
                                {
                                    arrayList.add(i, (Photo) iterator);
                                    break;
                                }
                            }
                        }
                        //输出未读消息的个数
                        writer.println(arrayList.size());
                        //依次输出图片以及文字的消息
                        for (Message a : arrayList)
                        {
                            if (a instanceof Text)
                            {
                                Text b = (Text) a;
                                writer.println(b.getSender());
                                writer.println(b.getText());
                            } else
                            {
                                Photo b = (Photo) a;
                                writer.println(b.getSender());
                                writer.println("`" + b.getNO());//+":" + b.getExtension()
                            }
                        }
                    }
                    break;
                    case 'b'://标记与某人的消息为已读
                    {
                        System.out.println("正在标记与某人的聊天记录为已读");
                        long temp = Long.parseLong(reader.readLine());
                        jdbc_texts = new JDBC_Texts();
                        jdbc_texts.update(temp, this.Id);
                    }
                    break;
                    case 'c'://标记大群的消息为已读
                    {
                        System.out.println("正在标记某人的大群聊天记录为已读");
                        jdbc_texts = new JDBC_Texts();
                        jdbc_texts.delete(this.Id);
                    }
                    break;
                    case 'd'://给某人单独发图片
                    {
                        System.out.println("正在给某人发图片");
                        jdbc_photos = new JDBC_Photos();
                        long temp = Long.parseLong(reader.readLine());
                        String extension = reader.readLine();
                        jdbc_photos.insert(this.Id, temp, extension, false, false);
                        Socket_Util temp_socket;
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            temp_socket = Listener_Server.managerAnnouncement.getSocketList().get(i);
                            if (temp_socket.getId() == temp)
                            {
                                temp_socket.outPhoto(temp, jdbc_photos.getNO(this.Id, temp), false);
                                break;
                            }
                        }
                    }
                    break;
                    case 'e'://给大群发图片
                    {
                        System.out.println("正在给大群发图片");
                        jdbc_photos = new JDBC_Photos();
                        jdbc_students = new JDBC_Students();
                        String extension = reader.readLine();

                        jdbc_photos.insert(this.Id, 100000000000L, extension, true, true);
                        long[] id = jdbc_students.getId();
                        for (int i = 0; i < id.length; i++)
                        {
                            jdbc_photos.insert(this.Id, id[i], extension, true, false);
                        }
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            Listener_Server.managerAnnouncement.getSocketList().get(i).
                                    outPhoto(this.Id, jdbc_photos.getNO(this.Id, 100000000000L), true);
                        }
                    }
                    break;
                    case 'f'://给某人单独发消息
                    {
                        jdbc_texts = new JDBC_Texts();
                        long temp = Long.parseLong(reader.readLine());//接收者的Id
                        String message = reader.readLine();//要收到的信息
                        jdbc_texts.insert(this.Id, temp, message, false, false);

                        Socket_Util temp_socket;
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            temp_socket = Listener_Server.managerAnnouncement.getSocketList().get(i);
                            if (temp_socket.getId() == temp)
                            {
                                temp_socket.outSending(this.Id, message, false);
                                break;
                            }
                        }
                    }
                    break;
                    case 'g'://向大群发送消息
                    {
                        jdbc_texts = new JDBC_Texts();
                        jdbc_students = new JDBC_Students();
                        System.out.println("正在发群消息");
                        String message = reader.readLine();
                        jdbc_texts.insert(this.Id, 100000000000L, message, true, true);
                        long[] id = jdbc_students.getId();
                        for (int i = 0; i < id.length; i++)
                        {
                            jdbc_texts.insert(this.Id, id[i], message, true, false);
                        }
                        for (int i = 0; i < Listener_Server.idList.size(); i++)
                        {
                            Listener_Server.managerAnnouncement.getSocketList().get(i).outSending(this.Id, message, true);
                        }
                    }
                    break;
                    case 'h'://查询与某人聊天记录
                    {
                        System.out.println("正在查询聊天记录");
                        jdbc_texts = new JDBC_Texts();
                        jdbc_photos = new JDBC_Photos();

                        long temp = Long.parseLong(reader.readLine());
                        ArrayList<Message> arrayList = jdbc_texts.query(this.Id, temp);
                        ArrayList<Photo> arrayList1 = jdbc_photos.query(this.Id, temp);
                        Iterator<Photo> iterator = arrayList1.iterator();
                        while (iterator.hasNext())
                        {
                            for (int i = 0; i < arrayList.size(); i++)
                            {
                                if (arrayList.get(i).compareTo(iterator) > 0)
                                {
                                    arrayList.add(i, (Photo) iterator);
                                    break;
                                }
                            }
                        }

                        writer.println(arrayList.size());

                        for (int i = 0; i < arrayList.size(); i++)
                        {
                            Message a = arrayList.get(i);
                            if (arrayList.get(i) instanceof Text)
                            {
                                Text b = (Text) a;
                                writer.println(b.getSender());
                                writer.println(b.getReceiver());
                                writer.println(b.getTime());
                                writer.println(b.getText());
                            } else
                            {
                                Photo b = (Photo) a;
                                writer.println(b.getSender());
                                writer.println(b.getReceiver());
                                writer.println(b.getTime());
                                writer.println("`" + b.getNO());
                            }
                        }
                    }
                    break;
                    case 'i'://查询大群群聊记录
                    {
                        System.out.println("正在查询群聊记录");
                        jdbc_texts = new JDBC_Texts();
                        jdbc_photos = new JDBC_Photos();

                        ArrayList<Message> arrayList = jdbc_texts.query();
                        ArrayList<Photo> arrayList1 = jdbc_photos.query();
                        Iterator<Photo> iterator = arrayList1.iterator();
                        while (iterator.hasNext())
                        {
                            for (int i = 0; i < arrayList.size(); i++)
                            {
                                if (arrayList.get(i).compareTo(iterator) > 0)
                                {
                                    arrayList.add(i, (Photo) iterator);
                                    break;
                                }
                            }
                        }

                        writer.println(arrayList.size());

                        for (int i = 0; i < arrayList.size(); i++)
                        {
                            Message a = arrayList.get(i);
                            if (arrayList.get(i) instanceof Text)
                            {
                                Text b = (Text) a;
                                writer.println(b.getSender());
                                writer.println(b.getReceiver());
                                writer.println(b.getText());
                                writer.println(b.getTime());
                            } else
                            {
                                Photo b = (Photo) a;
                                writer.println(b.getSender());
                                writer.println(b.getReceiver());
                                writer.println("`" + b.getNO());
                                writer.println(b.getTime());
                            }
                        }
                        System.out.println("OK");
                        writer.println("OK");
                    }
                    break;


                    case 'j'://查询用户的详细信息
                    {
                        long temp = Long.parseLong(reader.readLine());
                        jdbc_students = new JDBC_Students();
                        Student student = jdbc_students.getInformation(temp);
                        writer.println(student.getName());
                        writer.println(student.getNickname());
                        writer.println(student.getSex());
                        writer.println(student.getAdministrator());
                    }
                    break;
                    case 'k'://查询管理员操作日志
                    {
                        int count = JDBC_Log.count();
                        ResultSet rs = JDBC_Log.query();
                        writer.println(count);
                        for (int i = 0; i < count; i++)
                        {
                            rs.next();
                            writer.println(rs.getLong(2));//Id
                            writer.println(rs.getTimestamp(5).getTime());//Time
                            writer.println(rs.getString(3));//Name

                            writer.println(rs.getString(4));//Operation
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
            reader.close();
        } catch (IOException | SQLException e)
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
