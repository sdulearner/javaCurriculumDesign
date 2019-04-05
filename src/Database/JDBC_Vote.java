package Database;

import Entity.Result;
import Entity.Voting;

import java.sql.*;
import java.util.ArrayList;

public class JDBC_Vote
{
    private static Result result = new Result();

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

    public boolean judgeNo(int no)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql = "select *from vote where NO =" + no + ";";
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

    public boolean start(int no, String name, String title, String[] options)
    {
        int a = options.length;

        Connection conn = getConn();

        String sql1 = "insert into vote values(?,?,?);";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < a; i++)
        {
            builder.append("`" + options[i] + "`" + " tinyint(1),");
        }
        String sql2 = "create table options" + no + "(NO tinyint primary key auto_increment not null," + builder + "Time timestamp,opinion tinytext );";
//        String sql3 = "insert into options" + no + "values(1,?)";
        PreparedStatement statement;
        if (judgeNo(no)) return false;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql1);
            statement.setInt(1, no);
            statement.setString(2, name);
            statement.setString(3, title);
            statement.executeUpdate();

            statement = (PreparedStatement) conn.prepareStatement(sql2);
            statement.execute();

//            statement = (PreparedStatement) conn.prepareStatement(sql3);
//            statement.setInt(1, 1);
//            for (int i = 0; i < a; i++)
//            {
//                statement.setInt(i + 2, 0);
//            }
//            statement.

            voting(new Voting(no, 1, new int[options.length], null));

            statement.close();
            conn.close();


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args)
    {
        calculate(1);
    }

    public void voting(Voting voting)
    {
        int length = voting.getVotes().length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            builder.append("?,");
        }
        Connection conn = getConn();
        String sql = "insert into options" + voting.getNO_1() + " values (?," + builder + "now(),?);";
//        System.out.println(sql);
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            statement.setInt(1, voting.getNO_2());
            for (int i = 0; i < length; i++)
            {
                statement.setInt(i + 2, voting.getVotes()[i]);
            }
            statement.setString(length + 2, voting.getOpinion());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Result calculate(int no)
    {
        int rows = 0;
        int[] votes;
        Connection conn = getConn();
        String sql1 = "select*from vote where NO=" + no + ";";
        String sql2 = "select*from options" + no + ";";
        String sql3 = "desc options" + no + ";";
//        String sql3 = "select count(*) from options" + no + ";";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql1);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {

                result.setNo(rs.getInt(1));
                result.setName(rs.getString(2));
                result.setTitle(rs.getString(3));
            }

//            statement = (PreparedStatement) conn.prepareStatement(sql3);
//            rs = statement.executeQuery();
//            while (rs.next()) rows = rs.getInt(1);
            statement = (PreparedStatement) conn.prepareStatement(sql2);
            rs = statement.executeQuery();
            int columns = rs.getMetaData().getColumnCount() - 3;
            votes = new int[columns];
            while (rs.next())
            {
                for (int i = 0; i < columns; i++)
                {
                    votes[i] += rs.getInt(i + 2);
                }
            }
            result.setVotes(votes);


            statement = (PreparedStatement) conn.prepareStatement(sql2);
            rs = statement.executeQuery();
            ArrayList<Timestamp> time = new ArrayList<>();
            ArrayList<String> opinion = new ArrayList<>();
            while (rs.next())
            {
                time.add(rs.getTimestamp(columns + 2));
                opinion.add(rs.getString(columns + 3));
            }
            result.setOpinions(opinion.toArray(new String[opinion.size()]));
            result.setTime(time.toArray(new Timestamp[time.size()]));


            statement = conn.prepareStatement(sql3);
            rs = statement.executeQuery();
            String[] options = new String[columns];
            int counter = 0;
            rs.next();
            while (rs.next() && counter < columns)
            {
                options[counter] = rs.getString(1);
                counter++;
            }
            result.setOptions(options);

            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
