module org.example.individualproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.individualproject to javafx.fxml;
    exports org.example.individualproject;
}