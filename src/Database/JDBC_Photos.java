package Database;

import Entity.Photo;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JDBC_Photos
{
    private static ArrayList<Photo> photos = new ArrayList<>();

    private static Photo photo = new Photo();

    public Connection getConn()
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT&useSSL=false";
        String name = "root";
        String password = "357422";
        Connection conn = null;
        try
        {
            Class.forName(driver);
            conn = DriverManager.getConnection(URL, name, password);
        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    public void insert(long sender, long receiver, String path, boolean group, boolean flag)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into photos(Sender ,Receiver ,Path,MyGroup,Time,Flag) values(?,?,?,?,now(),?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setString(3, path);
            statement.setInt(4, group ? 1 : 0);
            statement.setInt(5, flag ? 1 : 0);
            statement.execute();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //查询某用户未读消息的个数，在登录时使用
    public Map register(long receiver)
    {
        Map<Long, Short> map = new HashMap<>();
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photos where Receiver=" + receiver + "&&Flag=0;";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                if (map.containsKey(rs.getLong(2)))
                {
                    short temp = map.get(rs.getLong(2));
                    map.put(rs.getLong(2), temp++);
                } else
                {
                    map.put(rs.getLong(2), (short) 1);
                }
            }


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    //查询私聊的未读消息，这个方法在私聊面板打开的时候使用

    public ArrayList<Photo> queryId(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photos where Receiver=" + receiver + "&&Sender=" + sender + "&&MyGroup=0&&Flag=0;";
        ArrayList<Photo> array = new ArrayList<>();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                photo.setPath(rs.getString(4));
                photo.setTime(rs.getTimestamp(6));
                array.add(photo);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        update(sender, receiver);
        return array;

    }

    //查询某人的大群未读消息，这个方法仅在打开群聊面板的时候使用
    public ArrayList<Photo> queryGroup(long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photos where Receiver=" + receiver + "&&MyGroup=1;";
        ArrayList<Photo> arrayList = new ArrayList<>();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                photo.setSender(rs.getLong(2));
                photo.setPath(rs.getString(4));
                photo.setTime(rs.getTimestamp(6));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        delete(receiver);
        return arrayList;
    }


    //查询大群的聊天记录
    public ArrayList<Photo> query()
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photos where MyGroup=1&&Receiver=100000000000;";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                photo.setSender(rs.getLong(2));
                photo.setPath(rs.getString(4));
                photo.setTime(rs.getTimestamp(6));
                photos.add(photo);
            }
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return photos;
    }

    //查询私聊记录
    public ArrayList<Photo> query(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photos where (Sender=" + sender + "&&Receiver=" + receiver +
                "&&MyGroup=0)||(Sender=" + receiver + "&&Receiver=" + sender + "&&MyGroup=0);";

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                photo.setSender(rs.getLong(2));
                photo.setReceiver(rs.getLong(3));
                photo.setPath(rs.getString(4));
                photo.setTime(rs.getTimestamp(6));
                photos.add(photo);
            }

            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        update(sender, receiver);
        return photos;
    }

    //更新一对一聊天为已读
    public void update(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "update photos set Flag=1 where Receiver=" + receiver + "&&Sender=" + sender + ";";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //相当于更新群聊记录为已读
    public void delete(long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "delete from photos where Receiver=" + receiver + "&&MyGroup=1;";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
