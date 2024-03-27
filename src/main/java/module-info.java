module com.example.mysocialapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.mysocialapp to javafx.fxml;
    exports com.example.mysocialapp;

    exports com.example.mysocialapp.service; // Exportă pachetul domain

}