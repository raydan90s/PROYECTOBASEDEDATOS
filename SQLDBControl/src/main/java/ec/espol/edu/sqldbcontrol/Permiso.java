/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.sql.Date;

/**
 *
 * @author DHAMAR
 */
public class Permiso {
    private int  idPermiso;
    private Date fechaFin;
    private Date fechaInicio;
    private String tipo;
    private int idEmpleado;
    private int idJefe;
    private String nombreEmpleado;

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }

    public Permiso(int idPermiso, String tipo, Date fechaInicio, Date fechaFin, int idEmpleado,String Nombre) {
        this.idPermiso = idPermiso;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.tipo = tipo;
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = Nombre;
    }

    public Permiso(int idPermiso,String tipo, Date fechaInicio, Date fechafin, int idEmpleado, int idJefe) {
        this.idPermiso = idPermiso;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.tipo = tipo;
        this.idEmpleado = idEmpleado;
        this.idJefe = idJefe;
    }
     public Permiso(int idPermiso,String tipo, Date fechaInicio, Date fechaFin, int idEmpleado,String nombre, int idJefe) {
        this.idPermiso = idPermiso;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.tipo = tipo;
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombre;
        this.idJefe = idJefe;
    }
    
}
