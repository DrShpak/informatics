package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Group {
    @Setter@Getter
    private long groupId;
    @Getter
    private final int groupNumber;
    @Getter
    private final int year;
    @Getter
    private final List<Student> students;

    private Group(int groupNumber, int year) {
        this.groupId = -1;
        this.groupNumber = groupNumber;
        this.year = year;
        this.students = new ArrayList<>();
    }

    public void addStudent(String name, String surname) {
        students.add(Student.getStudentWithDefaultsParams(name, surname, this.getGroupId()));
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(long studentId) {
        var student = students.stream()
            .filter(x -> x.getStudentId() == studentId)
            .findFirst();

        student.ifPresent(students::remove);
    }

    public static Group getGroupWithDefaultParams(int groupNumber, int year) {
        return new Group(groupNumber, year);
    }
}

