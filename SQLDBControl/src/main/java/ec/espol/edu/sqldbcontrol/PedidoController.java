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
import java.sql.Timestamp;
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
 * @author Diego
 */
public class PedidoController implements Initializable {
    @FXML
    private Text volver;
    @FXML
    private TableView<Pedido> pedidosTable;
    @FXML
    private TableColumn<Pedido, Integer> idPedido;
    @FXML
    private TableColumn<Pedido, Timestamp> fechaPedido;
    @FXML
    private TableColumn<Pedido, Integer> cantidadPedido;
    @FXML
    private TableColumn<Pedido, Integer> idEmpleado;
    @FXML
    private TableColumn<Pedido, Integer> idCaja;
    @FXML
    private TableColumn<Pedido, Integer> idCliente;
    @FXML
    private TableColumn<Pedido, Integer> idProducto;
    

    private ObservableList<Pedido> pedidosList;
    @FXML
    private Button agregar;
    @FXML
    private Button eliminar;
    @FXML
    private Button modificar;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void abrirAgregarPedido() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarPedido.fxml"));
        Parent root = loader.load();

        AgregarPedidoController controller = loader.getController();
        controller.setPedidoController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Pedido");
        stage.show();
    }
    @FXML
    public void abrirModificarPedido(ActionEvent event) throws IOException {
        // Obtener el pedido seleccionado de la tabla
        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado != null) {
            // Cargar la ventana para modificar el pedido
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarPedido.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la nueva ventana y pasarle la información necesaria
            AgregarPedidoController controller = loader.getController();
            controller.setPedidoController(this);

            // Pasar el pedido seleccionado al controlador para que los campos se autorellenen
            controller.cargarDatosPedido(pedidoSeleccionado);

            // Configurar y mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Pedido");
            stage.show();
        } else {
            // Mostrar alerta si no se selecciona un pedido
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un pedido para modificar.");
            alert.showAndWait();
        }
    }
    @FXML
    private void eliminarPedido() {
        Pedido pedidoSeleccionado = pedidosTable.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado != null) {
            // Mostrar alerta de confirmación
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmación de Eliminación");
            confirmAlert.setHeaderText("Advertencia");
            confirmAlert.setContentText("Si eliminas este pedido, es posible que se elimine también información relacionada. ¿Deseas continuar?");

            // Si el usuario confirma, proceder con la eliminación
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Connection connection = Conexion.conectar();

                        // Eliminar el pedido de la tabla correspondiente
                        String query = "DELETE FROM Pedido WHERE idPedido = ?";
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, pedidoSeleccionado.getIdPedido());

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            pedidosTable.getItems().remove(pedidoSeleccionado);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Pedido Eliminado");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("El pedido ha sido eliminado exitosamente.");
                            successAlert.showAndWait();
                        }

                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Hubo un error al intentar eliminar el pedido.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un pedido para eliminar.");
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
        idPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        fechaPedido.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        cantidadPedido.setCellValueFactory(new PropertyValueFactory<>("cantidadPedido"));
        idEmpleado.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        idCaja.setCellValueFactory(new PropertyValueFactory<>("idCaja"));
        idCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        idProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        
        pedidosList = FXCollections.observableArrayList();

        try {
            loadPedidos();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pedidosTable.setItems(pedidosList);
    }
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }    
    public void loadPedidos() throws SQLException {
        System.out.println("Cargando pedidos..."); // Debugging
        pedidosList.clear(); // Limpiar la lista observable para evitar duplicados

        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "Select * from Pedido";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("idPedido");
            Timestamp fechaPedido = resultSet.getTimestamp("fechaPedido");
            int cantidadPedido = resultSet.getInt("cantidadPedido");
            int idEmpleado = resultSet.getInt("idEmpleado");
            int idCaja = resultSet.getInt("idCaja");
            int idCliente = resultSet.getInt("idCliente");
            int idProducto = resultSet.getInt("idProducto");

            Pedido pedido = new Pedido(id, fechaPedido, cantidadPedido, idEmpleado, idCaja, idCliente, idProducto);
            pedidosList.add(pedido);
        }

        resultSet.close();
        statement.close();
        connection.close();

        pedidosTable.setItems(null); // Forzar actualización
        pedidosTable.setItems(pedidosList); // Volver a establecer la lista observable en la tabla
        pedidosTable.refresh(); // Refrescar la tabla manualmente

        System.out.println("Pedidos cargados: " + pedidosList.size()); // Debugging
    }
    
}
