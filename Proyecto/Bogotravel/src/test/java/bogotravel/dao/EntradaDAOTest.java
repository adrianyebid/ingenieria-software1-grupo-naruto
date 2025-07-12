package bogotravel.dao;

import bogotravel.model.Entrada;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntradaDAOTest {

    private static final String EMAIL_TEST = "ana@example.com";
    private static EntradaDAO dao;
    private static int idCreado;

    @BeforeAll
    static void setup() {
        dao = new EntradaDAO();
    }

    @Test
    @Order(1)
    void testCrearEntrada() {
        Entrada entrada = new Entrada(
                "Test Entrada",
                "Contenido de prueba",
                LocalDate.now(),
                "Descripción de lugar",
                EMAIL_TEST
        );

        boolean creada = dao.crear(entrada);
        assertTrue(creada, "La entrada debe ser creada correctamente");

        // Guardar ID creado
        List<Entrada> entradas = dao.listarPorUsuario(EMAIL_TEST);
        idCreado = entradas.get(0).getId();
    }

    @Test
    @Order(2)
    void testListarEntradas() {
        List<Entrada> entradas = dao.listarPorUsuario(EMAIL_TEST);
        assertFalse(entradas.isEmpty(), "Debe haber al menos una entrada para el usuario");
    }

    @Test
    @Order(3)
    void testBuscarPorId() {
        Entrada entrada = dao.buscarPorId(idCreado);
        assertNotNull(entrada, "Debe encontrarse la entrada por su ID");
        assertEquals("Test Entrada", entrada.getTitulo(), "El título debe coincidir");
    }

    @Test
    @Order(4)
    void testActualizarEntrada() {
        Entrada entrada = dao.buscarPorId(idCreado);
        assertNotNull(entrada);

        entrada.setTitulo("Entrada Actualizada");
        entrada.setContenido("Contenido actualizado");

        boolean actualizada = dao.actualizar(entrada);
        assertTrue(actualizada, "La entrada debe actualizarse correctamente");

        Entrada actualizadaDB = dao.buscarPorId(idCreado);
        assertEquals("Entrada Actualizada", actualizadaDB.getTitulo());
    }

    @Test
    @Order(5)
    void testEliminarEntrada() {
        boolean eliminada = dao.eliminar(idCreado);
        assertTrue(eliminada, "La entrada debe eliminarse correctamente");

        Entrada entrada = dao.buscarPorId(idCreado);
        assertNull(entrada, "La entrada debe haber sido eliminada");
    }
}