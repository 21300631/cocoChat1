package modelos;

import java.time.LocalDateTime;

public class Contactos {
    private int ContactoID;
    private int UsuarioID;
    private int ContactoUsuarioID;
    private LocalDateTime FechaAgregado;
    private boolean EsBloqueado;
    
    // Constructor con todos los parámetros
    public Contactos(int ContactoID, int UsuarioID, int ContactoUsuarioID, 
                    LocalDateTime FechaAgregado, boolean EsBloqueado) 
    {
        this.ContactoID = ContactoID;
        this.UsuarioID = UsuarioID;
        this.ContactoUsuarioID = ContactoUsuarioID;
        this.FechaAgregado = FechaAgregado;
        this.EsBloqueado = EsBloqueado;
    }
    
    // Getters y setters
    public int getContactoID() 
    {
        return ContactoID;
    }
    
    public void setContactoID(int ContactoID) 
    {
        this.ContactoID = ContactoID;
    }
    
    public int getUsuarioID() 
    {
        return UsuarioID;
    }
    
    public void setUsuarioID(int UsuarioID) 
    {
        this.UsuarioID = UsuarioID;
    }
    
    public int getContactoUsuarioID() 
    {
        return ContactoUsuarioID;
    }
    
    public void setContactoUsuarioID(int ContactoUsuarioID) 
    {
        this.ContactoUsuarioID = ContactoUsuarioID;
    }
    
    public LocalDateTime getFechaAgregado() 
    {
        return FechaAgregado;
    }
    
    public void setFechaAgregado(LocalDateTime FechaAgregado) 
    {
        this.FechaAgregado = FechaAgregado;
    }
    
    public boolean isEsBloqueado() 
    {
        return EsBloqueado;
    }
    
    public void setEsBloqueado(boolean EsBloqueado) 
    {
        this.EsBloqueado = EsBloqueado;
    }
}
