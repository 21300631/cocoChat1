
package modelos;

import java.time.LocalDate;

public class Tokens_Recuperacion {

    public Tokens_Recuperacion(int TokenID, int UsuarioID, String IPsolicitud, LocalDate FechaCreacion, String Token, LocalDate FechaExpiracion, boolean Usado) {
        this.TokenID = TokenID;
        this.UsuarioID = UsuarioID;
        this.IPsolicitud = IPsolicitud;
        this.FechaCreacion = FechaCreacion;
        this.Token = Token;
        this.FechaExpiracion = FechaExpiracion;
        this.Usado = Usado;
    }
    
    public int TokenID;
    public int UsuarioID;
    public String IPsolicitud;
    public LocalDate FechaCreacion;
    public String Token;
    public LocalDate FechaExpiracion;
    public boolean Usado;

    public void setTokenID(int TokenID) {
        this.TokenID = TokenID;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

    public void setIPsolicitud(String IPsolicitud) {
        this.IPsolicitud = IPsolicitud;
    }

    public void setFechaCreacion(LocalDate FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public void setFechaExpiracion(LocalDate FechaExpiracion) {
        this.FechaExpiracion = FechaExpiracion;
    }

    public void setUsado(boolean Usado) {
        this.Usado = Usado;
    }
    

    public int getTokenID() {
        return TokenID;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public String getIPsolicitud() {
        return IPsolicitud;
    }

    public LocalDate getFechaCreacion() {
        return FechaCreacion;
    }

    public String getToken() {
        return Token;
    }

    public LocalDate getFechaExpiracion() {
        return FechaExpiracion;
    }

    public boolean isUsado() {
        return Usado;
    }
    
}
