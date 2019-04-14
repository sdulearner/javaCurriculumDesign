package Entity;

import java.sql.Timestamp;

public class Messages
{
    private int NO;
    private long Sender;
    private long receiver;
    private String Text;
    private String MyGroup;
    private Timestamp Time;
    private boolean flag;

    public void setText(String text)
    {
        Text = text;
    }

    public String getText()
    {
        return Text;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public void setNO(int NO)
    {
        this.NO = NO;
    }

    public void setSender(long sender)
    {
        Sender = sender;
    }

    public void setReceiver(long receiver)
    {
        this.receiver = receiver;
    }

    public void setMyGroup(String myGroup)
    {
        MyGroup = myGroup;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }

    public int getNO()
    {
        return NO;
    }

    public long getSender()
    {
        return Sender;
    }

    public long getReceiver()
    {
        return receiver;
    }

    public String getMyGroup()
    {
        return MyGroup;
    }

    public Timestamp getTime()
    {
        return Time;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public Messages()
    {
    }
}
