package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.geometry.Bounds;

import java.util.ArrayList;
import java.util.List;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label welcomeLabel;
    @FXML private AnchorPane rootPane;
    @FXML private Button loginButton;
    @FXML private ImageView miImagen;
    @FXML private Pane starsPane;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private List<Label> estrellas = new ArrayList<>();

    @FXML
    public void initialize() {
        welcomeLabel.setVisible(false);
        crearEstrellas(4);
        animarEstrellas();
    }

    @FXML
    private void IniciarUsuario() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (usuarioDAO.validarCredenciales(email, password)) {
            lanzarEstrellasDesdeBoton(loginButton, 10);

            showWelcomeAnimation(() -> {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    root.getStylesheets().add("css/PaginaPrincipal.css");
                    stage.setScene(new Scene(root));
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage()).showAndWait();
                }
            });

        } else {
            new Alert(Alert.AlertType.ERROR, "Credenciales inválidas.").showAndWait();
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
            if (onFinish != null) onFinish.run();
        });

        animation.play();
    }

    private void lanzarEstrellasDesdeBoton(Button boton, int cantidad) {
        Bounds bounds = boton.localToScene(boton.getBoundsInLocal());
        for (int i = 0; i < cantidad; i++) {
            Label estrella = new Label("★");
            estrella.setStyle("-fx-text-fill:  #e5dba2; -fx-font-size: 10;");
            estrella.setOpacity(1);
            estrella.setLayoutX(bounds.getMinX() + boton.getWidth() / 2 + (Math.random() * 20 - 10));
            estrella.setLayoutY(bounds.getMinY() + boton.getHeight() / 2);
            rootPane.getChildren().add(estrella);

            TranslateTransition move = new TranslateTransition(Duration.seconds(1 + Math.random()), estrella);
            move.setByX((Math.random() * 60) - 30);
            move.setByY(-80 - Math.random() * 60);
            move.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition fade = new FadeTransition(Duration.seconds(1), estrella);
            fade.setFromValue(1);
            fade.setToValue(0);

            ScaleTransition scale = new ScaleTransition(Duration.seconds(1), estrella);
            scale.setFromX(1);
            scale.setToX(1.5);
            scale.setFromY(1);
            scale.setToY(1.5);

            ParallelTransition starEffect = new ParallelTransition(move, fade, scale);
            starEffect.setOnFinished(e -> rootPane.getChildren().remove(estrella));
            starEffect.play();
        }
    }

    private void crearEstrellas(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Label estrella2 = new Label("★");
            estrella2.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16; -fx-effect: dropshadow(gaussian, #ffffff, 8, 0.5, 0, 0);");
            estrella2.setOpacity(0);
            starsPane.getChildren().add(0, estrella2);
            estrellas.add(estrella2);
        }
    }

    private void animarEstrellas() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            for (Label estrella : estrellas) {
                animarDesvanecimientoAleatorio(estrella);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void animarDesvanecimientoAleatorio(Label label) {
        double randomX = Math.random() * starsPane.getWidth();
        double randomY = Math.random() * starsPane.getHeight();
        double randomDelay = Math.random() * 1.5;

        label.setLayoutX(randomX);
        label.setLayoutY(randomY);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), label);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.setDelay(Duration.seconds(randomDelay));
        fade.play();
    }
}
