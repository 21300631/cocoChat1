/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.time.LocalDate;

/**
 *
 * @author PC
 */
public class Credenciales {

    public Credenciales(int CredencialID, int UsuarioID, int IntentosFallidos, String Email, String HashContrasena, String Salt, LocalDate UltimoCambioContrasena, String Estado, LocalDate FechaBloqueo, boolean RequiereCambioContrasena, boolean CuentaBloqueada) {
        this.CredencialID = CredencialID;
        this.UsuarioID = UsuarioID;
        this.IntentosFallidos = IntentosFallidos;
        this.Email = Email;
        this.HashContrasena = HashContrasena;
        this.Salt = Salt;
        this.UltimoCambioContrasena = UltimoCambioContrasena;
        this.Estado = Estado;
        this.FechaBloqueo = FechaBloqueo;
        this.RequiereCambioContrasena = RequiereCambioContrasena;
        this.CuentaBloqueada = CuentaBloqueada;
    }
    
    public int CredencialID;
    public int UsuarioID;
    public int IntentosFallidos;
    public String Email;
    public String HashContrasena;
    public String Salt;
    public LocalDate UltimoCambioContrasena;
    public String Estado;
    public LocalDate FechaBloqueo;
    public boolean RequiereCambioContrasena;
    public boolean CuentaBloqueada;

    public void setCredencialID(int CredencialID) {
        this.CredencialID = CredencialID;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

    public void setIntentosFallidos(int IntentosFallidos) {
        this.IntentosFallidos = IntentosFallidos;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setHashContrasena(String HashContrasena) {
        this.HashContrasena = HashContrasena;
    }

    public void setSalt(String Salt) {
        this.Salt = Salt;
    }

    public void setUltimoCambioContrasena(LocalDate UltimoCambioContrasena) {
        this.UltimoCambioContrasena = UltimoCambioContrasena;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public void setFechaBloqueo(LocalDate FechaBloqueo) {
        this.FechaBloqueo = FechaBloqueo;
    }

    public void setRequiereCambioContrasena(boolean RequiereCambioContrasena) {
        this.RequiereCambioContrasena = RequiereCambioContrasena;
    }

    public void setCuentaBloqueada(boolean CuentaBloqueada) {
        this.CuentaBloqueada = CuentaBloqueada;
    }
    

    public int getCredencialID() {
        return CredencialID;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public int getIntentosFallidos() {
        return IntentosFallidos;
    }

    public String getEmail() {
        return Email;
    }

    public String getHashContrasena() {
        return HashContrasena;
    }

    public String getSalt() {
        return Salt;
    }

    public LocalDate getUltimoCambioContrasena() {
        return UltimoCambioContrasena;
    }

    public String getEstado() {
        return Estado;
    }

    public LocalDate getFechaBloqueo() {
        return FechaBloqueo;
    }

    public boolean isRequiereCambioContrasena() {
        return RequiereCambioContrasena;
    }

    public boolean isCuentaBloqueada() {
        return CuentaBloqueada;
    }
    
    
    

}
