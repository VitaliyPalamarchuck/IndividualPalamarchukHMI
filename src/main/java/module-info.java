module org.example.individualproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens org.example.individualproject to javafx.fxml;
    exports org.example.individualproject;
}