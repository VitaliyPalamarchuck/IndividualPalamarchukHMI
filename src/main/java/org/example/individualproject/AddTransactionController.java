package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class AddTransactionController {

    @FXML
    private Button addTransactionButton;

    @FXML
    private Button closeTransactionMenu;

    @FXML
    private ComboBox<String> transactionTypeBox;


    @FXML
    public void initialize() {

        transactionTypeBox.getItems().addAll(
                "Дохід","Витрата"
        );

        transactionTypeBox.setOnAction(e -> {
            String selected = transactionTypeBox.getValue();
            System.out.println("Вибрано: " + selected);
        });
    }


    @FXML
    private void closeWindow(ActionEvent actionEvent) {
        ((Pane) closeTransactionMenu.getParent().getParent()).getChildren().clear();
    }
}
