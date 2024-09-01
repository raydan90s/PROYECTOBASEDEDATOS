/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

/**
 *
 * @author Diego
 */
import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class Jefe {
    private int idJefe;
    private String nombreJefe;
    private String apellidoJefe;
    private LocalDateTime ultActualizacion;
    private String telefonoJefe;

    // Constructor
    public Jefe(int idJefe, String nombreJefe, String apellidoJefe, LocalDateTime ultActualizacion, String telefonoJefe) {
        this.idJefe = idJefe;
        this.nombreJefe = nombreJefe;
        this.apellidoJefe = apellidoJefe;
        this.ultActualizacion = ultActualizacion;
        this.telefonoJefe = telefonoJefe;
    }

    // Getters y Setters
    public int getIdJefe() {
        return idJefe;
    }

    public void setIdJefe(int idJefe) {
        this.idJefe = idJefe;
    }

    public String getNombreJefe() {
        return nombreJefe;
    }

    public void setNombreJefe(String nombreJefe) {
        this.nombreJefe = nombreJefe;
    }

    public String getApellidoJefe() {
        return apellidoJefe;
    }

    public void setApellidoJefe(String apellidoJefe) {
        this.apellidoJefe = apellidoJefe;
    }

    public LocalDateTime getUltActualizacion() {
        return ultActualizacion;
    }

    public void setUltActualizacion(LocalDateTime ultActualizacion) {
        this.ultActualizacion = ultActualizacion;
    }

    public String getTelefonoJefe() {
        return telefonoJefe;
    }

    public void setTelefonoJefe(String telefonoJefe) {
        this.telefonoJefe = telefonoJefe;
    }

    @Override
    public String toString() {
        return nombreJefe + " " +apellidoJefe;
    }
}

