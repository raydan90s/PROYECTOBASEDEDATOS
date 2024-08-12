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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
}
