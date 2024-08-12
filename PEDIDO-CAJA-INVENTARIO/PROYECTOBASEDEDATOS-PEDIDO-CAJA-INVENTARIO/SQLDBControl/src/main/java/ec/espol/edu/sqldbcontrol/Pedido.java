/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Diego
 */
public class Pedido {
    private int idPedido;
    private Timestamp fechaPedido;
    private int cantidadPedido;
    private int idEmpleado;
    private int idCaja;
    private int idCliente;
    private int idProducto;

    public Pedido(int idPedido, Timestamp fechaPedido, int cantidadPedido, int idEmpleado, int idCaja, int idCliente, int idProducto) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.cantidadPedido = cantidadPedido;
        this.idEmpleado = idEmpleado;
        this.idCaja = idCaja;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public int getCantidadPedido() {
        return cantidadPedido;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setId(int id) {
        this.idPedido = id;
    }

    
    
    
}
