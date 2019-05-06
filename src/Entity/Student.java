package Entity;

import java.io.Serializable;

public class Student implements Serializable
{
    private static final long serialVersionUID = 1L;
    private long Id;
    private String Name;
    private String Nickname;
    private String Sex;
    private String Password;
    private int Administrator;
    private int messagesUnread ;

    public int getMessagesUnread()
    {
        return messagesUnread;
    }

    public void setMessagesUnread(int messagesUnread)
    {
        this.messagesUnread = messagesUnread;
    }

    public Student()
    {
    }

    public Student(long id, String password)
    {
        Id = id;
        Password = password;
    }

    public Student(long id, String name, String nickname, String sex, int administrator)
    {
        Id = id;
        Name = name;
        Nickname = nickname;
        Sex = sex;
        Administrator = administrator;
    }

    public Student(long id, String name, String nickname, String sex, String password, int administrator)
    {
        Id = id;
        Name = name;
        Nickname = nickname;
        Sex = sex;
        Password = password;
        Administrator = administrator;
    }

    public void setId(long id)
    {
        Id = id;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public void setNickname(String nickname)
    {
        Nickname = nickname;
    }

    public void setSex(String sex)
    {
        Sex = sex;
    }

    public void setPassword(String password)
    {
        Password = password;
    }

    public void setAdministrator(int administrator)
    {
        Administrator = administrator;
    }

    public long getId()
    {
        return Id;
    }

    public String getName()
    {
        return Name;
    }

    public String getNickname()
    {
        return Nickname;
    }

    public String getSex()
    {
        return Sex;
    }

    public String getPassword()
    {
        return Password;
    }

    public int getAdministrator()
    {
        return Administrator;
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Nickname='" + Nickname + '\'' +
                ", Sex='" + Sex + '\'' +
                ", Password='" + Password + '\'' +
                ", Administrator=" + Administrator +
                '}';
    }
}
