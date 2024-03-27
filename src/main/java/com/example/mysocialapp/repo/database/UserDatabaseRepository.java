package com.example.mysocialapp.repo.database;



import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.Validator;
import com.example.mysocialapp.repo.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDatabaseRepository implements Repository<Long, User> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDatabaseRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User getOne(Long longid) {
        String sql = "SELECT * from users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setLong(1, longid);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String gender = resultSet.getString("gender");

            User user = new User(firstName, lastName, email, password, gender);
            user.setId(id);

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Iterable<User> getAll() {
        Set<User> users = new HashSet<>();
        String sql = "SELECT * from users";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String passwordU = resultSet.getString("password");
                String gender = resultSet.getString("gender");

                User user = new User(firstName, lastName, email, passwordU, gender);
                user.setId(id);

                String sql2 = "SELECT id_user1 FROM friendships WHERE id_user2 = ?";
                PreparedStatement statement2 = connection.prepareStatement(sql2);
                statement2.setLong(1, id);
                ResultSet resultSet2 = statement2.executeQuery();

                while (resultSet2.next()) {
                    User user2 = getOne(resultSet2.getLong(1));
                    user2.setId(resultSet2.getLong(1));
                    user.addFriend(user2);
                }

                String sql3 = "SELECT id_user2 FROM friendships WHERE id_user1 = ?";
                PreparedStatement statement3 = connection.prepareStatement(sql3);
                statement3.setLong(1, id);
                ResultSet resultSet3 = statement3.executeQuery();

                while (resultSet3.next()) {
                    User user3 = getOne(resultSet3.getLong(1));
                    user3.setId(resultSet3.getLong(1));
                    user.addFriend(user3);
                }

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void verifyEntity(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        HashSet<User> all = (HashSet<User>) getAll();
        if (all.contains(entity)) {
            throw new IllegalArgumentException("the id must be unique");
        }
    }

    @Override
    public User save(User entity) {
        verifyEntity(entity);
        String sql = "insert into users(first_name,last_name,email,password,gender) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getGender());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM users";

        int size = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            size = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
    }

    @Override
    public User delete(Long aLong) {
        String sql = "delete from users where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User entity) {
        String sql = "update users set first_name = ?, last_name = ?, email=?, password=?,gender =? where id = ?";
        verifyEntity(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPassword());
            ps.setString(5, entity.getGender());
            ps.setString(6, entity.getId().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }


}
