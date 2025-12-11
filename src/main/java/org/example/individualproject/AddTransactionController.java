package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTransactionController {

    @FXML
    private BorderPane mainPane;
    @FXML
    private TextArea transactionGoalField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> transactionTypeBox;
    @FXML
    private Button addTransactionButton;
    @FXML
    private Button closeTransactionMenu;

    private User currentUser;
    private UserMenuController userMenuController;
    private DatabaseHandler dbHandler;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public void setCurrentUserAndMenu(User user, UserMenuController menuController) {
        this.currentUser = user;
        this.userMenuController = menuController;
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
        setupDatePicker();
        datePicker.setValue(LocalDate.now());

        transactionTypeBox.getItems().addAll("Дохід", "Витрата");

        transactionTypeBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    getStyleClass().removeAll("income-cell", "expense-cell");
                    if (item.equals("Дохід")) {
                        getStyleClass().add("income-cell");
                    } else if (item.equals("Витрата")) {
                        getStyleClass().add("expense-cell");
                    }
                }
            }
        });

        transactionTypeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });

        addTransactionButton.setOnAction(this::saveTransaction);
    }

    private void setupDatePicker() {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        });
    }

    private void saveTransaction(ActionEvent event) {
        String type = transactionTypeBox.getValue();
        String amountStr = amountField.getText();
        String description = transactionGoalField.getText();
        LocalDate date = datePicker.getValue();

        if (type == null || amountStr.isEmpty() || date == null) {
            showAlert(Alert.AlertType.ERROR, "Помилка валідації", "Будь ласка, заповніть тип, суму та дату.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка формату", "Сума має бути числовим значенням.");
            return;
        }

        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Помилка системи", "Не вдалося визначити поточного користувача.");
            return;
        }

        boolean isAdded = dbHandler.addTransactionAndUpdateBalance(currentUser.getId(), type, amount, description, date);

        if (isAdded) {
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Транзакцію успішно додано!");
            clearFields();
            if (userMenuController != null) {
                userMenuController.updateUserInfo();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося зберегти транзакцію. Можливо, недостатньо коштів на балансі.");
        }
    }

    private void clearFields() {
        transactionTypeBox.setValue(null);
        amountField.clear();
        transactionGoalField.clear();
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void closeWindow(ActionEvent actionEvent) {
        if (mainPane != null) {
            mainPane.setVisible(false);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Застосовуємо стилі
        ReportPanelController.styleAlertDialog(alert);

        alert.showAndWait();
    }
}
