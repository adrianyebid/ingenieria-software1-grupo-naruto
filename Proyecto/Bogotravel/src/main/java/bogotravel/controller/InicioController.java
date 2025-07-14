package bogotravel.controller;

import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.model.LugarTuristico;
import bogotravel.model.Usuario;
import bogotravel.sesion.SesionActual;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class InicioController {

    @FXML
    private VBox lugaresContainer;

    @FXML
    private ScrollPane scrollPane;

    private final LugarTuristicoDAO lugarDAO = new LugarTuristicoDAO();

    public void initialize() {

        // Verificar si hay un usuario autenticado
        Usuario actual = SesionActual.getUsuario();
        if (actual == null) {
            // No hay usuario autenticado, volver al login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/LoginView.fxml"));
                Stage stage = (Stage) scrollPane.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<LugarTuristico> lugares = lugarDAO.listarTodos();
        for (LugarTuristico lugar : lugares) {
            lugaresContainer.getChildren().add(crearVistaLugar(lugar));
        }
    }

    private HBox crearVistaLugar(LugarTuristico lugar) {
        HBox box = new HBox(10);
        box.setStyle("-fx-background-color: #F9F9F9; -fx-padding: 10; -fx-border-radius: 8; -fx-background-radius: 8;");

        ImageView imagen = new ImageView();
        String url = lugar.getImagenUrl();
        System.out.println("Intentando cargar imagen desde URL: " + url);
        try {
            imagen.setImage(new Image(lugar.getImagenUrl(), true)); // true para cargar en segundo plano
            imagen.setFitWidth(300);
            imagen.setFitHeight(300);
            imagen.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("No se pudo cargar imagen desde URL: " + e.getMessage());
        }




        Label nombre = new Label(lugar.getNombre());
        Label localidad = new Label(lugar.getLocalidad());
        Label descripcion = new Label(lugar.getDescripcion());
        nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        box.getChildren().addAll(imagen, nombre,localidad,descripcion);
        return box;
    }

    @FXML
    private void cerrarSesion() {
        // 1. Cerrar la sesión
        SesionActual.cerrarSesion();

        // 2. Cargar la vista del login
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/LoginView.fxml"));
            Stage stage = (Stage) scrollPane.getScene().getWindow();  // Puedes usar cualquier nodo de la escena
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "No se pudo volver a la vista de inicio de sesión.").showAndWait();
        }
    }

}