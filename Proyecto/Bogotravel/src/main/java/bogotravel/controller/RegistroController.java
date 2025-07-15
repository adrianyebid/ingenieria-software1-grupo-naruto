package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import bogotravel.model.Usuario;
import javafx.animation.*;
import javafx.event.ActionEvent;
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

import java.util.ArrayList;
import java.util.List;

public class RegistroController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button RegistrarButton;
    @FXML private Button irALoginButton;
    @FXML private Label welcomeLabel;
    @FXML private AnchorPane rootPane;
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
    private void registrarUsuario(ActionEvent event) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Por favor completa todos los campos.").showAndWait();
            return;
        }

        Usuario usuario = new Usuario(nombre, email, password);
        boolean exito = usuarioDAO.registrar(usuario);

        Alert alert = new Alert(exito ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Registro de Usuario");
        alert.setHeaderText(null);
        alert.setContentText(exito ? "Usuario registrado correctamente." : "Error al registrar usuario.");
        alert.showAndWait();
    }

    @FXML
    private void irALogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/LoginView.fxml"));
            Stage stage = (Stage) irALoginButton.getScene().getWindow();
            root.getStylesheets().add("css/PaginaPrincipal.css");
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de inicio de sesión: " + e.getMessage()).showAndWait();
        }
    }

    private void crearEstrellas(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Label estrella = new Label("★");
            estrella.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16; -fx-effect: dropshadow(gaussian, #ffffff, 8, 0.5, 0, 0);");
            estrella.setOpacity(0);
            starsPane.getChildren().add(0, estrella);
            estrellas.add(estrella);
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
