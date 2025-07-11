package bogotravel.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class InicioController {

    @FXML
    private Button EntradaButton;

    @FXML
    private void GenerarEntrada(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/EntradaView.fxml"));
            Stage stage = (Stage) EntradaButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void BuscarAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/EntradaView.fxml"));
            Stage stage = (Stage) EntradaButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void AñadirVisita(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/EntradaView.fxml"));
            Stage stage = (Stage) EntradaButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar la vista: " + e.getMessage());
            alert.showAndWait();
        }
    }
}