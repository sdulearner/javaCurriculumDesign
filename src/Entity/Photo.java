package Entity;

import java.sql.Timestamp;

public class Photo extends Message
{
    private String Extension;

    public String getExtension()
    {
        return Extension;
    }

    public void setExtension(String extension)
    {
        Extension = extension;
    }

    public void setTime(Timestamp time)
    {
        Time = time;
        super.time = time.getTime();
    }
}
