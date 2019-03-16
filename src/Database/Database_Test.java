package Database;

import Users.Student;



public class Database_Test
{
    public static void main(String[] args)
    {

        JDBCOperation  jdbc=new JDBCOperation();
//    jdbc.insert(new Student(201800301165L ,"孙振瑜","java垃圾","??","88888888",0));
        jdbc.insert(new Student(201800301175L ,"王永超","java大佬","女","12345678",0));
//        System.out.println(JDBCOperation.judgeId(201800301165L));
    }
}
