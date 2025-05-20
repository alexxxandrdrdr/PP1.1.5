package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection conn = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        try(Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS USER ( ID INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(64) NOT NULL, LAST_NAME VARCHAR(64) NOT NULL, AGE INT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement stmt = conn.createStatement()){
            stmt.execute("DROP TABLE IF EXISTS USER");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        try(PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO USER (NAME, LAST_NAME, AGE) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {

        try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM USER WHERE ID = ?")){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        try (Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery("SELECT * FROM USER")) {
            while (rs.next()) {
                int id = rs.getInt("ID");
                User user = new User(rs.getString("NAME"), rs.getString("LAST_NAME"), (byte) rs.getInt("AGE"));
                user.setId((long) id);
                users.add(user);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {

    try (Statement stmt = conn.createStatement()){
        stmt.execute("DELETE FROM USER");
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }
}
