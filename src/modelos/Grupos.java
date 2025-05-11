package modelos;

import java.util.Date;

public class Grupos {
    private int grupo_ID;
    private String nombre;
    private String descripcion;
    private int creador_ID;
    private Date fecha_Creacion;
    private String foto_Grupo;
    
    // Constructor vacío
    public Grupos() {
    }
    
    // Constructor con campos obligatorios
    public Grupos(String nombre, String descripcion, int creador_ID, String foto_Grupo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador_ID = creador_ID;
        this.foto_Grupo = foto_Grupo;
    }
    
    // Getters y setters
    public int getGrupoID() {
        return grupo_ID;
    }

    public void setGrupoID(int grupo_ID) {
        this.grupo_ID = grupo_ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreadorID() {
        return creador_ID;
    }

    public void setCreadorID(int creador_ID) {
        this.creador_ID = creador_ID;
    }

    public Date getFechaCreacion() {
        return fecha_Creacion;
    }

    public void setFechaCreacion(Date fecha_Creacion) {
        this.fecha_Creacion = fecha_Creacion;
    }

    public String getFotoGrupo() {
        return foto_Grupo;
    }

    public void setFotoGrupo(String foto_Grupo) {
        this.foto_Grupo = foto_Grupo;
    }
}
