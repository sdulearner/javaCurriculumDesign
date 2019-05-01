package Entity;

public class Message implements Comparable
{
    protected long time;

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
}
