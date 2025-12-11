package org.example.individualproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingsController {

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button saveButton;

    private User currentUser;
    private UserMenuController userMenuController;
    private DatabaseHandler dbHandler;

    public void setCurrentUserAndMenu(User user, UserMenuController menuController) {
        this.currentUser = user;
        this.userMenuController = menuController;
        // Заповнюємо поле з поточним іменем
        nameField.setText(currentUser.getName());
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
    }

    @FXML
    private void saveSettings() {
        boolean nameChanged = false;
        boolean passwordChanged = false;

        // 1. Оновлення імені
        String newName = nameField.getText();
        if (!newName.isEmpty() && !newName.equals(currentUser.getName())) {
            if (dbHandler.updateUserName(currentUser.getId(), newName)) {
                currentUser.setName(newName); // Оновлюємо об'єкт
                nameChanged = true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося оновити ім'я.");
                return;
            }
        }

        // 2. Оновлення паролю
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Якщо хоча б одне поле паролю заповнене, намагаємося змінити
        if (!currentPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Нові паролі не співпадають.");
                return;
            }
            if (newPassword.length() < 4) { // Проста валідація
                showAlert(Alert.AlertType.ERROR, "Помилка", "Новий пароль має бути не менше 4 символів.");
                return;
            }

            if (dbHandler.updateUserPassword(currentUser.getId(), currentPassword, newPassword)) {
                passwordChanged = true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося змінити пароль. Перевірте поточний пароль.");
                return;
            }
        }

        if (nameChanged || passwordChanged) {
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Зміни успішно збережено!");
            if (userMenuController != null) {
                userMenuController.updateUserInfo(); // Оновлюємо привітання в головному меню
            }
            clearPasswordFields();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Інформація", "Ви не внесли жодних змін.");
        }
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
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
