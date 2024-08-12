/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

/**
 *
 * @author DHAMAR
 */
public class Sucursal {
<<<<<<< HEAD
    private int idSucursal;
    private String nombreSucursal; 
    private String direccionSucursal;
    private String telefono;
    private String horarioSucursal; 
=======

    private int idSucursal;
    private String nombreSucursal;
    private String direccionSucursal;
    private String telefono;
    private String horarioSucursal;
>>>>>>> Empleados-Incidente-Proveedor
    private int idJefe;

    public Sucursal(int idSucursal, String nombreSucursal, String direccionSucursal, String telefono, String horarioSucursal, int idJefe) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.direccionSucursal = direccionSucursal;
        this.telefono = telefono;
        this.horarioSucursal = horarioSucursal;
        this.idJefe = idJefe;
    }
<<<<<<< HEAD
    
=======

>>>>>>> Empleados-Incidente-Proveedor
    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorarioSucursal() {
        return horarioSucursal;
    }

    public void setHorarioSucursal(String horarioSucursal) {
        this.horarioSucursal = horarioSucursal;
    }

    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }
<<<<<<< HEAD
=======

    @Override
    public String toString() {
        return nombreSucursal + " / " + direccionSucursal;
    }

    public Sucursal(int idSucursal, String nombreSucursal) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
    }
>>>>>>> Empleados-Incidente-Proveedor
    
}
