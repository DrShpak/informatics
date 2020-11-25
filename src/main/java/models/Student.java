package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
public class Student {
    @Getter@Setter
    private long studentId;
    @Getter
    private final long groupId;
    @Getter
    private final String name;
    @Getter
    private final String surname;
    @Getter
    private final List<Task> tasks;

    private Student(long groupId, String name, String surname) {
        this.studentId = -1;
        this.groupId = groupId;
        this.name = name;
        this.surname = surname;
        this.tasks = List.of(new Task(), new Task(), new Task());
    }

    public static Student getStudentWithDefaultsParams(String name, String surname, long groupId) {
        return new Student(groupId, name, surname);
    }

    public void completeTask(int numberTask) {
        tasks.get(numberTask).complete(true);
    }
}
