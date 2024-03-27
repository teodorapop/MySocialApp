package com.example.mysocialapp.repo.database;
import com.example.mysocialapp.domain.Message;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.domain.validators.Validator;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MessageDatabaseRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    public MessageDatabaseRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message getOne(Long aLong) {
        {
            String sql = "SELECT * from messages WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql)
            ) {

                statement.setLong(1, aLong);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("from_id");
                Long idTo = resultSet.getLong("to_id");

                String messageText = resultSet.getString("messagetext");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"));
                Long idReplay = resultSet.getLong("id_replay");

                Message message = new Message(idFrom, idTo, messageText, date, idReplay);
                message.setId(id);
                return message;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Iterable<Message> getAll() {
        Set<Message> messagesSet = new HashSet<>();
        String sql = "SELECT * from messages";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFrom = resultSet.getLong("from_id");
                Long idTo = resultSet.getLong("to_id");
                String messageText = resultSet.getString("messagetext");
                LocalDateTime date = LocalDateTime.parse(resultSet.getString("data"));
                Long idReplay = resultSet.getLong("id_replay");

                Message message = new Message(idFrom, idTo, messageText, date, idReplay);
                message.setId(id);
                messagesSet.add(message);
            }
            return messagesSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messagesSet;
    }

    @Override
    public void verifyEntity(Message entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        HashSet<Message> all = (HashSet<Message>) getAll();
        if (all.contains(entity)) {
            throw new IllegalArgumentException("the id must be unique");
        }

    }

    @Override
    public Message save(Message entity) {
        String sql = "insert into messages (from_id,to_id,messagetext,data,id_replay) values (?, ?, ?, ?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);

             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3, entity.getMessage());
            ps.setString(4, entity.getData().toString());
            if (entity.getIdReplay() != 0) {
                ps.setLong(5, entity.getIdReplay());
            } else {
                ps.setNull(5, Types.INTEGER);
            }


            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM messages";

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
    public Message delete(Long aLong) {
        String sql = "delete from messages where id = ?";

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
    public Message update(Message entity) {
        String sql = "update messages set from = ?, to = ?,messageText = ?, date = ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3, entity.getMessage());
            ps.setString(4, entity.getData().toString());

            ps.setLong(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;

    }
}
