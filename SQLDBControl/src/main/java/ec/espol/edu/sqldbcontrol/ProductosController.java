/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.sql.CallableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 * 
 * @author Raydan
 */
public class ProductosController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<Productos> productoTable;
    @FXML
    private TableColumn<Productos, Integer> codigounicocolumn;
    @FXML
    private TableColumn<Productos, String> nombrecolumn;
    @FXML
    private TableColumn<Productos, Integer> cantidadcolumn;
    @FXML
    private TableColumn<Productos, Date> fechaproduccioncolumn;
    @FXML
    private TableColumn<Productos, Double> preciocolumn;
    @FXML
    private TableColumn<Productos, String> recetacolumn;
    @FXML
    private TableColumn<Productos, String> nombrecocinero;

    @FXML
    private VBox formProducto;
    @FXML
    private Button agregarProducto;
    @FXML
    private Button eliminarProducto;
    @FXML
    private Button modificarProducto;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField cantidadField;
    @FXML
    private TextField fechaField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField recetaField;
    @FXML
    private TextField cocineroField;
    private ObservableList<Productos> productosList;
    private ObservableList<Empleado> empleadoList;
    private int filaSeleccionada;

    @FXML
    private VBox modificar;
    @FXML
    private TextField idFieldM;
    @FXML
    private TextField nombreFieldM;
    @FXML
    private TextField cantidadFieldM;
    @FXML
    private TextField fechaProduccionM;
    @FXML
    private TextField precioFieldM;
    @FXML
    private TextField recetaFieldM;
    @FXML
    private TextField cocineroFieldM;
    int idEmpleado;

    // CODIGO PARA INSERTAR PRODUCTO CON PROCEDURE
    @FXML
    private void handleAddProducto(ActionEvent event) {
        // Captura los datos del formulario
        String nombre = nombreField.getText();
        int cantidad;
        double precio;
        int idCocinero;

        try {
            cantidad = Integer.parseInt(cantidadField.getText());
            precio = Double.parseDouble(precioField.getText());
            idCocinero = Integer.parseInt(cocineroField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Asegúrese de que los campos numéricos sean válidos.");
            return;
        }

        String receta = recetaField.getText();

        // Convertir la fecha del campo de texto a un objeto java.util.Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaUtil;
        try {
            fechaUtil = sdf.parse(fechaField.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha incorrecta");
            return; // Salir del método si hay un error en la fecha
        }

        // Convertir java.util.Date a java.sql.Date para la base de datos
        java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

        // Solicitar al usuario las materias primas utilizadas a través de un diálogo
        MateriaPrimaDialog materiaPrimaDialog = new MateriaPrimaDialog();
        List<MateriaPrima> materiasPrimasUsadas = materiaPrimaDialog.mostrarDialogo();

        if (materiasPrimasUsadas == null || materiasPrimasUsadas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se seleccionaron materias primas");
            return;
        }

        // Convertir la lista de materias primas a JSON
        String jsonMateriasPrimas = convertirListaAJSON(materiasPrimasUsadas);

        // Llamar al procedimiento almacenado para insertar el producto y las materias
        // primas
        String consulta = "{CALL agregarProducto(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection connection = Conexion.conectar();
                CallableStatement cs = connection.prepareCall(consulta)) {

            cs.setString(1, nombre);
            cs.setInt(2, cantidad);
            cs.setDate(3, fechaSql);
            cs.setDouble(4, precio);
            cs.setString(5, receta);
            cs.setInt(6, idCocinero);
            cs.setString(7, jsonMateriasPrimas);

            // Ejecutar el procedimiento almacenado
            cs.executeUpdate();

            // Obtener el ID del último producto insertado
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            int productoId = 0;
            if (rs.next()) {
                productoId = rs.getInt(1);
            }

            // Obtener el producto recién insertado de la base de datos
            String consultaProducto = "SELECT * FROM Producto WHERE idProducto = ?";
            try (PreparedStatement ps = connection.prepareStatement(consultaProducto)) {
                ps.setInt(1, productoId);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    // Obtener el objeto Empleado usando tu método obtenerEmpleado
                    Empleado empleado = obtenerEmpleado(resultSet.getInt("idEmpleado"));

                    // Verifica si el empleado se obtuvo correctamente antes de proceder
                    if (empleado == null) {
                        JOptionPane.showMessageDialog(null, "No se pudo encontrar el empleado correspondiente.");
                        return;
                    }

                    // Crear un objeto Productos con los datos del producto recién insertado
                    Productos nuevoProducto = new Productos(
                            resultSet.getInt("idProducto"),
                            resultSet.getString("nombreProducto"),
                            resultSet.getInt("cantidadRealizada"),
                            resultSet.getDate("fechaProduccion"),
                            resultSet.getDouble("precioProducto"),
                            resultSet.getString("recetaProducto"),
                            empleado // Pasar el objeto Empleado al constructor
                    );

                    // Añadir el nuevo producto a la tabla sin recargar todo
                    productoTable.getItems().add(nuevoProducto);
                }
            }

            JOptionPane.showMessageDialog(null, "Producto y materias primas registradas correctamente.");

        } catch (SQLException e) {
            // Verificar si el error contiene la palabra clave del mensaje personalizado
            if (e.getMessage()
                    .contains("Error: No se puede insertar o actualizar debido a una restricción de llave foránea")) {
                JOptionPane.showMessageDialog(null,
                        "Error al insertar el producto: Verifique las relaciones de los datos ingresados.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar el producto: " + e.getMessage());
            }
        }

        // Ocultar el formulario de producto después de agregar
        updateVisibility(formProducto, false);
    }

    // Método para convertir la lista de materias primas a JSON
    private String convertirListaAJSON(List<MateriaPrima> materiasPrimas) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < materiasPrimas.size(); i++) {
            MateriaPrima mp = materiasPrimas.get(i);
            json.append("{")
                    .append("\"idMateria\":").append(mp.getIdMateria()).append(",")
                    .append("\"idInventario\":").append(mp.getIdInventario()).append(",")
                    .append("\"cantidadRestar\":").append(mp.getCantidadRestar())
                    .append("}");
            if (i < materiasPrimas.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    // CODIGO PARA ELIMINAR PRODUCTO CON PROCEDURE
    @FXML
    private void eliminarProducto(ActionEvent event) {
        try {
            filaSeleccionada = productoTable.getSelectionModel().getSelectedIndex();

            if (filaSeleccionada >= 0) {
                Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);
                int idProducto = productoSeleccionado.getCodigo();

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Estás seguro que quieres eliminar el producto?");
                if (respuesta == JOptionPane.YES_OPTION) {
                    Connection conexion = Conexion.conectar();

                    // Llamar al procedimiento almacenado para eliminar el producto
                    String deleteProductoProcedure = "{CALL eliminarProducto(?)}";
                    try (CallableStatement cs = conexion.prepareCall(deleteProductoProcedure)) {
                        cs.setInt(1, idProducto);
                        cs.executeUpdate();
                    }

                    // Eliminar el producto de la lista observable
                    productoTable.getItems().remove(filaSeleccionada);
                    JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Eliminación cancelada");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
            }
        } catch (SQLException e) {
            // Manejar las excepciones lanzadas por el procedimiento almacenado
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el producto: " + e.toString());
        }
    }

    // CODIGO PARA MODIFICAR EL PRODUCTO CON PROCEDURE
    @FXML
    private void modificarProducto(ActionEvent event) {
        int idCocinero;

        try {
            // Tomar los valores actuales de los campos de texto
            String nombre = nombreFieldM.getText();
            int cantidad = Integer.parseInt(cantidadFieldM.getText().trim());
            double precio = Double.parseDouble(precioFieldM.getText());
            String receta = recetaFieldM.getText();
            idCocinero = Integer.parseInt(cocineroFieldM.getText().trim());

            // Convertir la fecha del campo de texto a un objeto java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fechaUtil = null;
            try {
                fechaUtil = sdf.parse(fechaProduccionM.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de fecha incorrecta");
                return; // Salir del método si hay un error en la fecha
            }

            java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

            // Obtener el ID del Producto seleccionado
            Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);
            int idProducto = productoSeleccionado.getCodigo();

            // Llamar al procedimiento almacenado para modificar el producto
            String consulta = "{CALL modificarProducto(?, ?, ?, ?, ?, ?, ?)}";
            try (Connection conexion = Conexion.conectar();
                    CallableStatement cs = conexion.prepareCall(consulta)) {

                cs.setInt(1, idProducto);
                cs.setString(2, nombre);
                cs.setInt(3, cantidad);
                cs.setDate(4, fechaSql);
                cs.setDouble(5, precio);
                cs.setString(6, receta);
                cs.setInt(7, idCocinero);

                cs.executeUpdate();
            }

            // Actualizar los datos del producto en la tabla
            Empleado empleado = obtenerEmpleado(idCocinero);
            productoSeleccionado.setNombreProducto(nombre);
            productoSeleccionado.setCantidad(cantidad);
            productoSeleccionado.setFechaProduccion(fechaSql);
            productoSeleccionado.setPrecio(precio);
            productoSeleccionado.setReceta(receta);
            productoSeleccionado.setEmpleado(empleado);

            productoTable.refresh(); // Refrescar la tabla para mostrar los cambios

            JOptionPane.showMessageDialog(null, "Modificación exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar el Producto: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se modificó correctamente el Producto: " + e.toString());
            e.printStackTrace();
        }

        updateVisibility(modificar, false);
    }

    @FXML
    private void seleccionarProducto() {
        try {
            filaSeleccionada = productoTable.getSelectionModel().getSelectedIndex();
            if (filaSeleccionada >= 0) {
                Productos productoSeleccionado = productoTable.getItems().get(filaSeleccionada);

                Date sqlDate = productoSeleccionado.getFechaProduccion();
                Date fechaUtil = new Date(sqlDate.getTime());

                // Formatear la fecha
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = sdf.format(fechaUtil);

                String consultaIdEmpleado = "select idEmpleado from Producto where idProducto = ?";
                try (PreparedStatement psIdEmpleado = Conexion.conectar().prepareStatement(consultaIdEmpleado)) {
                    psIdEmpleado.setInt(1, productoSeleccionado.getCodigo());
                    ResultSet rs = psIdEmpleado.executeQuery();
                    if (rs.next()) {
                        idEmpleado = rs.getInt("idEmpleado");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }

                idFieldM.setText(Integer.toString(productoSeleccionado.getCodigo()));
                nombreFieldM.setText(productoSeleccionado.getNombreProducto());
                cantidadFieldM.setText(Integer.toString(productoSeleccionado.getCantidad()));
                fechaProduccionM.setText(fechaStr);
                precioFieldM.setText(Double.toString(productoSeleccionado.getPrecio()));
                recetaFieldM.setText(productoSeleccionado.getReceta());
                cocineroFieldM.setText(Integer.toString(idEmpleado));
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de selección, error " + e.toString());
        }

    }

    private Empleado obtenerEmpleado(int idEmpleado) {
        Empleado empleado = null;
        String consulta = "SELECT nombreEmpleado, apellidoEmpleado FROM Cocinero WHERE idEmpleado = ?";

        try (PreparedStatement ps = Conexion.conectar().prepareStatement(consulta)) {
            // Establecer el parámetro de la consulta
            ps.setInt(1, idEmpleado);

            // Ejecutar la consulta
            try (ResultSet rs = ps.executeQuery()) {
                // Procesar el ResultSet
                if (rs.next()) {
                    String nombre = rs.getString("nombreEmpleado");
                    String apellido = rs.getString("apellidoEmpleado");

                    // Crear un objeto Empleado con los datos obtenidos
                    empleado = new Empleado(nombre, apellido);
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el empleado con id: " + idEmpleado);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el empleado: " + e.toString());
        }

        return empleado;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        agregarProducto.setOnAction(event -> updateVisibility(formProducto, true));
        modificarProducto.setOnAction(evento -> updateVisibility(modificar, true));
        productoTable.setOnMouseClicked(event -> seleccionarProducto());
        eliminarProducto.setOnAction(event -> eliminarProducto(event));
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        codigounicocolumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombrecolumn.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        cantidadcolumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        fechaproduccioncolumn.setCellValueFactory(new PropertyValueFactory<>("fechaProduccion"));
        preciocolumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        recetacolumn.setCellValueFactory(new PropertyValueFactory<>("receta"));

        nombrecocinero
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombreCocinero()));

        productosList = FXCollections.observableArrayList();
        empleadoList = FXCollections.observableArrayList();

        try {
            loadProductos();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        productoTable.setItems(productosList);
    }

    private void loadProductos() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT idEmpleado, idProducto, nombreProducto, cantidadRealizada, fechaProduccion, "
                + "precioProducto, recetaProducto, nombreEmpleado, apellidoEmpleado "
                + "FROM Producto JOIN Cocinero USING(idEmpleado) JOIN Sucursal USING(idSucursal)"; // Corregí el formato
                                                                                                   // de la consulta

        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int codigo = resultSet.getInt("idProducto");
            String nombre = resultSet.getString("nombreProducto");
            int cantidad = resultSet.getInt("cantidadRealizada");
            java.sql.Date fecha = resultSet.getDate("fechaProduccion");
            Double precio = resultSet.getDouble("precioProducto");
            String receta = resultSet.getString("recetaProducto");
            String nombreEmp = resultSet.getString("nombreEmpleado");
            String apellidoEmp = resultSet.getString("apellidoEmpleado");

            Empleado empleado = new Empleado(nombreEmp, apellidoEmp);
            Productos producto = new Productos(codigo, nombre, cantidad, fecha, precio, receta, empleado);
            productosList.add(producto);
            empleadoList.add(empleado);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }

    private void updateVisibility(VBox caja, boolean formVisible) {
        caja.setVisible(formVisible);
        agregarProducto.setVisible(!formVisible);
        eliminarProducto.setVisible(!formVisible);
        modificarProducto.setVisible(!formVisible);
    }
}