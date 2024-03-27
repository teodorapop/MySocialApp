package com.example.mysocialapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.mysocialapp.controller.MessageAlert;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.MessageValidator;
import com.example.mysocialapp.repo.database.MessageDatabaseRepository;
import com.example.mysocialapp.service.*;
import com.example.mysocialapp.utils.DBConstants;
import com.example.mysocialapp.utils.events.FriendshipEvent;
import com.example.mysocialapp.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFriendsController implements Observer<FriendshipEvent> {
    public Button backToProfile;
    public Button message;
    WorldService worldService;
    FriendshipsService friendshipsService;
    ObservableList<User> model = FXCollections.observableArrayList();

    Long userID;

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    private UserService usersService;

    public void setService(UserService usersService, WorldService service, FriendshipsService friendshipsService, Long user) {
        this.usersService = usersService;
        this.worldService = service;
        this.friendshipsService = friendshipsService;
        this.userID = user;
        friendshipsService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Map<User, LocalDateTime> friendshipsOfAnUser = worldService.friendshipsOfAnUser(userID);
        List<User> keys = new ArrayList<>(friendshipsOfAnUser.keySet());
        model.setAll(keys);
    }

    @Override
    public void update(FriendshipEvent friendshipEvent) {
        initModel();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();

        if (user != null) {
            this.friendshipsService.deleteFriendshipBetweenTwoUsers(userID, user.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete",
                    "The friendship has been deleted");
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete",
                    "You haven't selected anything!");
        }
    }

    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openApp(actionEvent);
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, userID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onMessage(ActionEvent event) throws IOException {
        User user = tableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/message-view.fxml"));
        Parent root = fxmlLoader.load();
        MessageController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, worldService, friendshipsService,
                new MessageService(new MessageDatabaseRepository(
                        DBConstants.url, DBConstants.username, DBConstants.password, new MessageValidator())),
                userID, user.getId());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
