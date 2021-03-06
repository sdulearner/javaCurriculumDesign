package Database;


import Entity.Student;

import java.sql.*;

public class JDBC_Students
{


    private Student student = new Student();

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
    //以上是dalao的代码 就目前的进度来看我需要些一个insert方法，
    // 以及一个检索用户Id与密码是否匹配的方法，也就是judgePassword方法。

    // 先写第一个insert方法。
    public long[] getId()
    {
        Connection conn = getConn();
        long[] array = new long[count()];
        String sql = "select*from students";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            int count = 0;
            while (rs.next())
            {
                array[count] = rs.getLong(1);
                count++;
            }
            rs.close();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return array;

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

    public boolean insert(Student student)
    {
        Connection conn = getConn();
        if (judgeId(student.getId()))
        {
            return false;
        }
        String sql = "insert into students values(?,?,?,?,?,?);";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            statement.setLong(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getNickname());
            statement.setString(4, student.getSex());
            statement.setString(5, student.getPassword());
            statement.setInt(6, student.getAdministrator());
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }


    // 再写第二个judgePassword方法
    public boolean judgePassword(long Id, String password)
    {
        Connection conn = getConn();
        String sql = "select *from students where Id =" + Id + ";";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (password.trim().equals(rs.getString(5))) return true;
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    //现在我想了一想，，再加一个用来判断某一个账号Id是否存在的方法会更好，我叫它judgeId方法。
    public boolean judgeId(long Id)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql = "select *from students where Id =" + Id + ";";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            long count = 0;
            while (rs.next())
            {
                count = rs.getLong(1);
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

    //我又回来了，，经过与dalao的“亲切”交流，我对于现在要干的事情有了一定的思路，
    //现在目前的思路是用socket  实现各种功能
    //
    // 接下来实现提取信息的方法，先写一个提取所有信息的方法，
    public StringBuilder getAll()
    {
        Connection conn = getConn();
        String sql = "select*from students;";
        PreparedStatement statement;
        StringBuilder builder = new StringBuilder();
        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
//            ResultSet rs=Test.rawQuery(sql);
//            int column = rs.getMetaData().getColumnCount();
            while (rs.next())
            {
                builder.append(rs.getLong(1)).append("\n");
                builder.append(rs.getString(2)).append("\n");
                builder.append(rs.getString(3)).append("\n");
                builder.append(rs.getString(4)).append("\n");
                builder.append(rs.getString(5)).append("\n");
                builder.append(rs.getInt(6)).append("\n");
//                System.out.println(builder);
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return builder;
    }

    //     再写一个根据Id提取信息的方法
    public Student getInformation(long id)
    {
        Connection conn = getConn();
        String sql = "select*from students where Id = " + id + ";";
        PreparedStatement statement;

        try
        {
            statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
//            int column = rs.getMetaData().getColumnCount();
            while (rs.next())
            {
                student.setId(rs.getLong(1));
                student.setName(rs.getString(2));
                student.setNickname(rs.getString(3));
                student.setSex(rs.getString(4));
                student.setPassword(rs.getString(5));
                student.setAdministrator(rs.getInt(6));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return student;
    }

    public int update(Student student)
    {
        int i = 0;
        Connection conn = getConn();
        String sql = "update students set Nickname ='" + student.getNickname() + "',Password=" + student.getPassword() + " where Id=" + student.getId() + ";";
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql);
            i = statement.executeUpdate();
            statement.close();
            conn.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return i;
    }

}