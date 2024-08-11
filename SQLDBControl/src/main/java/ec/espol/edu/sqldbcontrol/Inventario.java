/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

/**
 *
 * @author Diego
 */
public class Inventario {
    private int idInventario;
    private int idJefe;
    private String nombre;

    public Inventario(int idInventario, int idJefe, String nombre) {
        this.idInventario = idInventario;
        this.idJefe = idJefe;
        this.nombre = nombre;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public int getIdJefe() {
        return idJefe;
    }

    public String getNombre() {
        return nombre;
    }
    
}
