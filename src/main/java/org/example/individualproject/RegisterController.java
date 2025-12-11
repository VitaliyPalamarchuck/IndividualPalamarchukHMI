package org.example.individualproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField balanceField;
    @FXML
    private PasswordField secretWordField;

    @FXML
    private Button registerButton;

    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        DatabaseHandler.initDatabase();
    }

    @FXML
    private void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String balanceStr = balanceField.getText();
        String secretWord = secretWordField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || balanceStr.isEmpty() || secretWord.isEmpty()) {
            showAlert(AlertType.ERROR, "Помилка реєстрації", "Будь ласка, заповніть усі поля.");
            return;
        }

        // Нова перевірка довжини пароля
        if (password.length() < 4) {
            showAlert(AlertType.ERROR, "Помилка реєстрації", "Пароль має бути не менше 4 символів.");
            return;
        }

        try {
            double balance = Double.parseDouble(balanceStr);
            boolean isRegistered = dbHandler.registerUser(username, password, name, balance, secretWord);

            if (isRegistered) {
                showAlert(AlertType.INFORMATION, "Успіх", "Користувача успішно зареєстровано!");
                clearFields();
            } else {
                showAlert(AlertType.ERROR, "Помилка реєстрації", "Користувач з таким логіном вже існує.");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Помилка формату", "Баланс має бути числовим значенням.");
        }
    }
    
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        nameField.clear();
        balanceField.clear();
        secretWordField.clear();
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
