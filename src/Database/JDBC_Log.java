package Database;

import java.sql.*;

/**
 * @description: 对管理员日志操作的类
 * @author: sdulearner
 * @create: 2019-05-04 10:33
 **/

public class JDBC_Log
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
            conn = DriverManager.getConnection(URL, name, password);
        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return conn;
    }


    public static void insert(long id, String name, String operation)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into log (Operator ,Name ,Operation,Time ) values(?,?,?,now())";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.setString(2, name);
            statement.setString(3, operation);
            statement.executeUpdate();

            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static int count()
    {
        Connection conn = getConn();
        String sql = "select*from students;";
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

    public static ResultSet query()
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from log;";
        ResultSet rs = null;
        try
        {
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }
}
