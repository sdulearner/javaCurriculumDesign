package Database;

import java.sql.*;

public class Test
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

    public static void execSQL(String sql)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            statement.execute();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static ResultSet rawQuery(String sql) throws SQLException
    {
        Connection conn = getConn();
        PreparedStatement statement;
        ResultSet rs = null;
        try
        {
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            statement.close();
            conn.close();


        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            return rs;
        }
    }


}
