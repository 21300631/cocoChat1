package modelos;

import java.time.LocalDate;

public class Miembros_Grupo {

    public Miembros_Grupo(int MiembroID, int GrupoID, int UsuarioID, LocalDate FechaUnion, boolean EsAdministrador) {
        this.MiembroID = MiembroID;
        this.GrupoID = GrupoID;
        this.UsuarioID = UsuarioID;
        this.FechaUnion = FechaUnion;
        this.EsAdministrador = EsAdministrador;
    }
    public int MiembroID;
    public int GrupoID;
    public int UsuarioID;
    public LocalDate FechaUnion;
    public boolean EsAdministrador;

    public void setMiembroID(int MiembroID) {
        this.MiembroID = MiembroID;
    }

    public void setGrupoID(int GrupoID) {
        this.GrupoID = GrupoID;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

    public void setFechaUnion(LocalDate FechaUnion) {
        this.FechaUnion = FechaUnion;
    }

    public void setEsAdministrador(boolean EsAdministrador) {
        this.EsAdministrador = EsAdministrador;
    }
    

    public int getMiembroID() {
        return MiembroID;
    }

    public int getGrupoID() {
        return GrupoID;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public LocalDate getFechaUnion() {
        return FechaUnion;
    }

    public boolean isEsAdministrador() {
        return EsAdministrador;
    }
    
    
    
}
