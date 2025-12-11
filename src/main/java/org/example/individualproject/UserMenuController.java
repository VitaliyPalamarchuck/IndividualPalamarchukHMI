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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox; // Імпортуємо VBox
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class UserMenuController {
    @FXML
    public AnchorPane ReportAndTransactionPane;
    @FXML
    private Label sayHelloLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button addTransaction;
    @FXML
    private Button backToMainMenu;
    @FXML
    private AnchorPane buttonPaneUSer;
    @FXML
    private VBox contentPaneUser; // Змінено тип на VBox
    @FXML
    private Button reportCreateButton;
    @FXML
    private Button settingsButton;

    // Нові поля для огляду за місяць
    @FXML
    private Label monthlyIncomeLabel;
    @FXML
    private Label monthlyExpensesLabel;
    @FXML
    private Label monthlyNetLabel;

    private User currentUser;
    private DatabaseHandler dbHandler;

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserInfo();
    }

    public void updateUserInfo() {
        // Оновлення загального балансу
        double newBalance = dbHandler.getUserBalance(currentUser.getId());
        currentUser.setBalance(newBalance);
        
        sayHelloLabel.setText("Вітаю, " + currentUser.getName() + "!");
        balanceLabel.setText(FormatUtils.formatLargeNumber(currentUser.getBalance()) + " грн");
        
        if (currentUser.getBalance() >= 0) {
            balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        } else {
            balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #C62828;");
        }

        // Оновлення огляду за місяць
        updateMonthlyOverview();
    }

    private void updateMonthlyOverview() {
        if (currentUser == null) return;

        LocalDate now = LocalDate.now();
        double monthlyIncome = dbHandler.getMonthlyIncome(currentUser.getId(), now);
        double monthlyExpenses = dbHandler.getMonthlyExpenses(currentUser.getId(), now);
        double monthlyNet = monthlyIncome - monthlyExpenses;

        monthlyIncomeLabel.setText("Дохід: " + FormatUtils.formatLargeNumber(monthlyIncome) + " грн");
        monthlyExpensesLabel.setText("Витрати: " + FormatUtils.formatLargeNumber(monthlyExpenses) + " грн");
        
        monthlyNetLabel.setText("Чистий дохід: " + FormatUtils.formatLargeNumber(monthlyNet) + " грн");
        if (monthlyNet >= 0) {
            monthlyNetLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        } else {
            monthlyNetLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #C62828;");
        }
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
    void openSettings(ActionEvent event) {
        loadWindow("Settings.fxml");
    }

    @FXML
    void openTransactionAndReportWindow(ActionEvent event) {
        loadWindow("AddTransaction.fxml");
    }

    @FXML
    void openReportWindow(ActionEvent event) {
        loadWindow("ReportPanel.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            // Оскільки contentPaneUser тепер VBox, ми не можемо просто додати AnchorPane
            // Якщо fxmlFile завантажує AnchorPane, його потрібно обгорнути або змінити логіку
            // Для простоти, якщо це AnchorPane, ми просто додамо його
            Parent pane = loader.load();
            contentPaneUser.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWindow(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent pane = loader.load();
            
            if (fxmlFile.equals("AddTransaction.fxml")) {
                AddTransactionController controller = loader.getController();
                controller.setCurrentUserAndMenu(currentUser, this);
            } else if (fxmlFile.equals("ReportPanel.fxml")) {
                ReportPanelController controller = loader.getController();
                controller.setCurrentUser(currentUser);
            } else if (fxmlFile.equals("Settings.fxml")) {
                SettingsController controller = loader.getController();
                controller.setCurrentUserAndMenu(currentUser, this);
            }

            StackPane stackPane = new StackPane(pane);
            ReportAndTransactionPane.getChildren().setAll(stackPane);
            AnchorPane.setTopAnchor(stackPane, 0.0);
            AnchorPane.setBottomAnchor(stackPane, 0.0);
            AnchorPane.setLeftAnchor(stackPane, 0.0);
            AnchorPane.setRightAnchor(stackPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
