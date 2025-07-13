package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import bogotravel.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.geometry.Bounds;
import javafx.animation.*;
import javafx.scene.shape.Polygon;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Interpolator;



public class UsuarioController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button IniciarButton;
    @FXML
    private Button RegistrarButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView miImagen;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();


    @FXML
    public void initialize() {
        welcomeLabel.setVisible(false);

        // Crear estrella como clip
        Polygon estrella = crearEstrella(250, 300, 220, 110, 5);
        miImagen.setClip(estrella);

        // Animar crecimiento de la estrella
        ScaleTransition escalaEstrella = new ScaleTransition(Duration.seconds(2), estrella);
        escalaEstrella.setFromX(0);
        escalaEstrella.setFromY(0);
        escalaEstrella.setToX(1.1);
        escalaEstrella.setToY(1.1);
        escalaEstrella.setInterpolator(Interpolator.EASE_OUT);

        // Animar la imagen con fade-in + zoom leve
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), miImagen);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition zoom = new ScaleTransition(Duration.seconds(1.2), miImagen);
        zoom.setFromX(0.8);
        zoom.setFromY(0.8);
        zoom.setToX(1.0);
        zoom.setToY(1.0);
        zoom.setInterpolator(Interpolator.EASE_OUT);

        // Reproducir todo al mismo tiempo
        ParallelTransition total = new ParallelTransition(escalaEstrella, fadeIn, zoom);
        total.play();
    }

    // Crear estrella en forma de polígono
    private Polygon crearEstrella(double centerX, double centerY, double outerRadius, double innerRadius, int points) {
        Polygon polygon = new Polygon();
        double angleStep = Math.PI / points;
        for (int i = 0; i < points * 2; i++) {
            double r = (i % 2 == 0) ? outerRadius : innerRadius;
            double angle = i * angleStep - Math.PI / 2;
            double x = centerX + r * Math.cos(angle);
            double y = centerY + r * Math.sin(angle);
            polygon.getPoints().addAll(x, y);
        }
        return polygon;
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        Usuario usuario = new Usuario(nombre, email, password);
        boolean exito = usuarioDAO.registrar(usuario);

        Alert alert = new Alert(exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Registro de Usuario");
        alert.setHeaderText(null);
        alert.setContentText(exito ? "Usuario registrado correctamente." : "Error al registrar usuario.");
        alert.showAndWait();
    }

    @FXML
    private void buscarUsuarioPorEmail() {
        String email = emailField.getText();
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            nombreField.setText(usuario.getNombre());
            passwordField.setText(usuario.getPassword());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario encontrado.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Usuario no encontrado.");
            alert.showAndWait();
        }
    }


    @FXML
    private void IniciarUsuario(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (usuarioDAO.validarCredenciales(email, password)) {

            lanzarEstrellasDesdeBoton(IniciarButton, 10);
            // Mostrar animación de bienvenida
            showWelcomeAnimation(() -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
                    Stage stage = (Stage) IniciarButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Error al cargar la vista: " + e.getMessage());
                    alert.showAndWait();
                }
            });

            // Efecto rebote en el botón
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), IniciarButton);
            scale.setToX(0.95);
            scale.setToY(0.95);

            ScaleTransition scaleBack = new ScaleTransition(Duration.millis(150), IniciarButton);
            scaleBack.setToX(1.0);
            scaleBack.setToY(1.0);

            new SequentialTransition(scale, scaleBack).play();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credenciales inválidas. Por favor, inténtelo de nuevo.");
            alert.showAndWait();
        }
    }

    private void showWelcomeAnimation(Runnable onFinish) {
        welcomeLabel.setVisible(true);
        welcomeLabel.setOpacity(0);
        welcomeLabel.setTranslateY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(600), welcomeLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);

        TranslateTransition move = new TranslateTransition(Duration.millis(600), welcomeLabel);
        move.setByY(-20);
        move.setCycleCount(2);
        move.setAutoReverse(true);

        ParallelTransition animation = new ParallelTransition(fade, move);
        animation.setOnFinished(e -> {
            welcomeLabel.setVisible(false);
            if (onFinish != null) {
                onFinish.run();
            }
        });

        animation.play();
    }

    private void lanzarEstrellasDesdeBoton(Button boton, int cantidad) {
        Bounds bounds = boton.localToScene(boton.getBoundsInLocal());

        for (int i = 0; i < cantidad; i++) {
            Label estrella = new Label("★");
            estrella.setStyle("-fx-text-fill:  #e5dba2; -fx-font-size: 10;");
            estrella.setOpacity(1);

            // Posición inicial (relativa a la escena)
            estrella.setLayoutX(bounds.getMinX() + boton.getWidth() / 2 + (Math.random() * 20 - 10));
            estrella.setLayoutY(bounds.getMinY() + boton.getHeight() / 2);

            rootPane.getChildren().add(estrella);

            // Movimiento aleatorio hacia arriba
            TranslateTransition move = new TranslateTransition(Duration.seconds(1 + Math.random()), estrella);
            move.setByX((Math.random() * 60) - 30); // Movimiento lateral
            move.setByY(-80 - Math.random() * 60);  // Hacia arriba
            move.setInterpolator(Interpolator.EASE_OUT);

            // Desvanecimiento
            FadeTransition fade = new FadeTransition(Duration.seconds(1), estrella);
            fade.setFromValue(1);
            fade.setToValue(0);

            // Pequeño brillo (scale)
            ScaleTransition scale = new ScaleTransition(Duration.seconds(1), estrella);
            scale.setFromX(1);
            scale.setToX(1.5);
            scale.setFromY(1);
            scale.setToY(1.5);

            // Al terminar, eliminar la estrella
            ParallelTransition starEffect = new ParallelTransition(move, fade, scale);
            starEffect.setOnFinished(e -> rootPane.getChildren().remove(estrella));
            starEffect.play();
        }
    }
}