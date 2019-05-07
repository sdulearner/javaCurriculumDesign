package ClientSocket;

import Entity.Announcement;
import Entity.Document;
import Entity.Result;
import Entity.Student;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientSocket_Util extends Thread
{
    private Socket socket;
    private long Id;

//    public ClientSocket_Util(Socket socket, MainFrame mainFrame) {
//        this.socket = socket;
//        this.mainFrame = mainFrame;
//    }

    public ClientSocket_Util(Socket socket)
    {
        this.socket = socket;
    }

    public ClientSocket_Util(Socket socket, long id)
    {
        this.socket = socket;
        Id = id;
    }

    /**
     * @Description: 0. 点击主面板的右上角的×的时候调用，提示已经退出了
     * @Parameters: []
     * @return: void
     * @date: 2019/5/3
     * @time: 16:19
     */
    public void closeConnection()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("quit");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 1. 注册
     * @Parameters: [student:学生信息]
     * @return: boolean 是否注册成功
     * @Date: 2019/5/3
     */
    public boolean register(Student student)
    {
        boolean result = false;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(1);

            writer.println(student.getId());
            writer.println(student.getName());
            writer.println(student.getNickname());
            writer.println(student.getSex());
            writer.println(student.getPassword());
            writer.println(student.getAdministrator());
            switch (reader.readLine())
            {
                case "NO":
                {
                    //Id重复
                    result = false;
                }
                break;
                case "OK":
                {
                    //注册成功
                    result = true;
                }
                break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 2. 登录
     * @Parameters: [Id:用户Id, Password:用户密码]
     * @return: SignIn  0:Id不存在  1:密码错误  2:成功
     * @Date: 2019/5/3
     */
    public SignIn signIn(long Id, String Password) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        writer.println(2);

        writer.println(Id);
        if (reader.readLine().equals("NO1"))
        {
            //Id不存在

            return new SignIn('0');
        } else
        {
            writer.println(Password);
            if (reader.readLine().equals("NO2"))
            {
                //密码错误

                return new SignIn('1');
            } else
            {
                int Administrator = Integer.parseInt(reader.readLine());//是否为管理员
                System.out.println("是否为管理员：" + Administrator);

                //在线的学生
                int numOfStudentsOnline = Integer.parseInt(reader.readLine());
                Student[] studentsOnline = new Student[numOfStudentsOnline];
                for (int i = 0; i < numOfStudentsOnline; i++)
                {
                    studentsOnline[i] = new Student();
                    studentsOnline[i].setId(Long.parseLong(reader.readLine()));
                    studentsOnline[i].setName(reader.readLine());
                    studentsOnline[i].setNickname(reader.readLine());
                    studentsOnline[i].setSex(reader.readLine());
                    studentsOnline[i].setMessagesUnread(Integer.parseInt(reader.readLine())
                    );
//                    System.out.println(reader.readLine());
//                    System.out.println(reader.readLine());
//                    System.out.println(reader.readLine());
                }
                studentsOnline[numOfStudentsOnline - 1].setAdministrator(Administrator);
                //不在线的学生
                int numOfStudentsOffLine = Integer.parseInt(reader.readLine());
                System.out.println("不在线的学生：" + numOfStudentsOffLine);
                Student[] studentsOffline = new Student[numOfStudentsOffLine];
                for (int i = 0; i < numOfStudentsOffLine; i++)
                {
                    studentsOffline[i] = new Student();
                    studentsOffline[i].setId(Long.parseLong(reader.readLine()));
                    studentsOffline[i].setName(reader.readLine());
                    studentsOffline[i].setNickname(reader.readLine());
                    studentsOffline[i].setSex(reader.readLine());
                    studentsOffline[i].setMessagesUnread(Integer.parseInt(reader.readLine()));
                }
                //公告
                int numOfAnnouncements = Integer.parseInt(reader.readLine());
                Announcement[] announcements = new Announcement[numOfAnnouncements];
                for (int i = 0; i < numOfAnnouncements; i++)
                {
                    announcements[i] = new Announcement();
                    announcements[i].setNO(Integer.parseInt(reader.readLine()));
                    announcements[i].setName(reader.readLine());
                    announcements[i].setTitle(reader.readLine());
                    announcements[i].setTime(new Timestamp(Long.parseLong(reader.readLine())));
                    System.out.println(announcements[i]);
                }
                //所有文件
                int numOfFiles = Integer.parseInt(reader.readLine());
                Document[] documents = new Document[numOfFiles];
                for (int i = 0; i < numOfFiles; i++)
                {
                    documents[i] = new Document();
                    documents[i].setNo(Integer.parseInt(reader.readLine()));
                    documents[i].setName(reader.readLine());
                    documents[i].setSize(Long.parseLong(reader.readLine()));
                    System.out.println(documents[i]);
                }

//                //未读消息
//                int numOfUsersUnread = Integer.parseInt(reader.readLine());//有多少个用户有未读消息
//                System.out.println("未读消息用户数" + numOfUsersUnread);
//                Map<Long, Short> map = new HashMap<>();
//                String temp = null;
//                long id = 0;
//                int num = 0;
//                for (int i = 0; i < numOfUsersUnread; i++) {
//                    temp = reader.readLine();
////                    System.out.println(temp);
//                    id = Long.parseLong(temp.substring(0, 12));
//                    num = Integer.parseInt(temp.substring(13));
//                    map.put(id, (short) num);
//                }
                return new SignIn(Administrator, studentsOnline, studentsOffline, announcements, documents, '2');
            }
        }
    }

    /**
     * @Description: 3. 发公告
     * @Parameters: [title:标题, text:内容]
     * @return: boolean 公告标题是否重复
     * @Date: 2019/5/3
     */
    public boolean outAnnouncemnt(String title, String text)
    {
        boolean result = false;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(3);

            writer.println(title);
            writer.println(text);
            String line = reader.readLine();
            switch (line)
            {

                case "NO":
                {
                    //公告重名
                    result = false;
                }
                break;
                case "OK":
                {
                    //发送成功
                    result = true;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 4. 查公告
     * @Parameters: [no:公告序号]
     * @return: Announcement
     * @date: 2019/5/3
     * @time: 11:38
     */
    public Announcement selectAnnouncemnet(int no)
    {
        Announcement announcement = new Announcement();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(4);

            writer.println(no);
            announcement.setName(reader.readLine());
            announcement.setTitle(reader.readLine());
            announcement.setText(reader.readLine());
            announcement.setTime(new Timestamp(Integer.parseInt(reader.readLine())));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return announcement;
    }

    /**
     * @Description: 5. 修改个人信息
     * @Parameters: [nickname：修改后的昵称, password：修改后的密码]
     * @return: void
     * @date: 2019/5/3
     * @time: 15:52
     */
    public void updateInformation(String nickname, String password)
    {

        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(5);


            writer.println(nickname);
            writer.println(password);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * @Description: 6. 发布投票
     * @Parameters: [title:投票的标题, options:投票的选项]
     * @return: boolean  投票是否重名
     * @date: 2019/5/3
     * @time: 11:39
     */
    public boolean startVoting(String title, String[] options)
    {
        boolean result = false;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(6);

            writer.println(title);
            writer.println(options.length);
            for (String a : options)
            {
                writer.println(a);
            }
            String line = reader.readLine();
            switch (line)
            {
                case "OK":
                {
                    //成功
                    result = true;
                }
                break;
                case "标题重复":
                {
                    //失败
                    result = false;
                }
                break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 7. 查询投票结果
     * @Parameters: [no:投票的序号]
     * @return: Result:投票结果
     * @date: 2019/5/3
     * @time: 11:40
     */
    public Result selectVote(int no)
    {
        Result result = new Result();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(7);

            writer.println(no);


            result.setName(reader.readLine());
            result.setTitle(reader.readLine());
            result.setTime(new Timestamp(Long.parseLong(reader.readLine())));

            //numOfOptions:选项的个数
            int numOfOptions = Integer.parseInt(reader.readLine());
            String[] options = new String[numOfOptions];
            int[] votes = new int[numOfOptions];
            for (int i = 0; i < numOfOptions; i++)
            {
                options[i] = reader.readLine();
                votes[i] = Integer.parseInt(reader.readLine());
            }
            result.setOptions(options);
            result.setVotes(votes);

            int numOfOpinions = Integer.parseInt(reader.readLine());
            String[] opinions = new String[numOfOpinions];
            for (int i = 0; i < numOfOpinions; i++)
            {
                opinions[i] = reader.readLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 8. 上传文件
     * @Parameters: [name：文件名]
     * @return: boolean 文件是否重名
     * @date: 2019/5/3
     * @time: 16:01
     */
    public boolean uploadFile(String name)
    {
        boolean result = false;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(8);

            writer.println(name);
            String line = reader.readLine();
            switch (line)
            {
                case "NO":
                {
                    //文件重名
                    result = false;
                }
                break;
                case "OK":
                {
                    //上传成功
                    result = true;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 9. 按照序号删除某文件
     * @Parameters: [no：文件的序号]
     * @return: void
     * @date: 2019/5/3
     * @time: 16:10
     */
    public void deleteFile(int no)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(9);

            writer.println(no);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 10. 打开与某人的私聊面板,与下一个方法重载
     * @Parameters: [id：“某人”的Id]
     * @return: String[] 信息的内容，如果是图片，则前面有"`"来区分
     * @date: 2019/5/3
     * @time: 16:29
     */
    public String[] openChattingWindow(long id)
    {
        String[] result = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println(0);

            writer.println(id);
            int numOfMessagesUnread = Integer.parseInt(reader.readLine());
            result = new String[numOfMessagesUnread];
            for (int i = 0; i < numOfMessagesUnread; i++)
            {
                result[i] = reader.readLine();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 11. 打开大群的聊天面板,与上一个方法重载
     * @Parameters: []
     * @return: java.util.Map  Key:发送人的Id  Value:发送的内容，如果是图片则第一个字符为"`"
     * @date: 2019/5/3
     * @time: 16:37
     */
    public Map openChattingWindow()
    {
        Map<Long, String> result = new HashMap<>();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("a");

            int numOfMessagesUnread = Integer.parseInt(reader.readLine());
            long id;
            String text;

            for (int i = 0; i < numOfMessagesUnread; i++)
            {
                id = Long.parseLong(reader.readLine());
                text = reader.readLine();
                result.put(id, text);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 12. 标记与某人的消息为已读，如果与某人的聊天面板开着，且收到来自此人的消息时调用,与下一个方法重载
     * @Parameters: [id]：某人的Id
     * @return: void
     * @date: 2019/5/3
     * @time: 16:46
     */
    public void read(long id)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("b");

            writer.println(id);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * @Description: 13. 标记大群的消息为已读，如果与大群的聊天面板开着，且收到来自大群的消息时调用,与上一个方法重载
     * @Parameters: []
     * @return: void
     * @date: 2019/5/3
     * @time: 16:46
     */
    public void read()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("c");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 14. 给某人发图片
     * @Parameters: [id]
     * @return: void
     * @date: 2019/5/3
     * @time: 17:38
     */
    public void sendPhoto(long id)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("d");

            writer.println(id);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 15. 给大群发图片
     * @Parameters: []
     * @return: void
     * @date: 2019/5/3
     * @time: 21:34
     */
    public void sendPhoto()
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("e");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 16. 给某人发私聊消息
     * @Parameters: [id, text]
     * @return: void
     * @date: 2019/5/3
     * @time: 21:38
     */
    public void sendText(long id, String text)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("f");

            writer.println(id);
            writer.println(text);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 17. 给大群发消息
     * @Parameters: [text]
     * @return: void
     * @date: 2019/5/3
     * @time: 21:38
     */
    public void sendText(String text)
    {

        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("g");

            writer.println(text);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 18. 查询与某人的聊天记录
     * @Parameters: [id]
     * @return: java.util.Map  Key:1~12位为发送者的Id，13~24位为接收者的Id，剩下的为Timestamp  Value:内容
     * @date: 2019/5/4
     * @time: 10:10
     */
    public Map selectMessages(long id)
    {
        Map<String, String> map = new HashMap<>();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("h");


            writer.println(id);
            int numOfMessages = Integer.parseInt(reader.readLine());
            StringBuilder builder = new StringBuilder();
            String text = null;
            for (int i = 0; i < numOfMessages; i++)
            {
                builder.append(reader.readLine());
                builder.append(reader.readLine());
                builder.append(reader.readLine());
                text = reader.readLine();
                map.put(builder.toString(), text);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description: 19. 查询大群的聊天记录
     * @Parameters: []
     * @return: java.util.Map  同上(Key:1~12位为发送者的Id，13~24位为接收者的Id，剩下的为Timestamp  Value:内容)
     * @date: 2019/5/4
     * @time: 10:11
     */
    public Map selectMessages()
    {
        Map<String, String> map = new HashMap<>();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("i");

            int numOfMessages = Integer.parseInt(reader.readLine());
            StringBuilder builder = new StringBuilder();
            String text = null;
            for (int i = 0; i < numOfMessages; i++)
            {
                builder.append(reader.readLine());
                builder.append(reader.readLine());
                builder.append(reader.readLine());
                text = reader.readLine();
                map.put(builder.toString(), text);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    /**
//     * @Description: 20. 查询某用户的详细信息(废弃)
//     * @Parameters: [id]
//     * @return: Entity.Student
//     * @date: 2019/5/4
//     * @time: 10:12
//     */
//    public Student selectStudet(long id)
//    {
//
//        Student student = null;
//
//        try
//        {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//            writer.println("j");
//
//            writer.println(id);
//
//            String name = reader.readLine();
//            String nickname = reader.readLine();
//            String sex = reader.readLine();
//            boolean isAdministrator = reader.readLine().equals("1");
//            student = new Student(id, name, nickname, sex, isAdministrator ? 1 : 0);
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return student;
//    }

    /**
     * @Description: 21. 查询管理员的操作日志
     * @Parameters: []
     * @return: java.util.Map  Key:1~12位为操作人的Id,13~25位为Timestamp,剩下的为Name  Value:操作
     * @date: 2019/5/4
     * @time: 10:13
     */

    public Map selectLog()
    {
        Map<String, String> map = new LinkedHashMap<>();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("k");

            int numOfLog = Integer.parseInt(reader.readLine());
            StringBuilder builder = new StringBuilder();
            String operation = null;
            for (int i = 0; i < numOfLog; i++)
            {
                builder = new StringBuilder();
                builder.append(reader.readLine());//Id 12
                builder.append(reader.readLine());//Time  13
                builder.append(reader.readLine());//Name
                operation = reader.readLine();
                map.put(builder.toString(), operation);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @Description: 22. 完成投票
     * @Parameters: [no, votes, opinion]
     * @return: void
     * @date: 2019/5/6
     * @time: 22:57
     */
    public void voting(int no, int vote, String opinion)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            writer.println("l");

            writer.println(no);
            writer.println(vote);
            writer.println(opinion);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 23. 查看已经完成的投票
     * @Parameters: []
     * @return: java.util.Map
     * @date: 2019/5/7
     * @time: 16:08
     */
    public Map selectVotes()
    {
        Map<Integer, String> map = new LinkedHashMap<>();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("m");

            int numOfVotes = Integer.parseInt(reader.readLine());
            int no = 0;
            String title = null;
            for (int i = 0; i < numOfVotes; i++)
            {
                no = Integer.parseInt(reader.readLine());
                title = reader.readLine();
                map.put(no, title);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return map;
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
                switch (input.charAt(0))
                {
                    case '0'://上线提示
                    {
                        long id = Long.parseLong(reader.readLine());
                        String name = reader.readLine();
                        String nickname = reader.readLine();
                        Student student = new Student();
                        student.setId(id);
                        student.setName(name);
                        student.setNickname(nickname);
                        System.out.println("有人上线了:" + name);
                        System.out.println(student);
//                        mainFrame.OnlineStudentsUpdate(student);
                    }
                    break;
                    case '1':
                    {
                        long id = Long.parseLong(reader.readLine());
                        String name = reader.readLine();
                        String nickname = reader.readLine();
                        Student student = new Student();
                        student.setId(id);
                        student.setName(name);
                        student.setNickname(nickname);
                        System.out.println("有人下线了:" + name);
                        System.out.println(student);
//                        mainFrame.OffOnlineStudentUpdate(student);
                    }
                    break;
                    case '2'://弹出公告
                    {
                        String name = reader.readLine();
                        String title = reader.readLine();//标题
                        String text = reader.readLine();//内容
                        Timestamp time = new Timestamp(Long.parseLong(reader.readLine()));
                        Announcement announcement = new Announcement();
                        announcement.setName(name);
                        announcement.setTitle(title);
                        announcement.setText(text);
                        announcement.setTime(time);
//                        new AcceptAnnounce().acceptNotice(announcement);
//                        mainFrame.AnnouncementUpdate(announcement);
                    }
                    break;
                    case '3'://弹出投票
                    {
                        Result result = new Result();
                        result.setTitle(reader.readLine());
                        result.setName(reader.readLine());
                        int numOfOptions = Integer.parseInt(reader.readLine());
                        String[] options = new String[numOfOptions];
                        int[] votes = new int[numOfOptions];
                        for (int i = 0; i < numOfOptions; i++)
                        {
                            options[i] = reader.readLine();
                            votes[i] = Integer.parseInt(reader.readLine());
                        }
                        result.setOptions(options);
                        result.setVotes(votes);

                        int numOfOpinions = Integer.parseInt(reader.readLine());
                        String[] opinions = new String[numOfOpinions];
                        for (int i = 0; i < numOfOpinions; i++)
                        {
                            opinions[i] = reader.readLine();
                        }

                        boolean[] vote = new boolean[numOfOptions];
                        String opinion = "";
                        //读取结果


                        for (int i = 0; i < numOfOptions; i++)
                        {
                            writer.println(vote[i] ? 1 : 0);
                        }
                        writer.println(opinion);
                    }
                    break;

                    case '4'://有人上传了新文件
                    {
                        String fileName = reader.readLine();
                    }
                    break;
                    case '5'://有人删除了某文件
                    {
                        //no:文件的序号
                        int no = Integer.parseInt(reader.readLine());
                    }
                    break;
//                    case '5'://有人给你发消息
//                    {
//                        long sender = Long.parseLong(reader.readLine());
//                        String text = reader.readLine();
//
//                        //group:是否为群发消息
//                        boolean group = reader.readLine().equals("true");
//                        if (//和sender的聊天面板开着)
//                        {
//                            //如果是私聊消息就调用
//                            read(sender);
//                            //如果是群发消息
//                            read();
//                        }
//                    }
//                    break;
//                    case '6'://有人给你发图片
//                    {
//                        long sender = Long.parseLong(reader.readLine());
//                        int no = Integer.parseInt(reader.readLine());
//
//                        boolean group = reader.readLine().equals("true");
//                        //(同上)
//                        if (//和sender的聊天面板开着)
//                        {
//                            //如果是私聊消息就调用
//                            read(sender);
//                            //如果是群发消息
//                            read();
//                        }
//                    }
//                    break;
                    default:
                        break;
                }
            }
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


//        @Override
//        public void run ()
//        {
//
//            try
//            {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//                String input;
//                while ((input = reader.readLine()) != null)
//                {
//                    switch (input.charAt(0))
//                    {
//
//
//                    }
//                }
//            } catch (IOException e)
//            {
//                e.printStackTrace();
//            } finally
//            {
//                if (socket != null)
//                {
//                    try
//                    {
//                        socket.close();
//                    } catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

}