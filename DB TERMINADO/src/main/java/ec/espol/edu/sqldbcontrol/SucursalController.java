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
            String sql = "DELETE FROM Sucursal WHERE idSucursal = ?";

            try (Connection conn = Conexion.conectar();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
                ps.setInt(1, sucursalABorrar.getIdSucursal());
            
                int filasAfectadas = ps.executeUpdate();
            
                if (filasAfectadas > 0) {
                    sucursalList.remove(sucursalABorrar);
                    tableSucursal.refresh();
                    JOptionPane.showMessageDialog(null, "El permiso ha sido eliminado exitosamente.");
                    limpiar();
                    mostrarIcono(false);
                    sucursalSeleccionada = null;
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar la sucursal.");
                }
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
        String direcc= direccionSucursal.getText();
        String telef = TelefonoSucursal.getText();
        String hora = horarioSucursal.getText();
        int id_jefe = Integer.parseInt(codigoJefe.getText());
        String sql;
        if (!esNuevo) {
        sql = "UPDATE Sucursal SET nombreSucursal = ?, direccionSucursal = ?, telefono = ?, horarioSucursal = ?, idJefe = ? WHERE idSucursal = ?";
    } else {
        sql = "INSERT INTO Sucursal (nombreSucursal, direccionSucursal, telefono, horarioSucursal, idJefe) VALUES (?, ?, ?, ?, ?)";
    }

    try (Connection conn = Conexion.conectar();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        ps.setString(1, nomb);
        ps.setString(2, direcc);
        ps.setString(3, telef);
        ps.setString(4, hora);
        ps.setInt(5, id_jefe);
        
        if (!esNuevo) {
            ps.setInt(6, sucursalSeleccionada.getIdSucursal());
        }

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("La operación falló, no se afectaron filas.");
        }

        if (esNuevo) {
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idSucursal = generatedKeys.getInt(1);
                    Sucursal nuevoSucursal = new Sucursal(idSucursal, nomb, direcc,telef, hora, id_jefe);
                    sucursalList.add(nuevoSucursal);
                } else {
                    throw new SQLException("No se pudo obtener el ID del nuevo permiso.");
                }
            }
        } else {
            sucursalSeleccionada.setNombreSucursal(nomb);
            sucursalSeleccionada.setDireccionSucursal(direcc);
            sucursalSeleccionada.setTelefono(telef);
            sucursalSeleccionada.setHorarioSucursal(hora);
            sucursalSeleccionada.setIdJefe(id_jefe);
            tableSucursal.refresh();
        }
            JOptionPane.showMessageDialog(null, "Se insertó correctamente la sucursal");
            limpiar();
            mostrarIcono(false);
            recargarTabla();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se insertó correctamente la sucursal " + e.toString());
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
