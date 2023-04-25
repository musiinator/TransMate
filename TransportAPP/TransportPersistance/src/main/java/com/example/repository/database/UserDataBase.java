package com.example.repository.database;

import com.example.domain.Rezervare;
import com.example.domain.User;
import com.example.repository.Interfaces.UserRepoInterface;
import com.example.repository.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.security.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDataBase implements UserRepoInterface {

    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserDataBase (Properties props){
        System.out.println("Initializing UserDataBase with properties: {} "+ props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findOne(Long id) {
        Connection connection = dbUtils.getConnection();
        User user = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from user where id_user = ?")){
            preparedStatement.setLong(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                user = new User(username, password);
                user.setId(id);
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from user")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()) {
                    Long id = resultSet.getLong("id_user");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return users;
    }

    @Override
    public User save(User entity) {
        logger.traceEntry("Task: save", entity);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt= connection.prepareStatement("insert into user (username, password) values (?,?)")){
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            int result = preStmt.executeUpdate();
            logger.trace("Saved{}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User delete(Long id) {
        logger.traceEntry("Task: delete", findOne(id));
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preStmt= connection.prepareStatement("delete from user where id_user = ?")){
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User update(User entity) {
        logger.traceEntry("Task: update", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt= con.prepareStatement("update user set username = ?, password = ? where id_user = ?")){
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setLong(3, entity.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Updated{}", result);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB " + exception);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Connection connection = dbUtils.getConnection();
        User user = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username = ?")){
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                Long id = resultSet.getLong("id_user");
                String password = resultSet.getString("password");
                user = new User(username, password);
                user.setId(id);
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Error DB: " + exception);
        }
        logger.traceExit();
        return user;
    }
}
