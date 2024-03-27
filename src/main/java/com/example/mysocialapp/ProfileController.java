package com.example.mysocialapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.example.mysocialapp.domain.Friendship;
import com.example.mysocialapp.domain.Request;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.FriendshipValidator;
import com.example.mysocialapp.domain.validators.RequestValidator;
import com.example.mysocialapp.domain.validators.UserValidator;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.repo.database.FriendshipDatabaseRepository;
import com.example.mysocialapp.repo.database.RequestsDatabaseRepository;
import com.example.mysocialapp.repo.database.UserDatabaseRepository;
import com.example.mysocialapp.service.FriendshipsService;
import com.example.mysocialapp.service.RequestsService;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.service.WorldService;
import com.example.mysocialapp.utils.DBConstants;

import java.io.IOException;
import java.util.Objects;

public class ProfileController {

    @FXML
    Button signOut;
    @FXML
    Button profileMyFriends;
    @FXML
    Button profileAllUsers;
    @FXML
    Button profileNews;
    @FXML
    ImageView profileImage;
    @FXML
    Label profileFirstName;
    @FXML
    Label profileSecondName;
    @FXML
    Label profileMail;

    Long userID;
    UserService service;
    private Stage stage;
    private Scene scene;

    Repository<Long, User> userRepository = new UserDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new UserValidator());
    Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new FriendshipValidator());
    Repository<Long, Request> requestRepository = new RequestsDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new RequestValidator());

    public void setService(UserService service, Long user) {
        this.service = service;
        this.userID = user;
        setData();
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void setData() {
        profileFirstName.setText(service.getOne(userID).getFirstName());
        profileSecondName.setText(service.getOne(userID).getLastName());
        profileMail.setText(service.getOne(userID).getEmail());
        if (Objects.equals(service.getOne(userID).getGender(), "female")) {
            profileImage.setImage(new Image("ro/ubbcluj/map/Images/f.jpg"));
        }
    }

    public void onMyFriendsButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/my_friends-view.fxml"));
        Parent root = fxmlLoader.load();
        MyFriendsController ctrl = fxmlLoader.getController();
        ctrl.setService(new UserService(userRepository), new
                        WorldService(friendshipRepository, userRepository),
                new FriendshipsService(friendshipRepository, userRepository), userID);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onAllUsersButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/allUsers-view.fxml"));
        Parent root = fxmlLoader.load();
        AllUsersController ctrl = fxmlLoader.getController();
        ctrl.setService(new
                        WorldService(friendshipRepository, userRepository),
                new UserService(userRepository), userID);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onProfileNews(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/friend_requests.fxml"));
        Parent root = fxmlLoader.load();
        FriendsRequestsController ctrl = fxmlLoader.getController();
        ctrl.setService(new
                RequestsService(requestRepository, userRepository, friendshipRepository), new UserService(userRepository), userID);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSignOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/logIn-view.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 550, 600);
        stage.setScene(scene);
        stage.show();
    }
}