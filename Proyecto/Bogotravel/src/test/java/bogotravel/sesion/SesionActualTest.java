package bogotravel.sesion;

import bogotravel.model.Usuario;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SesionActualTest {
    @AfterEach
    void limpiarSesion() {
        SesionActual.cerrarSesion();
    }

    @Test
    void testIniciarSesion() {
        Usuario usuario = new Usuario(9,"juan", "juanito@gmail.com", "password123");
        SesionActual.iniciarSesion(usuario);
        assertTrue(SesionActual.estaLogueado());
        assertEquals(usuario, SesionActual.getUsuario());
    }

    @Test
    void testCerrarSesion() {
        Usuario usuario = new Usuario(6,"Ana", "ana@email.com", "password456");
        SesionActual.iniciarSesion(usuario);
        SesionActual.cerrarSesion();
        assertFalse(SesionActual.estaLogueado());
        assertNull(SesionActual.getUsuario());
    }

    @Test
    void testEstaLogueadoSinSesion() {
        assertFalse(SesionActual.estaLogueado());
        assertNull(SesionActual.getUsuario());
    }
}
