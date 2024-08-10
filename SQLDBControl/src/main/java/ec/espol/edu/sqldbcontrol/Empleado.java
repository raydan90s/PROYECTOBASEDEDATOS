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
    
    private int id;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String horarioEmpleado;
    private double salario;
    private String nombreSucursal;
    private String tipoEmpleado;

    public Empleado(int id,String nombreEmpleado, String apellidoEmpleado, String horarioEmpleado, double salario, String nombreSucursal, String tipoEmpleado) {
        this.id = id;
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

    public int getIdEmpleado() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
