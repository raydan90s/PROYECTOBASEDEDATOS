/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

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

    private void loadEmpleados() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioMesero AS salario, Sucursal.nombreSucursal, 'Mesero' AS tipoEmpleado "
                + "FROM Mesero JOIN Sucursal ON Mesero.idSucursal = Sucursal.idSucursal "
                + "UNION "
                + "SELECT nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioCocinero AS salario, Sucursal.nombreSucursal, 'Cocinero' AS tipoEmpleado "
                + "FROM Cocinero JOIN Sucursal ON Cocinero.idSucursal = Sucursal.idSucursal";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String nombreEmpleado = resultSet.getString("nombreEmpleado");
            String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
            String horarioEmpleado = resultSet.getString("horarioEmpleado");
            double salario = resultSet.getDouble("salario");
            String nombreSucursal = resultSet.getString("nombreSucursal");
            String tipoEmpleado = resultSet.getString("tipoEmpleado");

            Empleado empleado = new Empleado(nombreEmpleado, apellidoEmpleado, horarioEmpleado, salario, nombreSucursal, tipoEmpleado);
            empleadosList.add(empleado);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}
