package com.example.mysocialapp;
import com.example.mysocialapp.service.RequestsService;
import com.example.mysocialapp.utils.DBConstants;
import com.example.mysocialapp.utils.events.RequestEvent;
import com.example.mysocialapp.utils.observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import com.example.mysocialapp.domain.User;
import com.example.mysocialapp.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class FriendsRequestsController implements Observer<RequestEvent> {


    public Button backToProfile;
    RequestsService requestsService;
    UserService usersService;
    private final ObservableMap<User, LocalDateTime> model = FXCollections.observableHashMap();

    @FXML
    public TableView<Map.Entry<User, LocalDateTime>> tableView;

    @FXML
    TableColumn<Map.Entry<User, LocalDateTime>, String> tableColumnFirstName;
    @FXML
    TableColumn<Map.Entry<User, LocalDateTime>, String> tableColumnLastName;
    @FXML
    TableColumn<Map.Entry<User, LocalDateTime>, String> tableColumnDate;
    private Long userId;

    public FriendsRequestsController() {
    }

    public void setService(RequestsService requestsService, UserService usersService, Long userId) {
        this.requestsService = requestsService;
        this.usersService = usersService;
        this.userId = userId;
        requestsService.addObserver((Observer<RequestEvent>) this);
        initModel();

    }

    @FXML
    public void setData() {
        tableColumnFirstName.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<User, LocalDateTime>, String> p) -> new SimpleStringProperty(
                        p.getValue().getKey().getFirstName()));
        tableColumnLastName.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<User, LocalDateTime>, String> p) -> new SimpleStringProperty(
                        p.getValue().getKey().getLastName()));
        tableColumnDate.setCellValueFactory(
                (TableColumn.CellDataFeatures<Map.Entry<User, LocalDateTime>, String> p) -> new SimpleStringProperty(
                        p.getValue().getValue().format(DBConstants.DATE_TIME_FORMATTER)));
        tableView.setItems(FXCollections.observableArrayList(model.entrySet()));
    }

    private void initModel() {
        model.clear();
        Map<User, LocalDateTime> requests = this.requestsService.getAllRequestsForAnUser(userId);
        model.putAll(requests);
        setData();
    }

    public void handleConfirm(ActionEvent actionEvent) {
        Map.Entry<User, LocalDateTime> entry = tableView.getSelectionModel().getSelectedItem();
        this.requestsService.confirmRequest(entry.getKey().getId(), userId);
    }

    public void handleReject(ActionEvent actionEvent) {
        Map.Entry<User, LocalDateTime> entry = tableView.getSelectionModel().getSelectedItem();
        this.requestsService.deleteARequest(entry.getKey().getId(), userId);
    }

    @Override
    public void update(RequestEvent requestEvent) {
        initModel();
    }


    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openApp(actionEvent);
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/profile-view.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(usersService, userId);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
