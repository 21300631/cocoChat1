package modelos;

import java.time.LocalDate;

public class Mensajes_Individuales {

    public Mensajes_Individuales(int MensajeID, int RemitenteID, int DestinatarioID, String Contenido, LocalDate FechaEnvio, LocalDate FechaLeido, boolean EsArchivado) {
        this.MensajeID = MensajeID;
        this.RemitenteID = RemitenteID;
        this.DestinatarioID = DestinatarioID;
        this.Contenido = Contenido;
        this.FechaEnvio = FechaEnvio;
        this.FechaLeido = FechaLeido;
        this.EsArchivado = EsArchivado;
    }
    public int MensajeID;
    public int RemitenteID;
    public int DestinatarioID;
    public String Contenido;
    public LocalDate FechaEnvio;
    public LocalDate FechaLeido;
    public boolean EsArchivado;

    public void setMensajeID(int MensajeID) {
        this.MensajeID = MensajeID;
    }

    public void setRemitenteID(int RemitenteID) {
        this.RemitenteID = RemitenteID;
    }

    public void setDestinatarioID(int DestinatarioID) {
        this.DestinatarioID = DestinatarioID;
    }

    public void setContenido(String Contenido) {
        this.Contenido = Contenido;
    }

    public void setFechaEnvio(LocalDate FechaEnvio) {
        this.FechaEnvio = FechaEnvio;
    }

    public void setFechaLeido(LocalDate FechaLeido) {
        this.FechaLeido = FechaLeido;
    }

    public void setEsArchivado(boolean EsArchivado) {
        this.EsArchivado = EsArchivado;
    }
    
    

    public int getMensajeID() {
        return MensajeID;
    }

    public int getRemitenteID() {
        return RemitenteID;
    }

    public int getDestinatarioID() {
        return DestinatarioID;
    }

    public String getContenido() {
        return Contenido;
    }

    public LocalDate getFechaEnvio() {
        return FechaEnvio;
    }

    public LocalDate getFechaLeido() {
        return FechaLeido;
    }

    public boolean isEsArchivado() {
        return EsArchivado;
    }
    

    
}
