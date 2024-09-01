/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sak
 */
public class MenuJefeController implements Initializable {

    @FXML
    private Button MostrarEmpleados;
    @FXML
    private Button MostrarMateriaPrima;
    @FXML
    private Button MostrarProveedor;
    @FXML
    private Button MostrarIncidente;
    @FXML
    private Button MostrarPedido;
    @FXML
    private Button MostrarInventario;
    @FXML
    private Button MostrarCaja;
    @FXML
    private Button MostrarCliente;
    @FXML
    private Button MostrarPermisos;
    @FXML
    private Button mostrarSucursal;
    @FXML
    private Button mostrarSatisfaccion;
    @FXML
    private Button mostrarProducto;
    @FXML
    private Button reportes;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        MostrarPermisos.setOnMouseClicked(event -> {
            try {
                mostrarpermisos(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mostrarSucursal.setOnMouseClicked(event -> {
            try {
                mostrarsucursal(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mostrarSatisfaccion.setOnMouseClicked(event -> {
            try {
                mostrarsatisfaccion(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        MostrarEmpleados.setOnMouseClicked(event -> {
            try {
                MostrarEmpleados(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        MostrarIncidente.setOnMouseClicked(event -> {
            try {
                MostrarIncidente(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        MostrarProveedor.setOnMouseClicked(event -> {
            try {
                MostrarProveedores(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        MostrarMateriaPrima.setOnMouseClicked(event -> {
            try {
                MostrarMateriaPrima(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        MostrarPedido.setOnMouseClicked(event -> {
            try{
                MostrarPedido(event);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        });
        MostrarInventario.setOnMouseClicked(event -> {
            try{
                MostrarInventario(event);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        });
        MostrarCaja.setOnMouseClicked(event -> {
            try{
                MostrarCaja(event);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        });

        MostrarCliente.setOnMouseClicked(event -> {
            try {
                MostrarCliente(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });  

        mostrarProducto.setOnMouseClicked(event -> {
            try {
                mostrarproducto(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }); 
        reportes.setOnMouseClicked(event ->{
            try{
                reporte(event);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }
    void MostrarEmpleados(MouseEvent event) throws IOException {
        App.setRoot("Empleados");
    }
    void MostrarMateriaPrima(MouseEvent event) throws IOException {
        App.setRoot("MateriaPrima");
    }
    void MostrarProveedores(MouseEvent event) throws IOException {
        App.setRoot("Proveedor");
    }
    void MostrarIncidente(MouseEvent event) throws IOException {
        App.setRoot("Incidente");
    }
    void MostrarPedido(MouseEvent event) throws IOException {
        App.setRoot("Pedido");
    }
    void MostrarInventario(MouseEvent event) throws IOException {
        App.setRoot("Inventario");
    }
    void MostrarCaja(MouseEvent event) throws IOException {
        App.setRoot("Caja");
    }
    void MostrarCliente(MouseEvent event) throws IOException{
        App.setRoot("Cliente");
    }
    @FXML
    void mostrarpermisos(MouseEvent event) throws IOException{
        App.setRoot("Permisos");
    }

    @FXML
    void mostrarsucursal(MouseEvent event) throws IOException{
        App.setRoot("Sucursal");
    }

    @FXML
    void mostrarsatisfaccion(MouseEvent event) throws IOException{
        App.setRoot("Satisfaccion");
    }

    @FXML
    void mostrarproducto(MouseEvent event) throws IOException{
        App.setRoot("Producto");
    }
    @FXML
    void reporte(MouseEvent event) throws IOException{
        App.setRoot("Reportes");
    }

}
