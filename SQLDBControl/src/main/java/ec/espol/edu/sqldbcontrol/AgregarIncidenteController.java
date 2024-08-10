package ec.espol.edu.sqldbcontrol;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarIncidenteController {

    @FXML
    private TextField accionTomadaField;
    @FXML
    private TextField descripcionField;
    @FXML
    private DatePicker fechaIncidenteField;
    @FXML
    private TextField idJefeField;
    @FXML
    private TextField idEmpleadoField;

    private IncidenteController incidenteController;
    private boolean isModification = false;
    private Incidente incidenteToModify;

    public void setIncidenteController(IncidenteController incidenteController) {
        this.incidenteController = incidenteController;
    }

    public void setIncidenteToModify(Incidente incidente) {
        accionTomadaField.setText(incidente.getAccionTomada());
        descripcionField.setText(incidente.getDescripcion());

        // Alternativa manual para convertir java.sql.Date a LocalDate si no hay toLocalDate()
        java.sql.Date sqlDate = (java.sql.Date) incidente.getFechaIncidente();
        LocalDate localDate = sqlDate.toLocalDate(); // Este método es válido para java.sql.Date
        fechaIncidenteField.setValue(localDate);

        idJefeField.setText(String.valueOf(incidente.getIdJefe()));
        idEmpleadoField.setText(String.valueOf(incidente.getIdEmpleado()));

        this.isModification = true;
        this.incidenteToModify = incidente;
    }

    @FXML
    private void guardarIncidente() {
        String accionTomada = accionTomadaField.getText();
        String descripcion = descripcionField.getText();
        LocalDate fecha = fechaIncidenteField.getValue();
        int idJefe = Integer.parseInt(idJefeField.getText());
        int idEmpleado = Integer.parseInt(idEmpleadoField.getText());

        if (isModification) {
            try {
                Connection connection = Conexion.conectar();
                String query = "UPDATE Incidente SET accionTomada = ?, descripcion = ?, fechaIncidente = ?, idJefe = ?, idEmpleado = ? WHERE idIncidente = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, accionTomada);
                statement.setString(2, descripcion);
                statement.setDate(3, java.sql.Date.valueOf(fecha));
                statement.setInt(4, idJefe);
                statement.setInt(5, idEmpleado);
                statement.setInt(6, incidenteToModify.getIdIncidente());

                statement.executeUpdate();

                statement.close();
                connection.close();

                // Mostrar alerta de éxito al modificar
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modificación Exitosa");
                alert.setHeaderText(null);
                alert.setContentText("El incidente ha sido modificado correctamente.");
                alert.showAndWait();

                incidenteController.loadIncidentes(); // Refrescar la tabla
                Stage stage = (Stage) accionTomadaField.getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Código para agregar un nuevo incidente
            try {
                Connection connection = Conexion.conectar();
                String query = "INSERT INTO Incidente (accionTomada, descripcion, fechaIncidente, idJefe, idEmpleado) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, accionTomada);
                statement.setString(2, descripcion);
                statement.setDate(3, java.sql.Date.valueOf(fecha));
                statement.setInt(4, idJefe);
                statement.setInt(5, idEmpleado);

                statement.executeUpdate();

                statement.close();
                connection.close();

                // Mostrar alerta de éxito al agregar
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("El incidente ha sido registrado correctamente.");
                alert.showAndWait();

                incidenteController.loadIncidentes(); // Refrescar la tabla
                Stage stage = (Stage) accionTomadaField.getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
