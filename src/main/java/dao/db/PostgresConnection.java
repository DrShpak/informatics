package dao.db;

import org.postgresql.util.PSQLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnection implements ConnectionBuilder {

    private final Properties properties;
    private final String PATH_TO_LOCAL_CONFIG = "src/main/resources/credentials.properties";
    private final String DB_NAME = "informatics";

    public PostgresConnection() {
        properties = new Properties();
        try {
            if (new File(PATH_TO_LOCAL_CONFIG).exists())
                properties.load(new FileInputStream(PATH_TO_LOCAL_CONFIG));
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException | IOException ex) {

        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection con;
        try {
            con = DriverManager.getConnection(
                properties.getProperty("db.host") + DB_NAME,
                properties.getProperty("db.login"),
                properties.getProperty("db.password"));
        } catch (PSQLException e) {
            createDBandTables();
            con = DriverManager.getConnection(
                properties.getProperty("db.host") + DB_NAME,
                properties.getProperty("db.login"),
                properties.getProperty("db.password"));
        }
        return con;
    }

    private void createDBandTables() throws SQLException {
        var connection = DriverManager.getConnection(
            properties.getProperty("db.host"),
            properties.getProperty("db.login"),
            properties.getProperty("db.password"));

       //create database
        var stm = connection.prepareStatement(properties.getProperty("create_database"));
        stm.executeUpdate();

        //create table groups
        stm = connection.prepareStatement(properties.getProperty("create_groups"));
        stm.executeUpdate();

        //create students
        stm = connection.prepareStatement(properties.getProperty("create_students"));
        stm.executeUpdate();

        //create tasks
        stm = connection.prepareStatement(properties.getProperty("create_tasks"));
        stm.executeUpdate();
    }
}