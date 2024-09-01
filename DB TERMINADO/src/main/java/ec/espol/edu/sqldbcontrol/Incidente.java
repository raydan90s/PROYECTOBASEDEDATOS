/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.util.Date;

public class Incidente {
    private int idIncidente;
    private String accionTomada;
    private String descripcion;
    private Date fechaIncidente;
    private int idJefe;
    private int idEmpleado;

    // Constructor
    public Incidente(int idIncidente, String accionTomada, String descripcion, Date fechaIncidente, int idJefe, int idEmpleado) {
        this.idIncidente = idIncidente;
        this.accionTomada = accionTomada;
        this.descripcion = descripcion;
        this.fechaIncidente = fechaIncidente;
        this.idJefe = idJefe;
        this.idEmpleado = idEmpleado;
    }

    // Getters y Setters
    public int getIdIncidente() {
        return idIncidente;
    }

    public void setIdIncidente(int idIncidente) {
        this.idIncidente = idIncidente;
    }

    public String getAccionTomada() {
        return accionTomada;
    }

    public void setAccionTomada(String accionTomada) {
        this.accionTomada = accionTomada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(Date fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
}
