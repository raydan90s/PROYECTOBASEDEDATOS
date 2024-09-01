/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class SucursalController implements Initializable {

    @FXML
    private TableColumn<Sucursal, Integer> id;
    @FXML
    private TableColumn<Sucursal, String> nombre;
    @FXML
    private TableColumn<Sucursal, String> direccion;
    @FXML
    private TableColumn<Sucursal, String> telefono;
    @FXML
    private TableColumn<Sucursal, String> horario;
    @FXML
    private TableColumn<Sucursal, Integer> id_jefe;
    @FXML
    private Text textSucursal;
    @FXML
    private Text textDireccion;
    @FXML
    private Text textTelefono;
    @FXML
    private Text textHorario;
    @FXML
    private Text textIdjefe;
    @FXML
    private TextField nombreSucursal;
    @FXML
    private TextField direccionSucursal;
    @FXML
    private TextField TelefonoSucursal;
    @FXML
    private TextField horarioSucursal;
    @FXML
    private TextField codigoJefe;
    @FXML
    private Button agregarb;
    @FXML
    private Button editarb;
    @FXML
    private Button borrarb;
    @FXML
    private Button guardarb;
    @FXML
    private TableView<Sucursal> tableSucursal;
    @FXML
    private Text volver;
    ObservableList<Sucursal> sucursalList = FXCollections.observableArrayList(); 
    private Sucursal sucursalSeleccionada=null;

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
        id.setCellValueFactory(new PropertyValueFactory<>("idSucursal"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal"));
        direccion.setCellValueFactory(new PropertyValueFactory<>("direccionSucursal"));
        telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        horario.setCellValueFactory(new PropertyValueFactory<>("HorarioSucursal"));
        id_jefe.setCellValueFactory(new PropertyValueFactory<>("idJefe"));
        try {
            loadSucursales();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        
        tableSucursal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                
            }
        });
    }    
    
    void loadSucursales() throws SQLException{
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idSucursal, nombreSucursal, direccionSucursal, telefono, horarioSucursal, "
                + "idJefe "
                + "FROM Sucursal; "; 

        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int idSucursal = resultSet.getInt("idSucursal");
            String nombre = resultSet.getString("nombreSucursal");
            String direccion = resultSet.getString("direccionSucursal");
            String telefono = resultSet.getString("telefono");
            String horario  = resultSet.getString("horarioSucursal");
            int idJefe = resultSet.getInt("idJefe");
            
            Sucursal sucursal = new Sucursal(idSucursal,nombre,direccion,telefono,horario, idJefe);
            sucursalList.add(sucursal);
        }
        tableSucursal.setItems( sucursalList);
    }
    
    
    @FXML
    private void agregar(MouseEvent event) {
        limpiar();
        mostrarIcono(true);
        sucursalSeleccionada = null;
        
    }

    @FXML
    private void editar(MouseEvent event) {
        sucursalSeleccionada = tableSucursal.getSelectionModel().getSelectedItem();
        if (sucursalSeleccionada != null) {
            habilitarCampos(false);
            mostrarIcono(true);
            nombreSucursal.setText(String.valueOf(sucursalSeleccionada.getNombreSucursal()));
            direccionSucursal.setText(String.valueOf(sucursalSeleccionada.getDireccionSucursal()));
            TelefonoSucursal.setText(sucursalSeleccionada.getTelefono());
            horarioSucursal.setText(sucursalSeleccionada.getHorarioSucursal());
            codigoJefe.setText(String.valueOf(sucursalSeleccionada.getIdJefe()));
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una sucursal para editar.");
        }
    }

    @FXML
    private void borrar(MouseEvent event) {
        Sucursal sucursalABorrar = tableSucursal.getSelectionModel().getSelectedItem();
        if (sucursalABorrar == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione una sucursal para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, 
        "¿Está seguro de que desea eliminar esta sucursal?", 
        "Confirmar eliminación", 
        JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "call EliminarSucursal(?)";

            try (Connection conn = Conexion.conectar();
                CallableStatement cs = conn.prepareCall(sql)){
            
                cs.setInt(1, sucursalABorrar.getIdSucursal());
                    sucursalList.remove(sucursalABorrar);
                    tableSucursal.refresh();
                    JOptionPane.showMessageDialog(null, "El permiso ha sido eliminado exitosamente.");
                    limpiar();
                    mostrarIcono(false);
                    sucursalSeleccionada = null;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar la sucursal: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void guardar(MouseEvent event) {
    boolean esNuevo = sucursalSeleccionada == null;
    String nomb = nombreSucursal.getText();
    String direcc = direccionSucursal.getText();
    String telef = TelefonoSucursal.getText();
    String hora = horarioSucursal.getText();
    int id_jefe = Integer.parseInt(codigoJefe.getText());
    String sql;

    if (!esNuevo) {
        sql = "{call ActualizarSucursal(?, ?, ?, ?, ?, ?)}";
    } else {
        sql = "{call InsertarSucursal(?, ?, ?, ?, ?)}";
    }

    try (Connection conn = Conexion.conectar();
         CallableStatement cs = conn.prepareCall(sql)) {

        if (!esNuevo) {
            cs.setInt(1, sucursalSeleccionada.getIdSucursal());    
            cs.setString(2, nomb);
            cs.setString(3, direcc);
            cs.setString(4, telef);
            cs.setString(5, hora);
            cs.setInt(6, id_jefe);
        } else {
            cs.setString(1, nomb);
            cs.setString(2, direcc);
            cs.setString(3, telef);
            cs.setString(4, hora);
            cs.setInt(5, id_jefe);
        }

        cs.executeUpdate();

        if (esNuevo) {
            // Obtiene el último ID insertado
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                if (rs.next()) {
                    int idSucursal = rs.getInt(1);
                    Sucursal nuevoSucursal = new Sucursal(idSucursal, nomb, direcc, telef, hora, id_jefe);
                    sucursalList.add(nuevoSucursal);
                }
            }
        } else {
            // Actualiza la sucursal seleccionada
            sucursalSeleccionada.setNombreSucursal(nomb);
            sucursalSeleccionada.setDireccionSucursal(direcc);
            sucursalSeleccionada.setTelefono(telef);
            sucursalSeleccionada.setHorarioSucursal(hora);
            sucursalSeleccionada.setIdJefe(id_jefe);
            tableSucursal.refresh();
        }

        JOptionPane.showMessageDialog(null, esNuevo ? "Se insertó correctamente la sucursal" : "Se actualizó correctamente");
        limpiar();
        mostrarIcono(false);
        recargarTabla();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "No se insertó correctamente la sucursal: " + e.getMessage());
        e.printStackTrace();
    }
}
    @FXML
    private void seleccion(MouseEvent event) {
        limpiar();
        sucursalSeleccionada = tableSucursal.getSelectionModel().getSelectedItem();
        if (sucursalSeleccionada != null) {
            nombreSucursal.setText(String.valueOf(sucursalSeleccionada.getNombreSucursal()));
            direccionSucursal.setText(sucursalSeleccionada.getDireccionSucursal());
            horarioSucursal.setText(sucursalSeleccionada.getHorarioSucursal());
            telefono.setText(sucursalSeleccionada.getTelefono());
            codigoJefe.setText(String.valueOf(sucursalSeleccionada.getIdJefe()));
        }
    }
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    void  mostrarIcono(boolean valor){
        textSucursal.setVisible(valor);
        textDireccion.setVisible(valor);
        textTelefono.setVisible(valor);
        textHorario.setVisible(valor);
        textIdjefe.setVisible(valor);
        nombreSucursal.setVisible(valor);
        direccionSucursal.setVisible(valor);
        TelefonoSucursal.setVisible(valor);
        horarioSucursal.setVisible(valor);
        codigoJefe.setVisible(valor);
    }
    
    void limpiar(){
        nombreSucursal.clear();
        direccionSucursal.clear();
        TelefonoSucursal.clear();
        horarioSucursal.clear();
        codigoJefe.clear();
    }
    
    void habilitarCampos(boolean valor){
        nombreSucursal.setDisable(valor);
        direccionSucursal.setDisable(valor);
        TelefonoSucursal.setDisable(valor);
        horarioSucursal.setDisable(valor);
        codigoJefe.setDisable(valor);
    }
    private void recargarTabla(){
        sucursalList.clear(); // Limpia la lista observable
        tableSucursal.getItems().clear(); // Limpia la tabla
        try {
            loadSucursales(); // Recarga los permisos desde la base de datos
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recarga la sucursal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
