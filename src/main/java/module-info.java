module com.example.mysocialapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mysocialapp to javafx.fxml;
    exports com.example.mysocialapp;
}