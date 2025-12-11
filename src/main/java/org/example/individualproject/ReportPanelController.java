package org.example.individualproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.List;

public class ReportPanelController {

    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    @FXML
    private TableColumn<Transaction, Double> balanceColumn;

    private User currentUser;
    private DatabaseHandler dbHandler;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadTransactionData();
    }

    @FXML
    public void initialize() {
        dbHandler = new DatabaseHandler();

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFormatted"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("runningBalance"));

        formatAmountColumn(amountColumn, true);
        formatAmountColumn(balanceColumn, false);

        transactionsTable.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    showFullDescription(row.getItem());
                }
            });
            
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    row.getStyleClass().removeAll("income-row", "expense-row");
                    if ("Дохід".equals(newItem.getType())) {
                        row.getStyleClass().add("income-row");
                    } else {
                        row.getStyleClass().add("expense-row");
                    }
                } else {
                    row.getStyleClass().removeAll("income-row", "expense-row");
                }
            });
            return row;
        });
    }

    private void loadTransactionData() {
        if (currentUser != null) {
            List<Transaction> transactionList = dbHandler.getTransactionsForUser(currentUser.getId());
            calculateRunningBalance(transactionList);
            ObservableList<Transaction> observableList = FXCollections.observableArrayList(transactionList);
            transactionsTable.setItems(observableList);
        }
    }

    private void calculateRunningBalance(List<Transaction> transactions) {
        double runningBalance = currentUser.getBalance();
        for (Transaction t : transactions) {
            t.setRunningBalance(runningBalance);
            if ("Дохід".equals(t.getType())) {
                runningBalance -= t.getAmount();
            } else {
                runningBalance += t.getAmount();
            }
        }
    }

    private void formatAmountColumn(TableColumn<Transaction, Double> column, boolean showSign) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    double value = item;
                    if (showSign && getTableView().getItems().size() > getIndex()) {
                        Transaction transaction = getTableView().getItems().get(getIndex());
                        if ("Витрата".equals(transaction.getType())) {
                            value = -item;
                        }
                    }
                    // Виправлено: викликаємо статичний метод через його клас
                    setText(FormatUtils.formatLargeNumber(value));
                }
            }
        });
    }

    private void showFullDescription(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Повний опис транзакції");
        alert.setHeaderText("Опис для транзакції від " + transaction.getDateFormatted());
        
        TextArea textArea = new TextArea(transaction.getDescription());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 150);

        alert.getDialogPane().setContent(textArea);
        styleAlertDialog(alert);
        alert.showAndWait();
    }
    
    public static void styleAlertDialog(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(ReportPanelController.class.getResource("/styles/AlertStyles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        Stage stage = (Stage) dialogPane.getScene().getWindow();
        try {
            stage.getIcons().add(new Image(ReportPanelController.class.getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.out.println("Іконка не знайдена. Переконайтесь, що /images/logo.png існує.");
        }
    }
}
