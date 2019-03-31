package Database;


import Entity.Student;

public class Database_Test
{
    public static void main(String[] args)
    {

        JDBC_Students jdbc = new JDBC_Students();
//        jdbc.insert(new Student(201800301185L, "孙振瑜", "java垃圾", "请选择", "88888888", 0));
//        jdbc.insert(new Student(201800301175L ,"王永超","java大佬","女","12345678",0));
//        System.out.println(JDBC_Students.judgeId(201800301185L));
//        System.out.println(jdbc.getAll());
//        System.out.println(jdbc.getInformation(201800301175L));
        System.out.println(jdbc.update(new Student(201800301165L ,"孙振瑜","垃圾","请选择","88888888",0)));
    }
}
