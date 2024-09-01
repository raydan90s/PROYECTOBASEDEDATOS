module ec.espol.edu.sqldbcontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.logging;

    opens ec.espol.edu.sqldbcontrol to javafx.fxml;
    exports ec.espol.edu.sqldbcontrol;
    requires javafx.graphicsEmpty;
    requires java.base;
    requires java.desktop;
    
}
