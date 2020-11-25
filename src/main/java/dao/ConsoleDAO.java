package dao;

import models.Group;
import models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleDAO implements DAO {
    private final List<Group> groups;
    private final List<Student> students;

    private long groupIdIncrement = 0;
    private long studentIdIncrement = 0;

    public ConsoleDAO() {
        this.groups = new ArrayList<>();
        this.students = new ArrayList<>();

        fillTestData();
    }

    @Override
    public List<Group> selectAll() {
        return groups;
    }

    @Override
    public List<Student> selectAllStudentsByGroupId(long groupId) {
        return students.stream()
            .filter(x -> x.getGroupId() == groupId)
            .collect(Collectors.toList());
    }

    @Override
    public void insertGroup(Group group) {
        group.setGroupId(groupIdIncrement++);
        groups.add(group);
    }

    @Override
    public void deleteGroup(long groupId) {
        groups.removeIf(x -> x.getGroupId() == groupId);
        students.removeIf(student -> student.getGroupId() == groupId);
    }

//    @Override
//    public void deleteGroup(int groupNumber, int year) {
//
//    }

    @Override
    public void insertStudent(Student student) {
        student.setStudentId(studentIdIncrement++);
        students.add(student);
    }

    @Override
    public void deleteStudent(long studentId) {
        students.removeIf(x -> x.getStudentId() == studentId);
    }

    @Override
    public void updateGroup(Group group) {
        groups.removeIf(x -> x.getGroupId() == group.getGroupId());
        groups.add(group);
    }

    @Override
    public void updateStudent(Student student) {

    }

    @Override
    public Group getGroupById(long groupId) {
        return groups.stream()
            .filter(x -> x.getGroupId() == groupId)
            .findAny()
            .orElse(null);
    }

    @Override
    public Student getStudentById(long studentId) {
        return students.stream()
            .filter(x -> x.getStudentId() == studentId)
            .findAny()
            .orElse(null);
    }

    private void fillTestDataGroups() {
        for (int i = 0; i < 10; i++) {
            var group = Group.getGroupWithDefaultParams(i, i);
            insertGroup(group);
        }
    }

    private void fillTestData() {
        for (long i = groupIdIncrement; i < 10; i++) {
            var group = Group.getGroupWithDefaultParams((int) i, (int) i);
            insertGroup(group);
            for (int j = 0; j < 5; j++) {
                var student = Student.getStudentWithDefaultsParams("Имя " + i, "Фамилия " + j, group.getGroupId());
                insertStudent(student);
                group.addStudent(student);
                updateGroup(group);
            }
        }
    }
}
