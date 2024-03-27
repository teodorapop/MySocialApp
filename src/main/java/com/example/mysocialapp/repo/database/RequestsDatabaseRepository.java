package com.example.mysocialapp.repo.database;
import com.example.mysocialapp.domain.Request;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.domain.validators.Validator;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RequestsDatabaseRepository implements Repository<Long, Request> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<Request> validator;

    public RequestsDatabaseRepository(String url, String username, String password, Validator<Request> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Request getOne(Long aLong) {
        String sql = "SELECT * from requests WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            Long id = resultSet.getLong("id");
            Long id1 = resultSet.getLong("id_from");
            Long id2 = resultSet.getLong("id_to");
            String status = resultSet.getString("status");
            LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));

            Request request = new Request(id1, id2, status, date);
            request.setId(id);

            return request;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Request> getAll() {
        Set<Request> requestsSet = new HashSet<>();
        String sql = "SELECT * from requests";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("id_from");
                Long idTo = resultSet.getLong("id_to");
                String status = resultSet.getString("status");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("date"));

                Request request = new Request(idFrom, idTo, status, date);
                request.setId(id);
                requestsSet.add(request);
            }
            return requestsSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestsSet;
    }

    @Override
    public void verifyEntity(Request entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        HashSet<Request> all = (HashSet<Request>) getAll();
        if (all.contains(entity)) {
            throw new IllegalArgumentException("the id must be unique");
        }
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM requests";

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
    public Request delete(Long aLong) {
        String sql = "delete from requests where id = ?";

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
    public Request update(Request entity) {
        String sql = "update requests set status = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getStatus());
            ps.setLong(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Request save(Request entity) {
        String sql = "insert into requests (id_from, id_to, status, date ) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3, entity.getStatus());
            ps.setString(4, entity.getDateTime().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
