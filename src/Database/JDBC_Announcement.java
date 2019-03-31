package Database;

import Entity.Announcement;
import Entity.Student;

import java.sql.*;

public class JDBC_Announcement
{
    private Announcement announcement = new Announcement();

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
            conn = (Connection) DriverManager.getConnection(URL, name, password);
        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }

    public boolean insert(Announcement a)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql1 = "select *from announcement where NO =" + a.getNO() + ";";
        String sql2 = "insert into announcement values(?,?,?,?,now());";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql1);
            ResultSet rs = statement.executeQuery();
            long count = 0;
            while (rs.next())
            {
                count = rs.getInt(1);
            }
            if (count == 0) b = false;
            conn.close();
            statement.close();
            rs.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (b) return false;
        conn = getConn();
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql2);
            statement.setLong(1, a.getNO());
            statement.setString(2, a.getName());
            statement.setString(3, a.getTitle());
            statement.setString(4, a.getText());

            statement.executeUpdate();
            conn.close();
            statement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public Announcement query(int No)
    {
        Connection conn = getConn();
        String sql = "select*from announcement where NO =" + No + ";";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                announcement.setNO(rs.getInt(1));
                announcement.setName(rs.getString(2));
                announcement.setTitle(rs.getString(3));
                announcement.setText(rs.getString(4));
                announcement.setTime(rs.getTimestamp(5));
            }
            conn.close();
            statement.close();
            rs.close();


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return announcement;
    }


}
