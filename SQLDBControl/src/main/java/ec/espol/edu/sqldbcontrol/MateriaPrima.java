/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.sql.Date;
import java.sql.Timestamp;

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
    private int idInventario;
    private int cantidadRestar;

    public MateriaPrima(int idMateria, String nombreMateria, Date fechaCaducidad, Date fechaElaboracion, int cantidad) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.cantidad = cantidad;
    }

    public MateriaPrima(int idMateria, int idInventario, int cantidadRestar) {
        this.idMateria = idMateria;
        this.idInventario = idInventario;
        this.cantidadRestar = cantidadRestar;
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

    public int getIdInventario() {
        return idInventario;
    }
    
    public int getCantidadRestar() {
        return cantidadRestar;
    }


    private Proveedor proveedor;

    public MateriaPrima(int idMateria, String nombreMateria, Date fechaCaducidad, Date fechaElaboracion, int cantidad, Proveedor proveedor) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }

    public MateriaPrima(int idMateria2, String nombreMateria2, Date fechaCaducidad2, Date fechaElaboracion2,
            int cantidad2, Timestamp fechaAct, Proveedor proveedor2, int codigoi) {
        //TODO Auto-generated constructor stub
    }

    public MateriaPrima(int idMateria2, String nombre, Date fechaCSql, Date fechaESql, int cantidad2,
            Proveedor proveedor2, int idInventario2) {
        //TODO Auto-generated constructor stub
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

    
    public String getNombreProveedor() {
        return proveedor != null ? proveedor.getNombreCompleto() : "";
    }
}