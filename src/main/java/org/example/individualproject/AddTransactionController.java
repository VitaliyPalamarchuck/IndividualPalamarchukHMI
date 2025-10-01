package org.example.individualproject;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AddTransactionController {

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

}
