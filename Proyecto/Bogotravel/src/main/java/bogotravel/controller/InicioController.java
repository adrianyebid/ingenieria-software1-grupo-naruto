package bogotravel.controller;

import bogotravel.dao.EntradaDAO;
import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.dao.PorVisitarDAO;
import bogotravel.model.Entrada;
import bogotravel.model.LugarTuristico;
import bogotravel.service.FotoEntradaService;
import bogotravel.utils.PorVisitarInfo;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import bogotravel.model.Usuario;
import bogotravel.sesion.SesionActual;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;

import java.util.List;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


import java.util.LinkedHashMap;
import java.util.Arrays;



public class InicioController {

    @FXML
    private VBox lugaresContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button CerrarButton;
    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private ComboBox<String> localidadComboBox;

    @FXML
    private TextField nombreTextField;
    @FXML
    private HBox buscadorBox;

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

        // Mapear categorías
        categoriasMap.put("Cultural", 1);
        categoriasMap.put("Natural", 2);
        categoriasMap.put("Gastronómico", 3);
        categoriasMap.put("Tecnológico", 4);
        categoriasMap.put("Recreativo", 5);
        categoriasMap.put("Religioso", 6);
        categoriasMap.put("Histórico", 7);

        categoriaComboBox.getItems().addAll(categoriasMap.keySet());
        localidadComboBox.getItems().addAll(localidadesBogota);

        cargarLugares(lugarDAO.listarTodos());
    }

    private void cargarLugares(List<LugarTuristico> lugares) {
        lugaresContainer.getChildren().clear();
        for (LugarTuristico lugar : lugares) {
            lugaresContainer.getChildren().add(crearVistaLugar(lugar));
        }

        if (lugares.isEmpty()) {
            Label vacio = new Label("No hay lugares por visitar.");
            vacio.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            lugaresContainer.getChildren().add(vacio);
        }
    }

    private final Map<String, Integer> categoriasMap = new LinkedHashMap<>();
    private final List<String> localidadesBogota = Arrays.asList(
            "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
            "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
            "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires", "Antonio Nariño",
            "Puente Aranda", "La Candelaria", "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
    );

    @FXML
    private void buscarLugares() {
        String nombre = nombreTextField.getText().trim();
        String categoria = categoriaComboBox.getValue();
        String localidad = localidadComboBox.getValue();

        List<LugarTuristico> resultados = new ArrayList<>();

        if (!nombre.isEmpty()) {
            resultados = lugarDAO.buscarPorNombre(nombre);
        } else if (categoria != null && localidad != null) {
            int idCategoria = categoriasMap.get(categoria);
            resultados = lugarDAO.listarPorCategoriaYLocalidad(idCategoria, localidad);
        } else {
            resultados = lugarDAO.listarTodos();
        }

        cargarLugares(resultados);
    }

    @FXML
    private void limpiarFiltros() {
        nombreTextField.clear();
        categoriaComboBox.getSelectionModel().clearSelection();
        localidadComboBox.getSelectionModel().clearSelection();
        cargarLugares(lugarDAO.listarTodos());
    }


    private HBox crearVistaLugar(LugarTuristico lugar) {
        HBox box = new HBox(10);
        box.getStyleClass().add("lugar-box");

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
                root.getStylesheets().add("css/DetalleLugar.css");

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
        nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-font-family:Rockwell Condensed;");

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Parent root = loader.load();

            CrearEntradaController controller = loader.getController();

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Crear nueva entrada");
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.initModality(Modality.APPLICATION_MODAL); // Esto bloquea la ventana principal
            nuevaVentana.showAndWait(); // Espera hasta que se cierre la ventana

            if (controller.isEntradaGuardada()) {
                System.out.println("Recargando datos...");
                verEntradas(); // 🔁 Refrescamos la lista de entradas
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el formulario de entrada.").showAndWait();
        }
    }


    private void abrirFormularioEdicion(Entrada entrada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Parent root = loader.load();

            CrearEntradaController controller = loader.getController();
            controller.cargarEntrada(entrada);

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Editar entrada");
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();
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
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-font-family:Rockwell Condensed;");

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

        editarBtn.setStyle("-fx-background-color: #0b5563; -fx-text-fill: white;");
        editarBtn.getStyleClass().add("botones-entrada");
        eliminarBtn.setStyle("-fx-background-color: #9e443d; -fx-text-fill: white;");
        eliminarBtn.getStyleClass().add("botones-entrada");

        editarBtn.setOnAction(e -> abrirFormularioEdicion(entrada));
        eliminarBtn.setOnAction(e -> eliminarEntrada(entrada));

        botones.getChildren().addAll(editarBtn, eliminarBtn);
        tarjeta.getChildren().add(botones);

        return tarjeta;
    }


    @FXML
    private void verLugares() {
        buscadorBox.setVisible(true);
        buscadorBox.setManaged(true);
        lugaresContainer.getChildren().clear();

        List<LugarTuristico> lugares = lugarDAO.listarTodos();
        for (LugarTuristico lugar : lugares) {
            lugaresContainer.getChildren().add(crearVistaLugar(lugar));
        }
    }


    @FXML
    private void verEntradas() {
        buscadorBox.setVisible(false);
        buscadorBox.setManaged(false);
        lugaresContainer.getChildren().clear();

        String email = SesionActual.getUsuario().getEmail();
        List<Entrada> entradas = new EntradaDAO().listarPorUsuario(email);

        for (Entrada entrada : entradas) {
            lugaresContainer.getChildren().add(crearVistaEntrada(entrada));
        }
    }

    @FXML
    private void verPorVisitar() {
        buscadorBox.setVisible(false);
        buscadorBox.setManaged(false);
        lugaresContainer.getChildren().clear();

        PorVisitarDAO dao = new PorVisitarDAO();
        String email = SesionActual.getUsuario().getEmail();

        List<PorVisitarInfo> lista = dao.listarConNombrePorUsuario(email);

        for (PorVisitarInfo info : lista) {
            VBox tarjeta = new VBox(10);
            tarjeta.setStyle("-fx-background-color: #FDFEFE; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #BFC9CA; -fx-border-radius: 10;");

            Label nombre = new Label(info.getNombreLugar());
            nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            String prioridadTexto;
            switch (info.getPrioridad()) {
                case 1:
                    prioridadTexto = "Alta";
                    break;
                case 2:
                    prioridadTexto = "Media";
                    break;
                case 3:
                    prioridadTexto = "Baja";
                    break;
                default:
                    prioridadTexto = "Desconocida";
            }

            Label prioridad = new Label("Prioridad: " + prioridadTexto);
            Label recordatorio = new Label("Recordatorio: " + (info.getRecordatorio() != null ? info.getRecordatorio().toString() : "No asignado"));

            Button eliminarBtn = new Button("Eliminar");
            eliminarBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
            eliminarBtn.setOnAction(e -> {
                dao.eliminar(info.getIdPorVisitar());
                verPorVisitar(); // recargar la lista después de eliminar
            });

            tarjeta.getChildren().addAll(nombre, prioridad, recordatorio, eliminarBtn);
            lugaresContainer.getChildren().add(tarjeta);
        }


        if (lista.isEmpty()) {
            Label vacio = new Label("No hay lugares por visitar.");
            vacio.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            lugaresContainer.getChildren().add(vacio);
        }
    }


}