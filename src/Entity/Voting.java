package Entity;

import java.io.Serializable;

public class Voting implements Serializable
{
    private static final long serialVersionUID = 3L;

    private int NO;//总表的序号
    private int[] votes;
    private String Opinion;

    public Voting()
    {
    }

    public Voting(int NO, int[] votes, String opinion)
    {
        this.NO = NO;

        this.votes = votes;
        Opinion = opinion;
    }

    public void setNO(int NO)
    {
        this.NO = NO;
    }

    public void setVotes(int[] votes)
    {
        this.votes = votes;
    }

    public void setOpinion(String opinion)
    {
        Opinion = opinion;
    }

    public int getNO()
    {
        return NO;
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
