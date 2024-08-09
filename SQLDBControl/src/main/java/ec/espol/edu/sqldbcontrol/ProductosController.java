/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.beans.property.SimpleStringProperty;


/**
 * FXML Controller class
 * 
 * @author Raydan
 */
public class ProductosController implements Initializable {
    
    @FXML
    private Text volver;
    @FXML
    private TableView<Productos> productoTable;
    @FXML
    private TableColumn<Productos, Integer> codigounicocolumn;  
    @FXML
    private TableColumn<Productos, String> nombrecolumn;
    @FXML
    private TableColumn<Productos, Integer> cantidadcolumn;
    @FXML
    private TableColumn<Productos, Date> fechaproduccioncolumn;
    @FXML
    private TableColumn<Productos, Double> preciocolumn;
    @FXML
    private TableColumn<Productos, String> recetacolumn;
    @FXML
    private TableColumn<Productos, String> nombrecocinero;
    

    private ObservableList<Productos> productosList;
    private ObservableList<Empleado> empleadoList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        codigounicocolumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombrecolumn.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        cantidadcolumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        fechaproduccioncolumn.setCellValueFactory(new PropertyValueFactory<>("fechaProduccion"));
        preciocolumn.setCellValueFactory(new PropertyValueFactory<>("precio")); 
        recetacolumn.setCellValueFactory(new PropertyValueFactory<>("receta"));
        
        nombrecocinero.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getNombreCocinero()));


        productosList = FXCollections.observableArrayList();
        empleadoList = FXCollections.observableArrayList();

        try {
            loadProductos();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        productoTable.setItems(productosList);
    }  
    
    private void loadProductos() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idEmpleado, idProducto, nombreProducto, cantidadRealizada, fechaProduccion, "
                + "precioProducto, recetaProducto, nombreEmpleado, apellidoEmpleado "
                + "FROM Producto JOIN Cocinero USING(idEmpleado) JOIN Sucursal USING(idSucursal)";  // Corregí el formato de la consulta

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int codigo = resultSet.getInt("idProducto");
            String nombre = resultSet.getString("nombreProducto");
            int cantidad = resultSet.getInt("cantidadRealizada");
            Date fecha = resultSet.getDate("fechaProduccion");
            Double precio = resultSet.getDouble("precioProducto");
            String receta = resultSet.getString("recetaProducto");
            String nombreEmp = resultSet.getString("nombreEmpleado");
            String apellidoEmp = resultSet.getString("apellidoEmpleado");
            
            Empleado empleado = new Empleado(nombreEmp, apellidoEmp);
            Productos producto = new Productos(codigo, nombre, cantidad, fecha, precio, receta, empleado);
            productosList.add(producto);
            empleadoList.add(empleado);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}