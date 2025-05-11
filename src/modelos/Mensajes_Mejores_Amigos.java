package modelos;

import java.time.LocalDateTime;

public class Mensajes_Mejores_Amigos {
    private int mensaje_ID;
    private int mejor_Amigo_ID;
    private int remitente_ID;
    private String contenido;
    private LocalDateTime fecha_Envio;
    
    // Constructor vacío
    public Mensajes_Mejores_Amigos() {
    }
    
    // Constructor con campos obligatorios
    public Mensajes_Mejores_Amigos(int mejor_Amigo_ID, int remitente_ID, String contenido) {
        this.mejor_Amigo_ID = mejor_Amigo_ID;
        this.remitente_ID = remitente_ID;
        this.contenido = contenido;
    }
    
    // Constructor completo
    public Mensajes_Mejores_Amigos(int mensaje_ID, int mejor_Amigo_ID, int remitente_ID, 
                                   String contenido, LocalDateTime fecha_Envio) {
        this.mensaje_ID = mensaje_ID;
        this.mejor_Amigo_ID = mejor_Amigo_ID;
        this.remitente_ID = remitente_ID;
        this.contenido = contenido;
        this.fecha_Envio = fecha_Envio;
    }
    
    // Getters y setters
    public int getMensajeID() {
        return mensaje_ID;
    }

    public void setMensajeID(int mensaje_ID) {
        this.mensaje_ID = mensaje_ID;
    }

    public int getMejorAmigoID() {
        return mejor_Amigo_ID;
    }

    public void setMejorAmigoID(int mejor_Amigo_ID) {
        this.mejor_Amigo_ID = mejor_Amigo_ID;
    }

    public int getRemitenteID() {
        return remitente_ID;
    }

    public void setRemitenteID(int remitente_ID) {
        this.remitente_ID = remitente_ID;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fecha_Envio;
    }

    public void setFechaEnvio(LocalDateTime fecha_Envio) {
        this.fecha_Envio = fecha_Envio;
    }
    
    @Override
    public String toString() {
        return "Mensajes_Mejores_Amigos{" + 
               "mensaje_ID=" + mensaje_ID + 
               ", mejor_Amigo_ID=" + mejor_Amigo_ID + 
               ", remitente_ID=" + remitente_ID + 
               ", contenido='" + contenido + '\'' + 
               ", fecha_Envio=" + fecha_Envio + 
               '}';
    }
}
