package consoleUI;

import dao.ConsoleDAO;
import models.Group;
import servicies.GroupManager;

public class Main {
    private static GroupManager groupManager = new GroupManager(new ConsoleDAO());

    public static void main(String[] args) {
        deleteGroup(2);
    }

    private static void printAll() {
        groupManager.getDao().selectAll().forEach(System.out::println);
        System.out.println("\n\n");
    }

    private static void addGroup() {
        var group = Group.getGroupWithDefaultParams(1, 1);
        groupManager.insertGroup(group);
    }

    private static void addStudentToGroup(String name, String surname, int groupNumber, int year) {
        // что.
    }

    private static void deleteGroup(long groupId) {
        System.out.println("До удаления: ");
        printAll();
        groupManager.deleteGroup(groupId);
        System.out.println("После удаления");
        printAll();
    }
}
