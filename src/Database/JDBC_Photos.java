package Database;

import java.sql.*;

public class JDBC_Photos
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

    public void insert(long sender, long receiver, boolean group, boolean flag)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into photo (Sender, Receiver ,MyGroup,Time,Flag)values(?,?,?,now(),?)";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setInt(3, group ? 1 : 0);
            statement.setInt(4, flag ? 1 : 0);
            statement.executeUpdate();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getNO(long sender, long receiver)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from photo where Sender=" + sender + "&&Receiver=" + receiver + ";";
        int temp = 0;
        int result = 0;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                temp++;
            }
            rs.close();
            statement.close();
            conn.close();

            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            for (int i = 0; i < temp; i++)
            {
                rs.next();
            }
            result = rs.getInt(1);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }


//    public void  query(int no)
//    {
//        Connection conn = getConn();
//        PreparedStatement statement;
//        String sql = "select*from photo where NO=" + no + ";";
//        try{
//            statement=conn.prepareStatement(sql);
//            ResultSet rs=statement.executeQuery();
//            rs.next();
//
//
//
//
//        } catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//    }

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
}
