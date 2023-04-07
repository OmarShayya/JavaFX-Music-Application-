module com.example.ostunes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jbcrypt;
    requires javafx.media;

    opens com.example.ostunes to javafx.fxml;
    exports com.example.ostunes;
}