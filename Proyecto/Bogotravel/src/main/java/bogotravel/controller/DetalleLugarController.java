package bogotravel.controller;

import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.model.LugarTuristico;
import bogotravel.sesion.SesionActual;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalleLugarController {


    @FXML
    private Label nombre;
    @FXML
    private Label descripcion;
    @FXML
    private Label localidad;
    @FXML
    private ImageView imagenView;
    @FXML
    private Button CerrarButton;
    @FXML
    private Button VolverButton;

    private LugarTuristico lugarActual;

    public void cargarLugar(LugarTuristico lugar) {
        this.lugarActual = lugar;
        nombre.setText(lugar.getNombre());
        descripcion.setText(lugar.getDescripcion());
        localidad.setText(lugar.getLocalidad());
        imagenView.setImage(new Image(lugar.getImagenUrl()));
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        SesionActual.cerrarSesion();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/UsuarioLoginView.fxml"));
            Stage stage = (Stage) CerrarButton.getScene().getWindow();  // Puedes usar cualquier nodo de la escena
            root.getStylesheets().add("css/Inicio.css");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "No se pudo volver a la vista de inicio de sesión.").showAndWait();
        }
    }

    @FXML
    private void VolverAction(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
            Stage stage = (Stage) CerrarButton.getScene().getWindow();  //
            root.getStylesheets().add("css/PaginaPrincipal.css");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
