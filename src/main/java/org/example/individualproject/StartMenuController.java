package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class StartMenuController {

    @FXML
    public Button goMainMenu;

    public void goMainMenuAction1(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Попередження");
        alert.setHeaderText("Увага!");
        alert.setContentText("Дана програма знаходиться в розробці і далеко не весь функціонал працює.\nБажаєте продовжити?");

        // Застосовуємо єдиний стиль
        ReportPanelController.styleAlertDialog(alert);

        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(choice -> {
            if (choice == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) goMainMenu.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (choice == ButtonType.CANCEL) {
                Stage stage = (Stage) goMainMenu.getScene().getWindow();
                stage.close();
            }
        });
    }
}
