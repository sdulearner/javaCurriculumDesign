package ClientSocket;

import Entity.Announcement;
import Entity.Document;
import Entity.Student;

import java.util.Arrays;
import java.util.Map;

/**
 * @description:
 * @author: sdulearner
 * @create: 2019-05-05 08:14
 **/

public class SignIn
{
    @Override
    public String toString()
    {
        return "SignIn{" +
                "studentsOnline=" + Arrays.toString(studentsOnline) +
                ", studentsOffline=" + Arrays.toString(studentsOffline) +
                ", announcements=" + Arrays.toString(announcements) +
                ", documents=" + Arrays.toString(documents) +
                ", map=" + map +
                ", result=" + result +
                '}';
    }

    private Student[] studentsOnline;
    private Student[] studentsOffline;
    private Announcement[] announcements;
    private Document[] documents;
    private Map<Long, Short> map;
    private char result;

    public Student[] getStudentsOnline()
    {
        return studentsOnline;
    }

    public Student[] getStudentsOffline()
    {
        return studentsOffline;
    }

    public Announcement[] getAnnouncements()
    {
        return announcements;
    }

    public Document[] getDocuments()
    {
        return documents;
    }

    public Map<Long, Short> getMap()
    {
        return map;
    }

    public char getResult()
    {
        return result;
    }

    public SignIn(Student[] studentsOnline, Student[] studentsOffline, Announcement[] announcements, Document[] documents, Map<Long, Short> map, char result)
    {
        this.studentsOnline = studentsOnline;
        this.studentsOffline = studentsOffline;
        this.announcements = announcements;
        this.documents = documents;
        this.map = map;
        this.result = result;
    }

    public SignIn(char result)
    {
        this.result = result;
    }
}
