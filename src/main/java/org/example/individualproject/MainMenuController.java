package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private AnchorPane mainPane;
    @FXML
    void closeProgram(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void showDeleteMenu(ActionEvent event) {
        loadView("DeleteUser.fxml");
    }

    @FXML
    void showLoginMenu(ActionEvent event) {
        loadView("Login.fxml");
    }

    @FXML
    void showRegisterMenu(ActionEvent event) {
        loadView("Register.fxml");
    }

    private void loadView(String fxmlFile) {
    try {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
        contentPane.getChildren().setAll(pane);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
