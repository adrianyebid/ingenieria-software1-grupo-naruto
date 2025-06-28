package bogotravel.model;

public class FotoEntrada {
    private int id;
    private int entradaId;   // FK hacia entradas.id
    private String ruta;     // Ruta del archivo local (ej: "fotos/laura/entrada3/foto1.jpg")

    // Constructor vacío
    public FotoEntrada() {}

    // Constructor sin id (para inserción)
    public FotoEntrada(int entradaId, String ruta) {
        this.entradaId = entradaId;
        this.ruta = ruta;
    }

    // Constructor completo
    public FotoEntrada(int id, int entradaId, String ruta) {
        this.id = id;
        this.entradaId = entradaId;
        this.ruta = ruta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntradaId() {
        return entradaId;
    }

    public void setEntradaId(int entradaId) {
        this.entradaId = entradaId;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
