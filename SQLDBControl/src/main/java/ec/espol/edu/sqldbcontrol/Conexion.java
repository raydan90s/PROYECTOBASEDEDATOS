/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Sak
 */
public class Conexion {

    private static final String  bd = "b7xfe1k5tsfjqsnsd37h";
    private static final String url = "jdbc:mysql://b7xfe1k5tsfjqsnsd37h-mysql.services.clever-cloud.com/";
    private static final String user = "ud4vqrtrfljvjz3d";
    private static final String password = "xHbNoJo0daPaf1LrlwAE";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection cx;

    public Conexion() {
    }

    public static Connection conectar() {
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url + bd, user, password);
            System.out.println("Se conectó correctamente a la BD " + bd);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("No se conectó a BD " + bd);
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }

    public void desconectar() {
        try {
            cx.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
