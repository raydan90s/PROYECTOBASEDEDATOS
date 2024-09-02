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

public class ProveedorController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<Proveedor> proveedoresTable;
    @FXML
    private TableColumn<Proveedor, Integer> idColumn;
    @FXML
    private TableColumn<Proveedor, String> nombreColumn;
    @FXML
    private TableColumn<Proveedor, String> apellidoColumn;
    @FXML
    private TableColumn<Proveedor, String> emailColumn;
    @FXML
    private TableColumn<Proveedor, String> telefonoColumn;

    private ObservableList<Proveedor> proveedoresList;
    @FXML
    private Button agregar;
    @FXML
    private Button eliminar;
    @FXML
    private Button modificar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoProveedor"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailProveedor"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefonoProveedor"));

        proveedoresList = FXCollections.observableArrayList();

        try {
            loadProveedores();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        proveedoresTable.setItems(proveedoresList);
    }

    public void loadProveedores() throws SQLException {
        proveedoresList.clear(); // Limpiar la lista observable para evitar duplicados

        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "call ObtenerProveedores()";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("idProveedor");
            String nombreProveedor = resultSet.getString("nombreProveedor");
            String apellidoProveedor = resultSet.getString("apellidoProveedor");
            String emailProveedor = resultSet.getString("emailProveedor");
            String telefonoProveedor = resultSet.getString("telefonoProveedor");

            Proveedor proveedor = new Proveedor(id, nombreProveedor, apellidoProveedor, emailProveedor, telefonoProveedor);
            proveedoresList.add(proveedor);
        }

        resultSet.close();
        statement.close();
        connection.close();

        proveedoresTable.setItems(proveedoresList);
        proveedoresTable.refresh();
    }

    @FXML
    private void abrirAgregarProveedor() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarProveedor.fxml"));
        Parent root = loader.load();

        AgregarProveedorController controller = loader.getController();
        controller.setProveedorController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Proveedor");
        stage.show();
    }

    @FXML
    private void eliminarProveedor() {
        Proveedor proveedorSeleccionado = proveedoresTable.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            // Mostrar alerta de confirmación
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmación de Eliminación");
            confirmAlert.setHeaderText("Advertencia");
            confirmAlert.setContentText("¿Estás seguro de que deseas eliminar este proveedor?");

            // Si el usuario confirma, proceder con la eliminación
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Connection connection = Conexion.conectar();

                        String query = "{CALL EliminarProveedor(?)}";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, proveedorSeleccionado.getId());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            proveedoresTable.getItems().remove(proveedorSeleccionado);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Proveedor Eliminado");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("El proveedor ha sido eliminado exitosamente.");
                            successAlert.showAndWait();
                        }

                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Hubo un error al intentar eliminar el proveedor.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un proveedor para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    public void abrirModificarProveedor(ActionEvent event) throws IOException {
        // Obtener el proveedor seleccionado de la tabla
        Proveedor proveedorSeleccionado = proveedoresTable.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            // Cargar la ventana para modificar el proveedor
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarProveedor.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana y pasarle la información necesaria
            AgregarProveedorController controller = loader.getController();
            controller.setProveedorController(this);

            // Pasar el proveedor seleccionado al controlador para que los campos se autorellenen
            controller.cargarDatosProveedor(proveedorSeleccionado);

            // Configurar y mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Proveedor");
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un proveedor para modificar.");
            alert.showAndWait();
        }
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}
