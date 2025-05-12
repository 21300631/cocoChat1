package modelos;

import java.time.LocalDateTime;

public class Mensajes_Grupo {
    private int mensaje_ID;
    private int grupo_ID;
    private int remitente_ID;
    private String contenido;
    private LocalDateTime fecha_Envio;
    private boolean es_Anuncio;
    
    // Constructor vacío
    public Mensajes_Grupo() {
    }
    
    // Constructor con campos obligatorios
    public Mensajes_Grupo(int grupo_ID, int remitente_ID, String contenido, boolean es_Anuncio) {
        this.grupo_ID = grupo_ID;
        this.remitente_ID = remitente_ID;
        this.contenido = contenido;
        this.es_Anuncio = es_Anuncio;
    }
    
    // Constructor completo
    public Mensajes_Grupo(int mensaje_ID, int grupo_ID, int remitente_ID, 
                         String contenido, LocalDateTime fecha_Envio, boolean es_Anuncio) {
        this.mensaje_ID = mensaje_ID;
        this.grupo_ID = grupo_ID;
        this.remitente_ID = remitente_ID;
        this.contenido = contenido;
        this.fecha_Envio = fecha_Envio;
        this.es_Anuncio = es_Anuncio;
    }
    
    // Getters y setters
    public int getMensajeID() {
        return mensaje_ID;
    }

    public void setMensajeID(int mensaje_ID) {
        this.mensaje_ID = mensaje_ID;
    }

    public int getGrupoID() {
        return grupo_ID;
    }

    public void setGrupoID(int grupo_ID) {
        this.grupo_ID = grupo_ID;
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

    public boolean isEsAnuncio() {
        return es_Anuncio;
    }

    public void setEsAnuncio(boolean es_Anuncio) {
        this.es_Anuncio = es_Anuncio;
    }
    
    @Override
    public String toString() {
        return "Mensajes_Grupo{" +
                "mensaje_ID=" + mensaje_ID +
                ", grupo_ID=" + grupo_ID +
                ", remitente_ID=" + remitente_ID +
                ", contenido='" + contenido + '\'' +
                ", fecha_Envio=" + fecha_Envio +
                ", es_Anuncio=" + es_Anuncio +
                '}';
    }
}
