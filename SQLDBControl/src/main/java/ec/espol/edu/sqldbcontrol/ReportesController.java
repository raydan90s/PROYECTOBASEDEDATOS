/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author DHAMAR
 */
public class ReportesController implements Initializable {

    @FXML
    private Button mpd;
    @FXML
    private Button pps;
    @FXML
    private Button vpe;
    @FXML
    private Button cdp;
    @FXML
    private TableView<Map<String,String>> tb_Reportes;
    @FXML
    private TableColumn<String, String> c1;
    @FXML
    private TableColumn<String, String> c2;
    @FXML
    private TableColumn<String, String> c3;
    @FXML
    private TableColumn<String, String> c4;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tb_Reportes.setVisible(false);
    }    

    @FXML
    private void rp_MateriaPrimaD(MouseEvent event) throws SQLException {
        tb_Reportes.setVisible(true);  // Hacer visible la tabla
    
        // Limpiar la tabla para evitar datos duplicados
        tb_Reportes.getItems().clear();
    
        // Configurar las columnas para enlazar los datos
        TableColumn<Map<String, String>, String> c1 = new TableColumn<>("Nombre de Materia Prima");
        TableColumn<Map<String, String>, String> c2 = new TableColumn<>("Cantidad Disponible");
        TableColumn<Map<String, String>, String> c3 = new TableColumn<>("Nombre del Proveedor");
        TableColumn<Map<String, String>, String> c4 = new TableColumn<>("Teléfono del Proveedor");
    
        // Configurar la columna "Nombre de Materia Prima"
        c1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Nombre de Materia Prima")));
    
        // Configurar la columna "Cantidad Disponible" como String
        c2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Cantidad Disponible")));
    
        // Configurar las otras columnas
        c3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Nombre del Proveedor")));
        c4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Teléfono del Proveedor")));
    
        // Agregar las columnas al TableView
        tb_Reportes.getColumns().setAll(c1, c2, c3, c4);
    
        // Conectar a la base de datos
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM materia_prima_disponible";
        ResultSet resultSet = statement.executeQuery(query);
    
        // Crear una lista observable para los datos
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
    
        // Poblar la lista observable con los datos del ResultSet
        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            row.put("Nombre de Materia Prima", resultSet.getString("Nombre de Materia Prima"));
            row.put("Cantidad Disponible", resultSet.getString("Cantidad Disponible"));  // Mantener como String
            row.put("Nombre del Proveedor", resultSet.getString("Nombre del Proveedor"));
            row.put("Teléfono del Proveedor", resultSet.getString("Teléfono del Proveedor"));
    
            data.add(row); // Agregar cada fila de datos a la lista
        }
    
        // Asignar los datos al TableView
        tb_Reportes.setItems(data);
    }
    

    @FXML
    private void rp_PedidosPorSucursal(MouseEvent event) throws SQLException {

        tb_Reportes.setVisible(true);  // Hacer visible la tabla

    // Limpiar la tabla para evitar datos duplicados
    tb_Reportes.getItems().clear();

    // Configurar las columnas para enlazar los datos
    TableColumn<Map<String, String>, String> c1 = new TableColumn<>("Sucursal");
    TableColumn<Map<String, String>, String> c2 = new TableColumn<>("Fecha del Pedido");
    TableColumn<Map<String, String>, String> c3 = new TableColumn<>("Cantidad de Pedido");
    TableColumn<Map<String, String>, String> c4 = new TableColumn<>("Producto");

    // Configurar la columna "Nombre de Materia Prima"
    c1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Sucursal")));

    // Configurar la columna "Cantidad Disponible" como String
    c2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Fecha del Pedido")));

    // Configurar las otras columnas
    c3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Cantidad de Pedido")));
    c4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Producto")));

    // Agregar las columnas al TableView
    tb_Reportes.getColumns().setAll(c1, c2, c3, c4);

    // Conectar a la base de datos
    Connection connection = Conexion.conectar();
    Statement statement = connection.createStatement();
    String query = "SELECT * FROM Reporte_de_Pedidos_por_Sucursal";
    ResultSet resultSet = statement.executeQuery(query);

    // Crear una lista observable para los datos
    ObservableList<Map<String, String>> data = FXCollections.observableArrayList();

    // Poblar la lista observable con los datos del ResultSet
    while (resultSet.next()) {
        Map<String, String> row = new HashMap<>();
        row.put("Sucursal", resultSet.getString("Sucursal"));
        row.put("Fecha del Pedido", resultSet.getString("Fecha del Pedido"));  // Mantener como String
        row.put("Cantidad de Pedido", resultSet.getString("Cantidad de Pedido"));
        row.put("Producto", resultSet.getString("Producto"));

        data.add(row); // Agregar cada fila de datos a la lista
    }

    // Asignar los datos al TableView
    tb_Reportes.setItems(data);
    }

    @FXML
    private void rp_VentasPorEmpleado(MouseEvent event) throws SQLException {
        
        tb_Reportes.setVisible(true);  // Hacer visible la tabla

    // Limpiar la tabla para evitar datos duplicados
    tb_Reportes.getItems().clear();

    // Configurar las columnas para enlazar los datos
    TableColumn<Map<String, String>, String> c1 = new TableColumn<>("Nombre de la Sucursal");
    TableColumn<Map<String, String>, String> c2 = new TableColumn<>("Nombre del Empleado");
    TableColumn<Map<String, String>, String> c3 = new TableColumn<>("Apellido del Empleado");
    TableColumn<Map<String, String>, String> c4 = new TableColumn<>("Total en Ventas");

    // Configurar la columna "Nombre de Materia Prima"
    c1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Nombre de la Sucursal")));

    // Configurar la columna "Cantidad Disponible" como String
    c2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Nombre del Empleado")));

    // Configurar las otras columnas
    c3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Apellido del Empleado")));
    c4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("Total en Ventas")));

    // Agregar las columnas al TableView
    tb_Reportes.getColumns().setAll(c1, c2, c3, c4);

    // Conectar a la base de datos
    Connection connection = Conexion.conectar();
    Statement statement = connection.createStatement();
    String query = "SELECT * FROM Reporte_de_ventas_por_empledo";
    ResultSet resultSet = statement.executeQuery(query);

    // Crear una lista observable para los datos
    ObservableList<Map<String, String>> data = FXCollections.observableArrayList();

    // Poblar la lista observable con los datos del ResultSet
    while (resultSet.next()) {
        Map<String, String> row = new HashMap<>();
        row.put("Nombre de la Sucursal", resultSet.getString("Nombre de la Sucursal"));
        row.put("Nombre del Empleado", resultSet.getString("Nombre del Empleado"));  // Mantener como String
        row.put("Apellido del Empleado", resultSet.getString("Apellido del Empleado"));
        row.put("Total en Ventas", resultSet.getString("Total en Ventas"));

        data.add(row); // Agregar cada fila de datos a la lista
    }

    // Asignar los datos al TableView
    tb_Reportes.setItems(data);
    }

    @FXML
    private void rp_CalifDePedidos(MouseEvent event) throws SQLException  {
        tb_Reportes.setVisible(true);  // Hacer visible la tabla

        // Limpiar la tabla para evitar datos duplicados
        tb_Reportes.getItems().clear();
    
        // Configurar las columnas para enlazar los datos
        TableColumn<Map<String, String>, String> c1 = new TableColumn<>("idSucursal");
        TableColumn<Map<String, String>, String> c2 = new TableColumn<>("nombreSucursal");
        TableColumn<Map<String, String>, String> c3 = new TableColumn<>("calificacionPromedio");
        TableColumn<Map<String, String>, String> c4 = new TableColumn<>("porcentajeCalificacion");
    
        // Configurar la columna "Nombre de Materia Prima"
        c1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("idSucursal")));
    
        // Configurar la columna "Cantidad Disponible" como String
        c2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("nombreSucursal")));
    
        // Configurar las otras columnas
        c3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("calificacionPromedio")));
        c4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get("porcentajeCalificacion")));
    
        // Agregar las columnas al TableView
        tb_Reportes.getColumns().setAll(c1, c2, c3, c4);
    
        // Conectar a la base de datos
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM Reporte_Porcentaje_Calificación_Total_de_Pedidos_por_Sucursal";
        ResultSet resultSet = statement.executeQuery(query);
    
        // Crear una lista observable para los datos
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
    
        // Poblar la lista observable con los datos del ResultSet
        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            row.put("idSucursal", resultSet.getString("idSucursal"));
            row.put("nombreSucursal", resultSet.getString("nombreSucursal"));  // Mantener como String
            row.put("calificacionPromedio", resultSet.getString("calificacionPromedio"));
            row.put("porcentajeCalificacion", resultSet.getString("porcentajeCalificacion"));
    
            data.add(row); // Agregar cada fila de datos a la lista
        }
    
        // Asignar los datos al TableView
        tb_Reportes.setItems(data);
    }
    
}
