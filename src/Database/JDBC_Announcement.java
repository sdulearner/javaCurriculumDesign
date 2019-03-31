package Database;

import Entity.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_Announcement
{

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
    public boolean insert()
    {
        Connection conn = getConn();

        String sql = "insert into students values(?,?,?,?,?,?);";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            statement.setLong(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getNickname());
            statement.setString(4, student.getSex());
            statement.setString(5, student.getPassword());
            statement.setInt(6, student.getAdministrator());
            statement.executeUpdate();
            conn.close();
            statement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }



}
