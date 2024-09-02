package ec.espol.edu.sqldbcontrol;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarInventarioController implements Initializable {

    @FXML
    private TextField nombre;
    @FXML
    private ComboBox<Jefe> jefesComboBox;

    private Integer idInventario = null;
    private InventarioController inventarioController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Jefe> jefes = FXCollections.observableArrayList(obtenerJefesDesdeBD());
        jefesComboBox.setItems(jefes);
    }

    public void setInventarioController(InventarioController inventarioController) {
        this.inventarioController = inventarioController;
    }

    public void cargarDatosInventario(Inventario inventario) {
        this.idInventario = inventario.getIdInventario();
        nombre.setText(inventario.getNombre());

        for (Jefe jefe : jefesComboBox.getItems()) {
            if (jefe.getIdJefe() == inventario.getIdJefe()) {
                jefesComboBox.setValue(jefe);
                break;
            }
        }
    }

    private List<Jefe> obtenerJefesDesdeBD() {
        List<Jefe> jefes = new ArrayList<>();
        String query = "SELECT * FROM Jefe";

        try (Connection connection = Conexion.conectar();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idJefe = resultSet.getInt("idJefe");
                String nombreJefe = resultSet.getString("nombreJefe");
                String apellidoJefe = resultSet.getString("apellidoJefe");

                // Obtener el Timestamp del ResultSet y convertirlo a LocalDateTime
                Timestamp timestamp = resultSet.getTimestamp("ultActualizacion");
                LocalDateTime ultActualizacion = timestamp != null ? timestamp.toLocalDateTime() : null;

                String telefonoJefe = resultSet.getString("telefonoJefe");

                jefes.add(new Jefe(idJefe, nombreJefe, apellidoJefe, ultActualizacion, telefonoJefe));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jefes;
    }


    @FXML
    private void guardarInventario() {
        String nombreInventario = nombre.getText();
        Jefe jefeSeleccionado = jefesComboBox.getValue();

        try {
            Connection connection = Conexion.conectar();
            String query = "";

            if (idInventario == null) {
                query = "INSERT INTO Inventario (idJefe, nombre) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, jefeSeleccionado.getIdJefe());
                statement.setString(2, nombreInventario);
                statement.executeUpdate();
                statement.close();
            } else {
                query = "UPDATE Inventario SET idJefe = ?, nombre = ? WHERE idInventario = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, jefeSeleccionado.getIdJefe());
                statement.setString(2, nombreInventario);
                statement.setInt(3, idInventario);
                statement.executeUpdate();
                statement.close();
            }

            connection.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Inventario guardado exitosamente.");
            alert.showAndWait();

            Stage stage = (Stage) nombre.getScene().getWindow();
            stage.close();

            inventarioController.loadInventario();
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hubo un error al guardar el inventario.");
            alert.showAndWait();
        }
    }
}
