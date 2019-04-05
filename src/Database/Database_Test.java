package Database;


import Entity.Announcement;
import Entity.Student;
import Entity.Voting;


import java.sql.Timestamp;
import java.util.Date;

public class Database_Test
{
    public static void main(String[] args)
    {

        JDBC_Students jdbc = new JDBC_Students();
        JDBC_Announcement announcement = new JDBC_Announcement();
        JDBC_Vote vote = new JDBC_Vote();
//        jdbc.insert(new Student(201800301185L, "孙振瑜", "java垃圾", "请选择", "88888888", 0));
//        jdbc.insert(new Student(201800301175L ,"王永超","java大佬","女","12345678",0));
//        System.out.println(JDBC_Students.judgeId(201800301185L));
//        System.out.println(jdbc.getAll());
//        System.out.println(jdbc.getInformation(201800301175L));
//        System.out.println(jdbc.update(new Student(201800301165L ,"孙振瑜","垃圾","请选择","88888888",0)));

//        System.out.prinln(announcement.insert(new Announcement(4, "孙振瑜", "这是题目",
//                "这是内容", new Timestamp(System.currentTimeMillis()))));
//        System.out.println(new Timestamp(System.currentTimeMillis()));
//        String[] options = {"这是选项1", "option2", "This is option3"};
//        System.out.println(vote.start(2,"learner","This is title.",options));
//        System.out.println(announcement.query(2));
        int[] array = {1, 0, 1};
//        vote.voting(new Voting(1,3, array, "Good！"));
        System.out.println(vote.calculate(1));
    }

}
