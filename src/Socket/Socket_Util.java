package Socket;

import Database.*;
import Entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class Socket_Util extends Thread
{
    private String Password;
    private int Administrator;
    private long Id;
    private JDBC_Announcement jdbc_announcement;
    private JDBC_Documents jdbc_documents;
    private JDBC_Texts jdbc_texts;
    private JDBC_Photos jdbc_photos;
    private JDBC_Students jdbc_students;
    private JDBC_Vote jdbc_vote;
    Socket socket;

    private int fileNo;
    private long fileSize;
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

    public long getFileSize()
    {
        return fileSize;
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

    public void outOffLine(long id, String name, String nickname)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(1);

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
            writer.println(2);

            writer.println(Name);
            writer.println(Title);
            writer.println(Text);
            writer.println(Time.getTime());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //2弹出投票
    public void outVoting(int no)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(3);

            jdbc_vote = new JDBC_Vote();
            Result result = jdbc_vote.calculate(no);

            writer.println(no);//投票的序号
            writer.println(result.getTitle());//标题
            writer.println(result.getName());//发起人
            writer.println(result.getTime().getTime());

            //选项内容，以及现在的票数
            writer.println(result.getOptions().length);

            for (int i = 0; i < result.getOptions().length; i++)
            {
                writer.println(result.getOptions()[i]);
                writer.println(result.getVotes()[i]);
            }
            System.out.println(Arrays.toString(result.getOptions()));
            //已有的补充意见
            int counter = 0;
            for (String temp : result.getOpinions())
            {
                if (temp != null) counter++;
            }

            writer.println(counter);
            for (String a :
                    result.getOpinions())
            {
                if (a != null)
                {
                    writer.println(a);
                    System.out.println(a);
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //3弹出投票结果
    public void votingResult(int no)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(4);


            jdbc_vote = new JDBC_Vote();
            Result result = jdbc_vote.calculate(no);
            writer.println(no);
            writer.println(result.getTitle());//标题
            writer.println(result.getName());//发起人
            writer.println(result.getTime().getTime());
            //选项内容，以及现在的票数
            writer.println(result.getOptions().length);
            for (int i = 0; i < result.getOptions().length; i++)
            {
                writer.println(result.getOptions()[i]);
                writer.println(result.getVotes()[i]);
            }
            //所有的补充意见
            int counter = 0;
            for (String temp : result.getOpinions())
            {
                if (temp != null) counter++;
            }

            writer.println(counter);
            for (String a :
                    result.getOpinions())
            {
                if (a != null) writer.println(a);
            }
            Manager_Voting.subtractManager(no);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    //3有人上传了新文件

    public void outUpload(String name, long size)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(3);

            writer.println(name);
            writer.println(size);
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

    public void outPhoto(long sender, int no, boolean group)//发图片
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
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);


            String input;
            while ((input = reader.readLine()) != null)
            {
                if (input.equals("quit"))
                {
                    System.out.println("closed!" + socket.getPort());
                    //告诉其他人我下线了
                    Manager_Online.getManagerOnline().setFlag('2');
                    Manager_Online.getManagerOnline().setSocket(this);
                    Thread thread = new Thread(Manager_Online.getManagerOnline());
                    thread.start();
                    thread.join();


                    Manager_Online.getManagerOnline().subtract(this);
                    Manager_File.getManagerFile().subtract(this);
                    Manager_Announcement.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());
                    Manager_Voting.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());

                    Manager_Id.getManagerId().remove(this.Id);

                    break;
                }
                switch (input.charAt(0))
                {
                    case '1'://注册
                    {
                        System.out.println("正在注册");
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
                            System.out.println("注册成功");
                            writer.println("OK");
                            Manager_Online.getManagerOnline().setSocket(this);
                            new Thread(Manager_Online.getManagerOnline()).start();
                        } else
                        {
                            System.out.println("注册失败");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '2'://登录
                    {
                        System.out.println("正在登陆");
                        input = reader.readLine();
                        Id = Long.parseLong(input);
//                        System.out.println(Id);
                        jdbc_students = new JDBC_Students();

                        if (jdbc_students.judgeId(Id))
                        {
                            writer.println("OK");
                            Password = reader.readLine().trim();
                            if (jdbc_students.judgePassword(Id, Password))
                            {
                                writer.println("OK");
                                System.out.println("登陆成功");
                                Name = jdbc_students.getInformation(Id).getName();
                                Nickname = jdbc_students.getInformation(Id).getNickname();

                                writer.println(jdbc_students.getInformation(Id).getAdministrator());//告诉你是不是管理员
                                System.out.println("是否为管理员" + jdbc_students.getInformation(Id).getAdministrator());
                                Manager_Id.getManagerId().add(Id);
                                //告诉其他人我上线了
                                Manager_Online.getManagerOnline().setFlag('1');
                                Manager_Online.getManagerOnline().setSocket(this);
                                new Thread(Manager_Online.getManagerOnline()).start();

                                //载入学生信息以及未读消息数

                                jdbc_photos = new JDBC_Photos();
                                jdbc_texts = new JDBC_Texts();

                                Map<Long, Short> map1 = jdbc_texts.signIn(this.Id);
                                Map<Long, Short> map2 = jdbc_photos.signIn(this.Id);

                                for (Map.Entry<Long, Short> entry : map2.entrySet())
                                {
                                    if (map1.containsKey(entry.getKey()))
                                    {
                                        int temp = map1.get(entry.getKey()) + entry.getValue();

                                        map1.put(entry.getKey(), (short) temp);
                                    } else
                                    {
                                        map1.put(entry.getKey(), entry.getValue());
                                    }
                                }
                                for (Map.Entry entry : map1.entrySet())
                                {
                                    System.out.print(entry.getKey() + ": ");
                                    System.out.println(entry.getValue());

                                }
                                int num = JDBC_Students.count();//总人数
                                long[] array = jdbc_students.getId();

                                int n = Manager_Id.getManagerId().idList.size();//在线人数
                                writer.println(n);
                                for (int i = 0; i < n; i++)
                                {
                                    long id = Manager_Id.getManagerId().idList.get(i);
                                    writer.println(id);
                                    writer.println(jdbc_students.getInformation(Manager_Id.getManagerId().idList.get(i)).getName());
                                    writer.println(jdbc_students.getInformation(Manager_Id.getManagerId().idList.get(i)).getNickname());
                                    writer.println(jdbc_students.getInformation(Manager_Id.getManagerId().idList.get(i)).getSex());
                                    writer.println(map1.containsKey(id) ? map1.get(id) : 0);
                                }
                                //不在线的人
                                writer.println(num - n);
                                for (int i = 0; i < num; i++)
                                {
                                    if (!Manager_Id.getManagerId().idList.contains(array[i]))
                                    {
                                        writer.println(array[i]);
                                        writer.println(jdbc_students.getInformation(array[i]).getName());
                                        writer.println(jdbc_students.getInformation(array[i]).getNickname());
                                        writer.println(jdbc_students.getInformation(array[i]).getSex());
                                        writer.println(map1.containsKey(array[i]) ? map1.get(array[i]) : 0);

                                    }
                                }
                                //公告信息
                                jdbc_announcement = new JDBC_Announcement();
                                int countOfAnnouncements = JDBC_Announcement.count();
                                Timestamp now = new Timestamp(System.currentTimeMillis());
                                int count = 0;//一周之前的公告数
                                for (int i = 1; i <= countOfAnnouncements; i++)
                                {
                                    if ((now.getTime() - jdbc_announcement.query(i).getTime().getTime()) > 604800000)
                                    {
                                        count++;
                                    } else
                                    {
                                        break;
                                    }
                                }
                                writer.println(countOfAnnouncements - count);
//                                System.out.println("一周内公告数" + arrayList.size());
                                for (int i = 0; i < countOfAnnouncements - count; i++)
                                {
                                    Announcement announcement = jdbc_announcement.query(i + 1 + count);
                                    writer.println(announcement.getNO());
                                    writer.println(announcement.getName());
                                    writer.println(announcement.getTitle());
                                    writer.println(announcement.getTime().getTime());
//                                    System.out.println(announcement.getTime());
                                }

                                //文件信息
                                jdbc_documents = new JDBC_Documents();
                                writer.println(JDBC_Documents.count());
                                System.out.println("文件数：" + JDBC_Documents.count());
                                for (int i = 1; i <= JDBC_Documents.count(); i++)
                                {
                                    writer.println(i);
                                    writer.println(jdbc_documents.query(i).getName());
                                    writer.println(jdbc_documents.query(i).getSize());
                                }
//                                //未读消息 的数目
//
//                                int numOfUsersUnread = map1.size();
//                                writer.println(numOfUsersUnread);
////                                System.out.println("未读消息用户数：" + numOfUsersUnread);
//                                long[] id = jdbc_students.getId();
//                                for (int i = 0; i < JDBC_Students.count(); i++)
//                                {
//                                    if (map1.containsKey(id[i]))
//                                    {
//                                        writer.println(id[i] + ":" + map1.get(id[i]));
//                                        System.out.println(id[i] + ":" + map1.get(id[i]));
//                                    }
//                                }
                                {
                                    ServerSocket serverSocket = new ServerSocket(6666);

                                    Socket socket = serverSocket.accept();
                                    System.out.println("6666:" + socket.getPort() + "\n");
                                    Manager_Online.getManagerOnline().add(this, new Socket_Util(socket));
                                    Manager_File.getManagerFile().add(this, new Socket_Util(socket));
                                    Manager_Announcement.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());
                                    Manager_Voting.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());
                                    serverSocket.close();
                                }
                            } else
                            {
                                System.out.println("NO2:Password");
                                writer.println("NO2");//false2密码错误
                            }
                        } else
                        {
                            System.out.println("NO1:Id");
                            writer.println("NO1");//false1是Id不存在
                        }
                    }
                    break;
                    case '3'://发公告
                    {
                        System.out.println("发公告");

                        Title = reader.readLine().trim();

                        Text = reader.readLine().trim();

                        Time = new Timestamp(System.currentTimeMillis());

                        Announcement announcement = new Announcement(Name, Title, Text, Time);

                        if (jdbc_announcement.insert(announcement))
                        {
                            System.out.println("发布成功");
                            writer.println("OK");

                            JDBC_Log.insert(this.Id, this.Name, "发布了公告:" + Title);
                            int count = JDBC_Announcement.count();
                            System.out.println("目前的公告数："+count);
                            //告诉其他人我发公告了
                            Manager_Announcement.addManager(count, this);
                            Manager_Announcement.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());
                            new Thread(Manager_Announcement.getManagerAnnouncement(count)).start();
                        } else
                        {
                            System.out.println("公告标题重复");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '4'://查公告
                    {
                        System.out.println("正在查公告");
                        input = reader.readLine();
                        int no = Integer.parseInt(input);
                        Announcement announcement = jdbc_announcement.query(no);
//                        builder.append(announcement.getNO()+"\t");
                        writer.println(announcement.getName());
                        writer.println(announcement.getTitle());
                        writer.println(announcement.getText());
                        writer.println(announcement.getTime().getTime());
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
                        jdbc_vote = new JDBC_Vote();
                        System.out.println("正在发起投票");
                        String title = reader.readLine().trim();
                        System.out.println("投票题目：" + title);

                        int num = Integer.parseInt(reader.readLine());
                        System.out.print("选项数 " + num + "    ");

                        String[] options = new String[num];
                        for (int i = 0; i < num; i++)
                        {
                            options[i] = reader.readLine();
                        }
                        System.out.println(Arrays.toString(options));

                        if (jdbc_vote.start(this.Name, title, options))
                        {
                            int count = JDBC_Vote.count();
                            System.out.println("发布成功");
                            writer.println("OK");
                            JDBC_Log.insert(this.Id, this.Name, "发起了投票:" + title);
                            //开启投票线程
                            Manager_Voting.addManager(count, this);
                            Manager_Voting.setAllSocketLists(Manager_Online.getManagerOnline().getSocketList());
                            new Thread(Manager_Voting.getManagerVoting(count)).start();
                        } else
                        {
                            System.out.println("投票标题重复");
                            writer.println("NO");
                        }
                    }
                    break;
                    case '7'://查询投票结果
                    {
                        jdbc_vote = new JDBC_Vote();

                        System.out.println("正在查询投票结果");
                        int a = Integer.parseInt(reader.readLine());//a:投票的序号

                        Result result = jdbc_vote.calculate(a);
                        writer.println(result.getName());
                        writer.println(result.getTitle());
                        writer.println(result.getTime().getTime());

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
                        System.out.println("已查询投票结果");
                    }
                    break;
                    case 'S'://某人完成投票
                    {
                        System.out.println(Name + "完成了投票");
                        jdbc_vote = new JDBC_Vote();
                        int no = Integer.parseInt(reader.readLine());//投票的序号

                        //这个人投的哪一票
                        int index = Integer.parseInt(reader.readLine());
                        int[] votes = new int[jdbc_vote.calculate(no).getOptions().length];
                        for (int i = 0; i < votes.length; i++)
                        {
                            if (i != index - 1) votes[i] = 0;
                            else votes[i] = 1;
                        }
                        //这个人的意见
                        String opinion = reader.readLine();
                        if (opinion.equals("null")) opinion = null;
                        //插库
                        Voting voting = new Voting();
                        voting.setNO(no);
                        voting.setVotes(votes);
                        voting.setOpinion(opinion);
                        jdbc_vote.voting(voting);

                        System.out.println("已经完成投票结果的插库");

                        //此人已经完成投票，寻找下一个人来投票
                        new Thread(Manager_Voting.getManagerVoting(no)).start();
                    }
                    break;
                    case 'm'://查看已经完成的投票
                    {
                        jdbc_vote = new JDBC_Vote();
                        System.out.println("正在查看已经完成的投票");

                        Map<Integer, String> map = jdbc_vote.selectVotes();
                        System.out.println(map.toString());

                        writer.println(map.size());
                        for (Map.Entry<Integer, String> entry : map.entrySet())
                        {
                            writer.println(entry.getKey());
                            writer.println(entry.getValue());
                            System.out.println(entry.getKey()+":"+entry.getValue());
                        }
                    }
                    break;
                    case '8'://上传文件
                    {
                        System.out.println("正在判断文件名是否重复");
                        fileName = reader.readLine();
                        fileSize = Long.parseLong(reader.readLine());

                        jdbc_documents = new JDBC_Documents();
                        if (jdbc_documents.judge(fileName))
                        {
                            jdbc_documents.insert(fileName, fileSize);
                            System.out.println("文件名没有重复");
                            writer.println("OK");
                            //告诉其他人我传了文件
                            Manager_File.getManagerFile().setFlag('1');
                            Manager_File.getManagerFile().setSocket(this);
                            new Thread(Manager_File.getManagerFile()).start();
                        } else
                        {
                            System.out.println("文件名重复");
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
                        JDBC_Log.insert(this.Id, this.Name, "删除了文件:" + fileName);
                        //告诉其他人我删了文件
                        Manager_File.getManagerFile().setFlag('2');
                        Manager_File.getManagerFile().setSocket(this);
                        new Thread(Manager_File.getManagerFile()).start();
                    }
                    break;
                    case '0'://打开与某人的聊天面板
                    {
                        jdbc_texts = new JDBC_Texts();
                        jdbc_photos = new JDBC_Photos();
                        long temp = Long.parseLong(reader.readLine());
                        ArrayList<Message> arrayList = jdbc_texts.queryId(temp, this.Id);
                        ArrayList<Photo> arrayList1 = jdbc_photos.queryId(temp, this.Id);
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

                        ArrayList<Message> arrayList = jdbc_texts.queryGroup(this.Id);
                        ArrayList<Photo> arrayList1 = jdbc_photos.queryGroup(this.Id);
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
                        for (int i = 0; i < Manager_Id.getManagerId().getIdList().size(); i++)
                        {
                            temp_socket = Manager_Online.getManagerOnline().getSocketList().get(i);
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
                        for (int i = 0; i < Manager_Id.getManagerId().getIdList().size(); i++)
                        {
                            Manager_Online.getManagerOnline().getSocketList().get(i).
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
                        for (int i = 0; i < Manager_Id.getManagerId().getIdList().size(); i++)
                        {
                            temp_socket = Manager_Online.getManagerOnline().getSocketList().get(i);
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
                        for (int i = 0; i < Manager_Id.getManagerId().getIdList().size(); i++)
                        {
                            Manager_Online.getManagerOnline().getSocketList().get(i).outSending(this.Id, message, true);
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
                                Entity.Text b = (Text) a;
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
                        System.out.println("查看操作日志");
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
        } catch (IOException | SQLException |
                InterruptedException e)

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
