package Database;

import Entity.Document;

import java.sql.*;

public class JDBC_Documents
{
    private Document document = new Document();

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


    public void insert(String name)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into documents (Name)values (?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Document query(int no)
    {
        Connection conn = getConn();
        String sql = "select*from documents where NO=" + no + ";";
        PreparedStatement statement;

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            document.setNo(no);
            document.setName(rs.getString(2));
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return document;
    }

    public void delete(int no)
    {

        Connection conn = getConn();
        String sql1 = "delete from  documents where NO=" + no + ";";
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
