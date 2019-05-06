package Database;

import Entity.Photo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JDBC_Photos
{

    private static Photo photo = new Photo();

    public static Connection getConn()
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
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

    public void insert(long sender, long receiver, String extension, boolean group, boolean flag)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into photos(Sender ,Receiver ,Extension,MyGroup,Time,Flag) values(?,?,?,?,now(),?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setString(3, extension);
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
    public Map signIn(long receiver)
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
                photo.setNO(rs.getInt(1));
                photo.setExtension(rs.getString(4));
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
                photo.setNO(rs.getInt(1));
                photo.setSender(rs.getLong(2));
                photo.setExtension(rs.getString(4));
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
        ArrayList<Photo> photos = new ArrayList<>();
        String sql = "select*from photos where MyGroup=1&&Receiver=100000000000;";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                photo.setNO(rs.getInt(1));
                photo.setSender(rs.getLong(2));
                photo.setExtension(rs.getString(4));
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
        ArrayList<Photo> photos = new ArrayList<>();
        String sql = "select*from photos where (Sender=" + sender + "&&Receiver=" + receiver +
                "&&MyGroup=0)||(Sender=" + receiver + "&&Receiver=" + sender + "&&MyGroup=0);";

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                photo.setNO(rs.getInt(1));
                photo.setSender(rs.getLong(2));
                photo.setReceiver(rs.getLong(3));
                photo.setExtension(rs.getString(4));
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

    public static int count(long sender, long receiver)
    {
        Connection conn = getConn();
        String sql = "select*from photos where Receiver=" + receiver + "&&Sender=" + sender + ";";
        int count = 0;
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                count++;
            }
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return count;
    }

    //得到sender发给receiver的最新的图片的序号
    public int getNO(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        int count = count(sender, receiver);
        int result = 0;
        String sql = "select*from photos where Receiver=" + receiver + "&&Sender=" + sender + ";";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            for (int i = 0; i < count; i++)
            {
                rs.next();
            }
            result = rs.getInt(1);
            rs.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    //根据NO获取对应的扩展名
    public String getExtension(int no)
    {
        Connection conn = getConn();
        String extension = null;
        PreparedStatement statement;
        String sql = "select*from photos where NO=" + no + ";";
        try
        {
            statement = conn.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            rs.next();
            extension = rs.getString(4);
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return extension;
    }
}
