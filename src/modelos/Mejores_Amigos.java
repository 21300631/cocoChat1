package modelos;

import java.time.LocalDate;

public class Mejores_Amigos {
    private int mejor_Amigo_ID;
    private int contacto_ID;
    private LocalDate fecha_Seleccion;
    private int prioridad;
    
    // Constructor vacío
    public Mejores_Amigos() {
    }
    
    // Constructor con campos obligatorios
    public Mejores_Amigos(int contacto_ID, int prioridad) {
        this.contacto_ID = contacto_ID;
        this.prioridad = prioridad;
    }
    
    // Constructor completo
    public Mejores_Amigos(int mejor_Amigo_ID, int contacto_ID, LocalDate fecha_Seleccion, int prioridad) {
        this.mejor_Amigo_ID = mejor_Amigo_ID;
        this.contacto_ID = contacto_ID;
        this.fecha_Seleccion = fecha_Seleccion;
        this.prioridad = prioridad;
    }
    
    // Getters y setters
    public int getMejorAmigoID() {
        return mejor_Amigo_ID;
    }

    public void setMejorAmigoID(int mejor_Amigo_ID) {
        this.mejor_Amigo_ID = mejor_Amigo_ID;
    }

    public int getContactoID() {
        return contacto_ID;
    }

    public void setContactoID(int contacto_ID) {
        this.contacto_ID = contacto_ID;
    }

    public LocalDate getFechaSeleccion() {
        return fecha_Seleccion;
    }

    public void setFechaSeleccion(LocalDate fecha_Seleccion) {
        this.fecha_Seleccion = fecha_Seleccion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }
    
    @Override
    public String toString() {
        return "Mejores_Amigos{" + 
               "mejor_Amigo_ID=" + mejor_Amigo_ID + 
               ", contacto_ID=" + contacto_ID + 
               ", fecha_Seleccion=" + fecha_Seleccion + 
               ", prioridad=" + prioridad + 
               '}';
    }
}
