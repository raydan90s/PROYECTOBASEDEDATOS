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

    public MateriaPrima(int idMateria, String nombreMateria, Date fechaCaducidad, Date fechaElaboracion, int cantidad) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.cantidad = cantidad;
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
}