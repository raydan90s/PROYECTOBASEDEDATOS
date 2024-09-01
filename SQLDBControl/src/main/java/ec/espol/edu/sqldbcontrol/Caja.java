/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 *
 * @author Diego
 */
public class Caja {
    private int idCaja;
    private float gastos;
    private float sobrantes;
    private float valorCajaChiquita;
    private LocalDateTime fechaRegistro;
    private int idSucursal;

    public Caja(int idCaja, float gastos, float sobrantes, float valorCajaChiquita, LocalDateTime fechaRegistro, int idSucursal) {
        this.idCaja = idCaja;
        this.gastos = gastos;
        this.sobrantes = sobrantes;
        this.valorCajaChiquita = valorCajaChiquita;
        this.fechaRegistro = fechaRegistro;
        this.idSucursal = idSucursal;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public float getGastos() {
        return gastos;
    }

    public float getSobrantes() {
        return sobrantes;
    }

    public float getValorCajaChiquita() {
        return valorCajaChiquita;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    @Override
    public String toString() {
        return String.valueOf(idCaja) ;
    }
    
}
