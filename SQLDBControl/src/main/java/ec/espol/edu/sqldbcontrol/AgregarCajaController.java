/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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


public class AgregarCajaController implements Initializable {

    @FXML
    private TextField gastos;
    @FXML
    private TextField sobrantes;
    @FXML
    private TextField valorCajaChiquita;
    @FXML
    private DatePicker registro;
    @FXML
    private TextField hora;
    @FXML
    private ComboBox<Sucursal> sucursalesComboBox;

    private Integer idCaja = null;
    private CajaController cajaController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Sucursal> sucursales = FXCollections.observableArrayList(obtenerSucursalesDesdeBD());
        sucursalesComboBox.setItems(sucursales);
    }

    private List<Sucursal> obtenerSucursalesDesdeBD() {
        List<Sucursal> sucursales = new ArrayList<>();
        try (Connection connection = Conexion.conectar();
             PreparedStatement statement = connection.prepareStatement("SELECT idSucursal, nombreSucursal, direccionSucursal, telefono, horarioSucursal, idJefe FROM Sucursal")) {
            
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idSucursal = resultSet.getInt("idSucursal");
                String nombreSucursal = resultSet.getString("nombreSucursal");
                String direccionSucursal = resultSet.getString("direccionSucursal");
                String telefono = resultSet.getString("telefono");
                String horarioSucursal = resultSet.getString("horarioSucursal");
                int idJefe = resultSet.getInt("idJefe");

                sucursales.add(new Sucursal(idSucursal, nombreSucursal, direccionSucursal, telefono, horarioSucursal, idJefe));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sucursales;
    }

    public void setCajaController(CajaController cajaController) {
        this.cajaController = cajaController;
    }

    public void cargarDatosCaja(Caja caja) {
        this.idCaja = caja.getIdCaja();
        gastos.setText(String.valueOf(caja.getGastos()));
        sobrantes.setText(String.valueOf(caja.getSobrantes()));
        valorCajaChiquita.setText(String.valueOf(caja.getValorCajaChiquita()));

        LocalDateTime fechaHora = caja.getFechaRegistro();
        LocalDate fecha = fechaHora.toLocalDate();
        registro.setValue(fecha);  // Asigna la fecha al DatePicker
        hora.setText(fechaHora.toLocalTime().toString());  // Asigna la hora al TextField

        // Seleccionar la sucursal correspondiente en el ComboBox
        for (Sucursal sucursal : sucursalesComboBox.getItems()) {
            if (sucursal.getIdSucursal() == caja.getIdSucursal()) {
                sucursalesComboBox.setValue(sucursal);
                break;
            }
        }
    }

@FXML
private void guardarCaja() {
    try {
        float gasto = Float.parseFloat(gastos.getText());
        float sobrante = Float.parseFloat(sobrantes.getText());
        float valorCajaChiquitas = Float.parseFloat(valorCajaChiquita.getText());
        LocalDate fechaRegistro = registro.getValue();
        String horaRegistro = hora.getText()+ ":00";

        if (fechaRegistro == null || horaRegistro.isEmpty()) {
            throw new IllegalArgumentException("Fecha y hora deben estar completas");
        }

        LocalDateTime fechaHora = LocalDateTime.of(fechaRegistro, LocalTime.parse(horaRegistro, DateTimeFormatter.ofPattern("HH:mm:ss")));
        Sucursal sucursalSeleccionada = sucursalesComboBox.getValue();

        try (Connection connection = Conexion.conectar()) {
            CallableStatement statement;

            if (idCaja == null) {
                // Llamada al procedimiento almacenado `insertarCaja`
                statement = connection.prepareCall("{CALL insertarCaja(?, ?, ?, ?, ?)}");
                statement.setFloat(1, gasto);
                statement.setFloat(2, sobrante);
                statement.setFloat(3, valorCajaChiquitas);
                statement.setTimestamp(4, Timestamp.valueOf(fechaHora));
                statement.setInt(5, sucursalSeleccionada.getIdSucursal());
            } else {
                // Llamada al procedimiento almacenado `actualizarCaja`
                statement = connection.prepareCall("{CALL actualizarCaja(?, ?, ?, ?, ?, ?)}");
                statement.setInt(1, idCaja);
                statement.setFloat(2, gasto);
                statement.setFloat(3, sobrante);
                statement.setFloat(4, valorCajaChiquitas);
                statement.setTimestamp(5, Timestamp.valueOf(fechaHora));
                statement.setInt(6, sucursalSeleccionada.getIdSucursal());
            }

            statement.execute();
            statement.close();

            cajaController.loadCajas();

            Stage stage = (Stage) gastos.getScene().getWindow();
            stage.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Caja guardada exitosamente.");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Hubo un error al guardar la caja.");
        }

    } catch (NumberFormatException e) {
        mostrarError("Por favor, ingrese valores numéricos válidos.");
    } catch (IllegalArgumentException e) {
        mostrarError(e.getMessage());
    }
}

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) gastos.getScene().getWindow();
        stage.close();
    }
}
