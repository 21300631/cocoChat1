package modelos;

import java.time.LocalDateTime;

public class Contactos {
    private int contactoId;
    private int usuarioId;      // Usuario que tiene el contacto
    private int contactoUsuarioId; // ID del usuario que es contacto
    private LocalDateTime fechaAgregado;
    private boolean esBloqueado;
    
    public Contactos(int contactoId, int usuarioId, int contactoUsuarioId, 
                    LocalDateTime fechaAgregado, boolean esBloqueado) {
        this.contactoId = contactoId;
        this.usuarioId = usuarioId;
        this.contactoUsuarioId = contactoUsuarioId;
        this.fechaAgregado = fechaAgregado;
        this.esBloqueado = esBloqueado;
    }
    
    // Getters y setters

    public int getContactoId() {
        return contactoId;
    }

    public void setContactoId(int contactoId) {
        this.contactoId = contactoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getContactoUsuarioId() {
        return contactoUsuarioId;
    }

    public void setContactoUsuarioId(int contactoUsuarioId) {
        this.contactoUsuarioId = contactoUsuarioId;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public boolean isEsBloqueado() {
        return esBloqueado;
    }

    public void setEsBloqueado(boolean esBloqueado) {
        this.esBloqueado = esBloqueado;
    }
    
}
