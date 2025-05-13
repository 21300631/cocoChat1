package modelos;

import java.time.LocalDateTime;

public class Mensajes_Individuales {
    private int mensajeID;
    private int remitenteID;
    private int destinatarioID;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaLeido;
    private boolean esArchivado;
    private String adjunto;

    // Constructor por defecto
    public Mensajes_Individuales() {
        this.mensajeID = 0;
        this.remitenteID = 0;
        this.destinatarioID = 0;
        this.contenido = "";
        this.fechaEnvio = LocalDateTime.now();
        this.fechaLeido = null;
        this.esArchivado = false;
        this.adjunto = null;
    }

    // Constructor con parámetros
    public Mensajes_Individuales(int remitenteID, int destinatarioID, String contenido, boolean esArchivado) {
        this.remitenteID = remitenteID;
        this.destinatarioID = destinatarioID;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
        this.fechaLeido = null;
        this.esArchivado = esArchivado;
        this.adjunto = null;
    }

    // Getters y setters
    public int getMensajeID() {
        return mensajeID;
    }

    public void setMensajeID(int mensajeID) {
        this.mensajeID = mensajeID;
    }

    public int getRemitenteID() {
        return remitenteID;
    }

    public void setRemitenteID(int remitenteID) {
        this.remitenteID = remitenteID;
    }

    public int getDestinatarioID() {
        return destinatarioID;
    }

    public void setDestinatarioID(int destinatarioID) {
        this.destinatarioID = destinatarioID;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(LocalDateTime fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    public boolean isEsArchivado() {
        return esArchivado;
    }

    public void setEsArchivado(boolean esArchivado) {
        this.esArchivado = esArchivado;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }
}
