package ec.espol.edu.sqldbcontrol;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarProveedorController implements Initializable {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telefonoField;

    private ProveedorController proveedorController;
    private Integer idProveedor = null; // Variable para almacenar el ID del proveedor

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization if needed
    }

    public void setProveedorController(ProveedorController proveedorController) {
        this.proveedorController = proveedorController;
    }

    public void cargarDatosProveedor(Proveedor proveedor) {
        this.idProveedor = proveedor.getId();
        nombreField.setText(proveedor.getNombreProveedor());
        apellidoField.setText(proveedor.getApellidoProveedor());
        emailField.setText(proveedor.getEmailProveedor());
        telefonoField.setText(proveedor.getTelefonoProveedor());
    }

    @FXML
    private void guardarProveedor() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();

        try {
            Connection connection = Conexion.conectar();
            String query;

            if (idProveedor == null) {
                // Si idProveedor es null, se agrega un nuevo proveedor
                query = "{CALL InsertarProveedor(?, ?, ?, ?)}";
            } else {
                // Si idProveedor no es null, se actualiza el proveedor existente
                query = "{CALL ActualizarProveedor(?, ?, ?, ?, ?)}";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, email);
            statement.setString(4, telefono);

            if (idProveedor != null) {
                statement.setInt(5, idProveedor); // ID para el WHERE
            }

            statement.executeUpdate();
            statement.close();
            connection.close();

            proveedorController.loadProveedores();  // Refrescar la tabla

            // Mostrar la alerta de éxito
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Proveedor Guardado");
            alert.setHeaderText(null);
            alert.setContentText("El proveedor ha sido guardado exitosamente.");
            alert.showAndWait();

            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

