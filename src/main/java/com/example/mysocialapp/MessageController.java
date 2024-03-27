package com.example.mysocialapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import com.example.mysocialapp.domain.Message;
import com.example.mysocialapp.service.FriendshipsService;
import com.example.mysocialapp.service.MessageService;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.service.WorldService;
import com.example.mysocialapp.utils.DBConstants;
import com.example.mysocialapp.utils.events.MessageEvent;
import com.example.mysocialapp.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MessageController implements Observer<MessageEvent> {
    public Button backToFriendsList;
    public TextField textNewMessage;

    private UserService usersService;
    private WorldService worldService;
    private FriendshipsService friendshipsService;
    private MessageService messageService;

    private Long userId;
    private Long friendId;

    ObservableList<Message> model = FXCollections.observableArrayList();

    @FXML
    ListView<Label> messages;

    public void onBackToFriendsList(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/my_friends-view.fxml"));
        Parent root = fxmlLoader.load();
        MyFriendsController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, worldService, friendshipsService, userId);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setService(UserService usersService, WorldService worldService, FriendshipsService friendshipsService,
                           MessageService messageService, Long userID, Long friendId) {
        this.usersService = usersService;
        this.userId = userID;
        this.worldService = worldService;
        this.friendshipsService = friendshipsService;
        this.friendId = friendId;
        this.messageService = messageService;
        messageService.addObserver(this);
        setData();
        initialize();
    }

    private void setData() {
       // List<Message> messages = messageService.getConversation(userId, friendId).collect(Collectors.toList());
        List<Message> messages = messageService.getConversation(userId, friendId);

        model.removeAll();
        model.setAll(messages);

        ObservableList<Label> items = FXCollections.observableArrayList();
        for (Message message : messages) {
            String text = "";
            if (message.getIdReplay() != 0) {
                Message messageReply = messageService.getOne(message.getIdReplay());
                String textReply = messageReply.getText();
                text += "Replied:" + textReply + "\n";
            }
            text += message.getText() + "\n" + message.getData().format(DBConstants.DATE_TIME_FORMATTER);
            Label label = new Label(text);
            if (message.getIdReplay() != 0) {
                label.setPrefHeight(70D);
            } else {
                label.setPrefHeight(50D);
            }
            if (Objects.equals(message.getFrom(), userId)) {
                label.setAlignment(Pos.BASELINE_RIGHT);
                label.setTextAlignment(TextAlignment.RIGHT);
                Border padding = new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5, 0, 5, 100)));
                Insets insets = new Insets(5, 0, 5, 100);
                Background background = new Background(new BackgroundFill(Color.color(0.263, 0.471, 0.49),
                        new CornerRadii(18D, 18D, 18D, 18D, false), insets));
                label.setBackground(background);
                label.setBorder(padding);
            } else {
                Background background = new Background(new BackgroundFill(Color.color(0.263, 0.471, 0.49),
                        new CornerRadii(18D, 18D, 18D, 18D, false), new Insets(5, 100, 5, 0)));
                label.setBackground(background);
            }
            label.setId(Double.toString(message.getId()));
            items.add(label);
        }

        this.messages.setVisible(false);
        this.messages.setItems(items);
        this.messages.setVisible(true);
    }

    @FXML
    private void initialize() {
    }

    public void onSendButton(ActionEvent actionEvent) {
        String text = this.textNewMessage.getText();
        Label label = this.messages.getSelectionModel().getSelectedItem();
        if (!text.isEmpty()) {
            if (label != null) {
                String text2 = label.getId().substring(0, label.getId().length() - 2);
                Long id = Long.valueOf(text2);
                this.messageService.addMessage(userId, friendId, text, LocalDateTime.now(), id);
            } else {
                this.messageService.addMessage(userId, friendId, text, LocalDateTime.now(), 0L);
            }
        }
        this.messages.getSelectionModel().clearSelection();
    }

    @Override
    public void update(MessageEvent messageEvent) {
        setData();
        this.messages.refresh();
    }

    public void deselect(ActionEvent actionEvent) {
        this.messages.getSelectionModel().clearSelection();
        this.messages.getSelectionModel().clearSelection();
    }
}
