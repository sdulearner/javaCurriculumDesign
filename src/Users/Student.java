package Users;

public  class Student
{
    private long Id;
    private String Name;
    private String Nickname;
    private String Sex;
    private String Password;
    private int Administrator;

    public Student(long id, String password)
    {
        Id = id;
        Password = password;
    }

    public Student(long  id, String name, String nickname, String sex, String password, int administrator)
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
}
