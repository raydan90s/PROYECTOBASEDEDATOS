/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;




/**
 *
 * @author Raydan
 */
public class ClienteController implements Initializable{
    @FXML
    private Text volver;
    @FXML
    private TableView<Cliente> clienteTable;
    @FXML
    private TableColumn<Cliente, Integer> codigoColumn;
    @FXML
    private TableColumn<Cliente, String> nombreColumn;
    @FXML
    private TableColumn<Cliente, String> apellidoColumn;
    @FXML
    private TableColumn<Cliente, String> cedulaColumn;
    @FXML
    private TableColumn<Cliente, String> direccionColumn;
    @FXML
    private TableColumn<Cliente, String> telefonoColumn;
    @FXML
    private VBox formPane;
    @FXML
    private Button agregarCliente;
    @FXML
    private Button eliminarCliente;
    @FXML
    private Button modificarCliente;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField cedulaField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;

    private ObservableList<Cliente> clienteList;
    

    @FXML
    private void handleAddClient(ActionEvent event) {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String cedula = cedulaField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        
        String consulta = "Insert into Cliente(nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, cedula) "
                + "values(?, ?, ?, ?, ?)";
        try{
            PreparedStatement ps = Conexion.conectar().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, direccion);
            ps.setString(4, telefono);
            ps.setString(5, cedula);
            
            ps.executeUpdate();
                        
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int idCliente = 0;
            
            if (generatedKeys.next()) {
            idCliente = generatedKeys.getInt(1);
        }
        
            // Crear el nuevo cliente con el ID recuperado
            Cliente nuevoCliente = new Cliente(idCliente, nombre, apellido, cedula, direccion, telefono);
        
            // Añadir el nuevo cliente a la lista observable
            clienteList.add(nuevoCliente);
            
            JOptionPane.showMessageDialog(null, "Se insertó correctamente el Cliente");

            
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se insertó correctamente el Cliente " + e.toString());
        }


        updateVisibility(false);
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {
        formPane.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarCliente.setOnAction(event -> updateVisibility(true));
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        cedulaColumn.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono")); 

        clienteList = FXCollections.observableArrayList();

        try {
            loadEmpleados();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clienteTable.setItems(clienteList);
    }
    
    
    private void loadEmpleados() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, cedula "
                + "FROM Cliente";

                
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            
            int codigo = resultSet.getInt("idCliente");
            String nombre = resultSet.getString("nombreCliente");
            String apellido = resultSet.getString("apellidoCliente");
            String direccion = resultSet.getString("direccionCliente");
            String telefono = resultSet.getString("telefonoCliente");
            String cedula = resultSet.getString("cedula");

            Cliente cliente = new Cliente(codigo, nombre, apellido, cedula, direccion, telefono);
            clienteList.add(cliente);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    
    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
    
    private void updateVisibility(boolean formVisible) {
    formPane.setVisible(formVisible);
    agregarCliente.setVisible(!formVisible);
    eliminarCliente.setVisible(!formVisible);
    modificarCliente.setVisible(!formVisible);
}


}
