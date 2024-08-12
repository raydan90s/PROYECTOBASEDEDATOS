/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sak
 */
public class EmpleadosController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<Empleado> empleadosTable;
    private TableColumn<Empleado, Integer> idColumn;
    @FXML
    private TableColumn<Empleado, String> nombreColumn;
    @FXML
    private TableColumn<Empleado, String> apellidoColumn;
    @FXML
    private TableColumn<Empleado, String> horarioColumn;
    @FXML
    private TableColumn<Empleado, Double> salarioColumn;
    @FXML
    private TableColumn<Empleado, String> sucursalColumn;

    private ObservableList<Empleado> empleadosList;
    @FXML
    private TableColumn<Empleado, String> tipoColumn;
    @FXML
    private Button agregar;
    @FXML
    private Button eliminar;
    @FXML
    private Button modificar;

    @FXML
    private void abrirAgregarEmpleado() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarEmpleado.fxml"));
        Parent root = loader.load();

        AgregarEmpleadoController controller = loader.getController();
        controller.setEmpleadosController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Empleado");
        stage.show();
    }

    @FXML
    public void abrirModificarEmpleado(ActionEvent event) throws IOException {
        // Obtener el empleado seleccionado de la tabla
        Empleado empleadoSeleccionado = empleadosTable.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Cargar la ventana para modificar el empleado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarEmpleado.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana y pasarle la información necesaria
            AgregarEmpleadoController controller = loader.getController();
            controller.setEmpleadosController(this);

            // Pasar el empleado seleccionado al controlador para que los campos se autorellenen
            controller.cargarDatosEmpleado(empleadoSeleccionado);

            // Configurar y mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Empleado");
            stage.show();
        } else {
            // Mostrar alerta si no se selecciona un empleado
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un empleado para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarEmpleado() {
        Empleado empleadoSeleccionado = empleadosTable.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            // Mostrar alerta de confirmación
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmación de Eliminación");
            confirmAlert.setHeaderText("Advertencia");
            confirmAlert.setContentText("Si eliminas este empleado, es posible que se elimine también información relacionada como permisos, pedidos, productos e incidentes. ¿Deseas continuar?");

            // Si el usuario confirma, proceder con la eliminación
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Connection connection = Conexion.conectar();

                        // Determinar qué tipo de empleado es (Mesero o Cocinero) y eliminarlo de la tabla correspondiente
                        String query = "";
                        if (empleadoSeleccionado.getTipoEmpleado().equalsIgnoreCase("Mesero")) {
                            query = "DELETE FROM Mesero WHERE idEmpleado = ?";
                        } else if (empleadoSeleccionado.getTipoEmpleado().equalsIgnoreCase("Cocinero")) {
                            query = "DELETE FROM Cocinero WHERE idEmpleado = ?";
                        }

                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, empleadoSeleccionado.getIdEmpleado());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            empleadosTable.getItems().remove(empleadoSeleccionado);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Empleado Eliminado");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("El empleado ha sido eliminado exitosamente.");
                            successAlert.showAndWait();
                        }

                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Hubo un error al intentar eliminar el empleado.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un empleado para eliminar.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoEmpleado"));
        horarioColumn.setCellValueFactory(new PropertyValueFactory<>("horarioEmpleado"));
        salarioColumn.setCellValueFactory(new PropertyValueFactory<>("salario"));
        sucursalColumn.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal")); // Cambiado a nombreSucursal
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoEmpleado"));

        empleadosList = FXCollections.observableArrayList();

        try {
            loadEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        empleadosTable.setItems(empleadosList);
    }

    public void loadEmpleados() throws SQLException {
        System.out.println("Cargando empleados..."); // Debugging
        empleadosList.clear(); // Limpiar la lista observable para evitar duplicados

        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idEmpleado,nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioMesero AS salario, Sucursal.nombreSucursal, 'Mesero' AS tipoEmpleado "
                + "FROM Mesero JOIN Sucursal ON Mesero.idSucursal = Sucursal.idSucursal "
                + "UNION "
                + "SELECT idEmpleado,nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioCocinero AS salario, Sucursal.nombreSucursal, 'Cocinero' AS tipoEmpleado "
                + "FROM Cocinero JOIN Sucursal ON Cocinero.idSucursal = Sucursal.idSucursal";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("idEmpleado");
            String nombreEmpleado = resultSet.getString("nombreEmpleado");
            String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
            String horarioEmpleado = resultSet.getString("horarioEmpleado");
            double salario = resultSet.getDouble("salario");
            String nombreSucursal = resultSet.getString("nombreSucursal");
            String tipoEmpleado = resultSet.getString("tipoEmpleado");

            Empleado empleado = new Empleado(id, nombreEmpleado, apellidoEmpleado, horarioEmpleado, salario, nombreSucursal, tipoEmpleado);
            empleadosList.add(empleado);
        }

        resultSet.close();
        statement.close();
        connection.close();

        empleadosTable.setItems(null); // Forzar actualización
        empleadosTable.setItems(empleadosList); // Volver a establecer la lista observable en la tabla
        empleadosTable.refresh(); // Refrescar la tabla manualmente

        System.out.println("Empleados cargados: " + empleadosList.size()); // Debugging
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}
