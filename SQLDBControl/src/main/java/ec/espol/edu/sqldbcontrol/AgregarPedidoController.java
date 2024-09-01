/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Diego
 */
public class AgregarPedidoController implements Initializable {
    @FXML
    private DatePicker fecha;
    @FXML
    private TextField hora;
    @FXML
    private TextField cantidad;
    @FXML
    private ComboBox<Empleado> empleadosComboBox;
    @FXML
    private ComboBox<Caja> cajaComboBox;
    @FXML
    private ComboBox<Cliente> clientesComboBox;
    @FXML
    private ComboBox<Productos> productosComboBox;

    private Integer idPedido = null; // Variable para almacenar el ID del pedido
    private PedidoController pedidoController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(obtenerEmpleadosDesdeBD());
        empleadosComboBox.setItems(empleados);
        ObservableList<Caja> caja = FXCollections.observableArrayList(obtenerCajaDesdeBD());
        cajaComboBox.setItems(caja);
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(obtenerClientesDesdeBD());
        clientesComboBox.setItems(clientes);
        ObservableList<Productos> productos = FXCollections.observableArrayList(obtenerProductosDesdeBD());
        productosComboBox.setItems(productos);
    }

    public void setPedidoController(PedidoController pedidoController) {
        this.pedidoController = pedidoController;
    }

    public void cargarDatosPedido(Pedido pedido) {
        this.idPedido = pedido.getIdPedido();
        Timestamp timestamp = pedido.getFechaPedido();
        LocalDate fechaLocalDate = timestamp.toLocalDateTime().toLocalDate();
        LocalTime horaLocalTime = timestamp.toLocalDateTime().toLocalTime();

        fecha.setValue(fechaLocalDate);
        hora.setText(horaLocalTime.toString());

        cantidad.setText(String.valueOf(pedido.getCantidadPedido()));

        for (Empleado empleado : empleadosComboBox.getItems()) {
            if (empleado.getIdEmpleado() == pedido.getIdEmpleado()) {
                empleadosComboBox.setValue(empleado);
                break;
            }
        }

        for (Caja caja : cajaComboBox.getItems()) {
            if (caja.getIdCaja() == pedido.getIdCaja()) {
                cajaComboBox.setValue(caja);
                break;
            }
        }

        for (Cliente cliente : clientesComboBox.getItems()) {
            if (cliente.getCodigo() == pedido.getIdCliente()) {
                clientesComboBox.setValue(cliente);
                break;
            }
        }

        for (Productos producto : productosComboBox.getItems()) {
            if (producto.getCodigo() == pedido.getIdProducto()) {
                productosComboBox.setValue(producto);
                break;
            }
        }
    }

    private List<Empleado> obtenerEmpleadosDesdeBD() {
        List<Empleado> empleados = new ArrayList<>();
        try {
            Connection connection = Conexion.conectar();
            String query = "SELECT idEmpleado, nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioMesero, nombreSucursal FROM Mesero join Sucursal using (idSucursal)";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idEmpleado = resultSet.getInt("idEmpleado");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                String horarioEmpleado = resultSet.getString("horarioEmpleado");
                double salarioMesero = resultSet.getFloat("salarioMesero");
                String nombreSucursal = resultSet.getString("nombreSucursal");

                empleados.add(new Empleado(idEmpleado, nombreEmpleado, apellidoEmpleado, horarioEmpleado, salarioMesero, nombreSucursal, "Mesero"));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    private List<Caja> obtenerCajaDesdeBD() {
        List<Caja> cajas = new ArrayList<>();
        try {
            Connection connection = Conexion.conectar();
            String query = "SELECT * FROM Caja";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idCaja = resultSet.getInt("idCaja");
                float gastos = resultSet.getFloat("gastos");
                float sobrantes = resultSet.getFloat("sobrantes");
                float valorCajaChiquita = resultSet.getFloat("valorCajaChiquita");
                LocalDateTime fechaRegistro = resultSet.getTimestamp("fechaRegistro").toLocalDateTime();
                int idSucursal = resultSet.getInt("idSucursal");

                cajas.add(new Caja(idCaja, gastos, sobrantes, valorCajaChiquita, fechaRegistro, idSucursal));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cajas;
    }

    private List<Cliente> obtenerClientesDesdeBD() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection connection = Conexion.conectar();
            String query = "SELECT * FROM Cliente";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idCliente = resultSet.getInt("idCliente");
                String nombreCliente = resultSet.getString("nombreCliente");
                String apellidoCliente = resultSet.getString("apellidoCliente");
                String direccionCliente = resultSet.getString("direccionCliente");
                String telefonoCliente = resultSet.getString("telefonoCliente");
                String cedula = resultSet.getString("cedula");

                clientes.add(new Cliente(idCliente, nombreCliente, apellidoCliente, direccionCliente, telefonoCliente, cedula));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    private List<Productos> obtenerProductosDesdeBD() {
        List<Productos> productos = new ArrayList<>();
        try {
            Connection connection = Conexion.conectar();
            String query = "SELECT * FROM Producto";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idProducto = resultSet.getInt("idProducto");
                String nombreProducto = resultSet.getString("nombreProducto");
                int cantidadRealizada = resultSet.getInt("cantidadRealizada");
                Date fechaProduccion = resultSet.getDate("fechaProduccion");
                double precioProducto = resultSet.getDouble("precioProducto");
                String recetaProducto = resultSet.getString("recetaProducto");
                int idEmpleado = resultSet.getInt("idEmpleado");

                productos.add(new Productos(idProducto, nombreProducto, cantidadRealizada, fechaProduccion, precioProducto, recetaProducto, idEmpleado));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    @FXML
    private void guardarPedido() {
        if (fecha.getValue() == null || hora.getText().isEmpty() || cantidad.getText().isEmpty() ||
            empleadosComboBox.getValue() == null || cajaComboBox.getValue() == null ||
            clientesComboBox.getValue() == null || productosComboBox.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Datos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, completa todos los campos.");
            alert.showAndWait();
            return;
        }

        String fechaSeleccionada = fecha.getValue().toString();
        String horaSeleccionada = hora.getText();
        String fechaYHora = fechaSeleccionada + " " + horaSeleccionada + ":00";

        int cantidades;
        try {
            cantidades = Integer.parseInt(cantidad.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de formato");
            alert.setHeaderText(null);
            alert.setContentText("La cantidad debe ser un número entero.");
            alert.showAndWait();
            return;
        }

        Empleado empleadoSeleccionado = empleadosComboBox.getValue();
        Caja cajaSeleccionada = cajaComboBox.getValue();
        Cliente clienteSeleccionado = clientesComboBox.getValue();
        Productos productoSeleccionado = productosComboBox.getValue();

        try {
            Connection connection = Conexion.conectar();
            String query;

            if (idPedido == null) {
                query = "INSERT INTO Pedido (fechaPedido, cantidadPedido, idEmpleado, idCaja, idCliente, idProducto) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setTimestamp(1, Timestamp.valueOf(fechaYHora));
                statement.setInt(2, cantidades);
                statement.setInt(3, empleadoSeleccionado.getIdEmpleado());
                statement.setInt(4, cajaSeleccionada.getIdCaja());
                statement.setInt(5, clienteSeleccionado.getCodigo());
                statement.setInt(6, productoSeleccionado.getCodigo());

                statement.executeUpdate();
                statement.close();
            } else {
                query = "UPDATE Pedido SET fechaPedido = ?, cantidadPedido = ?, idEmpleado = ?, idCaja = ?, idCliente = ?, idProducto = ? "
                        + "WHERE idPedido = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setTimestamp(1, Timestamp.valueOf(fechaYHora));
                statement.setInt(2, cantidades);
                statement.setInt(3, empleadoSeleccionado.getIdEmpleado());
                statement.setInt(4, cajaSeleccionada.getIdCaja());
                statement.setInt(5, clienteSeleccionado.getCodigo());
                statement.setInt(6, productoSeleccionado.getCodigo());
                statement.setInt(7, idPedido);

                statement.executeUpdate();
                statement.close();
            }

            connection.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("El pedido se ha guardado correctamente.");
            alert.showAndWait();

            if (pedidoController != null) {
                pedidoController.loadPedidos();
            }

            Stage stage = (Stage) fecha.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al guardar el pedido. Por favor, inténtalo de nuevo.");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) fecha.getScene().getWindow();
        stage.close();
    }
}
