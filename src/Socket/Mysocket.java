package Socket;

import Database.JDBCOperation;
import Users.Student;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Mysocket
{
    private JDBCOperation jdbc = new JDBCOperation();
    private Student student;


    private long Id;
    private String Name;
    private String Nickname;
    private String Sex;
    private String Password;
    private int Administrator;
    ServerSocket server;
    private int port = 8080;

    public Mysocket() throws IOException
    {
        server = new ServerSocket(port);
        System.out.println("启动");

    }

    public void service()
    {
        Socket socket = null;
        while (true)
        {
            try
            {
                System.out.println("address:" + socket.getInetAddress() + ":" + socket.getPort());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                PrintWriter printWriter = new PrintWriter(writer, true);
                String input = null;
                while ((input = reader.readLine()) != null)
                {
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
                            jdbc.insert(student);
                        }
                        case '2'://登录
                        {
                            input=reader.readLine();
                            Id=Long.parseLong(input);
                            if (jdbc.judgeId(Id)){
                                input=reader.readLine();
                                Password=input.trim();
                                printWriter.println(jdbc.judgePassword(Id,Password)?"true":"false2");
                            }else{
                                printWriter.println("false1");
                            }
                        }

                        case '3':


                    }

                    if (input.equals("quit"))
                    { //如果用户输入“quit”就退出
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


    }


}
