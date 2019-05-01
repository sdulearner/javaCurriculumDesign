package Entity;

import java.sql.Timestamp;

public class Photo extends Message
{
    private int NO;
    private long Sender;
    private long receiver;
    private String Path;
    private boolean Photo;

    private boolean MyGroup;
    private Timestamp Time;
    private boolean flag;

    public void setMyGroup(boolean myGroup)
    {
        MyGroup = myGroup;
    }

    public String getPath()
    {
        return Path;
    }

    public void setPath(String path)
    {
        Path = path;
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

    public Timestamp getTime()
    {
        return Time;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
        super.time =time.getTime();
    }
}
