package dao;

import models.Group;
import models.Student;
import models.Task;

import java.util.List;

public interface DAO {

    public List<Group> selectAll();

    public List<Student> selectAllStudentsByGroupId(long groupId);

    public void insertGroup(Group group);

//    public void deleteGroup(int groupNumber, int year);

    public void deleteGroup(long groupId);

    public void insertStudent(Student student);

    public void deleteStudent(long studentId);

    public void updateGroup(Group group);

    public void updateStudent(Student student);

    public Group getGroupById(long groupId);

    public Student getStudentById(long studentId);

    List<Task> getTasks(long studentId);

}
