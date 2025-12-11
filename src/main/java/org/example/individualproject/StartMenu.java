package org.example.individualproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartMenu.class.getResource("StartMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("FinanceManager"); // Змінено тут
        stage.setResizable(false);

        try {
            stage.getIcons().add(new Image(StartMenu.class.getResourceAsStream("/images/logo.png")));
        } catch (Exception e) {
            System.out.println("Іконка не знайдена. Переконайтесь, що /images/logo.png існує.");
        }
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
