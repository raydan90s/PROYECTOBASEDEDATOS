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
    @FXML
    private Permiso permisoSeleccionado = null;
    
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
        limpiar();
        permisoSeleccionado = tablePermiso.getSelectionModel().getSelectedItem();
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
        limpiar();
        mostrarIcono(true);
        permisoSeleccionado = null;
    }

    @FXML
    private void editar(MouseEvent event) {
        permisoSeleccionado = tablePermiso.getSelectionModel().getSelectedItem();
        if (permisoSeleccionado != null) {
            habilitarcampos(false);
            mostrarIcono(true);
            tipo.setText(permisoSeleccionado.getTipo());
            id_empleado.setText(String.valueOf(permisoSeleccionado.getIdEmpleado()));
            fecha_inicio.setValue(permisoSeleccionado.getFechaInicio().toLocalDate());
            fechafin.setValue(permisoSeleccionado.getFechaFin().toLocalDate());
            id_Jefe.setText(String.valueOf(permisoSeleccionado.getIdJefe()));
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un permiso para editar.");
        }
    }

    @FXML
    private void borrar(MouseEvent event) {
        Permiso permisoABorrar = tablePermiso.getSelectionModel().getSelectedItem();
        if (permisoABorrar == null) {
            JOptionPane.showMessageDialog(null, "Por favor, seleccione un permiso para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, 
        "¿Está seguro de que desea eliminar este permiso?", 
        "Confirmar eliminación", 
        JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String sql = "call EliminarPermiso(?)";

            try (Connection conn = Conexion.conectar();
                CallableStatement cs = conn.prepareCall(sql)) {
            
                cs.setInt(1, permisoABorrar.getIdPermiso());
                    permisosList.remove(permisoABorrar);
                    tablePermiso.refresh();
                    JOptionPane.showMessageDialog(null, "El permiso ha sido eliminado exitosamente.");
                    limpiar();
                    mostrarIcono(false);
                    permisoSeleccionado = null;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el permiso: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
 private void guardar(MouseEvent event) {
    boolean esNuevo = permisoSeleccionado == null;
    String tipot = tipo.getText().trim();
    Date fechaInicio = fecha_inicio.getValue() != null ? Date.valueOf(fecha_inicio.getValue()) : null;
    Date fechfin = fechafin.getValue() != null ? Date.valueOf(fechafin.getValue()) : null;
    int codigo = id_empleado.getText().isEmpty() ? 0 : Integer.parseInt(id_empleado.getText().trim());
    int id_jefe = id_Jefe.getText().isEmpty() ? 0 : Integer.parseInt(id_Jefe.getText().trim());
    // Verifica que las fechas no sean nulas
    if (fechaInicio == null || fechfin == null) {
        JOptionPane.showMessageDialog(null, "Las fechas no pueden ser nulas");
        return;
    }
    // Verifica que los IDs no sean cero (o según la lógica de tu aplicación)
    if (codigo == 0 || id_jefe == 0) {
        JOptionPane.showMessageDialog(null, "Los IDs de empleado y jefe deben ser válidos");
        return;
    }
    String sql;
    if (!esNuevo) {
        sql = "{call ActualizarPermiso(?, ?, ?, ?, ?, ?)}";
    } else {
        sql = "{call InsertarPermiso(?, ?, ?, ?, ?)}";
    }

    try (Connection conn = Conexion.conectar();
         CallableStatement cs = conn.prepareCall(sql)) {

        if (!esNuevo) {
            cs.setInt(1, permisoSeleccionado.getIdPermiso());
            cs.setDate(2, fechaInicio);
            cs.setDate(3, fechfin);
            cs.setString(4, tipot);
            cs.setInt(5, codigo);
            cs.setInt(6, id_jefe);
        } else {
            cs.setDate(1, fechaInicio);
            cs.setDate(2, fechfin);
            cs.setString(3, tipot);
            cs.setInt(4, codigo);
            cs.setInt(5, id_jefe);
        }

        cs.execute();

        if (esNuevo) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                if (rs.next()) {
                    int idPermiso = rs.getInt(1);
                    Permiso nuevoPermiso = new Permiso(idPermiso, tipot, fechfin, fechaInicio, codigo, "", id_jefe);
                    permisosList.add(nuevoPermiso);
                }
            }
        } else {
            permisoSeleccionado.setTipo(tipot);
            permisoSeleccionado.setFechaInicio(fechaInicio);
            permisoSeleccionado.setFechaFin(fechfin);
            permisoSeleccionado.setIdEmpleado(codigo);
            permisoSeleccionado.setIdJefe(id_jefe);
            tablePermiso.refresh();
        }
        JOptionPane.showMessageDialog(null, esNuevo ? "Se insertó correctamente el Permiso" : "Se actualizó correctamente el Permiso");
        limpiar();
        mostrarIcono(false);
        recargarTabla();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "No se pudo ejecutar la operación: " + e.getMessage() + " (SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode() + ")");
        e.printStackTrace();
    }
    recargarTabla();
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

    void habilitarcampos(boolean valor){
        tipo.setDisable(valor);
        id_empleado.setDisable(valor);
        fecha_inicio.setDisable(valor);
        fechafin.setDisable(valor);
        id_Jefe.setDisable(valor);
    }
    private void recargarTabla() {
        permisosList.clear(); // Limpia la lista observable
        tablePermiso.getItems().clear(); // Limpia la tabla
        try {
            loadPermisos(); // Recarga los permisos desde la base de datos
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recargar los permisos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}