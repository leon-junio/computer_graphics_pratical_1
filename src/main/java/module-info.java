module com.leonjr {
    requires transitive javafx.graphics;
    requires lombok;
    requires transitive javafx.swing;
    requires javafx.controls;
    requires transitive javafx.fxml;
    requires java.desktop;
    requires atlantafx.base;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    opens com.leonjr to javafx.fxml;
    opens com.leonjr.controllers to javafx.fxml;
    exports com.leonjr;
}