package bogotravel.controller;

import bogotravel.dao.EntradaDAO;
import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.model.Entrada;
import bogotravel.model.LugarTuristico;
import bogotravel.service.FotoEntradaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import bogotravel.model.Usuario;
import bogotravel.sesion.SesionActual;


import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class InicioController {

    @FXML
    private VBox lugaresContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button CerrarButton;

    private final LugarTuristicoDAO lugarDAO = new LugarTuristicoDAO();


    public void initialize() {

        // Verificar si hay un usuario autenticado
        Usuario actual = SesionActual.getUsuario();
        if (actual == null) {
            // No hay usuario autenticado, volver al login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/LoginView.fxml"));
                Stage stage = (Stage) scrollPane.getScene().getWindow();
                root.getStylesheets().add("css/Inicio.css");
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
        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            try {
                Image img = new Image(url, true);
                imagen.setImage(img);
            } catch (Exception e) {
                System.out.println("No se pudo cargar imagen desde URL: " + e.getMessage());
            }
        }
        box.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/DetalleLugarView.fxml"));
                Parent root = loader.load();

                DetalleLugarController controller = loader.getController();
                controller.cargarLugar(lugar);

                Stage stage = (Stage) scrollPane.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la vista de detalle del lugar: " + e.getMessage());
            }
        });



        imagen.setFitWidth(300);
        imagen.setFitHeight(300);
        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);
        // Clip redondeado
        Rectangle clip = new Rectangle(imagen.getFitWidth(), imagen.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imagen.setClip(clip);

        // Sombra (opcional)
        imagen.setEffect(new DropShadow(10, javafx.scene.paint.Color.GRAY));

        // 👉 Agrupar texto en VBox
        VBox textoBox = new VBox(5);

        Label nombre = new Label(lugar.getNombre());
        nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label localidad = new Label("Localidad: " + lugar.getLocalidad());
        localidad.setStyle("-fx-font-size: 14px;");

        Label descripcion = new Label(lugar.getDescripcion());
        descripcion.setWrapText(true);
        descripcion.setMaxWidth(400); // puedes ajustar el ancho máximo
        descripcion.setStyle("-fx-font-size: 13px;");

        textoBox.getChildren().addAll(nombre, localidad, descripcion);

        box.getChildren().addAll(imagen, textoBox);
        return box;
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        // 1. Cerrar la sesión
        SesionActual.cerrarSesion();

        // 2. Cargar la vista del login
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
    private void abrirFormularioEntrada(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar la vista de entrada.").showAndWait();
        }
    }

    private void abrirFormularioEdicion(Entrada entrada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Parent root = loader.load();

            CrearEntradaController controller = loader.getController();
            controller.cargarEntrada(entrada); // << Pasamos la entrada

            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar el formulario de edición.").showAndWait();
        }
    }

    private void eliminarEntrada(Entrada entrada) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar esta entrada?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            EntradaDAO dao = new EntradaDAO();
            boolean eliminado = dao.eliminar(entrada.getId());

            if (eliminado) {
                lugaresContainer.getChildren().clear();
                verEntradas();
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo eliminar la entrada.").showAndWait();
            }
        }
    }



    private VBox crearVistaEntrada(Entrada entrada) {
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-background-color: #FAFAFA; -fx-padding: 12; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-radius: 10;");

        Label titulo = new Label(entrada.getTitulo());
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label fecha = new Label("Visita: " + entrada.getFechaVisita().toString());
        Label lugar = new Label("Lugar: " + (entrada.getLugarDescripcion() != null ? entrada.getLugarDescripcion() : "No especificado"));
        Label contenido = new Label(entrada.getContenido());
        contenido.setWrapText(true);

        tarjeta.getChildren().addAll(titulo, fecha, lugar, contenido);

        // Mostrar fotos (si hay)
        List<File> fotos = new FotoEntradaService().obtenerArchivosFotos(entrada.getId());
        if (!fotos.isEmpty()) {
            HBox galeria = new HBox(10);
            for (File archivo : fotos) {
                if (archivo.exists()) {
                    ImageView img = new ImageView(new Image(archivo.toURI().toString()));
                    img.setFitWidth(180);
                    img.setPreserveRatio(true);
                    img.setSmooth(true);
                    galeria.getChildren().add(img);
                }
            }
            tarjeta.getChildren().add(galeria);
        }

        // Botones de acción (Editar y Eliminar)
        HBox botones = new HBox(10);
        botones.setStyle("-fx-alignment: center-right;");

        Button editarBtn = new Button("Editar");
        Button eliminarBtn = new Button("Eliminar");

        editarBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        eliminarBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        editarBtn.setOnAction(e -> abrirFormularioEdicion(entrada));
        eliminarBtn.setOnAction(e -> eliminarEntrada(entrada));

        botones.getChildren().addAll(editarBtn, eliminarBtn);
        tarjeta.getChildren().add(botones);

        return tarjeta;
    }


    @FXML
    private void verLugares() {
        lugaresContainer.getChildren().clear();

        List<LugarTuristico> lugares = lugarDAO.listarTodos();
        for (LugarTuristico lugar : lugares) {
            lugaresContainer.getChildren().add(crearVistaLugar(lugar));
        }
    }


    @FXML
    private void verEntradas() {
        lugaresContainer.getChildren().clear();

        String email = SesionActual.getUsuario().getEmail();
        List<Entrada> entradas = new EntradaDAO().listarPorUsuario(email);

        for (Entrada entrada : entradas) {
            lugaresContainer.getChildren().add(crearVistaEntrada(entrada));
        }
    }

}