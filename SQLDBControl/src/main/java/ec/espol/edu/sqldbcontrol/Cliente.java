/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;
import javax.swing.JTextField;
import java.sql.CallableStatement;
import javax.swing.JOptionPane;



/**
 *
 * @author Raydan
 */
public class Cliente {
    private int codigo;
    private String nombre;
    private String apellido;
    private String cedula;
    private String direccionCliente;
    private String telefono;

    public Cliente(int codigo, String nombre, String apellido, String cedula, String direccionCliente, String telefono) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccionCliente = direccionCliente;
        this.telefono = telefono;
    }
    
    public Cliente(String nombre, String apellido, String cedula, String direccionCliente, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.direccionCliente = direccionCliente;
        this.telefono = telefono;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getTelefono() {
        return telefono;
    }
    

    
}
