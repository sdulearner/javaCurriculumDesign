package Entity;

import java.sql.Timestamp;

public class Message implements Comparable
{
    protected long time;
    protected int NO;
    long Sender;
    long receiver;
    boolean Photo;

    boolean MyGroup;
    protected Timestamp Time;
    boolean flag;

    @Override
    public int compareTo(Object o)
    {
        int i = 0;
        if (o instanceof Message)
        {
            Message a = (Message) o;
            i = Long.compare(this.time, a.time);
        }
        return i;
    }
//
//    public String getText()
//    {
//        return "aha";
//    }

    public long getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }


    public int getNO()
    {
        return NO;
    }

    public void setNO(int NO)
    {
        this.NO = NO;
    }

    public long getSender()
    {
        return Sender;
    }

    public void setSender(long sender)
    {
        Sender = sender;
    }

    public long getReceiver()
    {
        return receiver;
    }

    public void setReceiver(long receiver)
    {
        this.receiver = receiver;
    }

    public boolean isPhoto()
    {
        return Photo;
    }

    public void setPhoto(boolean photo)
    {
        Photo = photo;
    }

    public boolean isMyGroup()
    {
        return MyGroup;
    }

    public void setMyGroup(boolean myGroup)
    {
        MyGroup = myGroup;
    }
}
