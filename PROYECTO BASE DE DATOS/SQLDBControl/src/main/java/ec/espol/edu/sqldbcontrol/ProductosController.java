/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.swing.JOptionPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;






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
    
    @FXML
    private VBox formProducto;
    @FXML
    private Button agregarProducto;
    @FXML
    private Button eliminarProducto;
    @FXML
    private Button modificarProducto;
    
    @FXML
    private TextField nombreField; 
    @FXML
    private TextField cantidadField;
    @FXML
    private TextField fechaField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField recetaField;
    @FXML
    private TextField cocineroField;
    private ObservableList<Productos> productosList;
    private ObservableList<Empleado> empleadoList;
    private int filaSeleccionada;
    
    @FXML
    private VBox modificar;
    @FXML
    private TextField idFieldM;
    @FXML
    private TextField nombreFieldM;
    @FXML
    private TextField cantidadFieldM;
    @FXML
    private TextField fechaProduccionM;
    @FXML
    private TextField precioFieldM;
    @FXML
    private TextField recetaFieldM;
    @FXML
    private TextField cocineroFieldM;
    int idEmpleado;
    

    
    @FXML
    private void handleAddProducto(ActionEvent event) {
    String nombre = nombreField.getText();
    int cantidad = Integer.parseInt(cantidadField.getText());
    double precio = Double.parseDouble(precioField.getText());
    String receta = recetaField.getText();
    int idCocinero = Integer.parseInt(cocineroField.getText());

    // Convertir la fecha del campo de texto a un objeto java.util.Date
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date fechaUtil = null;
    try {
        fechaUtil = sdf.parse(fechaField.getText());
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(null, "Formato de fecha incorrecta");
        return; // Salir del método si hay un error en la fecha
    }

    // Convertir java.util.Date a java.sql.Date para la base de datos
    java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

    String consulta = "INSERT INTO Producto(nombreProducto, cantidadRealizada, fechaProduccion, precioProducto, recetaProducto, idEmpleado) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = Conexion.conectar();
         PreparedStatement ps = connection.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, nombre);
        ps.setInt(2, cantidad);
        ps.setDate(3, fechaSql); // Usar java.sql.Date
        ps.setDouble(4, precio);
        ps.setString(5, receta);
        ps.setInt(6, idCocinero);

        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        int idProducto = 0;

        if (generatedKeys.next()) {
            idProducto = generatedKeys.getInt(1);
        }

        // Crear el empleado
        Empleado empleado = obtenerEmpleado(idCocinero);

        // Crear el producto con java.util.Date
        Productos producto = new Productos(idProducto, nombre, cantidad, fechaSql, precio, receta, empleado);
        
        // Agregar el producto a la lista si es necesario
        productosList.add(producto);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "No se encontró el empleado con id: " + idCocinero);
    }

        updateVisibility(formProducto ,false);
    }
    
    @FXML
    private void seleccionarProducto(){
        try{
            filaSeleccionada = productoTable.getSelectionModel().getSelectedIndex();
            if (filaSeleccionada >= 0){
                Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);
                
                Date sqlDate = productoSeleccionado.getFechaProduccion();
                Date fechaUtil = new Date(sqlDate.getTime());

                // Formatear la fecha
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = sdf.format(fechaUtil);
                
                String consultaIdEmpleado = "select idEmpleado from Producto where idProducto = ?";
                try (PreparedStatement psIdEmpleado = Conexion.conectar().prepareStatement(consultaIdEmpleado)) {
                    psIdEmpleado.setInt(1, productoSeleccionado.getCodigo());
                    ResultSet rs = psIdEmpleado.executeQuery();
                    if (rs.next()) {
                        idEmpleado = rs.getInt("idEmpleado");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                idFieldM.setText(Integer.toString(productoSeleccionado.getCodigo()));
                nombreFieldM.setText(productoSeleccionado.getNombreProducto());
                cantidadFieldM.setText(Integer.toString(productoSeleccionado.getCantidad()));
                fechaProduccionM.setText(fechaStr);
                precioFieldM.setText(Double.toString(productoSeleccionado.getPrecio()));
                recetaFieldM.setText(productoSeleccionado.getReceta());
                cocineroFieldM.setText(Integer.toString(idEmpleado));
            }else{
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error de selección, error " + e.toString());
        }
        
    }
    
    @FXML
    private void modificarProducto(ActionEvent event){
        int idCocinero;
        
        try {
            // Tomar los valores actuales de los campos de texto
            String nombre = nombreFieldM.getText();
            int cantidad = Integer.parseInt(cantidadFieldM.getText().trim());
            double precio = Double.parseDouble(precioFieldM.getText());
            String receta = recetaFieldM.getText();
            idCocinero = Integer.parseInt(cocineroFieldM.getText().trim());

            // Convertir la fecha del campo de texto a un objeto java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaUtil = null;
            try {
                fechaUtil = sdf.parse(fechaProduccionM.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha incorrecta");
                return; // Salir del método si hay un error en la fecha
            }
            
            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

            // Obtener el ID del Producto seleccionado
            Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);
            int idProducto = productoSeleccionado.getCodigo();
  
            // Actualizar los datos en la base de datos
            String consulta = "update Producto set nombreProducto = ?, cantidadRealizada = ?, fechaProduccion = ?, precioProducto = ?, recetaProducto = ?, idEmpleado = ? "
                    + "where idProducto = ?;";
            PreparedStatement ps = Conexion.conectar().prepareStatement(consulta);

            Empleado empleado = obtenerEmpleado(idCocinero);
            
            ps.setString(1, nombre);
            ps.setInt(2, cantidad);
            ps.setDate(3, fechaSql);
            ps.setDouble(4, precio);
            ps.setString(5, receta);
            ps.setInt(6, idCocinero);
            ps.setInt(7, idProducto);

            ps.executeUpdate();

            productoSeleccionado.setNombreProducto(nombre);
            productoSeleccionado.setCantidad(cantidad);
            productoSeleccionado.setFechaProduccion(fechaSql);
            productoSeleccionado.setPrecio(precio);
            productoSeleccionado.setReceta(receta);
            productoSeleccionado.setEmpleado(empleado);
            
            productoTable.refresh(); // Refrescar la tabla para mostrar los cambios

            JOptionPane.showMessageDialog(null, "Modificación exitosa");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se modificó correctamente el Producto: " + e.toString());
            e.printStackTrace();
        }
        
        updateVisibility(modificar ,false);
    
    }
    
    private Empleado obtenerEmpleado(int idEmpleado) {
        Empleado empleado = null;
        String consulta = "SELECT nombreEmpleado, apellidoEmpleado FROM Cocinero WHERE idEmpleado = ?";
        
        try (PreparedStatement ps = Conexion.conectar().prepareStatement(consulta)) {
            // Establecer el parámetro de la consulta
            ps.setInt(1, idEmpleado);
            
            // Ejecutar la consulta
            try (ResultSet rs = ps.executeQuery()) {
                // Procesar el ResultSet
                if (rs.next()) {
                    String nombre = rs.getString("nombreEmpleado");
                    String apellido = rs.getString("apellidoEmpleado");
                    
                    // Crear un objeto Empleado con los datos obtenidos
                    empleado = new Empleado(nombre, apellido);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el empleado con id: " + idEmpleado);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el empleado: " + e.toString());
        }
        
        return empleado;
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarProducto.setOnAction(event -> updateVisibility(formProducto,true));
        modificarProducto.setOnAction(evento -> updateVisibility(modificar, true));
        productoTable.setOnMouseClicked(event -> seleccionarProducto());
        eliminarProducto.setOnAction(event -> eliminarProducto(event));
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
            java.sql.Date fecha = resultSet.getDate("fechaProduccion");
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
    
    @FXML
    private void eliminarProducto(ActionEvent event) {
        try {
            filaSeleccionada = productoTable.getSelectionModel().getSelectedIndex();

            if (filaSeleccionada >= 0) {
                Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);
                int idProducto = productoSeleccionado.getCodigo();

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar el producto?");
                if (respuesta == JOptionPane.YES_OPTION) {
                    Connection conexion = Conexion.conectar();

                    //Eliminar el producto
                    String deleteProductoQuery = "DELETE FROM Producto WHERE idProducto = ?";
                    try (PreparedStatement psProducto = conexion.prepareStatement(deleteProductoQuery)) {
                        psProducto.setInt(1, idProducto);
                        psProducto.executeUpdate();
                    }

                    // Eliminar el producto de la lista observable
                    productoTable.getItems().remove(filaSeleccionada);
                    JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Eliminación cancelada");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto. Verifique si hay registros dependientes: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto: " + e.toString());
        }
    }

    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    
    private void updateVisibility(VBox caja, boolean formVisible) {
    caja.setVisible(formVisible);
    agregarProducto.setVisible(!formVisible);
    eliminarProducto.setVisible(!formVisible);
    modificarProducto.setVisible(!formVisible);
    }
}