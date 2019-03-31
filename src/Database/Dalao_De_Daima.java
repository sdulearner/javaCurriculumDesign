package Database;

import Entity.Student;

import java.sql.*;

public class Dalao_De_Daima
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

        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return conn;
    }

    public static int insert(Student student)
    {
        Connection conn = getConn();
        int i = 0;
        String sql = "insert into students(Id,Name ,Sex) values(?,?,?);";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            statement.setLong(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getSex());
            i = statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return i;
    }

    public static int update(Student student)
    {
        Connection conn = getConn();
        int i = 0;
        String sql = "update students set Id='" + student.getId() + "' where Name='" + student.getName() + "'";
        PreparedStatement pstmt;
        try
        {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }

    public static Integer getAll()
    {
        Connection conn = getConn();
        String sql = "select * from students";
        PreparedStatement pstmt;
        try
        {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            while (rs.next())
            {
                for (int i = 1; i <= col; i++)
                {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8))
                    {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
            }
            System.out.println("============================");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static int delete(String name)
    {
        Connection conn = getConn();
        int i = 0;
        String sql = "delete from students where Name='" + name + "'";
        PreparedStatement pstmt;
        try
        {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }
}
