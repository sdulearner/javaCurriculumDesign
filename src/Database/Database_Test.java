package Database;


import java.sql.SQLException;
import java.util.Map;

public class Database_Test
{
    public static void main(String[] args) throws SQLException
    {

        JDBC_Students jdbc_students = new JDBC_Students();
        JDBC_Announcement jdbc_announcement = new JDBC_Announcement();
        JDBC_Vote jdbc_vote = new JDBC_Vote();
        JDBC_Documents jdbc_documents = new JDBC_Documents();
        JDBC_Photos jdbc_photos = new JDBC_Photos();
        JDBC_Texts jdbc_texts = new JDBC_Texts();
//
//
//        jdbc_students.insert(new Student(201800301185L, "啊哈", "java大佬", "请选择", "88888888", 0));
//
//        jdbc_students.insert(new Student(201800301176L, "王永超", "java大佬", "女", "12345678", 0));
//
        System.out.println(jdbc_students.count());

//        System.out.println(jdbc_students.judgeId(201800301185L));
//
//        System.out.println(jdbc_students.getAll());
//
//        System.out.println(jdbc_students.getInformation(201800301175L));
//
//        System.out.println(jdbc_students.update(new Student(201800301165L, "孙振瑜", "垃圾", "请选择", "88888888", 0)));
//
//
//        jdbc_announcement.delete(6);
//
//        System.out.println(new Timestamp(System.currentTimeMillis()));
//
//        String[] options = {"这是选项1", "option2", "This is option3"};
////
//        System.out.println(jdbc_vote.start("learner", "title", options));
//        int[] array = {0, 1, 1};
//        jdbc_vote.voting(new Voting(1, array, "Good！"));
//        System.out.println(jdbc_vote.calculate(1));

//        jdbc_students.update(new Student(201800301165L, "孙振瑜", "javaAha", "请选择", "12345678", 0));
//        System.out.println(jdbc_announcement.insert(new Announcement("孙振瑜", "这是题目",
//                "这是内容", new Timestamp(System.currentTimeMillis()))));

//        System.out.println(jdbc_announcement.judgeTitle("This is title  ".trim()));
//        System.out.println(jdbc_announcement.query(2));
//        System.out.println(jdbc_documents.insert("test.txt",10241024) );

//        System.out.println(jdbc_documents.query(2));
//        jdbc_documents.delete(1);
//        jdbc_texts.insert(201800301185L,201800301165L,"ahaha",true,false);
//        jdbc_texts.update(201800301185L,201800301165L);
//        System.out.println(jdbc_texts.signIn(201800301165L).toString());
//        jdbc_photos.insert(201800301165L,201800301175L,"aha",false,false);
//        System.out.println(jdbc_photos.getExtension(3));
//        JDBC_Log.insert(201800301175L, "啊哈怪", "aha");
//        ResultSet rs = JDBC_Log.query();
//        while (rs.next())
//        {
//            System.out.println(rs.getInt(1));
//            System.out.print(rs.getLong(2) + "\t");
//            System.out.print(rs.getString(3) + "\t");
//            System.out.print(rs.getString(4) + "\t");
//            System.out.println(rs.getTimestamp(5));

        Map<Integer,String > map = jdbc_vote.selectVotes();
        for (Map.Entry<Integer,String> entry : map.entrySet())
        {
            System.out.print(entry.getKey()+": ");
            System.out.println(entry.getValue());
        }
        //测试注释

    }


}
