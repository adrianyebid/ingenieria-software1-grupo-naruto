package bogotravel.dao;

import bogotravel.model.Entrada;
import bogotravel.model.FotoEntrada;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FotoEntradaDAOTest {

    private static EntradaDAO entradaDAO;
    private static FotoEntradaDAO fotoDAO;
    private static int entradaId;
    private static int fotoId;

    private static final String EMAIL_TEST = "ana@example.com";

    @BeforeAll
    static void setup() {
        entradaDAO = new EntradaDAO();
        fotoDAO = new FotoEntradaDAO();

        // Creamos una entrada de prueba para asociarle la(s) foto(s)
        Entrada entrada = new Entrada(
                "Entrada con foto",
                "Contenido con foto",
                LocalDate.now(),
                "Lugar de prueba con foto",
                EMAIL_TEST
        );

        boolean creada = entradaDAO.crear(entrada);
        assertTrue(creada, "Debe poder crearse la entrada para la foto");

        // Recuperamos la entrada recién creada (asumimos que está al principio)
        List<Entrada> entradas = entradaDAO.listarPorUsuario(EMAIL_TEST);
        entradaId = entradas.get(0).getId();
    }

    @Test
    @Order(1)
    void testGuardarFoto() {
        FotoEntrada foto = new FotoEntrada(entradaId, "fotos/ana/entrada-test/foto1.jpg");
        boolean guardada = fotoDAO.guardarFoto(foto);
        assertTrue(guardada, "Debe guardarse la foto asociada a la entrada");

        // Guardamos ID para otras pruebas
        List<FotoEntrada> fotos = fotoDAO.listarPorEntrada(entradaId);
        assertFalse(fotos.isEmpty(), "Debe haber al menos una foto asociada");
        fotoId = fotos.get(0).getId();
    }

    @Test
    @Order(2)
    void testListarFotosPorEntrada() {
        List<FotoEntrada> fotos = fotoDAO.listarPorEntrada(entradaId);
        assertFalse(fotos.isEmpty(), "Debe listar las fotos asociadas a la entrada");
        assertEquals("fotos/ana/entrada-test/foto1.jpg", fotos.get(0).getRuta());
    }

    @Test
    @Order(3)
    void testEliminarFoto() {
        boolean eliminada = fotoDAO.eliminar(fotoId);
        assertTrue(eliminada, "Debe eliminarse la foto correctamente");

        List<FotoEntrada> fotos = fotoDAO.listarPorEntrada(entradaId);
        assertTrue(fotos.isEmpty(), "No debe haber fotos asociadas después de eliminar");
    }

    @AfterAll
    static void cleanup() {
        // Limpiar la entrada de prueba
        entradaDAO.eliminar(entradaId);
    }
}