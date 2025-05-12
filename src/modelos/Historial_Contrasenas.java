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
public class Historial_Contrasenas {

    public Historial_Contrasenas(int HistorialID, int UsuarioID, String HashContrasena, String Salt, LocalDate FechaUso) {
        this.HistorialID = HistorialID;
        this.UsuarioID = UsuarioID;
        this.HashContrasena = HashContrasena;
        this.Salt = Salt;
        this.FechaUso = FechaUso;
    }
    public int HistorialID;
    public int UsuarioID;
    public String HashContrasena;
    public String Salt;
    public LocalDate FechaUso;

    public void setHistorialID(int HistorialID) {
        this.HistorialID = HistorialID;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

    public void setHashContrasena(String HashContrasena) {
        this.HashContrasena = HashContrasena;
    }

    public void setSalt(String Salt) {
        this.Salt = Salt;
    }

    public void setFechaUso(LocalDate FechaUso) {
        this.FechaUso = FechaUso;
    }
    
    
    

    public int getHistorialID() {
        return HistorialID;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public String getHashContrasena() {
        return HashContrasena;
    }

    public String getSalt() {
        return Salt;
    }

    public LocalDate getFechaUso() {
        return FechaUso;
    }
    
   
}
