package Entity;

import java.sql.Timestamp;

public class Photo extends Message
{
    private String extension;

    public Photo(String readLine)
    {
        super();
    }

    public Photo()
    {

    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
        super.time = time.getTime();
    }
}
