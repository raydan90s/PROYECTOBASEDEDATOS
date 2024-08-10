/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
        califacion.setCellValueFactory(new PropertyValueFactory<>("califiacionCliente"));
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
            int factura = resultSet.getInt("numFactura");
            int iCliente = resultSet.getInt("idCliente");
            int iProducto = resultSet.getInt("idProducto");
            int iSucursal = resultSet.getInt("idSucursal");
            Date fEvaluacion = resultSet.getDate("fechaEvaluacion");
            
            Satisfaccion satisfaccion = new Satisfaccion(idSatisfaccion,calificacion,factura,iCliente,iProducto,iSucursal, fEvaluacion);
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
            factura.setText(String.valueOf(satisfaccionSeleccionado.getNumFactura()));
            codCliente.setText(String.valueOf(satisfaccionSeleccionado.getIdCliente()));
            codProducto.setText(String.valueOf(satisfaccionSeleccionado.getIdProducto()));
            CodSucursal.setText(String.valueOf(satisfaccionSeleccionado.getIdSucursal()));
            fecha_evaluacion.setValue(satisfaccionSeleccionado.getFechaEvaluacion().toLocalDate());
        }
    }

    @FXML
    private void agregar(MouseEvent event) {
    }

    @FXML
    private void editar(MouseEvent event) {
    }

    @FXML
    private void borrar(MouseEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
    }
    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
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
    }
    
    void limpiar(){
        calif.clear();
        codCliente.clear();
        codProducto.clear();
        CodSucursal.clear();
        fechaEvalucion.setValue(null);
    }
    
    void hablitarCampo(boolean valor){
        calif.setDisable(valor);
        codCliente.setDisable(valor);
        codProducto.setDisable(valor);
        CodSucursal.setDisable(valor);
        fechaEvalucion.setDisable(valor);
    }
}
