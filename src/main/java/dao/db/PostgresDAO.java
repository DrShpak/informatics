package dao.db;

import dao.DAO;
import models.Group;
import models.Student;
import models.Task;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PostgresDAO implements DAO {
    private final static String PATH_TO_QUERIES = "src/main/resources/queries.properties";
    private final ConnectionBuilder builder = new PostgresConnection();
    private final static Properties queries = new Properties();

    public PostgresDAO() {
        try {
            queries.load(new FileInputStream(PATH_TO_QUERIES));
        } catch (IOException e) {
            //todo log it
        }
    }

    @Override
    public List<Group> selectAll() {
        List<Group> groups = new ArrayList<>();
        try (var con = builder.getConnection();
             var groupsSet = con.createStatement().executeQuery(queries.getProperty("select_groups"))) {

            while (groupsSet.next()) {
                groups.add(getGroupById(groupsSet.getLong(1)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return groups;
    }

    @Override
    public List<Student> selectAllStudentsByGroupId(long groupId) {
        List<Student> students = new ArrayList<>();
        try (var con = builder.getConnection();
             var stmStudents = con.prepareStatement(queries.getProperty("select_students"))) {

            stmStudents.setLong(1, groupId);
            var studentsSet = stmStudents.executeQuery();
            while (studentsSet.next()) {
                students.add(parseStudent(studentsSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return students;
    }

    @Override
    public void insertGroup(Group group) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("insert_group"))) {

            stm.setInt(1, group.getGroupNumber());
            stm.setInt(2, group.getYear());
            stm.executeUpdate();

            //insert students
            for (Student student : group.getStudents()) {
                insertStudent(student);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteGroup(long groupId) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("delete_group"))) {

            stm.setLong(1, groupId);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void insertStudent(Student student) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("insert_student"))) {

            stm.setLong(1, student.getGroupId());
            stm.setString(2, student.getName());
            stm.setString(3, student.getSurname());
            stm.executeUpdate();

            var stm2 = con.prepareStatement(queries.getProperty("insert_task"));
            for (Task task : student.getTasks()) {
                stm2.setBoolean(1, task.isCompleted());
                stm2.setLong(2, student.getStudentId());
                stm2.addBatch();
            }

            stm2.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(long studentId) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("delete_student"))) {

            stm.setLong(1, studentId);
            stm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateGroup(Group group) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("update_group"))) {
            stm.setInt(1, group.getGroupNumber());
            stm.setInt(2, group.getYear());
            stm.setLong(3, group.getGroupId());

            stm.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateStudent(Student student) {
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("update_student"))) {
            stm.setLong(1, student.getGroupId());
            stm.setString(2, student.getName());
            stm.setString(3, student.getSurname());
            stm.setLong(4, student.getStudentId());

            stm.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Group getGroupById(long groupId) {
        Group group = null;
        try (var con = builder.getConnection();
             var stmGroup = con.prepareStatement(queries.getProperty("select_group"));
             var stmStudents = con.prepareStatement(queries.getProperty("select_students"))) {
            stmGroup.setLong(1, groupId);
            stmStudents.setLong(1, groupId);
            var groupSet = stmGroup.executeQuery();
            group = parseGroup(groupSet);
            var studentsSet = stmStudents.executeQuery();
            while (studentsSet.next()) {
                group.addStudent(parseStudent(studentsSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return group;
    }

    private Student parseStudent(ResultSet studentsSet) throws SQLException {
//        studentsSet.next();
        return Student.getStudent(studentsSet.getLong(1),
            studentsSet.getString(3), studentsSet.getString(4), studentsSet.getLong(2));
    }

    private Group parseGroup(ResultSet groupSet) throws SQLException {
//        Group group = null;
        groupSet.next();
        return Group.getGroup(groupSet.getLong(1), groupSet.getInt(2), groupSet.getInt(3));
    }

    @Override
    public Student getStudentById(long studentId) {
        Student student = null;
        try (var con = builder.getConnection();
             var stmStudents = con.prepareStatement(queries.getProperty("select_student"))) {
            stmStudents.setLong(1, studentId);
            var studentsSet = stmStudents.executeQuery();
            studentsSet.next();
            student = parseStudent(studentsSet);
            student.setTasks(getTasks(studentId));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return student;
    }

    @Override
    public List<Task> getTasks(long studentId) {
        List<Task> tasks = new ArrayList<>();
        try (var con = builder.getConnection();
             var stm = con.prepareStatement(queries.getProperty("select_tasks"))) {
            stm.setLong(1, studentId);
            var tasksSet = stm.executeQuery();
            while (tasksSet.next()) {
                tasks.add(new Task(tasksSet.getLong(1), tasksSet.getBoolean(2)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tasks;
    }
}
