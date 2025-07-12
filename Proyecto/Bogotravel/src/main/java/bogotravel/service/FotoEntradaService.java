package bogotravel.service;

import bogotravel.dao.FotoEntradaDAO;
import bogotravel.model.FotoEntrada;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FotoEntradaService {

    private FotoEntradaDAO fotoDAO = new FotoEntradaDAO();

    public boolean guardarFoto(File archivoOriginal, String emailUsuario, int entradaId) {
        try {
            // Preparar carpeta
            String baseFotos = "fotos/";
            String carpetaUsuario = emailUsuario.replace("@", "_").replace(".", "_");
            String carpetaEntrada = "entrada_" + entradaId;
            String rutaCarpeta = baseFotos + carpetaUsuario + "/" + carpetaEntrada + "/";

            File directorio = new File(rutaCarpeta);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Definir ruta destino
            String nombreArchivo = archivoOriginal.getName();
            File archivoDestino = new File(rutaCarpeta + nombreArchivo);

            // Copiar archivo
            Files.copy(archivoOriginal.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Guardar ruta relativa en BD
            String rutaRelativa = rutaCarpeta + nombreArchivo;
            FotoEntrada foto = new FotoEntrada(entradaId, rutaRelativa);

            return fotoDAO.guardarFoto(foto);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<File> obtenerArchivosFotos(int entradaId) {
        List<FotoEntrada> fotos = fotoDAO.listarPorEntrada(entradaId);
        List<File> archivos = new ArrayList<>();

        for (FotoEntrada foto : fotos) {
            File archivo = new File(foto.getRuta());
            if (archivo.exists()) {
                archivos.add(archivo);
            } else {
                System.out.println("⚠️ Archivo no encontrado: " + archivo.getPath());
            }
        }

        return archivos;
    }
}
