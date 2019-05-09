package Database;

import Entity.Document;

import java.sql.*;

public class JDBC_Documents
{
    private static Document document = new Document();

    public static Connection getConn()
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
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

    public static int count()
    {
        Connection conn = getConn();
        String sql = "select*from documents;";
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


    public void insert(String name, long size)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into documents (Name,Size)values (?,?);";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setLong(2, size);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean judge(String name)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql = "select *from documents where Name ='" + name.trim() + "';";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            long count = 0;
            while (rs.next())
            {
                count++;
            }
            if (count == 0) b = false;
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return b;
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
            rs.next();
            document.setNo(no);
            document.setName(rs.getString(2));
            document.setSize(rs.getLong(3));

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
        String sql2 = " alter table `documents` drop `NO`;";
        String sql3 = " alter table `documents` add `NO` tinyint not null first;";
        String sql4 = " alter table `documents`modify column `NO` tinyint not null auto_increment,add primary key(NO);";
        PreparedStatement statement;

        try
        {
            statement = conn.prepareStatement(sql1);
            statement.execute();
            statement.close();

            statement = conn.prepareStatement(sql2);
            statement.execute();
            statement.close();

            statement = conn.prepareStatement(sql3);
            statement.execute();
            statement.close();

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
