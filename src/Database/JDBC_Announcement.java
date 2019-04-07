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

    public static int count()
    {
        Connection conn = getConn();
        String sql = "select*from announcement;";
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

    public boolean judgeTitle(String title)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql = "select *from announcement where Title =`" + title + "`;";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            long count = 0;
            while (rs.next())
            {
                count = rs.getLong(1);
            }
            if (count == 0) b = false;
            conn.close();
            statement.close();
            rs.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    public boolean insert(Announcement a)
    {
        Connection conn = getConn();
        String sql = "insert into announcement (Name ,Title ,Text,Time )values(?,?,?,now());";
        PreparedStatement statement;
        if (judgeTitle(a.getTitle())) return false;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            statement.setString(1, a.getName());
            statement.setString(2, a.getTitle());
            statement.setString(3, a.getText());

            statement.executeUpdate();
            statement.close();
            conn.close();
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

    public void delete(int no)
    {
        Connection conn = getConn();
        String sql1 = "delete from  announcement where NO=" + no + ";";
        String sql2 = " alter table `announcement` drop `NO`;";
        String sql3 = " alter table `announcement` add `NO` int not null first;";
        String sql4 = " alter table `announcement`modify column `NO` int not null auto_increment,add primary key(NO);";
        PreparedStatement statement;

        try
        {
            statement = conn.prepareStatement(sql1);
            statement.execute();
            statement = conn.prepareStatement(sql2);
            statement.execute();
            statement = conn.prepareStatement(sql3);
            statement.execute();
            statement = conn.prepareStatement(sql4);
            statement.execute();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}
