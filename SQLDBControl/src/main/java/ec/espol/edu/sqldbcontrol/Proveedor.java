/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

/**
 *
 * @author Raydan
 */
public class Proveedor {
    String nombreProveedor;
    String apellidoProveedor;
    int Codigo;

    public Proveedor(int Codigo, String nombreProveedor, String apellidoProveedor) {
        this.nombreProveedor = nombreProveedor;
        this.apellidoProveedor = apellidoProveedor;
        this.Codigo = Codigo;
    }
    
    public Proveedor(String nombreProveedor, String apellidoProveedor){
        this.nombreProveedor = nombreProveedor;
        this.apellidoProveedor = apellidoProveedor;
    }
    

    public String getNombreEmpleado() {
        return nombreProveedor;
    }

    public String getApellidoEmpleado() {
        return apellidoProveedor;
    }

    public int getCodigo() {
        return Codigo;
    }
    
    public String getNombreCompleto() {
        return nombreProveedor + " " + apellidoProveedor;
    }

}
