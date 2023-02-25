module com.calculator.bin {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    // other module declarations

    opens com.calculator.bin to javafx.fxml;

    exports com.calculator.bin;
}
