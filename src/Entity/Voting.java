package Entity;

import java.sql.Timestamp;

public class Voting  implements  java.io.Serializable
 {
    private int NO_1;//NO_1与vote表里的NO对应
    private int NO_2;//NO_2与options表里的NO对应
    private int[] votes;
    private String Opinion;

    public Voting()
    {
    }

    public Voting(int NO_1, int NO_2, int[] votes, String opinion)
    {
        this.NO_1 = NO_1;
        this.NO_2 = NO_2;
        this.votes = votes;
        Opinion = opinion;
    }

    public void setNO_1(int NO_1)
    {
        this.NO_1 = NO_1;
    }

    public void setNO_2(int NO_2)
    {
        this.NO_2 = NO_2;
    }

    public void setVotes(int[] votes)
    {
        this.votes = votes;
    }

    public void setOpinion(String opinion)
    {
        Opinion = opinion;
    }

    public int getNO_1()
    {
        return NO_1;
    }

    public int getNO_2()
    {
        return NO_2;
    }

    public int[] getVotes()
    {
        return votes;
    }

    public String getOpinion()
    {
        return Opinion;
    }
}
