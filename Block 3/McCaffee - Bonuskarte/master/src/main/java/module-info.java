module com.quantenquellcode {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;

    opens com.quantenquellcode to javafx.fxml;
    exports com.quantenquellcode;
}
