package ec.espol.edu.sqldbcontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaPrimaDialog {

    private List<MateriaPrima> listaMateriasPrimas = new ArrayList<>();

    // Método para mostrar un diálogo de entrada de materias primas
    public List<MateriaPrima> mostrarDialogo() {
        Dialog<List<MateriaPrima>> dialog = new Dialog<>();
        dialog.setTitle("Agregar Materias Primas");
        dialog.setHeaderText("Ingrese las materias primas y las cantidades utilizadas");

        // Ajustar el tamaño del diálogo
        dialog.setWidth(600);
        dialog.setHeight(400);

        // Botones del diálogo: Confirmar y Cancelar
        ButtonType agregarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, ButtonType.CANCEL);

        // Contenedores para los campos
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        // Lista de campos para añadir materias primas
        ObservableList<HBox> campos = FXCollections.observableArrayList();

        // Botón para añadir un nuevo campo
        Button btnAgregarCampo = new Button("Agregar Materia Prima");

        // Acción del botón para añadir nuevos campos
        btnAgregarCampo.setOnAction(e -> {
            HBox campo = crearCampoMateriaPrima();  // Crea un campo dinámico con datos de la base de datos
            campos.add(campo);
            vbox.getChildren().add(campo);
        });

        vbox.getChildren().add(btnAgregarCampo);

        dialog.getDialogPane().setContent(vbox);

        // Acción cuando se presiona el botón Confirmar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == agregarButtonType) {
                // Procesar los campos y agregar a la lista de materias primas
                for (HBox campo : campos) {
                    ComboBox<String> materiaPrimaCombo = (ComboBox<String>) campo.getChildren().get(0);
                    TextField inventarioField = (TextField) campo.getChildren().get(1);
                    TextField cantidadField = (TextField) campo.getChildren().get(2);

                    int idMateria = Integer.parseInt(materiaPrimaCombo.getValue().split("-")[0].trim());
                    int idInventario = Integer.parseInt(inventarioField.getText());
                    int cantidadRestar = Integer.parseInt(cantidadField.getText());

                    listaMateriasPrimas.add(new MateriaPrima(idMateria, idInventario, cantidadRestar));
                }
                return listaMateriasPrimas;
            }
            return null;
        });

        dialog.showAndWait();

        return listaMateriasPrimas;
    }

    // Método para crear un campo para una materia prima, cargando datos dinámicamente
    private HBox crearCampoMateriaPrima() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

        // ComboBox de selección de Materia Prima
        ComboBox<String> materiaPrimaCombo = new ComboBox<>();
        cargarMateriasPrimas(materiaPrimaCombo);  // Llenar el ComboBox dinámicamente desde la base de datos

        // Campo de entrada para ID de Inventario
        TextField inventarioField = new TextField();
        inventarioField.setPromptText("ID Inventario");

        // Campo de entrada para cantidad a restar
        TextField cantidadField = new TextField();
        cantidadField.setPromptText("Cantidad");

        hbox.getChildren().addAll(materiaPrimaCombo, inventarioField, cantidadField);
        return hbox;
    }

    // Método para cargar las materias primas en el ComboBox desde la base de datos
    private void cargarMateriasPrimas(ComboBox<String> materiaPrimaCombo) {
        String consulta = "SELECT idMateria, nombreMateria FROM MateriaPrima";

        try (Connection connection = Conexion.conectar();
             PreparedStatement ps = connection.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            // Limpiar el ComboBox antes de agregar nuevos datos
            materiaPrimaCombo.getItems().clear();

            // Agregar cada materia prima al ComboBox
            while (rs.next()) {
                int idMateria = rs.getInt("idMateria");
                String nombreMateria = rs.getString("nombreMateria");
                materiaPrimaCombo.getItems().add(idMateria + " - " + nombreMateria);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar las materias primas: " + e.getMessage());
        }
    }
}