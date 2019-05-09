package Entity;

import java.sql.Timestamp;

public class Text extends Message
{

    private String Text;

    public Text(String readLine)
    {
        this.Text = readLine;
    }

    public Text()
    {
    }


    public String getText()
    {
        return Text;
    }

    public void setText(String text)
    {
        Text = text;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
    }


}
