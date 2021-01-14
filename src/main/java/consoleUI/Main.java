package consoleUI;

import dao.ConsoleDAO;
import dao.db.PostgresDAO;
import models.Group;
import models.Student;
import servicies.GroupManager;

public class Main {
    private static GroupManager groupManager = new GroupManager(new PostgresDAO());

    public static void main(String[] args) {
//        System.out.println(groupManager.getAllGroups());
        System.out.println(groupManager.getGroupById(1));
    }

    private static void printAll() {
        groupManager.getDao().selectAll().forEach(System.out::println);
        System.out.println("\n\n");
    }

    private static void addGroup() {
        var group = Group.getGroupWithDefaultParams(1, 1);
        groupManager.insertGroup(group);
    }

    private static void addStudentToGroup(long groupId, String name, String surname, int year) {
        // что.
        groupManager.insertStudent(Student.getStudentWithDefaultsParams(name, surname, groupId));
    }

    private static void deleteGroup(long groupId) {
        System.out.println("До удаления: ");
        printAll();
        groupManager.deleteGroup(groupId);
        System.out.println("После удаления");
        printAll();
    }
}
