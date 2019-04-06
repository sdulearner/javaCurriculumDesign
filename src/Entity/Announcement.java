package Entity;

import java.io.Serializable;
import java.sql.Timestamp;


public class Announcement implements Serializable
{
    private static final long serialVersionUID = 2L;

    private int NO;
    private String Name;
    private String Title;
    private String Text;
    private Timestamp Time;

    public Announcement()
    {

    }

    public Announcement( String name, String title, String text, Timestamp time)
    {
        Name = name;
        Title = title;
        Text = text;
        Time = time;
    }

    public void setNO(int NO)
    {
        this.NO = NO;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public void setText(String text)
    {
        Text = text;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }

    public int getNO()
    {
        return NO;
    }

    public String getName()
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

    @Override
    public String toString()
    {
        return "Announcement{" +
                "NO=" + NO +
                ", Name='" + Name + '\'' +
                ", Title='" + Title + '\'' +
                ", Text='" + Text + '\'' +
                ", Time=" + Time +
                '}';
    }
}
