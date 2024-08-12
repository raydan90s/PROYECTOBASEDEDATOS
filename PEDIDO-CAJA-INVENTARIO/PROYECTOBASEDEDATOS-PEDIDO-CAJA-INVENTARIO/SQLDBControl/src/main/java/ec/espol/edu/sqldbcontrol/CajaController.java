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
import java.time.LocalDateTime;
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

public class CajaController implements Initializable {

    @FXML
    private TableView<Caja> cajasTable;
    @FXML
    private TableColumn<Caja, Integer> idCaja;
    @FXML
    private TableColumn<Caja, Float> gastos;
    @FXML
    private TableColumn<Caja, Float> sobrantes;
    @FXML
    private TableColumn<Caja, Float> valorCajaChiquita;
    @FXML
    private TableColumn<Caja, LocalDateTime> fechaRegistro; // Cambiado a LocalDateTime
    @FXML
    private TableColumn<Caja, Integer> idSucursal;
    @FXML
    private Text volver;
    @FXML
    private Button agregar;
    @FXML
    private Button eliminar;
    @FXML
    private Button modificar;

    private ObservableList<Caja> cajaList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        idCaja.setCellValueFactory(new PropertyValueFactory<>("idCaja"));
        gastos.setCellValueFactory(new PropertyValueFactory<>("gastos"));
        sobrantes.setCellValueFactory(new PropertyValueFactory<>("sobrantes"));
        valorCajaChiquita.setCellValueFactory(new PropertyValueFactory<>("valorCajaChiquita"));
        fechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro")); // Cambiado a LocalDateTime
        idSucursal.setCellValueFactory(new PropertyValueFactory<>("idSucursal"));

        cajaList = FXCollections.observableArrayList();

        try {
            loadCajas();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cajasTable.setItems(cajaList);
    }
     void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }  

    public void loadCajas() throws SQLException {
        cajaList.clear();

        String query = "SELECT * FROM Caja";
        try (Connection connection = Conexion.conectar();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("idCaja");
                float gastos = resultSet.getFloat("gastos");
                float sobrantes = resultSet.getFloat("sobrantes");
                float valorCajaChiquita = resultSet.getFloat("valorCajaChiquita");
                LocalDateTime fechaHora = resultSet.getTimestamp("fechaRegistro").toLocalDateTime();
                int idSucursal = resultSet.getInt("idSucursal");

                Caja caja = new Caja(id, gastos, sobrantes, valorCajaChiquita, fechaHora, idSucursal);
                cajaList.add(caja);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   

    @FXML
    private void abrirAgregarCaja() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarCaja.fxml"));
        Parent root = loader.load();

        AgregarCajaController controller = loader.getController();
        controller.setCajaController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Caja");
        stage.show();
    }

    @FXML
    public void abrirModificarCaja(ActionEvent event) throws IOException {
        Caja cajaSeleccionada = cajasTable.getSelectionModel().getSelectedItem();

        if (cajaSeleccionada != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarCaja.fxml"));
            Parent root = loader.load();

            AgregarCajaController controller = loader.getController();
            controller.setCajaController(this);
            controller.cargarDatosCaja(cajaSeleccionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Caja");
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una caja para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void eliminarCaja() {
        Caja cajaSeleccionada = cajasTable.getSelectionModel().getSelectedItem();

        if (cajaSeleccionada != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmación de Eliminación");
            confirmAlert.setHeaderText("Advertencia");
            confirmAlert.setContentText("¿Estás seguro de que quieres eliminar esta caja?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String query = "DELETE FROM Caja WHERE idCaja = ?";
                    try (Connection connection = Conexion.conectar();
                         PreparedStatement statement = connection.prepareStatement(query)) {

                        statement.setInt(1, cajaSeleccionada.getIdCaja());
                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            cajaList.remove(cajaSeleccionada);
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Caja Eliminada");
                            successAlert.setHeaderText(null);
                            successAlert.setContentText("La caja ha sido eliminada exitosamente.");
                            successAlert.showAndWait();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Hubo un error al intentar eliminar la caja.");
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una caja para eliminar.");
            alert.showAndWait();
        }
    }
}

