package ec.espol.edu.sqldbcontrol;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ec.espol.edu.sqldbcontrol.Conexion;
import ec.espol.edu.sqldbcontrol.EmpleadosController;
import ec.espol.edu.sqldbcontrol.Sucursal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

public class AgregarEmpleadoController implements Initializable {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField horarioField;
    @FXML
    private TextField salarioField;
    @FXML
    private ComboBox<Sucursal> sucursalComboBox;
    @FXML
    private ComboBox<String> tipoField;

    private EmpleadosController empleadosController;
    private Integer idEmpleado = null; // Variable para almacenar el ID del empleado

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Sucursal> sucursales = FXCollections.observableArrayList(obtenerSucursalesDesdeBD());
        sucursalComboBox.setItems(sucursales);
        tipoField.setItems(FXCollections.observableArrayList("Mesero", "Cocinero"));
    }

    public void setEmpleadosController(EmpleadosController empleadosController) {
        this.empleadosController = empleadosController;
    }

    public void cargarDatosEmpleado(Empleado empleado) {
        // Guardar el ID del empleado que se está editando
        this.idEmpleado = empleado.getIdEmpleado();

        // Rellenar los campos con los datos del empleado seleccionado
        nombreField.setText(empleado.getNombreEmpleado());
        apellidoField.setText(empleado.getApellidoEmpleado());
        horarioField.setText(empleado.getHorarioEmpleado());
        salarioField.setText(String.valueOf(empleado.getSalario()));
        tipoField.setValue(empleado.getTipoEmpleado());

        // Seleccionar la sucursal correspondiente en el ComboBox
        for (Sucursal sucursal : sucursalComboBox.getItems()) {
            if (sucursal.getNombreSucursal().equals(empleado.getNombreSucursal())) {
                sucursalComboBox.setValue(sucursal);
                break;
            }
        }
    }

    private List<Sucursal> obtenerSucursalesDesdeBD() {
        // Método para obtener las sucursales desde la base de datos
        List<Sucursal> sucursales = new ArrayList<>();
        try {
            Connection connection = Conexion.conectar();
            String query = "SELECT idSucursal, nombreSucursal, direccionSucursal, telefono, horarioSucursal, idJefe FROM Sucursal";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idSucursal = resultSet.getInt("idSucursal");
                String nombreSucursal = resultSet.getString("nombreSucursal");
                String direccionSucursal = resultSet.getString("direccionSucursal");
                String telefono = resultSet.getString("telefono");
                String horarioSucursal = resultSet.getString("horarioSucursal");
                int idJefe = resultSet.getInt("idJefe");

                sucursales.add(new Sucursal(idSucursal, nombreSucursal, direccionSucursal, telefono, horarioSucursal, idJefe));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucursales;
    }

    @FXML
    private void guardarEmpleado() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String horario = horarioField.getText();
        double salario = Double.parseDouble(salarioField.getText());
        Sucursal sucursalSeleccionada = sucursalComboBox.getValue();
        String tipo = tipoField.getValue();
        String direccionEmpleado = sucursalSeleccionada.getDireccionSucursal();

        try {
            Connection connection = Conexion.conectar();
            String query = "";

            if (idEmpleado == null) {
                // Si idEmpleado es null, se agrega un nuevo empleado
                if (tipo.equals("Mesero")) {
                    query = "INSERT INTO Mesero (nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioMesero, idSucursal, direccionEmpleado) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";
                } else if (tipo.equals("Cocinero")) {
                    query = "INSERT INTO Cocinero (nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioCocinero, idSucursal, direccionEmpleado, asignacionCocinero) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                }

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nombre);
                statement.setString(2, apellido);
                statement.setString(3, horario);
                statement.setDouble(4, salario);
                statement.setInt(5, sucursalSeleccionada.getIdSucursal());
                statement.setString(6, direccionEmpleado);

                if (tipo.equals("Cocinero")) {
                    String asignacionCocinero = "Valor por defecto";
                    statement.setString(7, asignacionCocinero);
                }

                statement.executeUpdate();
                statement.close();

            } else {
                // Si idEmpleado no es null, se actualiza el empleado existente
                if (tipo.equals("Mesero")) {
                    query = "UPDATE Mesero SET nombreEmpleado = ?, apellidoEmpleado = ?, horarioEmpleado = ?, salarioMesero = ?, idSucursal = ?, direccionEmpleado = ? "
                            + "WHERE idEmpleado = ?";
                } else if (tipo.equals("Cocinero")) {
                    query = "UPDATE Cocinero SET nombreEmpleado = ?, apellidoEmpleado = ?, horarioEmpleado = ?, salarioCocinero = ?, idSucursal = ?, direccionEmpleado = ?, asignacionCocinero = ? "
                            + "WHERE idEmpleado = ?";
                }

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nombre);
                statement.setString(2, apellido);
                statement.setString(3, horario);
                statement.setDouble(4, salario);
                statement.setInt(5, sucursalSeleccionada.getIdSucursal());
                statement.setString(6, direccionEmpleado);

                if (tipo.equals("Cocinero")) {
                    String asignacionCocinero = "Valor por defecto";
                    statement.setString(7, asignacionCocinero);
                    statement.setInt(8, idEmpleado); // ID para el WHERE
                } else {
                    statement.setInt(7, idEmpleado); // ID para el WHERE
                }

                statement.executeUpdate();
                statement.close();
            }

            connection.close();

            empleadosController.loadEmpleados();  // Refrescar la tabla

            // Mostrar la alerta de éxito
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Empleado Guardado");
            alert.setHeaderText(null);
            alert.setContentText("El empleado ha sido guardado exitosamente.");
            alert.showAndWait();

            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
