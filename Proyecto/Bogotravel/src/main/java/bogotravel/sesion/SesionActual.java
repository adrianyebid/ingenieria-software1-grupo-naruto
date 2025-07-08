package bogotravel.sesion;

import bogotravel.model.Usuario;

public class SesionActual {
    /**
     * Usuario actualmente autenticado en la sesión.
     * Inicialmente es null, indicando que no hay sesión activa.
     */
    private static Usuario usuarioLogueado;

    /**
     * Inicia sesión guardando el usuario autenticado.
     * @param usuario Usuario autenticado
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    /**
     * Finaliza la sesión actual.
     */
    public static void cerrarSesion() {
        usuarioLogueado = null;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     * @return Usuario logueado o null si no hay sesión activa
     */
    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    /**
     * Verifica si hay un usuario logueado actualmente.
     * @return true si hay sesión activa, false si no
     */
    public static boolean estaLogueado() {
        return usuarioLogueado != null;
    }
}
