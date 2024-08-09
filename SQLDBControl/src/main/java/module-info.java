module ec.espol.edu.sqldbcontrol {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires java.logging;
    

    opens ec.espol.edu.sqldbcontrol to javafx.fxml;
    exports ec.espol.edu.sqldbcontrol;
    requires javafx.graphicsEmpty;
    requires java.base;
}
