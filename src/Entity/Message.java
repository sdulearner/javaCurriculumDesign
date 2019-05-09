package Entity;

import java.sql.Timestamp;

public class Message
{
    protected int NO;
    private long Sender;
    private long receiver;
    private boolean Photo;//是否为图片
    private boolean MyGroup;//是否为群聊消息
    private String content;
    protected Timestamp Time;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "NO=" + NO +
                ", Sender=" + Sender +
                ", receiver=" + receiver +
                ", Photo=" + Photo +
                ", MyGroup=" + MyGroup +
                ", content='" + content + '\'' +
                ", Time=" + Time +
                '}';
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

    public Timestamp getTime()
    {
        return Time;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }
}
