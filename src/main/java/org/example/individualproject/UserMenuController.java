package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMenuController {
    @FXML
    public AnchorPane ReportAndTransactionPane;

    @FXML
    private Label sayHelloLabel;

    @FXML
    private Button addTransaction;

    @FXML
    private Button backToMainMenu;

    @FXML
    private AnchorPane buttonPaneUSer;

    @FXML
    private AnchorPane contentPaneUser;

    @FXML
    private Button reportCreateButton;

    @FXML
    private Button setLimitButton;

    public void sayHello(String name){
        sayHelloLabel.setText("Вітаю "+name +" !");
    }

    @FXML
    void backToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backToMainMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void createReport(ActionEvent event) {

    }

    @FXML
    void setLimit(ActionEvent event) {
        loadView("SetLimit.fxml");
    }

    @FXML
    void openTransactionAndReportWindow(ActionEvent event) {loadWindow("AddTransaction.fxml");}

    @FXML
    void openReportWindow(ActionEvent event) {loadWindow("ReportPanel.fxml");}

    private void loadView(String fxmlFile) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPaneUser.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWindow(String fxmlFile) {
        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            ReportAndTransactionPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
