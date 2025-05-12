package modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

public class Usuarios 
{
    private int usuario_ID;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDateTime fecha_registro;
    private LocalDateTime ultima_conexion;
    private String foto_perfil;
    private String estado;
    private boolean cuenta_verificada;
    private LocalDate fecha_nacimiento;
    private char genero;

    // Constructor por defecto
    public Usuarios() 
    {
        this.usuario_ID = 0;
        this.nombre = "";
        this.apellido = "";
        this.telefono = "";
        this.email = "";
        this.fecha_registro = null;
        this.ultima_conexion = null;
        this.foto_perfil = "";
        this.estado = "";
        this.cuenta_verificada = false;
        this.fecha_nacimiento = null;
        this.genero = ' ';
    }
    
    // Constructor con todos los parámetros
    public Usuarios(String nombre, String apellido, String telefono, String email, 
                        LocalDateTime fecha_registro, LocalDateTime ultima_conexion, 
                        String foto_perfil, String estado, boolean cuenta_verificada, 
                        LocalDate fecha_nacimiento, char genero)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.fecha_registro = fecha_registro;
        this.ultima_conexion = ultima_conexion;
        this.foto_perfil = foto_perfil;
        this.estado = estado;
        this.cuenta_verificada = cuenta_verificada;
        this.fecha_nacimiento = fecha_nacimiento;
        this.genero = genero;
    }

    public int getUsuario_Id() 
    {
        return usuario_ID;
    }

    public String getNombre() 
    {
        return nombre;
    }

    public String getApellido() 
    {
        return apellido;
    }

    public String getTelefono() 
    {
        return telefono;
    }

    public String getEmail() 
    {
        return email;
    }

    public LocalDateTime getFecha_registro() 
    {
        return fecha_registro;
    }

    public LocalDateTime getUltima_conexion() 
    {
        return ultima_conexion;
    }

    public String getFoto_perfil() 
    {
        return foto_perfil;
    }

    public String getEstado() 
    {
        return estado;
    }

    public boolean isCuenta_verificada() 
    {
        return cuenta_verificada;
    }

    public LocalDate getFecha_nacimiento() 
    {
        return fecha_nacimiento;
    }

    public char getGenero() 
    {
        return genero;
    }

    public void setUsuarioID(int usuario_ID) 
    {
        this.usuario_ID = usuario_ID;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) 
    {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) 
    {
        this.telefono = telefono;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setFecha_registro(LocalDateTime fecha_registro) 
    {
        this.fecha_registro = fecha_registro;
    }

    public void setUltima_conexion(LocalDateTime ultima_conexion) 
    {
        this.ultima_conexion = ultima_conexion;
    }

    public void setFoto_perfil(String foto_perfil) 
    {
        this.foto_perfil = foto_perfil;
    }

    public void setEstado(String estado) 
    {
        this.estado = estado;
    }

    public void setCuenta_verificada(boolean cuenta_verificada) 
    {
        this.cuenta_verificada = cuenta_verificada;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) 
    {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public void setGenero(char genero) 
    {
        this.genero = genero;
    }
    
    /**
     * Obtiene el nombre completo del usuario
     * @return Nombre y apellido concatenados
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
    
    /**
     * Calcula la edad del usuario basado en su fecha de nacimiento
     * @return La edad en años, o -1 si no hay fecha de nacimiento
     */
    public int getEdad() {
        if (fecha_nacimiento == null) {
            return -1;
        }
        return Period.between(fecha_nacimiento, LocalDate.now()).getYears();
    }
    
    /**
     * Determina si el usuario ha estado activo recientemente
     * @param horasInactividad Número de horas que definen la inactividad
     * @return true si el usuario ha estado activo dentro del período especificado
     */
    public boolean estaActivo(int horasInactividad) {
        if (ultima_conexion == null) {
            return false;
        }
        return LocalDateTime.now().minusHours(horasInactividad).isBefore(ultima_conexion);
    }
    
    /**
     * Verifica si el usuario tiene una foto de perfil
     * Esto puede ser útil para mostrar un avatar por defecto
     */
    public boolean tieneFotoPerfil() {
        return foto_perfil != null && !foto_perfil.isEmpty();
    }

    /**
     * Compara dos objetos Usuarios
     * Se considera que son iguales si tienen el mismo ID o el mismo email
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Usuarios other = (Usuarios) obj;
        return usuario_ID == other.usuario_ID 
            || (email != null && email.equals(other.email));
    }
    
    /**
     * Genera un código hash basado en el ID de usuario y el email
     * Esto es útil para comparar objetos en colecciones
     */
    @Override
    public int hashCode() {
        return Objects.hash(usuario_ID, email);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "usuario_ID=" + usuario_ID +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", estado='" + estado + '\'' +
                ", verificada=" + cuenta_verificada +
                '}';
    }
}
