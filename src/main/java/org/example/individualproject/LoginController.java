package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button enterAccount;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passField;

    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        DatabaseHandler.initDatabase();
    }

    @FXML
    public void enterAccount(ActionEvent event) {
        String username = loginField.getText();
        String password = passField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Помилка входу", "Будь ласка, введіть логін та пароль.");
            return;
        }

        User user = dbHandler.loginUser(username, password);

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserMenu.fxml"));
                Parent root = loader.load();
                UserMenuController userMenuController = loader.getController();
                userMenuController.setCurrentUser(user);
                Stage stage = (Stage) enterAccount.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Помилка завантаження UserMenu.fxml:");
                e.printStackTrace(); // Додано для детального логування
                showAlert(AlertType.ERROR, "Помилка завантаження", "Не вдалося завантажити меню користувача.");
            }
        } else {
            showAlert(AlertType.ERROR, "Помилка входу", "Неправильний логін або пароль.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        ReportPanelController.styleAlertDialog(alert);

        alert.showAndWait();
    }
}
