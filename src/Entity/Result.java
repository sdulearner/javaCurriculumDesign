package Entity;

import java.sql.Timestamp;
import java.util.Arrays;

public class Result implements  java.io.Serializable
{
    private int no;//vote表的NO
    private String name;
    private String title;
    private  String []options;
    private int []votes;
    private String [] opinions;
    private Timestamp [] time ;

    @Override
    public String toString()
    {
        return "Result{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", options=" + Arrays.toString(options) +
                ", votes=" + Arrays.toString(votes) +
                ", opinions=" + Arrays.toString(opinions) +
                ", time=" + Arrays.toString(time) +
                '}';
    }

    public Result(int no, String name, String title, String[] options, int[] votes, String[] opinions, Timestamp[] time)
    {
        this.no = no;
        this.name = name;
        this.title = title;
        this.options = options;
        this.votes = votes;
        this.opinions = opinions;
        this.time = time;
    }

    public void setOptions(String[] options)
    {
        this.options = options;
    }

    public String[] getOptions()
    {
        return options;
    }

    public void setOpinions(String[] opinions)
    {
        this.opinions = opinions;
    }

    public String[] getOpinions()
    {
        return opinions;
    }

    public void setNo(int no)
    {
        this.no = no;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setVotes(int[] votes)
    {
        this.votes = votes;
    }

    public void setTime(Timestamp[] time)
    {
        this.time = time;
    }

    public int getNo()
    {
        return no;
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public int[] getVotes()
    {
        return votes;
    }

    public Timestamp[] getTime()
    {
        return time;
    }

    public Result()
    {
    }
}
