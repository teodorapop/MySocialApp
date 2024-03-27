package com.example.mysocialapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.domain.validators.ValidationException;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.utils.MD5Password;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {
    public TextField fieldSignUpFirstName;
    public TextField fieldSignUpLastName;
    public PasswordField passwordSignUp;
    public TextField fieldSignUpEmail;
    public PasswordField confirmPasswordSignUp;
    public Button createAnAccount;
    public Button backToLogIn;
    public Label statusLogIn;
    public ChoiceBox<String> genderBox;
    UserService service;
    private Stage stage;
    private Scene scene;
    private final MD5Password hashPassword = new MD5Password();
    private Long currentId;
    private User currentUser;


    public void setService(UserService service) {
        this.service = service;
        genderBox.getItems().add("male");
        genderBox.getItems().add("female");
        genderBox.setValue("male");
    }

    public void onCreateAnAccountButtonClick(ActionEvent event) throws IOException {
        String firstName = fieldSignUpFirstName.getText();
        String lastName = fieldSignUpLastName.getText();
        String email = fieldSignUpEmail.getText();
        String password = passwordSignUp.getText();
        String confirmPassword = confirmPasswordSignUp.getText();
        String gender = this.genderBox.getValue();
        if (!Objects.equals(password, confirmPassword)) {
            statusLogIn.setText("the passwords are not the same");
        }else
        try {
            currentUser = new User(firstName, lastName, email, hashPassword.hashPassword(password), gender);
            service.addUser(currentUser);
            this.openApp(event);
        } catch (ValidationException | IllegalArgumentException validationException) {
            statusLogIn.setText(validationException.getMessage());
        }
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        Iterable<User> users = service.getAllUsers();
        for (User user : users) {
            if (Objects.equals(user.getEmail(), currentUser.getEmail())) {
                currentId = user.getId();
            }

        }
        ctrl.setService(service, currentId);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onBackToLogInButtonClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/logIn-view.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
