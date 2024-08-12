/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Sak
 */
public class InventarioController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<MateriaPrima> materiaPrimaTable;
    @FXML
    private TableColumn<MateriaPrima, String> nombreMateriaColumn;
    @FXML
    private TableColumn<MateriaPrima, String> codigoColumn;
    @FXML
    private TableColumn<MateriaPrima, Integer> cantidadColumn;
    @FXML
    private TableColumn<MateriaPrima, Date> fechaElaboracionColumn;
    @FXML
    private TableColumn<MateriaPrima, Date> fechaCaducidadColumn;
    @FXML
    private TableColumn<MateriaPrima, String> proveedorColumn;
    
    @FXML
    private VBox formMateria;
    @FXML
    private TextField nombreField; 
    @FXML
    private TextField fechaEField;
    @FXML
    private TextField fechaCField;
    @FXML
    private TextField cantidadField;
    @FXML
    private TextField proveedorField;
    
    
    @FXML
    private Button agregarMateria;
    @FXML
    private Button eliminarMateria;
    @FXML
    private Button modificarMateria;
    
    @FXML
    private VBox modificar;
    @FXML
    private TextField idFieldM; 
    @FXML
    private TextField nombreFieldM; 
    @FXML
    private TextField fechaEFieldM;
    @FXML
    private TextField fechaCFieldM;
    @FXML
    private TextField cantidadFieldM;
    @FXML
    private TextField idProveedoroFieldM;
    

    private ObservableList<MateriaPrima> materiaPrimaList;
    private ObservableList<Proveedor> proveedorList;
    int filaSeleccionada;
    int idProveedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarMateria.setOnAction(event -> updateVisibility(formMateria,true));
        modificarMateria.setOnAction(evento -> updateVisibility(modificar, true));
        materiaPrimaTable.setOnMouseClicked(event -> seleccionarMateria());
        eliminarMateria.setOnAction(event -> eliminarMateria(event));
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        nombreMateriaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreMateria"));
        fechaCaducidadColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCaducidad"));
        fechaElaboracionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaElaboracion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
        materiaPrimaList = FXCollections.observableArrayList();
        
        proveedorColumn.setCellValueFactory(cellData -> 
        new SimpleStringProperty(cellData.getValue().getNombreProveedor()));
        
        proveedorList = FXCollections.observableArrayList();
        materiaPrimaList = FXCollections.observableArrayList();

        try {
            loadMateriaPrima();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        materiaPrimaTable.setItems(materiaPrimaList);
    }
    
    public void handleAddMateria(ActionEvent event) {
        String nombre = nombreField.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fechaEUtil = null;
        java.util.Date fechaCUtil = null;
        int cantidad = Integer.parseInt(cantidadField.getText());
        int idProveedor = Integer.parseInt(proveedorField.getText());
    
        try {
            if (!fechaEField.getText().isEmpty()) {
                fechaEUtil = sdf.parse(fechaEField.getText());
            }
            if (!fechaCField.getText().isEmpty()) {
                fechaCUtil = sdf.parse(fechaCField.getText());
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha incorrecta");
            return; // Salir del método si hay un error en la fecha
        }
    
        java.sql.Date fechaESql = fechaEUtil != null ? new java.sql.Date(fechaEUtil.getTime()) : null;
        java.sql.Date fechaCSql = fechaCUtil != null ? new java.sql.Date(fechaCUtil.getTime()) : null;
    
        String consulta = "INSERT INTO MateriaPrima(nombreMateria, fechaElaboracion, fechaCaducidad, idProveedor) "
                        + "VALUES (?,?,?,?);";
    
        try (Connection connection = Conexion.conectar();
            PreparedStatement ps = connection.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
        
            ps.setString(1, nombre);
            if (fechaESql != null) {
                ps.setDate(2, fechaESql);
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
        
            if (fechaCSql != null) {
                ps.setDate(3, fechaCSql);
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
        
            ps.setInt(4, idProveedor);
        
            ps.executeUpdate();
        
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int idMateria = 0;

            if (generatedKeys.next()) {
                idMateria = generatedKeys.getInt(1);
            }
        
            Proveedor proveedor = obtenerProveedor(idProveedor);
            MateriaPrima materia = new MateriaPrima(idMateria, nombre, fechaCSql, fechaESql, cantidad, proveedor);
            materiaPrimaList.add(materia);
        
            // Inserción en la tabla Detalle
            String consultaDetalle = "INSERT INTO Detalle(cantidadDisponible, idMateria) VALUES (?, ?);";
            try (PreparedStatement psDetalle = connection.prepareStatement(consultaDetalle)) {
                psDetalle.setInt(1, cantidad);
                psDetalle.setInt(2, idMateria);
                psDetalle.executeUpdate();
            }
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar datos: " + e.getMessage());
        }
    
    updateVisibility(formMateria, false);
}
    
    private Proveedor obtenerProveedor(int idProveedor) {
        Proveedor proveedor = null;
        String consulta = "select nombreProveedor, apellidoProveedor from Proveedor where idProveedor = ?;";
        
        try (PreparedStatement ps = Conexion.conectar().prepareStatement(consulta)) {
            // Establecer el parámetro de la consulta
            ps.setInt(1, idProveedor);
            
            // Ejecutar la consulta
            try (ResultSet rs = ps.executeQuery()) {
                // Procesar el ResultSet
                if (rs.next()) {
                    String nombre = rs.getString("nombreProveedor");
                    String apellido = rs.getString("apellidoProveedor");
                    
                    // Crear un objeto Empleado con los datos obtenidos
                    proveedor = new Proveedor(nombre, apellido);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el proveedor con id: " + idProveedor);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el proveedor: " + e.toString());
        }
        
        return proveedor;
    }
    
    @FXML
    private void seleccionarMateria() {
        try {
            filaSeleccionada = materiaPrimaTable.getSelectionModel().getSelectedIndex();
            if (filaSeleccionada >= 0) {

                MateriaPrima materiaPrimaS = materiaPrimaTable.getItems().get(filaSeleccionada);

                String fechaStrE = null;
                if (materiaPrimaS.getFechaElaboracion() != null) {
                    java.util.Date sqlEDate = materiaPrimaS.getFechaElaboracion();
                    java.util.Date fechaEUtil = new java.util.Date(sqlEDate.getTime());

                    // Formatear la fecha de elaboración
                    SimpleDateFormat sdfE = new SimpleDateFormat("yyyy-MM-dd");
                    fechaStrE = sdfE.format(fechaEUtil);
                }

                String fechaStrC = null;
                if (materiaPrimaS.getFechaCaducidad() != null) {
                    java.util.Date sqlCDate = materiaPrimaS.getFechaCaducidad();
                    java.util.Date fechaCUtil = new java.util.Date(sqlCDate.getTime());

                    // Formatear la fecha de caducidad
                    SimpleDateFormat sdfC = new SimpleDateFormat("yyyy-MM-dd");
                    fechaStrC = sdfC.format(fechaCUtil);
                }

                // Obtener idProveedor
                String consultaIdProveedor = "SELECT idProveedor FROM MateriaPrima WHERE idMateria= ?";
                try (PreparedStatement psIdPoveedor = Conexion.conectar().prepareStatement(consultaIdProveedor)) {
                    psIdPoveedor.setInt(1, materiaPrimaS.getIdMateria());
                    ResultSet rs = psIdPoveedor.executeQuery();
                    if (rs.next()) {
                        idProveedor = rs.getInt("idProveedor");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                // Asignar valores a los campos de texto
                idFieldM.setText(Integer.toString(materiaPrimaS.getIdMateria()));
                nombreFieldM.setText(materiaPrimaS.getNombreMateria());
                cantidadFieldM.setText(Integer.toString(materiaPrimaS.getCantidad()));
                fechaEFieldM.setText(fechaStrE != null ? fechaStrE : ""); // Manejar fechas nulas
                fechaCFieldM.setText(fechaStrC != null ? fechaStrC : ""); // Manejar fechas nulas
                idProveedoroFieldM.setText(Integer.toString(idProveedor));

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de selección, error " + e.toString());
        }   
    }
    
    @FXML
    private void modificarMateria(ActionEvent event) {
        int idProveedor;
        try {
            String nombre = nombreFieldM.getText();
            int cantidad = Integer.parseInt(cantidadFieldM.getText().trim());
            idProveedor = Integer.parseInt(idProveedoroFieldM.getText().trim());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date fechaESql = null;
            java.sql.Date fechaCSql = null;

            // Verificar si los campos de fecha están vacíos antes de intentar parsearlos
            if (!fechaEFieldM.getText().trim().isEmpty()) {
                try {
                    java.util.Date fechaEUtil = sdf.parse(fechaEFieldM.getText().trim());
                    fechaESql = new java.sql.Date(fechaEUtil.getTime());
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Formato de fecha de elaboración incorrecta");
                    return; // Salir del método si hay un error en la fecha
                }
            }

            if (!fechaCFieldM.getText().trim().isEmpty()) {
                try {
                    java.util.Date fechaCUtil = sdf.parse(fechaCFieldM.getText().trim());
                    fechaCSql = new java.sql.Date(fechaCUtil.getTime());
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Formato de fecha de caducidad incorrecta");
                    return; // Salir del método si hay un error en la fecha
                }
            }

            MateriaPrima materiaPrimaS = materiaPrimaTable.getItems().get(filaSeleccionada);
            int idMateriPrima = materiaPrimaS.getIdMateria();

            String consulta = "UPDATE MateriaPrima SET nombreMateria = ?, fechaCaducidad = ?, fechaElaboracion = ?, idProveedor = ? WHERE idMateria = ?";

            try (PreparedStatement ps = Conexion.conectar().prepareStatement(consulta)) {
                Proveedor proveedor = obtenerProveedor(idProveedor);

                ps.setString(1, nombre);
                // Asignar las fechas o null si están vacías
                if (fechaCSql != null) {
                    ps.setDate(2, fechaCSql);
                } else {
                    ps.setNull(2, java.sql.Types.DATE);
                }

                if (fechaESql != null) {
                    ps.setDate(3, fechaESql);
                } else {
                    ps.setNull(3, java.sql.Types.DATE);
                }

                ps.setInt(4, idProveedor);
                ps.setInt(5, idMateriPrima);

                ps.executeUpdate();

                // Actualizar los valores en el objeto MateriaPrima
                materiaPrimaS.setNombreMateria(nombre);
                materiaPrimaS.setCantidad(cantidad);
                materiaPrimaS.setFechaElaboracion(fechaESql);
                materiaPrimaS.setFechaCaducidad(fechaCSql);
                materiaPrimaS.setProveedor(proveedor);

                materiaPrimaTable.refresh(); // Refrescar la tabla para mostrar los cambios

                JOptionPane.showMessageDialog(null, "Modificación exitosa");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se modificó correctamente la Materia: " + e.toString());
            e.printStackTrace();
        }

        updateVisibility(modificar, false);
    }
    
    @FXML
    private void eliminarMateria(ActionEvent event) {
        try {
            filaSeleccionada = materiaPrimaTable.getSelectionModel().getSelectedIndex();

            if (filaSeleccionada >= 0) {
                MateriaPrima materiaPrimaS = materiaPrimaTable.getItems().get(filaSeleccionada);
                int idMateriaPrima = materiaPrimaS.getIdMateria();

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar la Materia Prima?");
                
                if (respuesta == JOptionPane.YES_OPTION) {
                    Connection conexion = Conexion.conectar();

                    //Eliminar el producto
                    String deleteProductoQuery = "DELETE FROM MateriaPrima WHERE idMateria = ?";
                    try (PreparedStatement psProducto = conexion.prepareStatement(deleteProductoQuery)) {
                        psProducto.setInt(1, idMateriaPrima);
                        psProducto.executeUpdate();
                    }

                    // Eliminar el producto de la lista observable
                    materiaPrimaTable.getItems().remove(filaSeleccionada);
                    JOptionPane.showMessageDialog(null, "Materia Prima eliminado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Eliminación cancelada");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona una Materia Prima para eliminar.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la Materia Prima. Verifique si hay registros dependientes: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar la Materia Prima: " + e.toString());
        }
    }

    private void loadMateriaPrima() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idProveedor, nombreProveedor, apellidoProveedor, MateriaPrima.idMateria, nombreMateria, fechaCaducidad, fechaElaboracion, cantidadDisponible as cantidad" +
"                FROM MateriaPrima" +
"                JOIN Detalle ON MateriaPrima.idMateria = Detalle.idMateria" +
"                JOIN Proveedor using(idProveedor);";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int idMateria = resultSet.getInt("idMateria");
            String nombreMateria = resultSet.getString("nombreMateria");
            Date fechaCaducidad = resultSet.getDate("fechaCaducidad");
            Date fechaElaboracion = resultSet.getDate("fechaElaboracion");
            int cantidad = resultSet.getInt("cantidad");
            int codigoP = resultSet.getInt("idProveedor");
            String nombreP = resultSet.getString("nombreProveedor");
            String apellidoP = resultSet.getString("apellidoProveedor");

            Proveedor proveedor = new Proveedor(codigoP, nombreP, apellidoP);
            MateriaPrima materiaPrima = new MateriaPrima(idMateria, nombreMateria, fechaCaducidad, fechaElaboracion, cantidad, proveedor);
            materiaPrimaList.add(materiaPrima);
            proveedorList.add(proveedor);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    
    private void updateVisibility(VBox caja, boolean formVisible) {
    caja.setVisible(formVisible);
    agregarMateria.setVisible(!formVisible);
    eliminarMateria.setVisible(!formVisible);
    modificarMateria.setVisible(!formVisible);
    }
}
