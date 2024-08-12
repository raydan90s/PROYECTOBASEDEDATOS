<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;
import java.sql.Date;

/**
 *
 * @author Raydan
 */
public class Productos {
    private int codigo;
    private String nombreProducto;
    private int cantidad;
    private Date fechaProduccion;
    private double precio;
    private String receta;
    private Empleado empleado;

    public Productos(int codigo, String nombreProducto, int cantidad, Date fechaProduccion, double precio, String receta, Empleado empleado) {
        this.codigo = codigo;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.fechaProduccion = fechaProduccion;
        this.precio = precio;
        this.receta = receta;
        this.empleado = empleado;
        
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public double getPrecio() {
        return precio;
    }
    
    public Empleado getEmpleado() {
        return empleado;
    }

    public String getReceta() {
        return receta;
    }
    
    public String getNombreCocinero() {
        return empleado != null ? empleado.getNombreCompleto() : "";
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;
import java.sql.Date;

/**
 *
 * @author Raydan
 */
public class Productos {
    private int codigo;
    private String nombreProducto;
    private int cantidad;
    private Date fechaProduccion;
    private double precio;
    private String receta;
    private int idEmpleado;

    public Productos(int codigo, String nombreProducto, int cantidad, Date fechaProduccion, double precio, String receta, int empleado) {
        this.codigo = codigo;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.fechaProduccion = fechaProduccion;
        this.precio = precio;
        this.receta = receta;
        this.idEmpleado = empleado;
        
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public double getPrecio() {
        return precio;
    }
    
    public int getEmpleado() {
        return idEmpleado;
    }

    public String getReceta() {
        return receta;
    }
    

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }

    public void setEmpleado(int empleado) {
        this.idEmpleado = empleado;
    }

    @Override
    public String toString() {
        return nombreProducto ;
    }
    
}
>>>>>>> PEDIDO-CAJA-INVENTARIO
