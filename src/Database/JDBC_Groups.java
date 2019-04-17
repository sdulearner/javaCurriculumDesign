package Database;

import Entity.MyGroup;

import java.sql.*;
import java.util.Arrays;

public class JDBC_Groups
{
    private static MyGroup myGroup = new MyGroup();

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

    public void insert(String name, long[] id)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "insert into mygroups (Name, String_Id,Time )values(?,?,now());";
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, Arrays.toString(id));
            statement.execute();

            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }



    public static MyGroup query(int no)
    {
        Connection conn = getConn();
        PreparedStatement statement;
        String sql = "select*from mygroups where NO=" + no + ";";

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                myGroup.setName(rs.getString(2));
                String[] temp = rs.getString(3).replace("[", "")
                        .replace("]", "").split(", ");
                long[] id = new long[temp.length];
                for (int i = 0; i < id.length; i++)
                {
                    id[i] = Long.parseLong(temp[i]);
                }
                myGroup.setId(id);
                myGroup.setTime(rs.getTimestamp(4));
                rs.close();
                statement.close();
                conn.close();
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return myGroup;
    }

    public static void delete(int no)
    {

        Connection conn = getConn();
        String sql1 = "delete from  mygroups where NO=" + no + ";";
        String sql2 = " alter table `mygroups` drop `NO`;";
        String sql3 = " alter table `mygroups` add `NO` bigint not null first;";
        String sql4 = " alter table `mygroups`modify column `NO` int not null auto_increment,add primary key(NO);";
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
