package com.example.mysocialapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.UserValidator;
import com.example.mysocialapp.repo.Repository;
import com.example.mysocialapp.repo.database.UserDatabaseRepository;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.utils.DBConstants;

import javafx.scene.Parent;


public class LogInApplication extends Application {
    Repository<Long, User> userRepository = new UserDatabaseRepository(DBConstants.url,DBConstants.username, DBConstants.password, new UserValidator());

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logIn-view.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl= fxmlLoader.getController();
        ctrl.setService(new UserService(userRepository));

        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Social App!");
        stage.setScene(scene);
        stage.show();
//        stage.getIcons().add(new Image("ro/ubbcluj/map/Images/logo.png"));
    }
    public static void main(String[] args) {
        launch();
    }
}