package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import bogotravel.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UsuarioRegistroController {

  // ===========================
  // === FXML Elementos UI  ====
  // ===========================
  @FXML private TextField nombreField;
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Button IniciarButton;
  @FXML private Button RegistrarButton;
  @FXML private AnchorPane rootPane;
  @FXML private ImageView miImagen;
  @FXML private Label labelAnimado;
  @FXML private Pane starsPane;

  // ===========================
  // ======= Atributos =========
  // ===========================
  private final UsuarioDAO usuarioDAO = new UsuarioDAO();
  private final List<Label> estrellas = new ArrayList<>();

  // ===========================
  // ======= Inicializador =====
  // ===========================
  @FXML
  public void initialize() {
    nombreField.setVisible(true);

    crearEstrellas(4); // Crea 4 estrellas decorativas
    animarEstrellas();

    // Crear estrella animada como clip para la imagen
    Polygon estrella = crearEstrella(295, 300, 240, 130, 5);
    miImagen.setClip(estrella);

    // Rotación constante de la estrella (animación visual)
    RotateTransition rotarEstrella = new RotateTransition(Duration.seconds(90), estrella);
    rotarEstrella.setByAngle(360);
    rotarEstrella.setCycleCount(Animation.INDEFINITE);
    rotarEstrella.setInterpolator(Interpolator.LINEAR);
    rotarEstrella.play();

    // Escalado de entrada para la estrella
    ScaleTransition escalaEstrella = new ScaleTransition(Duration.seconds(2), estrella);
    escalaEstrella.setFromX(0);
    escalaEstrella.setFromY(0);
    escalaEstrella.setToX(1.1);
    escalaEstrella.setToY(1.1);
    escalaEstrella.setInterpolator(Interpolator.EASE_OUT);

    // Fade-in + zoom para la imagen
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), miImagen);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    ScaleTransition zoom = new ScaleTransition(Duration.seconds(1.2), miImagen);
    zoom.setFromX(0.8);
    zoom.setFromY(0.8);
    zoom.setToX(1.0);
    zoom.setToY(1.0);
    zoom.setInterpolator(Interpolator.EASE_OUT);

    // Ejecutar animaciones en paralelo
    ParallelTransition total = new ParallelTransition(escalaEstrella, fadeIn, zoom);
    total.play();
  }

  // ===================================================
  // === Funcionalidad Principal: Registro y Navegación ==
  // ===================================================

  /** Registra un nuevo usuario con los datos del formulario. */
  @FXML
  private void registrarUsuario(ActionEvent event) {
    String nombre = nombreField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();

    // Validación de campos vacíos
    if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
      new Alert(Alert.AlertType.WARNING, "Por favor completa todos los campos.").showAndWait();
      return;
    }

    // Validación de email
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
      new Alert(Alert.AlertType.WARNING, "El correo electrónico no es válido.").showAndWait();
      return;
    }

    // Validación de password (mínimo 8 caracteres, al menos una mayúscula, una minúscula y un
    // número)
    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
      new Alert(
              Alert.AlertType.WARNING,
              "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.")
          .showAndWait();
      return;
    }

    // Verificar si el usuario ya existe
    if (usuarioDAO.buscarPorEmail(email) != null) {
      new Alert(Alert.AlertType.ERROR, "El usuario ya está registrado con este correo.")
          .showAndWait();
      return;
    }

    Usuario usuario = new Usuario(nombre, email, password);
    boolean exito = usuarioDAO.registrar(usuario);

    Alert alert = new Alert(exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
    alert.setTitle("Registro de Usuario");
    alert.setHeaderText(null);
    alert.setContentText(
        exito ? "Usuario registrado correctamente." : "Error al registrar usuario.");
    alert.showAndWait();
  }

  /** Busca un usuario por su correo electrónico y llena los campos si lo encuentra. */
  @FXML
  private void buscarUsuarioPorEmail() {
    String email = emailField.getText();
    Usuario usuario = usuarioDAO.buscarPorEmail(email);

    if (usuario != null) {
      nombreField.setText(usuario.getNombre());
      passwordField.setText(usuario.getPassword());
      new Alert(Alert.AlertType.INFORMATION, "Usuario encontrado.").showAndWait();
    } else {
      new Alert(Alert.AlertType.ERROR, "Usuario no encontrado.").showAndWait();
    }
  }

  /** Cambia la vista al formulario de login. */
  @FXML
  private void IniciarUsuario(ActionEvent event) {
    try {
      Parent root =
          FXMLLoader.load(getClass().getResource("/bogotravel/view/UsuarioLoginView.fxml"));
      Stage stage = (Stage) IniciarButton.getScene().getWindow();
      root.getStylesheets().add("css/Inicio.css");
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.INFORMATION, "Error al cargar la vista: " + e.getMessage())
          .showAndWait();
    }
  }

  // ====================================
  // === Utilidades de Animación UI  ====
  // ====================================

  /** Crea una forma de estrella con puntos alternos para usar como clip. */
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

  /** Crea una cantidad determinada de estrellas para efectos visuales. */
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

  /** Anima las estrellas del fondo con un efecto de aparición y desaparición aleatoria. */
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

  /** Efecto de fade in/out para cada estrella en una posición aleatoria. */
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

  /** Lanza estrellas animadas desde un botón al hacer clic (efecto visual). */
  private void lanzarEstrellasDesdeBoton(Button boton, int cantidad) {
    Bounds bounds = boton.localToScene(boton.getBoundsInLocal());

    for (int i = 0; i < cantidad; i++) {
      Label estrella = new Label("★");
      estrella.setStyle("-fx-text-fill:  #e5dba2; -fx-font-size: 10;");
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
}
