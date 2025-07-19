package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import bogotravel.model.Usuario;
import bogotravel.sesion.SesionActual;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controlador para el login de usuarios en la aplicación BogoTravel. Incluye animaciones visuales y
 * validación de credenciales.
 */
public class UsuarioLoginController {

  // ================================
  // Elementos de la vista FXML
  // ================================

  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Button IniciarButton;
  @FXML private Label welcomeLabel;
  @FXML private AnchorPane rootPane;
  @FXML private ImageView miImagen;
  @FXML private Label labelAnimado;
  @FXML private Pane starsPane;

  // ================================
  // Atributos de lógica
  // ================================

  private final UsuarioDAO usuarioDAO = new UsuarioDAO();
  private final List<Label> estrellas = new ArrayList<>();

  // ================================
  // Inicialización de la vista
  // ================================

  @FXML
  public void initialize() {
    welcomeLabel.setVisible(false);

    crearEstrellas(4);
    animarEstrellas();

    // Crear estrella como recorte sobre la imagen
    Polygon estrella = crearEstrella(295, 300, 240, 130, 5);
    miImagen.setClip(estrella);

    // Rotación infinita de la estrella
    RotateTransition rotarEstrella = new RotateTransition(Duration.seconds(90), estrella);
    rotarEstrella.setByAngle(360);
    rotarEstrella.setCycleCount(Animation.INDEFINITE);
    rotarEstrella.setInterpolator(Interpolator.LINEAR);
    rotarEstrella.play();

    // Transiciones de entrada
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), miImagen);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    ScaleTransition zoom = new ScaleTransition(Duration.seconds(1.2), miImagen);
    zoom.setFromX(0.8);
    zoom.setFromY(0.8);
    zoom.setToX(1.0);
    zoom.setToY(1.0);

    ScaleTransition escalaEstrella = new ScaleTransition(Duration.seconds(2), estrella);
    escalaEstrella.setFromX(0);
    escalaEstrella.setFromY(0);
    escalaEstrella.setToX(1.1);
    escalaEstrella.setToY(1.1);

    new ParallelTransition(escalaEstrella, fadeIn, zoom).play();
  }

  // ================================
  // Acciones de botones
  // ================================

  @FXML
  private void VolverUsuario(ActionEvent event) {
    try {
      Parent root =
          FXMLLoader.load(getClass().getResource("/bogotravel/view/UsuarioRegistroView.fxml"));
      Stage stage = (Stage) IniciarButton.getScene().getWindow();
      root.getStylesheets().add("css/Inicio.css");
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      mostrarAlerta(Alert.AlertType.INFORMATION, "Error al cargar la vista: " + e.getMessage());
    }
  }

  @FXML
  private void buscarUsuarioPorEmail() {
    String email = emailField.getText();
    Usuario usuario = usuarioDAO.buscarPorEmail(email);

    if (usuario != null) {
      passwordField.setText(usuario.getPassword());
      mostrarAlerta(Alert.AlertType.INFORMATION, "Usuario encontrado.");
    } else {
      mostrarAlerta(Alert.AlertType.ERROR, "Usuario no encontrado.");
    }
  }

  @FXML
  private void IniciarUsuario(ActionEvent event) {
    String email = emailField.getText();
    String password = passwordField.getText();

    if (email.isEmpty() || password.isEmpty()) {
      mostrarAlerta(Alert.AlertType.WARNING, "Por favor ingrese su correo y contraseña.");
      return;
    }

    if (usuarioDAO.validarCredenciales(email, password)) {
      Usuario usuario = usuarioDAO.buscarPorEmail(email);
      SesionActual.iniciarSesion(usuario);

      lanzarEstrellasDesdeBoton(IniciarButton, 10);

      showWelcomeAnimation(
          () -> {
            try {
              Parent root =
                  FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
              Stage stage = (Stage) IniciarButton.getScene().getWindow();
              root.getStylesheets().add("css/PaginaPrincipal.css");
              stage.setScene(new Scene(root));
            } catch (Exception e) {
              mostrarAlerta(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage());
            }
          });
    } else {
      mostrarAlerta(Alert.AlertType.ERROR, "Credenciales inválidas.");
    }
  }

  // ================================
  // Métodos auxiliares
  // ================================

  private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
    new Alert(tipo, mensaje).showAndWait();
  }

  private Polygon crearEstrella(
      double centerX, double centerY, double outerRadius, double innerRadius, int points) {
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
    animation.setOnFinished(
        e -> {
          welcomeLabel.setVisible(false);
          if (onFinish != null) onFinish.run();
        });

    animation.play();
  }

  private void lanzarEstrellasDesdeBoton(Button boton, int cantidad) {
    Bounds bounds = boton.localToScene(boton.getBoundsInLocal());

    for (int i = 0; i < cantidad; i++) {
      Label estrella = new Label("★");
      estrella.setStyle("-fx-text-fill: #e5dba2; -fx-font-size: 10;");
      estrella.setOpacity(1);

      estrella.setLayoutX(bounds.getMinX() + boton.getWidth() / 2 + (Math.random() * 20 - 10));
      estrella.setLayoutY(bounds.getMinY() + boton.getHeight() / 2);

      rootPane.getChildren().add(estrella);

      TranslateTransition move =
          new TranslateTransition(Duration.seconds(1 + Math.random()), estrella);
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
      estrella2.setStyle(
          "-fx-text-fill: #ffffff; -fx-font-size: 16; -fx-effect: dropshadow(gaussian, #ffffff, 8, 0.5, 0, 0);");
      estrella2.setOpacity(0);
      starsPane.getChildren().add(0, estrella2);
      estrellas.add(estrella2);
    }
  }

  private void animarEstrellas() {
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(5),
                e -> {
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
