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
import javafx.stage.Stage;

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

    private UsuarioDAO usuarioDAO = new UsuarioDAO();


    public void verificarCargaImagen() {
        try {
            Image imagen = new Image(getClass().getResourceAsStream("src/main/resources/Images/imagen_2.jpg"));
            if (imagen.isError()) {
                System.out.println("Error al cargar la imagen: " + imagen.getException().getMessage());
            } else {
                System.out.println("La imagen se cargó correctamente.");
            }
        } catch (Exception e) {
            System.out.println("Excepción al cargar la imagen: " + e.getMessage());
        }
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
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/InicioView.fxml"));
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/UsuarioView.fxml"));
                Stage stage = (Stage) IniciarButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Error al cargar la vista: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credenciales inválidas. Por favor, inténtelo de nuevo.");
        }
    }
}