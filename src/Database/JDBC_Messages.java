package Database;

import Entity.Messages;

import java.sql.*;
import java.util.ArrayList;

public class JDBC_Messages
{
    private static ArrayList<Messages> messages = new ArrayList<>();

    private static Messages message = new Messages();

    public static Connection getConn()
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

    public static void insert(long sender, long receiver, String text, String group, boolean flag)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into messages(Sender ,Receiver ,Text,MyGroup,Time ,Flag) values(?,?,?,?,now(),?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setString(3, text);
            statement.setString(4, group);
            statement.setInt(5, flag ? 1 : 0);
            statement.execute();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //查询未读消息
    public ArrayList<Messages> queryId(long id)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where Receiver=" + id + " and Flag=0;";
        ArrayList<Messages> array = new ArrayList<>();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                message.setNO(rs.getInt(1));
                message.setSender(rs.getLong(2));
                message.setReceiver(rs.getLong(3));
                message.setText(rs.getString(4));
                message.setMyGroup(rs.getString(5));
                message.setTime(rs.getTimestamp(6));
                message.setFlag(false);
                array.add(message);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return array;

    }

    //查询与某人的聊天记录
    public static ArrayList<Messages> query(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where (Sender=" + sender + "&&Receiver=" + receiver +
                "&&MyGroup<=>NULL)||(Sender=" + receiver + "&&Receiver=" + sender + "&&MyGroup<=>NULL);";

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                message.setNO(rs.getInt(1));
                message.setSender(rs.getLong(2));
                message.setReceiver(rs.getLong(3));
                message.setText(rs.getString(4));
                message.setMyGroup(rs.getString(5));
                message.setTime(rs.getTimestamp(6));
                message.setFlag(rs.getInt(7) == 1);
                messages.add(message);
            }

            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        update(sender, receiver);
        return messages;
    }

    //查询某群组的聊天记录
    public static ArrayList<Messages> query(String name)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where MyGroup=" + name + ";";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                message.setNO(rs.getInt(1));
                message.setSender(rs.getLong(2));
                message.setReceiver(rs.getLong(3));
                message.setText(rs.getString(4));
                message.setMyGroup(rs.getString(5));
                message.setTime(rs.getTimestamp(6));
                message.setFlag(rs.getInt(7) == 1);
                messages.add(message);
            }
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return messages;

    }

    //更新一对一聊天为已读
    public static void update(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "update messages set Flag=1 where Receiver=" + receiver + " andSender=" + sender + ";";
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
    //  更新某人的群聊记录为已读

    public void update(long id, String name)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "update messages set Flag=1 where Receiver=" + id + " MyGroup=" + name + ";";
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
