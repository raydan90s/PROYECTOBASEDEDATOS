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
import javafx.event.ActionEvent;
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
public class PermisosController implements Initializable {
    private Permiso permisoSelecionado;
    @FXML
    private TableColumn<Permiso, Integer> idPermiso;
    @FXML
    private TableColumn<Permiso, String> Ttipo;
    @FXML
    private TableColumn<Permiso, Date> TfechaInicio;
    @FXML
    private TableColumn<Permiso, Date> TFechaFin;
    @FXML
    private TableColumn<Permiso, Integer> TId_emp;
    @FXML
    private TableColumn<Permiso, String> NombreEmpleado;
    @FXML
    private Button btagregar;
    @FXML
    private Button bteditar;
    @FXML
    private Button btborrar;
    @FXML
    private TextField tipo;
    @FXML
    private TextField id_empleado;
    @FXML
    private DatePicker fecha_inicio;
    @FXML
    private Button guardar;
    @FXML
    private Text volver;
    @FXML
    private Text textTipo;
    @FXML
    private Text textfi;
    @FXML
    private Text textff;
    @FXML
    private Text codemp;
    @FXML
    private DatePicker fechafin;
    ObservableList<Permiso> permisosList = FXCollections.observableArrayList(); 
    @FXML
    private TableView<Permiso> tablePermiso;
    @FXML
    private TableColumn<Permiso, Integer> idjefe;
    @FXML
    private TextField id_Jefe;
    @FXML
    private Text codeJefe;
    
    
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
        idPermiso.setCellValueFactory(new PropertyValueFactory<>("idPermiso"));
        Ttipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        TfechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        TFechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        TId_emp.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        NombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        idjefe.setCellValueFactory(new PropertyValueFactory<>("idJefe"));
        try {
            loadPermisos();
        }   catch (SQLException e) {
            e.printStackTrace();
        }
        
        tablePermiso.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                
            }
        });
        
    }    
    
    public void loadPermisos() throws SQLException{
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idPermiso, tipo, FechaInicio, FechaFin, IdEmpleado, "
                + "concat(nombreEmpleado,' ',apellidoEmpleado) as Nombre_completo, idJefe "
                + "FROM Permiso JOIN Mesero USING(idEmpleado);"; 

        ResultSet resultSet = statement.executeQuery(query);
        
        while (resultSet.next()) {
            int idPermiso = resultSet.getInt("idPermiso");
            String tipo = resultSet.getString("tipo");
            Date fechai = resultSet.getDate("FechaInicio");
            Date fechaf = resultSet.getDate("FechaFin");
            int idEmpleado = resultSet.getInt("idEmpleado");
            String nombreEmp = resultSet.getString("Nombre_completo");
            int iJefe = resultSet.getInt("idJefe");
            
            Permiso permiso = new Permiso(idPermiso, tipo, fechai, fechaf, idEmpleado, nombreEmp, iJefe);
            permisosList.add(permiso);
        }
        tablePermiso.setItems(permisosList);
    }
    @FXML
    private void seleccion(MouseEvent event) {
        
        Permiso permisoSeleccionado = tablePermiso.getSelectionModel().getSelectedItem();
        if (permisoSeleccionado != null) {
            id_empleado.setText(String.valueOf(permisoSeleccionado.getIdEmpleado()));
            tipo.setText(permisoSeleccionado.getTipo());
            fecha_inicio.setValue(permisoSeleccionado.getFechaInicio().toLocalDate());
            fechafin.setValue(permisoSeleccionado.getFechaFin().toLocalDate());
            id_Jefe.setText(String.valueOf(permisoSeleccionado.getIdJefe()));
        }
    }

    @FXML
    private void agregar(MouseEvent event) {
        mostrarIcono(true);
        
    }

    @FXML
    private void editar(MouseEvent event) {
        mostrarIcono(true);
    }

    @FXML
    private void borrar(MouseEvent event) {
    }

    @FXML
    private void guardar(MouseEvent event) {
        String tipot = tipo.getText();
        Date fechaInicio = Date.valueOf(fecha_inicio.getValue());
        Date fechfin = Date.valueOf(fechafin.getValue());
        int codigo = Integer.parseInt( id_empleado.getText());
        int id_jefe = Integer.parseInt(id_Jefe.getText());
        String sql = "INSERT INTO Permiso ( fechaInicio, fechaFin, tipo, idEmpleado, idJefe) VALUES (?, ?, ?,?,?)";
        
        try{
            PreparedStatement ps = Conexion.conectar().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);  
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechfin);
            ps.setString(3, tipot);
            ps.setInt(4, codigo);
            ps.setInt(5, id_jefe);
            
            ps.executeUpdate();
                        
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int idPermiso = 0;
            
            if (generatedKeys.next()) {
            idPermiso = generatedKeys.getInt(1);
        }
        
            // Crear el nuevo cliente con el ID recuperado
            Permiso nuevoPermiso = new Permiso( idPermiso, tipot, fechfin, fechaInicio, codigo, id_jefe);
        
            // Añadir el nuevo cliente a la lista observable
            permisosList.add(nuevoPermiso);
            
            JOptionPane.showMessageDialog(null, "Se insertó correctamente el Permiso");
            limpiar();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se insertó correctamente el Cliente " + e.toString());
        }
    }
    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    
    void mostrarIcono(boolean valor){
        tipo.setVisible(valor);
        id_empleado.setVisible(valor);
        fecha_inicio.setVisible(valor);
        fechafin.setVisible(valor);
        guardar.setVisible(valor);
        textTipo.setVisible(valor);
        textfi.setVisible(valor);
        textff.setVisible(valor);
        codemp.setVisible(valor);
        id_Jefe.setVisible(valor);
        codeJefe.setVisible(valor);
    }
    
    void limpiar(){
        tipo.clear();
        id_empleado.clear();
        fecha_inicio.setValue(null);
        fechafin.setValue(null);
        id_Jefe.clear();
   

    }

    
}
