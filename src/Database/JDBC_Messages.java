package Database;

import Entity.Messages;

import java.sql.*;
import java.util.ArrayList;

public class JDBC_Messages
{
    private static ArrayList<Messages> messages1 = new ArrayList<>();
    private static ArrayList<Messages> messages2 = new ArrayList<>();
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

    public void update(long id)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "update messages set Flag=1 where Receiver=" + id + ";";
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

    public ArrayList[] query(long id)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql1 = "select*from messages where Sender=" + id + ";";
        String sql2 = "select*from messages where Receiver=" + id + ";";
        try
        {
            statement = conn.prepareStatement(sql1);
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
                messages1.add(message);
            }
            rs.close();
            statement.close();
            statement = conn.prepareStatement(sql2);
            rs = statement.executeQuery();
            while (rs.next())
            {
                message.setNO(rs.getInt(1));
                message.setSender(rs.getLong(2));
                message.setReceiver(rs.getLong(3));
                message.setText(rs.getString(4));
                message.setMyGroup(rs.getString(5));
                message.setTime(rs.getTimestamp(6));
                message.setFlag(rs.getInt(7) == 1);
                messages2.add(message);
            }
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return new ArrayList[]{messages1, messages2};
    }


}
