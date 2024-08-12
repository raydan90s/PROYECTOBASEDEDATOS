/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.sql.Date;

/**
 *
 * @author Sak
 */
public class MateriaPrima {
    private int idMateria;
    private String nombreMateria;
    private Date fechaCaducidad;
    private Date fechaElaboracion;
    private int cantidad;
    private Proveedor proveedor;

    public MateriaPrima(int idMateria, String nombreMateria, Date fechaCaducidad, Date fechaElaboracion, int cantidad, Proveedor proveedor) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public void setFechaElaboracion(Date fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public Date getFechaElaboracion() {
        return fechaElaboracion;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public String getNombreProveedor() {
        return proveedor != null ? proveedor.getNombreCompleto() : "";
    }

}