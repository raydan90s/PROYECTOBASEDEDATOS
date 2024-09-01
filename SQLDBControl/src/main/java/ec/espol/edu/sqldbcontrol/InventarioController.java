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

public class InventarioController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<Inventario> inventarioTable;
    @FXML
    private TableColumn<Inventario, Integer> idInventario;
    @FXML
    private TableColumn<Inventario, Integer> idJefe;
    @FXML
    private TableColumn<Inventario, String> nombreInventario;
    private ObservableList<Inventario> inventarioList;

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
        idInventario.setCellValueFactory(new PropertyValueFactory<>("idInventario"));
        idJefe.setCellValueFactory(new PropertyValueFactory<>("idJefe"));
        nombreInventario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        inventarioList = FXCollections.observableArrayList();
        try {
            loadInventario();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inventarioTable.setItems(inventarioList);
    }

    @FXML
    private void abrirAgregarInventario() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarInventario.fxml"));
        Parent root = loader.load();

        AgregarInventarioController controller = loader.getController();
        controller.setInventarioController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Inventario");
        stage.show();
    }

    @FXML
    public void abrirModificarInventario(ActionEvent event) throws IOException {
        Inventario inventarioSeleccionado = inventarioTable.getSelectionModel().getSelectedItem();

        if (inventarioSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarInventario.fxml"));
            Parent root = loader.load();

            AgregarInventarioController controller = loader.getController();
            controller.setInventarioController(this);
            controller.cargarDatosInventario(inventarioSeleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Inventario");
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un inventario para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarInventario() {
        Inventario inventarioSeleccionado = inventarioTable.getSelectionModel().getSelectedItem();

        if (inventarioSeleccionado != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmación de Eliminación");
            confirmAlert.setHeaderText("Advertencia");
            confirmAlert.setContentText("¿Deseas eliminar este inventario?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Connection connection = Conexion.conectar();

                        String query = "DELETE FROM Inventario WHERE idInventario = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, inventarioSeleccionado.getIdInventario());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            inventarioTable.getItems().remove(inventarioSeleccionado);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Inventario Eliminado");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("El inventario ha sido eliminado exitosamente.");
                            successAlert.showAndWait();
                        }

                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Hubo un error al intentar eliminar el inventario.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un inventario para eliminar.");
            alert.showAndWait();
        }
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }

    public void loadInventario() throws SQLException {
        inventarioList.clear();

        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM Inventario";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("idInventario");
            int idJefe = resultSet.getInt("idJefe");
            String nombre = resultSet.getString("nombre");

            Inventario inventario = new Inventario(id, idJefe, nombre);
            inventarioList.add(inventario);
        }

        resultSet.close();
        statement.close();
        connection.close();

        inventarioTable.setItems(null);
        inventarioTable.setItems(inventarioList);
        inventarioTable.refresh();
    }
}
