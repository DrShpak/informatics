package servicies;

import dao.DAO;
import lombok.Getter;
import models.Group;
import models.Student;

import java.util.List;

public class GroupManager {
    @Getter
    private final DAO dao;

    public GroupManager(DAO dao) {
        this.dao = dao;
    }

    public void createGroup(int groupNumber, int year) {
        var group = Group.getGroupWithDefaultParams(groupNumber, year);
        dao.insertGroup(group);
    }

    public void createStudent(String name, String surname, long groupId) {
        var student = Student.getStudentWithDefaultsParams(name, surname, groupId);
        dao.insertStudent(student);
    }

    public void insertGroup(Group group) {
        dao.insertGroup(group);
    }

    public void insertStudent(Student student) {
        dao.insertStudent(student);
    }

    public void deleteGroup(long groupId) {
        dao.deleteGroup(groupId);
    }

    public void deleteStudent(long studentId) {
        dao.deleteStudent(studentId);
    }

    public void completeTask(long studentId, int taskNumber) {
        var student = getStudentById(studentId);
        if (student != null && taskNumber >= 1 && taskNumber <= 3 ) {
            student.completeTask(taskNumber);
            dao.updateStudent(student);
        }
    }

    public Group getGroupById(long groupId) {
        return dao.getGroupById(groupId);
    }

    public Student getStudentById(long studentId) {
        return dao.getStudentById(studentId);
    }

    public List<Group> getAllGroups() {
        return dao.selectAll();
    }
}
