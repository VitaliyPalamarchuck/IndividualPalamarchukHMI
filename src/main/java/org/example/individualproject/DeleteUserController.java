package org.example.individualproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class DeleteUserController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField secretWordField;
    @FXML
    private Button deleteButton;

    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
    }

    @FXML
    private void deleteUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String secretWord = secretWordField.getText();

        if (username.isEmpty() || password.isEmpty() || secretWord.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Будь ласка, заповніть усі поля.");
            return;
        }

        int userId = dbHandler.verifyUserForDeletion(username, password, secretWord);

        if (userId != -1) {
            Optional<ButtonType> result = showConfirmationDialog();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = dbHandler.deleteUserAndTransactions(userId);
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Успіх", "Акаунт та всі дані було успішно видалено.");
                    clearFields(); // Просто очищуємо поля
                } else {
                    showAlert(Alert.AlertType.ERROR, "Помилка бази даних", "Не вдалося видалити акаунт.");
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Неправильний логін, пароль або секретне слово.");
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        secretWordField.clear();
    }

    private Optional<ButtonType> showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Остаточне підтвердження");
        alert.setHeaderText("Ви впевнені, що хочете видалити акаунт?");
        alert.setContentText("Ця дія видалить ваш профіль та ВСІ пов'язані з ним транзакції НАЗАВЖДИ. Відновити дані буде неможливо.");
        ReportPanelController.styleAlertDialog(alert);
        return alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ReportPanelController.styleAlertDialog(alert);
        alert.showAndWait();
    }
}
