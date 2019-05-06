package Entity;

public class Document
{

    private int No;
    private String Name;
    private long size;

    public Document()
    {
    }

    public Document(int no, String name, long size)
    {
        No = no;
        Name = name;
        this.size = size;
    }
    @Override
    public String toString()
    {
        return "Document{" +
                "No=" + No +
                ", Name='" + Name + '\'' +
                ", size=" + size +
                '}';
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public void setNo(int no)
    {
        No = no;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public int getNo()
    {
        return No;
    }

    public String getName()
    {
        return Name;
    }
}
