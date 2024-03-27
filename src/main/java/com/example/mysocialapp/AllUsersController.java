package com.example.mysocialapp;


import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.service.UserService;
import com.example.mysocialapp.service.WorldService;
import com.example.mysocialapp.utils.events.UserEvent;
import com.example.mysocialapp.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class AllUsersController implements Observer<UserEvent> {
    public Button backToProfile;
    public Button userDetails;
    public TextField textSearchFirstName;
    public TextField textSearchLastName;
    WorldService service;
    UserService usersService;

    Long userID;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableViewAllUsers;
    @FXML
    TableColumn<User, String> tableColumnFirstNameAllUsers;
    @FXML
    TableColumn<User, String> tableColumnLastNameAllUsers;


    public void setService(WorldService service, UserService usersService, Long userID) {
        this.service = service;
        this.usersService = usersService;
        this.userID = userID;
        usersService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstNameAllUsers.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastNameAllUsers.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewAllUsers.setItems(model);

        textSearchFirstName.textProperty().addListener(o -> handleFilter());
        textSearchLastName.textProperty().addListener(o -> handleFilter());

    }

    private void initModel() {
        Iterable<User> usersI = usersService.getAllUsers();
        List<User> userList = StreamSupport.stream(usersI.spliterator(), false)
                .collect(Collectors.toList());
        userList.removeIf(u -> Objects.equals(u.getId(), userID));
        model.setAll(userList);
    }


    private void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textSearchFirstName.getText());
        Predicate<User> p2 = n -> n.getLastName().startsWith(textSearchLastName.getText());


        Iterable<User> usersI = usersService.getAllUsers();
        List<User> userList = StreamSupport.stream(usersI.spliterator(), false)
                .collect(Collectors.toList());
        userList.removeIf(u -> Objects.equals(u.getId(), userID));
        model.setAll(userList
                .stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList()));
    }


    @Override
    public void update(UserEvent userEvent) {
        initModel();
    }

    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openAppBack(actionEvent, userID);
    }

    private void openAppBack(ActionEvent event, Long id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, id);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onUserDetails(ActionEvent actionEvent) throws IOException {
        User user = tableViewAllUsers.getSelectionModel().getSelectedItem();
        openAppDetails(actionEvent, user.getId());

    }

    private void openAppDetails(ActionEvent event, Long id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/userProfile.fxml"));
        Parent root = fxmlLoader.load();
        UserProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, id, userID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
