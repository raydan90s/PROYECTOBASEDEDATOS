<<<<<<< HEAD
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

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    

    
}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;


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

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
    

    
}
>>>>>>> PEDIDO-CAJA-INVENTARIO
