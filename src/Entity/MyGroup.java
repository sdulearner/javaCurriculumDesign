package Entity;

import java.sql.Timestamp;

public class MyGroup
{
    private String Name;
    private long[] id;
    private Timestamp Time;

    public void setName(String name)
    {
        Name = name;
    }

    public void setId(long[] id)
    {
        this.id = id;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }

    public String getName()
    {
        return Name;
    }

    public long[] getId()
    {
        return id;
    }

    public Timestamp getTime()
    {
        return Time;
    }

    public MyGroup()
    {
    }
}
