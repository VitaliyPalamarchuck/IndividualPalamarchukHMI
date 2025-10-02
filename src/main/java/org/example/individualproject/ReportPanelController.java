package org.example.individualproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ReportPanelController {

    @FXML
    private Button closeReportWindow;

    @FXML
    private void closeWindow(ActionEvent actionEvent) {
        ((Pane) closeReportWindow.getParent().getParent()).getChildren().clear();
    }

}
