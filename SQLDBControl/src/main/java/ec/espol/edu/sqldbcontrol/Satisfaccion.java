/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.time.LocalDate;
import java.sql.Date;

/**
 *
 * @author DHAMAR
 */
public class Satisfaccion {
    private int idSatisfaccion;
    private int calificacionCliente;
    private int numFactura; 
    private int idCliente; 
    private int idProducto; 
    private int idSucursal;
    private Date fechaEvaluacion;

    public Satisfaccion(int idSatisfaccion, int calificacionCliente, int numFactura, int idCliente, int idProducto, int idSucursal, Date fechaEvaluacion) {
        this.idSatisfaccion = idSatisfaccion;
        this.calificacionCliente = calificacionCliente;
        this.numFactura = numFactura;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.idSucursal = idSucursal;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public int getIdSatisfaccion() {
        return idSatisfaccion;
    }

    public void setIdSatisfaccion(int idSatisfaccion) {
        this.idSatisfaccion = idSatisfaccion;
    }

    public int getCalificacionCliente() {
        return calificacionCliente;
    }

    public void setCalificacionCliente(int calificacionCliente) {
        this.calificacionCliente = calificacionCliente;
    }

    public int getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Date getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(LocalDate fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
    
    
}
