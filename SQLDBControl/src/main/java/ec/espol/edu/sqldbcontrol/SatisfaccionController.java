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
import java.sql.Time;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author DHAMAR
 */
public class SatisfaccionController implements Initializable {
    private Satisfaccion satisfaccionSeleccionado;
    @FXML
    private TableView<Satisfaccion> tableSatisfaccion;
    @FXML
    private TableColumn<Satisfaccion, Integer> id;
    @FXML
    private TableColumn<Satisfaccion , Integer> califacion;
    @FXML
    private TableColumn<Satisfaccion, Integer> factura;
    @FXML
    private TableColumn<Satisfaccion, Integer> id_Cliente;
    @FXML
    private TableColumn<Satisfaccion, Integer> id_Producto;
    @FXML
    private TableColumn<Satisfaccion, Integer> id_Sucursal;
    @FXML
    private TableColumn<Satisfaccion,Integer > fecha_evaluacion;
    @FXML
    private Text textCalif;
    @FXML
    private Text textFactura;
    @FXML
    private Text textCliente;
    @FXML
    private Text textProducto;
    @FXML
    private Text textSucursal;
    @FXML
    private Text fecha;
    @FXML
    private TextField calif;
    @FXML
    private TextField codCliente;
    @FXML
    private TextField codProducto;
    @FXML
    private TextField CodSucursal;
    @FXML
    private DatePicker fechaEvalucion;
    @FXML
    private Text volver;
    @FXML
    private Button guardarid;
    ObservableList<Satisfaccion> satisfaccionList = FXCollections.observableArrayList(); 
    @FXML
    private TextField facturat;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarIcono(false);
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        id.setCellValueFactory(new PropertyValueFactory<>("idSatisfaccion"));
        califacion.setCellValueFactory(new PropertyValueFactory<>("calificacionCliente"));
        factura.setCellValueFactory(new PropertyValueFactory<>("numFactura"));
        id_Cliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        id_Producto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        id_Sucursal.setCellValueFactory(new PropertyValueFactory<>("idSucursal"));
        fecha_evaluacion.setCellValueFactory(new PropertyValueFactory<>("fechaEvaluacion"));
        try {
            loadSatisfaccion();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        
        tableSatisfaccion.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                
            }
        });
    } 
    
    public void loadSatisfaccion() throws SQLException{
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT * "
                + "FROM Satisfaccion;"; 

        ResultSet resultSet = statement.executeQuery(query);
        
        while (resultSet.next()) {
            int idSatisfaccion = resultSet.getInt("idSatisfaccion");
            int calificacion = resultSet.getInt("calificacionCliente");
            int facturas = resultSet.getInt("numFactura");
            int iCliente = resultSet.getInt("idCliente");
            int iProducto = resultSet.getInt("idProducto");
            int iSucursal = resultSet.getInt("idSucursal");
            Date fEvaluacion = resultSet.getDate("fechaEvaluacion");
            
            Satisfaccion satisfaccion = new Satisfaccion(idSatisfaccion,calificacion,facturas,iCliente,iProducto,iSucursal, fEvaluacion);
            satisfaccionList.add(satisfaccion);
        }
        tableSatisfaccion.setItems(satisfaccionList);
    }

    @FXML
    private void seleccion(MouseEvent event) {
        limpiar();
        satisfaccionSeleccionado = tableSatisfaccion.getSelectionModel().getSelectedItem();
        if (satisfaccionSeleccionado != null) {
            calif.setText(String.valueOf(satisfaccionSeleccionado.getCalificacionCliente()));
            facturat.setText(String.valueOf(satisfaccionSeleccionado.getNumFactura()));
            codCliente.setText(String.valueOf(satisfaccionSeleccionado.getIdCliente()));
            codProducto.setText(String.valueOf(satisfaccionSeleccionado.getIdProducto()));
            CodSucursal.setText(String.valueOf(satisfaccionSeleccionado.getIdSucursal()));
            fechaEvalucion.setValue(satisfaccionSeleccionado.getFechaEvaluacion().toLocalDate());
        }
    }

    @FXML
    private void agregar(MouseEvent event) {
        limpiar();
        mostrarIcono(true);
        satisfaccionSeleccionado = null;
    }

    @FXML
    private void editar(MouseEvent event) {
        satisfaccionSeleccionado = tableSatisfaccion.getSelectionModel().getSelectedItem();
        if (satisfaccionSeleccionado != null) {
            habilitarCampo(false);
            mostrarIcono(true);
            calif.setText(String.valueOf(satisfaccionSeleccionado.getCalificacionCliente()));
            facturat.setText(String.valueOf(satisfaccionSeleccionado.getNumFactura()));
            codCliente.setText(String.valueOf(satisfaccionSeleccionado.getIdCliente()));
            codProducto.setText(String.valueOf(satisfaccionSeleccionado.getIdProducto()));
            CodSucursal.setText(String.valueOf(satisfaccionSeleccionado.getIdSucursal()));
            fechaEvalucion.setValue(satisfaccionSeleccionado.getFechaEvaluacion().toLocalDate());
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una Encuesta de Satisfaccion para editar.");
        }
    }

    @FXML
    private void borrar(MouseEvent event) {
        Satisfaccion satisfaccionABorrar = tableSatisfaccion.getSelectionModel().getSelectedItem();
        if (satisfaccionABorrar == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una satisfaccion para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, 
        "¿Está seguro de que desea eliminar este permiso?", 
        "Confirmar eliminación", 
        JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Satisfaccion WHERE idSatisfaccion = ?";

            try (Connection conn = Conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
                ps.setInt(1, satisfaccionABorrar.getIdSatisfaccion());
            
                int filasAfectadas = ps.executeUpdate();
            
                if (filasAfectadas > 0) {
                    satisfaccionList.remove(satisfaccionABorrar);
                    tableSatisfaccion.refresh();
                    JOptionPane.showMessageDialog(null, "La satisfaccion ha sido eliminado exitosamente.");
                    limpiar();
                    mostrarIcono(false);
                    satisfaccionSeleccionado = null;
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar la satisfaccion.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar la satisfaccion: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void guardar(MouseEvent event) {
        boolean esNuevo = satisfaccionSeleccionado == null;
        int factu = Integer.parseInt(facturat.getText());
        int Calificacion = Integer.parseInt(calif.getText());
        int CodiCliente = Integer.parseInt(codCliente.getText());
        int codigoProducto = Integer.parseInt( codProducto.getText());
        int codigoSucursal = Integer.parseInt(CodSucursal.getText());
        Date fecheva = Date.valueOf(fechaEvalucion.getValue());
        
        String sql;
        if (!esNuevo) {
        sql = "UPDATE Satisfaccion SET calificacionCliente = ?, numFactura = ?, idCliente = ?, idProducto = ?, idSucursal = ?, fechaEvaluacion = ? WHERE idSatisfaccion = ?";
    } else {
        sql = "INSERT INTO Satisfaccion (calificacionCliente, numFactura, idCliente, idProducto, idSucursal, fechaEvaluacion) VALUES (?, ?, ?, ?, ?, ?)";    }

    try (Connection conn = Conexion.conectar();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        ps.setInt(1, Calificacion);
        ps.setInt(2, factu);
        ps.setInt(3, CodiCliente);
        ps.setInt(4, codigoProducto);
        ps.setInt(5, codigoSucursal);
        ps.setDate(6, fecheva);
        
        if (!esNuevo) {
            ps.setInt(7, satisfaccionSeleccionado.getIdSatisfaccion());
        }

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("La operación falló, no se afectaron filas.");
        }

        if (esNuevo) {
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idSatisfaccion = generatedKeys.getInt(1);
                    Satisfaccion nuevoSatisfaccion = new Satisfaccion(idSatisfaccion, Calificacion,factu, CodiCliente, codigoProducto, codigoSucursal, fecheva);
                    satisfaccionList.add(nuevoSatisfaccion);
                } else {
                    throw new SQLException("No se pudo obtener el ID de la nuevo Satisfaccion.");
                }
            }
        } else {
            satisfaccionSeleccionado.setCalificacionCliente(Calificacion);
            satisfaccionSeleccionado.setNumFactura(factu);
            satisfaccionSeleccionado.setIdCliente(CodiCliente);
            satisfaccionSeleccionado.setIdProducto(codigoProducto);
            satisfaccionSeleccionado.setIdSucursal(codigoSucursal);
            satisfaccionSeleccionado.setFechaEvaluacion(fecheva);
            tableSatisfaccion.refresh();
        }
            JOptionPane.showMessageDialog(null, "Se insertó correctamente la Satisfaccion");
            limpiar();
            mostrarIcono(false);
            recargarTabla();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se insertó correctamente el permiso " + e.toString());
        } 
    }
    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    
    void recargarTabla(){
        satisfaccionList.clear(); // Limpia la lista observable
        tableSatisfaccion.getItems().clear(); // Limpia la tabla
        try {
            loadSatisfaccion(); // Recarga los permisos desde la base de datos
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recargar las Satisfacciones: " + e.getMessage());
            e.printStackTrace();
        } 
    }
    
    void mostrarIcono(boolean valor){
    textCalif.setVisible(valor);
    textFactura.setVisible(valor);
    textCliente.setVisible(valor);
    textProducto.setVisible(valor);
    textSucursal.setVisible(valor);
    fecha.setVisible(valor);
    calif.setVisible(valor);
    codCliente.setVisible(valor);
    codProducto.setVisible(valor);
    CodSucursal.setVisible(valor);
    fechaEvalucion.setVisible(valor);
    guardarid.setVisible(valor);
    facturat.setVisible(valor);
    }
    
    void limpiar(){
        calif.clear();
        codCliente.clear();
        codProducto.clear();
        CodSucursal.clear();
        fechaEvalucion.setValue(null);
        facturat.clear();
    }
    
    void habilitarCampo(boolean valor){
        calif.setDisable(valor);
        codCliente.setDisable(valor);
        codProducto.setDisable(valor);
        CodSucursal.setDisable(valor);
        fechaEvalucion.setDisable(valor);
        facturat.setDisable(valor);
    }
}
