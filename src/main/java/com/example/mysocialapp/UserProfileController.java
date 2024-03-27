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
import com.example.mysocialapp.service.*;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.utils.DBConstants;

import java.io.IOException;
import java.util.Objects;

public class UserProfileController {
    public Label profileFirstName;

    public Label profileSecondName;
    public Label profileMail;
    public Button backToProfile;
    public Button friendStatus;
    public ImageView profileImage;

    Long userID;
    Long openProfileId;
    UserService service;

    Repository<Long, User> userRepository = new UserDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new UserValidator());
    Repository<Long, Friendship> friendshipRepository = new FriendshipDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new FriendshipValidator());
    Repository<Long, Request> requestRepository = new RequestsDatabaseRepository(DBConstants.url, DBConstants.username,
            DBConstants.password, new RequestValidator());
    RequestsService requestsService = new RequestsService(requestRepository, userRepository, friendshipRepository);
    FriendshipsService friendshipsService = new FriendshipsService(friendshipRepository, userRepository);
    WorldService worldService = new WorldService(friendshipRepository, userRepository);

    public void setService(UserService service, Long openProfileId, Long user) {
        this.service = service;
        this.openProfileId = openProfileId;
        this.userID = user;
        setData();
    }


    @FXML
    private void setData() {
        profileFirstName.setText(service.getOne(openProfileId).getFirstName());
        profileSecondName.setText(service.getOne(openProfileId).getLastName());
        profileMail.setText(service.getOne(openProfileId).getEmail());
        if (Objects.equals(service.getOne(openProfileId).getGender(), "female")) {
            profileImage.setImage(new Image("ro/ubbcluj/map/Images/f.jpg"));
        }
        FriendshipsService friendshipsService = new FriendshipsService(friendshipRepository, userRepository);
        int friend = worldService.fridshipE(userID, openProfileId);

        if (friend == 1)
            friendStatus.setText("delete friend");
        else {
            if (friend == 0) {

                int idrequest = requestsService.existRequest(userID, openProfileId);
                if (idrequest == 0) {
                    friendStatus.setText("delete request");
                } else
                    friendStatus.setText("add friend");

            }
        }
    }


    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openApp(actionEvent);
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/allUsers-view.fxml"));
        Parent root = fxmlLoader.load();
        AllUsersController ctrl = fxmlLoader.getController();
        ctrl.setService(new WorldService(friendshipRepository, userRepository),
                new UserService(userRepository), userID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onStatus(ActionEvent actionEvent) {
        String text = friendStatus.getText();
        if (text.equals("delete request")) {
            friendStatus.setText("add friend");
            this.requestsService.deleteARequest(userID, openProfileId);

        }
        if (text.equals("add friend")) {
            friendStatus.setText("delete request");
            this.requestsService.addRequest(userID, openProfileId);
        }
        if (text.equals("delete friend")) {
            friendStatus.setText("add friend");
            this.friendshipsService.deleteFriendshipBetweenTwoUsers(userID, openProfileId);
        }
    }
}
