package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IncidenteController implements Initializable {

    @FXML
    private Text volver; 
    @FXML
    private TableView<Incidente> incidentesTable;
    @FXML
    private TableColumn<Incidente, Integer> idIncidenteColumn;
    @FXML
    private TableColumn<Incidente, String> accionTomadaColumn;
    @FXML
    private TableColumn<Incidente, String> descripcionColumn;
    @FXML
    private TableColumn<Incidente, String> fechaIncidenteColumn;
    @FXML
    private TableColumn<Incidente, Integer> idJefeColumn;
    @FXML
    private TableColumn<Incidente, Integer> idEmpleadoColumn;

    private ObservableList<Incidente> incidentesList;

    @FXML
    private Button agregar;
    @FXML
    private Button modificar;
    @FXML
    private Button eliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        idIncidenteColumn.setCellValueFactory(new PropertyValueFactory<>("idIncidente"));
        accionTomadaColumn.setCellValueFactory(new PropertyValueFactory<>("accionTomada"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fechaIncidenteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaIncidente"));
        idJefeColumn.setCellValueFactory(new PropertyValueFactory<>("idJefe"));
        idEmpleadoColumn.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));

        incidentesList = FXCollections.observableArrayList();

        try {
            loadIncidentes();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        incidentesTable.setItems(incidentesList);
    }

    public void loadIncidentes() throws SQLException {
        incidentesList.clear();

        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "call ObtenerIncidentes()";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int idIncidente = resultSet.getInt("idIncidente");
            String accionTomada = resultSet.getString("accionTomada");
            String descripcion = resultSet.getString("descripcion");
            Date fechaIncidente = resultSet.getDate("fechaIncidente");
            int idJefe = resultSet.getInt("idJefe");
            int idEmpleado = resultSet.getInt("idEmpleado");

            Incidente incidente = new Incidente(idIncidente, accionTomada, descripcion, fechaIncidente, idJefe, idEmpleado);
            incidentesList.add(incidente);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    @FXML
    private void abrirAgregarIncidente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarIncidente.fxml"));
        Parent root = loader.load();

        AgregarIncidenteController controller = loader.getController();
        controller.setIncidenteController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Agregar Incidente");
        stage.show();
    }

    @FXML
    private void abrirModificarIncidente(ActionEvent event) throws IOException {
        Incidente selectedIncidente = incidentesTable.getSelectionModel().getSelectedItem();
        
        if (selectedIncidente != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarIncidente.fxml"));
            Parent root = loader.load();

            AgregarIncidenteController controller = loader.getController();
            controller.setIncidenteController(this);
            controller.setIncidenteToModify(selectedIncidente);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Incidente");
            stage.show();
        } else {
            System.out.println("Por favor, seleccione un incidente para modificar.");
        }
    }

    @FXML
    private void eliminarIncidente(ActionEvent event) {
        Incidente selectedIncidente = incidentesTable.getSelectionModel().getSelectedItem();
        
        if (selectedIncidente != null) {
            try {
                Connection connection = Conexion.conectar();
                String query = "{CALL EliminarIncidente(?)}";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedIncidente.getIdIncidente());

                statement.executeUpdate();

                statement.close();
                connection.close();

                incidentesList.remove(selectedIncidente); // Eliminar de la lista observable

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Por favor, seleccione un incidente para eliminar.");
        }
    }
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}

           

