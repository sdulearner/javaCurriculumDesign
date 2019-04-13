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

    public boolean judgeTitle(String title)
    {
        boolean b = true;
        Connection conn = getConn();
        String sql = "select*from vote where Title='" + title + "';";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            long count = 0;
            while (rs.next())
            {
                count = rs.getInt(1);
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

    public boolean start(String name, String title, String[] options)
    {
        int a = options.length;
        StringBuilder builder = new StringBuilder();

        Connection conn = getConn();

        if (judgeTitle(title)) return false;
        String sql1 = "insert into vote (Name,Title) values(?,?);";
        for (int i = 0; i < a; i++)
        {
            builder.append("`" + options[i] + "`" + " tinyint(1),");
        }
        String sql2 = "create table `options_" + title + "`(NO tinyint pimary key auto_increment not null," + builder + "Time timestamp,opinion tinytext );";
//        String sql3 = "insert into options" + no + "values(1,?)";
        PreparedStatement statement;
        try
        {
            statement = (PreparedStatement) conn.prepareStatement(sql1);
//            statement.setInt(1, no);
            statement.setString(1, name);
            statement.setString(2, title);
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


            statement.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        voting(new Voting(1, new int[options.length], null));
        return true;
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

    public void voting(Voting voting)
    {
        String title = "";
        int length = voting.getVotes().length;
        String[] options = new String[length];
        StringBuilder builder1 = new StringBuilder();

        StringBuilder builder2 = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            builder2.append("?,");
        }
        Connection conn = getConn();
        String sql1 = "select*from vote where NO=" + voting.getNO() + ";";
//        System.out.println(sql1);
        PreparedStatement statement;
        try
        {
            statement = conn.prepareStatement(sql1);
            ResultSet rs = statement.executeQuery();
            rs.next();
            title = rs.getString(3);
            rs.close();

            String sql = "desc `options_" + title + "`;";
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            rs.next();
            int counter = 0;
            while (rs.next() && counter < length)
            {
                options[counter] = rs.getString(1);
                counter++;
            }
            rs.close();
            statement.close();
            for (int i = 0; i < length; i++)
            {
                builder1.append("`"+options[i]+"`,");
            }


            String sql2 = "insert into `options_" + title + "` (" + builder1 + "Time,opinion)values (" + builder2 + "now(),?);";
            statement = conn.prepareStatement(sql2);


            for (int i = 0; i < length; i++)
            {
                statement.setInt(i + 1, voting.getVotes()[i]);
            }
            statement.setString(length + 1, voting.getOpinion());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public  Result calculate(int no)
    {
//        int rows = 0;
        int[] votes;
        Connection conn = getConn();
        String sql1 = "select*from vote where NO=" + no + ";";
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
            String sql2 = "select*from `options_" + result.getTitle() + "`;";
            String sql3 ="desc `options_" + result.getTitle() + "`;";

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
