/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.espol.edu.sqldbcontrol;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Sak
 */
public class MateriaPrimaController implements Initializable {

    @FXML
    private Text volver;
    @FXML
    private TableView<MateriaPrima> materiaPrimaTable;
    @FXML
    private TableColumn<MateriaPrima, String> nombreMateriaColumn;
    @FXML
    private TableColumn<MateriaPrima, Integer> cantidadColumn;
    @FXML
    private TableColumn<MateriaPrima, Date> fechaElaboracionColumn;
    @FXML
    private TableColumn<MateriaPrima, Date> fechaCaducidadColumn;

    private ObservableList<MateriaPrima> materiaPrimaList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volver.setOnMouseClicked(event -> {
            try {
                volverLink(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        nombreMateriaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreMateria"));
        fechaCaducidadColumn.setCellValueFactory(new PropertyValueFactory<>("fechaCaducidad"));
        fechaElaboracionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaElaboracion"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        materiaPrimaList = FXCollections.observableArrayList();

        try {
            loadMateriaPrima();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        materiaPrimaTable.setItems(materiaPrimaList);
    }

    private void loadMateriaPrima() throws SQLException {
        Connection connection = Conexion.conectar();
        Statement statement = connection.createStatement();
        String query = "SELECT MateriaPrima.idMateria, nombreMateria, fechaCaducidad, fechaElaboracion, cantidadDisponible as cantidad "
                + "FROM MateriaPrima "
                + "JOIN Detalle ON MateriaPrima.idMateria = Detalle.idMateria";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int idMateria = resultSet.getInt("idMateria");
            String nombreMateria = resultSet.getString("nombreMateria");
            Date fechaCaducidad = resultSet.getDate("fechaCaducidad");
            Date fechaElaboracion = resultSet.getDate("fechaElaboracion");
            int cantidad = resultSet.getInt("cantidad");

            MateriaPrima materiaPrima = new MateriaPrima(idMateria, nombreMateria, fechaCaducidad, fechaElaboracion, cantidad);
            materiaPrimaList.add(materiaPrima);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }

    void volverLink(MouseEvent event) throws IOException {
        App.setRoot("MenuJefe");
    }
}
