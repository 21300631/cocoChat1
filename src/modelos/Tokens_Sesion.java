package modelos;

import java.time.LocalDateTime;

public class Tokens_Sesion {
    private int sesion_ID;
    private int usuario_ID;
    private String token;
    private LocalDateTime fecha_Creacion;
    private LocalDateTime fecha_Expiracion;
    private String dispositivo;
    private String direccion_IP;
    private boolean activa;
    
    // Constructor vacío
    public Tokens_Sesion() 
    {
    }
    
    // Constructor con campos obligatorios
    public Tokens_Sesion(int usuario_ID, String token, LocalDateTime fecha_Expiracion, 
                         String dispositivo, String direccion_IP) 
    {
        this.usuario_ID = usuario_ID;
        this.token = token;
        this.fecha_Expiracion = fecha_Expiracion;
        this.dispositivo = dispositivo;
        this.direccion_IP = direccion_IP;
        this.activa = true; // Por defecto, un nuevo token está activo
    }
    
    // Constructor completo
    public Tokens_Sesion(int sesion_ID, int usuario_ID, String token, 
                         LocalDateTime fecha_Creacion, LocalDateTime fecha_Expiracion, 
                         String dispositivo, String direccion_IP, boolean activa) 
    {
        this.sesion_ID = sesion_ID;
        this.usuario_ID = usuario_ID;
        this.token = token;
        this.fecha_Creacion = fecha_Creacion;
        this.fecha_Expiracion = fecha_Expiracion;
        this.dispositivo = dispositivo;
        this.direccion_IP = direccion_IP;
        this.activa = activa;
    }

    // Getters y setters
    public int getSesionID() {
        return sesion_ID;
    }

    public void setSesionID(int sesion_ID) {
        this.sesion_ID = sesion_ID;
    }

    public int getUsuarioID() {
        return usuario_ID;
    }

    public void setUsuarioID(int usuario_ID) {
        this.usuario_ID = usuario_ID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaCreacion() {
        return fecha_Creacion;
    }

    public void setFechaCreacion(LocalDateTime fecha_Creacion) {
        this.fecha_Creacion = fecha_Creacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fecha_Expiracion;
    }

    public void setFechaExpiracion(LocalDateTime fecha_Expiracion) {
        this.fecha_Expiracion = fecha_Expiracion;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getDireccionIP() {
        return direccion_IP;
    }

    public void setDireccionIP(String direccion_IP) {
        this.direccion_IP = direccion_IP;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    // Método para verificar si el token ha expirado
    public boolean haExpirado() {
        LocalDateTime ahora = LocalDateTime.now();
        return ahora.isAfter(fecha_Expiracion) || !activa;
    }
    
    @Override
    public String toString() {
        return "Tokens_Sesion{" +
                "sesion_ID=" + sesion_ID +
                ", usuario_ID=" + usuario_ID +
                ", token='" + token + '\'' +
                ", fecha_Creacion=" + fecha_Creacion +
                ", fecha_Expiracion=" + fecha_Expiracion +
                ", dispositivo='" + dispositivo + '\'' +
                ", direccion_IP='" + direccion_IP + '\'' +
                ", activa=" + activa +
                '}';
    }
}
