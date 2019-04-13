package Entity;

public class Document
{

    private int No;
    private String Name;

    public Document()
    {
    }

    public Document(int no, String name)
    {
        No = no;
        Name = name;
    }

    @Override
    public String toString()
    {
        return "Document{" +
                "No=" + No +
                ", Name='" + Name + '\'' +
                '}';
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
