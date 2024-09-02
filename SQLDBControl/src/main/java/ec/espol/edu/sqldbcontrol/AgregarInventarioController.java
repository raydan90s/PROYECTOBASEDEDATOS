package ec.espol.edu.sqldbcontrol;

import java.net.URL;
import java.sql.CallableStatement;
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
    @FXML
    private ComboBox<Proveedor> proveedoresComboBox;

    private Integer idInventario = null;
    private InventarioController inventarioController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Jefe> jefes = FXCollections.observableArrayList(obtenerJefesDesdeBD());
        jefesComboBox.setItems(jefes);
        ObservableList<Proveedor> proveedores = FXCollections.observableArrayList(obtenerProveedoresDesdeBD());
        proveedoresComboBox.setItems(proveedores);
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

    for (Proveedor proveedor : proveedoresComboBox.getItems()) {
        if (proveedor.getId() == inventario.getIdProveedor()) { // Asegúrate de tener este campo en la clase Inventario
            proveedoresComboBox.setValue(proveedor);
            break;
        }
    }
}

    private List<Proveedor> obtenerProveedoresDesdeBD() {
    List<Proveedor> proveedores = new ArrayList<>();
    String query = "SELECT * FROM Proveedor";

    try (Connection connection = Conexion.conectar();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            int idProveedor = resultSet.getInt("idProveedor");
            String nombreProveedor = resultSet.getString("nombreProveedor");
            String apellidoProveedor = resultSet.getString("apellidoProveedor");
            String emailProveedor = resultSet.getString("emailProveedor");
            String telefonoProveedor = resultSet.getString("telefonoProveedor");
            

            proveedores.add(new Proveedor(idProveedor, nombreProveedor, apellidoProveedor, emailProveedor, telefonoProveedor));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return proveedores;
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
    Proveedor proveedorSeleccionado = proveedoresComboBox.getValue();

    try (Connection connection = Conexion.conectar()) {
        CallableStatement statement;

        if (idInventario == null) {
            // Llamada al procedimiento almacenado `insertarInventario`
            statement = connection.prepareCall("{CALL insertarInventario(?, ?, ?)}");
            statement.setInt(1, jefeSeleccionado.getIdJefe());
            statement.setString(2, nombreInventario);
            statement.setInt(3, proveedorSeleccionado.getId());
        } else {
            // Llamada al procedimiento almacenado `actualizarInventario`
            statement = connection.prepareCall("{CALL actualizarInventario(?, ?, ?, ?)}");
            statement.setInt(1, idInventario);
            statement.setInt(2, jefeSeleccionado.getIdJefe());
            statement.setString(3, nombreInventario);
            statement.setInt(4, proveedorSeleccionado.getId());
        }

        statement.execute();
        statement.close();

        inventarioController.loadInventario();

        Stage stage = (Stage) nombre.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("Inventario guardado exitosamente.");
        alert.showAndWait();

    } catch (SQLException e) {
        e.printStackTrace();
        mostrarError("Hubo un error al guardar el inventario.");
    }
}

private void mostrarError(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
}
