package com.example.mysocialapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.utils.MD5Password;
import java.io.IOException;
import java.util.Objects;

public class LogInController {
    @FXML
    TextField fieldLogInEmail;
    @FXML
    PasswordField fieldLogInPassword;

    @FXML
    Button buttonLogIn;
    @FXML
    Button buttonSignUp;

    @FXML
    Label logIn;

    UserService service;
    private Stage stage;
    private Scene scene;
    private Long currentId;
    private final MD5Password hashPassword = new MD5Password();

    public LogInController() {
    }

    @FXML
    protected void onLogInButtonClick(ActionEvent event) throws IOException {

        String email = fieldLogInEmail.getText();
        String passwordText = fieldLogInPassword.getText();
        String password = hashPassword.hashPassword(passwordText);
        Iterable<User> users = service.getAllUsers();
        logIn.setText("nu va putem loga");

        for (User user : users) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                currentId = user.getId();
                logIn.setText("conectare cu succes");
                this.openApp(event);
            }

        }

    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(service, currentId);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setService(UserService service) {
        this.service = service;
    }

    public void onSignUpButtonClick(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/signUp-view.fxml"));
        Parent root = fxmlLoader.load();
        SignUpController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}