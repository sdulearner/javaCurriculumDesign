package Database;

import Entity.Messages;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.sql.*;
import java.util.ArrayList;

public class JDBC_Messages
{
    private static ArrayList<Messages> messages = new ArrayList<>();

    private static Messages message = new Messages();

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

    public void insert(long sender, long receiver, String text, boolean group, boolean flag)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into messages(Sender ,Receiver ,Text,MyGroup,Time,Flag) values(?,?,?,?,now(),?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setString(3, text);
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

    //查询私聊的未读消息，这个方法在私聊面板打开的时候使用

    public ArrayList<Messages> queryId(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where Receiver=" + receiver + "&&Sender=" + sender + "&&Mygroup=0;";
        ArrayList<Messages> array = new ArrayList<>();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                message.setText(rs.getString(4));
                message.setTime(rs.getTimestamp(6));
                array.add(message);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        update(sender, receiver);
        return array;

    }

    //查询某人的大群未读消息，这个方法仅在打开群聊面板的时候使用
    public ArrayList<Messages> queryGroup(long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where Receiver=" + receiver + "&&MyGroup=1&&;";
        ArrayList<Messages> arrayList = new ArrayList<Messages>();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                message.setSender(rs.getInt(2));
                message.setText(rs.getString(4));
                message.setTime(rs.getTimestamp(6));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        delete(receiver);
        return arrayList;
    }


    //查询大群的聊天记录
    public ArrayList<Messages> query()
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where MyGroup=1&&Receiver=1;";
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                message.setSender(rs.getLong(2));
                message.setText(rs.getString(4));
                message.setTime(rs.getTimestamp(6));
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

    //查询私聊记录
    public ArrayList<Messages> query(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from messages where (Sender=" + sender + "&&Receiver=" + receiver +
                "&&MyGroup=0)||(Sender=" + receiver + "&&Receiver=" + sender + "&&MyGroup=0);";

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                message.setSender(rs.getLong(2));
                message.setReceiver(rs.getLong(3));
                message.setText(rs.getString(4));
                message.setTime(rs.getTimestamp(6));
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

    //更新一对一聊天为已读
    public void update(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "update messages set Flag=1 where Receiver=" + receiver + "&&Sender=" + sender + ";";
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
        String sql = "delete from messages where Receiver=" + receiver + "&&MyGroup=1;";
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
