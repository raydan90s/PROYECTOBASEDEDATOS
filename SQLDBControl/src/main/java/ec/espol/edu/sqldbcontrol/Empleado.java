/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

/**
 *
 * @author Sak
 */
public class Empleado {

    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String horarioEmpleado;
    private double salario;
    private String nombreSucursal;
    private String tipoEmpleado;

    public Empleado(String nombreEmpleado, String apellidoEmpleado, String horarioEmpleado, double salario, String nombreSucursal, String tipoEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.horarioEmpleado = horarioEmpleado;
        this.salario = salario;
        this.nombreSucursal = nombreSucursal;
        this.tipoEmpleado = tipoEmpleado;
    }

    // Getters y setters
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public String getHorarioEmpleado() {
        return horarioEmpleado;
    }

    public double getSalario() {
        return salario;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }
}
