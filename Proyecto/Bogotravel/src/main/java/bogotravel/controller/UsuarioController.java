package bogotravel.controller;

import bogotravel.dao.UsuarioDAO;
import bogotravel.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UsuarioController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void registrarUsuario() {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        Usuario usuario = new Usuario(0, nombre, email, password);
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
}