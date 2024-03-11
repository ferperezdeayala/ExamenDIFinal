module com.example.examend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires jasperreports;
    requires java.desktop;


    opens com.example.examend to javafx.fxml;
    exports com.example.examend;
}