package bogotravel.controller;

import bogotravel.dao.EntradaDAO;
import bogotravel.dao.LugarTuristicoDAO;
import bogotravel.dao.PorVisitarDAO;
import bogotravel.model.Entrada;
import bogotravel.model.LugarTuristico;
import bogotravel.model.Usuario;
import bogotravel.service.FotoEntradaService;
import bogotravel.sesion.SesionActual;
import bogotravel.utils.PorVisitarInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class InicioController {

    // UI Elements
    @FXML private VBox lugaresContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private Button CerrarButton;
    @FXML private ComboBox<String> categoriaComboBox;
    @FXML private ComboBox<String> localidadComboBox;
    @FXML private TextField nombreTextField;
    @FXML private HBox buscadorBox;

    // Data
    private final LugarTuristicoDAO lugarDAO = new LugarTuristicoDAO();
    private final Map<String, Integer> categoriasMap = new LinkedHashMap<>();
    private final List<String> localidadesBogota = Arrays.asList(
            "Usaquén", "Chapinero", "Santa Fe", "San Cristóbal", "Usme",
            "Tunjuelito", "Bosa", "Kennedy", "Fontibón", "Engativá",
            "Suba", "Barrios Unidos", "Teusaquillo", "Los Mártires", "Antonio Nariño",
            "Puente Aranda", "La Candelaria", "Rafael Uribe Uribe", "Ciudad Bolívar", "Sumapaz"
    );

    // Inicializa la pantalla de inicio y carga los lugares turísticos
    public void initialize() {
        Usuario actual = SesionActual.getUsuario();
        if (actual == null) {
            redirigirLogin();
            return;
        }

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

    // Redirige al login si no hay sesión
    private void redirigirLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bogotravel/view/UsuarioLoginView.fxml"));
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            root.getStylesheets().add("css/Inicio.css");
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carga visualmente los lugares en la vista principal
    private void cargarLugares(List<LugarTuristico> lugares) {
        lugaresContainer.getChildren().clear();
        if (lugares.isEmpty()) {
            Label vacio = new Label("No hay lugares por visitar.");
            vacio.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            lugaresContainer.getChildren().add(vacio);
            return;
        }
        for (LugarTuristico lugar : lugares) {
            lugaresContainer.getChildren().add(crearVistaLugar(lugar));
        }
    }

    // Acción: Buscar lugares filtrados por nombre, categoría o localidad
    @FXML
    private void buscarLugares() {
        String nombre = nombreTextField.getText().trim();
        String categoria = categoriaComboBox.getValue();
        String localidad = localidadComboBox.getValue();

        List<LugarTuristico> resultados = new ArrayList<>();
        if (!nombre.isEmpty()) {
            resultados = lugarDAO.buscarPorNombre(nombre);
        } else if (categoria != null && localidad != null) {
            resultados = lugarDAO.listarPorCategoriaYLocalidad(categoriasMap.get(categoria), localidad);
        } else {
            resultados = lugarDAO.listarTodos();
        }

        cargarLugares(resultados);
    }

    // Acción: Limpiar filtros de búsqueda
    @FXML
    private void limpiarFiltros() {
        nombreTextField.clear();
        categoriaComboBox.getSelectionModel().clearSelection();
        localidadComboBox.getSelectionModel().clearSelection();
        cargarLugares(lugarDAO.listarTodos());
    }

    // Crea la tarjeta visual para un lugar turístico
    private HBox crearVistaLugar(LugarTuristico lugar) {
        HBox box = new HBox(10);
        box.getStyleClass().add("lugar-box");

        ImageView imagen = new ImageView();
        String url = lugar.getImagenUrl();
        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            try {
                imagen.setImage(new Image(url, true));
            } catch (Exception e) {
                System.out.println("No se pudo cargar imagen desde URL: " + e.getMessage());
            }
        }

        imagen.setFitWidth(300);
        imagen.setFitHeight(300);
        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);

        Rectangle clip = new Rectangle(imagen.getFitWidth(), imagen.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imagen.setClip(clip);
        imagen.setEffect(new DropShadow(10, javafx.scene.paint.Color.GRAY));

        VBox textoBox = new VBox(5);
        Label nombre = new Label(lugar.getNombre());
        nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-font-family:Rockwell Condensed;");
        Label localidad = new Label("Localidad: " + lugar.getLocalidad());
        localidad.setStyle("-fx-font-size: 14px;");
        Label descripcion = new Label(lugar.getDescripcion());
        descripcion.setWrapText(true);
        descripcion.setMaxWidth(400);
        descripcion.setStyle("-fx-font-size: 13px;");
        textoBox.getChildren().addAll(nombre, localidad, descripcion);

        box.setOnMouseClicked(event -> abrirDetalleLugar(lugar));
        box.getChildren().addAll(imagen, textoBox);
        return box;
    }

    // Abre el detalle de un lugar turístico seleccionado
    private void abrirDetalleLugar(LugarTuristico lugar) {
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
        }
    }

    // Acción: Cerrar sesión
    @FXML
    private void cerrarSesion(ActionEvent event) {
        SesionActual.cerrarSesion();
        redirigirLogin();
    }

    // Acción: Mostrar lugares
    @FXML
    private void verLugares() {
        buscadorBox.setVisible(true);
        buscadorBox.setManaged(true);
        cargarLugares(lugarDAO.listarTodos());
    }

    // Acción: Mostrar entradas del usuario
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

    // Acción: Mostrar lugares por visitar
    @FXML
    private void verPorVisitar() {
        buscadorBox.setVisible(false);
        buscadorBox.setManaged(false);

        lugaresContainer.getChildren().clear();
        PorVisitarDAO dao = new PorVisitarDAO();
        List<PorVisitarInfo> lista = dao.listarConNombrePorUsuario(SesionActual.getUsuario().getEmail());

        if (lista.isEmpty()) {
            Label vacio = new Label("No hay lugares por visitar.");
            vacio.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
            lugaresContainer.getChildren().add(vacio);
            return;
        }

        for (PorVisitarInfo info : lista) {
            VBox tarjeta = new VBox(10);
            tarjeta.setStyle("-fx-background-color: #FDFEFE; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #BFC9CA; -fx-border-radius: 10;");

            Label nombre = new Label(info.getNombreLugar());
            nombre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            String prioridadTexto = switch (info.getPrioridad()) {
                case 1 -> "Alta";
                case 2 -> "Media";
                case 3 -> "Baja";
                default -> "Desconocida";
            };

            Label prioridad = new Label("Prioridad: " + prioridadTexto);
            Label recordatorio = new Label("Recordatorio: " + (info.getRecordatorio() != null ? info.getRecordatorio().toString() : "No asignado"));

            Button eliminarBtn = new Button("Eliminar");
            eliminarBtn.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
            eliminarBtn.setOnAction(e -> {
                dao.eliminar(info.getIdPorVisitar());
                verPorVisitar();
            });

            tarjeta.getChildren().addAll(nombre, prioridad, recordatorio, eliminarBtn);
            lugaresContainer.getChildren().add(tarjeta);
        }
    }

    // Acción: Abrir formulario para crear nueva entrada
    @FXML
    private void abrirFormularioEntrada(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Parent root = loader.load();
            CrearEntradaController controller = loader.getController();

            Stage ventana = new Stage();
            ventana.setTitle("Crear nueva entrada");
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            if (controller.isEntradaGuardada()) {
                verEntradas();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Crear tarjeta visual para entrada guardada por el usuario
    private VBox crearVistaEntrada(Entrada entrada) {
        VBox tarjeta = new VBox(10);
        tarjeta.setStyle("-fx-background-color: #FAFAFA; -fx-padding: 12; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-radius: 10;");

        Label titulo = new Label(entrada.getTitulo());
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;-fx-font-family:Rockwell Condensed;");

        Label fecha = new Label("Visita: " + entrada.getFechaVisita().toString());
        Label lugar = new Label("Lugar: " + Optional.ofNullable(entrada.getLugarDescripcion()).orElse("No especificado"));
        Label contenido = new Label(entrada.getContenido());
        contenido.setWrapText(true);

        tarjeta.getChildren().addAll(titulo, fecha, lugar, contenido);

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

        HBox botones = new HBox(10);
        botones.setStyle("-fx-alignment: center-right;");

        Button editarBtn = new Button("Editar");
        Button eliminarBtn = new Button("Eliminar");
        editarBtn.setStyle("-fx-background-color: #0b5563; -fx-text-fill: white;");
        eliminarBtn.setStyle("-fx-background-color: #9e443d; -fx-text-fill: white;");

        editarBtn.setOnAction(e -> abrirFormularioEdicion(entrada));
        eliminarBtn.setOnAction(e -> eliminarEntrada(entrada));

        botones.getChildren().addAll(editarBtn, eliminarBtn);
        tarjeta.getChildren().add(botones);

        return tarjeta;
    }

    // Abre formulario para editar entrada existente
    private void abrirFormularioEdicion(Entrada entrada) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bogotravel/view/CrearEntradaView.fxml"));
            Parent root = loader.load();
            CrearEntradaController controller = loader.getController();
            controller.cargarEntrada(entrada);

            Stage ventana = new Stage();
            ventana.setTitle("Editar entrada");
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            // Refresh entries after editing
            if (controller.isEntradaGuardada()) {
                verEntradas();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Elimina una entrada seleccionada
    private void eliminarEntrada(Entrada entrada) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas eliminar esta entrada?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            EntradaDAO dao = new EntradaDAO();
            if (dao.eliminar(entrada.getId())) {
                verEntradas();
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo eliminar la entrada.").showAndWait();
            }
        }
    }
}
